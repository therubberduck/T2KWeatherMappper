package almanac

import model.GroundCover
import kotlin.math.ceil
import kotlin.math.max

class GroundCoverBucket(startingGroundCover: GroundCover) {
    private var lastGroundCover: GroundCover

    init {
        lastGroundCover = startingGroundCover
    }

    fun addGroundCoverWith(rainAmount: Float, snowAmount: Float, tempC: Double, dewPointC: Float): GroundCover {
        // x5 mud to take into account some areas being worse than others
        // 2.5 mm of rain for 1 mm of mud (after adjustment: 1mm rain = 2mm mud)
        // 10 mm of snow for 1 mm of rain (25mm snow == 1mm mud, after adjustment: 5mm snow == 1mm mud)
        // Units are in mm

        val latitude = 52.237 // For Warsaw
//        val evaporation = ceil( max((((700 * tempC ) / (100 - latitude)) + 15 * (tempC - dewPointC)) / (80 - tempC), 0.0)).toInt()
        val evaporation = max((((700 * tempC ) / (100 - latitude)) + 15 * (tempC - dewPointC)) / (80 - tempC), 0.0).toInt()

        val roundedTemp = tempC.roundToFiveInt()
        val adjustment = getAdjustments(roundedTemp, rainAmount, snowAmount, evaporation)

        var newMud = max(lastGroundCover.mudDepth + adjustment.mud, 0f)
        if(newMud > 500f) {
            newMud = 500f
        }

        var newFrozenMud = max(lastGroundCover.frozenMudDepth + adjustment.frozenMud, 0f)
        if(newFrozenMud > 250f) {
            newFrozenMud = 250f
        }

        var newFrostDepth =
            max(lastGroundCover.frostDepth + adjustment.frozenGround, 0f)
        if(newFrozenMud + newFrostDepth > 250f) {
            newFrostDepth = 250f - newFrozenMud
        }

        val newSnow = max(lastGroundCover.snowDepth + adjustment.snow, 0f)

        val topMudFrozen = if(newMud == 0f) {
            false
        }
        else {
            adjustment.frozenMud > 0f
        }

        val newGroundCover = GroundCover(
            mudDepth = newMud,
            snowDepth = newSnow,
            frozenMudDepth = newFrozenMud,
            frostDepth = newFrostDepth,
            topMudFrozen = topMudFrozen,
            evaporation = evaporation,
            mudAdjust = adjustment.mud
        )
        lastGroundCover = newGroundCover
        return newGroundCover
    }

    private data class MudAdjustment(val mud: Float, val snow: Float, val frozenMud: Float, val frozenGround: Float)

    private fun getAdjustments(roundedTemp: Int, rainAmount: Float, snowAmount: Float, evaporation: Int): MudAdjustment {
        var frozenGroundAdjustment = 0f
        val rawAdjustment = mudToFrostAmount(roundedTemp).toFloat()

        // During frost
        if (roundedTemp <= 0) {
            // Never freeze more mud than we have mud
            val frozenMudAdjustment = if (rawAdjustment > lastGroundCover.mudDepth) {
                frozenGroundAdjustment = (rawAdjustment - lastGroundCover.mudDepth) / 4f // Freeze ground, after mud runs out (ground freezes slower without mud, so divide by 4)
                lastGroundCover.mudDepth
            } else {
                rawAdjustment
            }
            // Mud equals new mod, minus mud frozen
            val mudAdjustment = frozenMudAdjustment.unaryMinus() - evaporation
            // And also add the snow
            val snowAdjustment = snowAmount + rainAmount * 10 // 1mm rain = 10cm snow
            return MudAdjustment(
                mud = mudAdjustment,
                snow = snowAdjustment,
                frozenMud = frozenMudAdjustment,
                frozenGround = frozenGroundAdjustment
            )

        }
        // During thaw / normal conditions
        else {
            // Thaw snow
            // Get snowToMud and multiply by 2 if raining
            val rawSnowAdjustment = snowToMud(roundedTemp).toFloat() * if(rainAmount > 0f) 2 else 1
            // Never thaw more snow than we have
            val snowAdjustment = if(rawSnowAdjustment > lastGroundCover.snowDepth) {
                -lastGroundCover.snowDepth
            }
            else {
                -rawSnowAdjustment
            }

            // Raw freeze adjustment will be negative, indicating how much to reduce frozenMudDepth by
            // Never thaw more mud than we have frozen mud
            var frozenMudAdjustment = if (-rawAdjustment > lastGroundCover.frozenMudDepth) {
                // Thaw frozen ground, after frozen mud runs out
                frozenGroundAdjustment = rawAdjustment + lastGroundCover.frozenMudDepth
                // Then return all the mud we can thaw
                -lastGroundCover.frozenMudDepth
            } else {
                rawAdjustment
            }

            // Flip rawAdjustmentFreeze to get the amount of frozen mud turned to mud
            // Add mud for thawed snow (10mm snow = 1mm rain / 2.5 rain to mud * 5 worst area adjustment = 1/5 modifier)
            // Then add rain or subtract mud drying
            val mudAdjustment = frozenMudAdjustment.unaryMinus() +
                    (snowAdjustment.unaryMinus() / 5) +
                    snowAmount / 10f + // 1cm snow = 1mm rain
                    if (rainAmount == 0f) {
                        -evaporation.toFloat()
                    } else {
                        rainAmount
                    }

            return MudAdjustment(
                mud = mudAdjustment,
                snow = snowAdjustment,
                frozenMud = frozenMudAdjustment,
                frozenGround = frozenGroundAdjustment
            )
        }
    }

    private fun Double.roundToFiveInt(): Int {
        return (Math.round(this / 5) * 5).toInt()
    }

    private fun snowToMud(roundedTemp: Int): Int {
        return when (roundedTemp) {
            30 -> 50
            25 -> 40
            20 -> 30
            15 -> 20
            10 -> 10
            5 -> 5
            else -> throw IllegalStateException("All temperatures below zero are handled in a different branch in getMudAdjustment()")
        }
    }

    private fun mudToFrostAmount(roundedTemp: Int): Int {
        return when (roundedTemp) {
            30 -> -35
            25 -> -25
            20 -> -15
            15 -> -10
            10 -> -5
            5 -> -3
            0 -> 1
            -5 -> 3
            -10 -> 5
            -15 -> 10
            -20 -> 15
            -25 -> 25
            -30 -> 35
            else -> throw NotImplementedError()
        }
    }
}