package almanac

import mock.IRandom
import mock.IVisibilityConverter
import mock.Random
import model.MoonPhase
import model.Shift
import java.text.DecimalFormat
import kotlin.math.absoluteValue
import kotlin.math.ceil
import kotlin.math.pow
import kotlin.math.sqrt

class VisibilityConverter(private val rand: IRandom = Random()) : IVisibilityConverter {

    override fun getVisibility(
        shift: Shift,
        moonPhase: MoonPhase,
        weather: Weather
    ): Visibility {
        return if (shift == Shift.MORNING || shift == Shift.AFTERNOON) {
            getVisibilityDay(weather.dominant, weather.precipitation)
        } else {
            getVisibilityNight(moonPhase, weather.roundedCloudCover, weather.dominant)
        }
    }

    override fun getVisibility(
        shift: Shift,
        moonPhase: MoonPhase,
        dominantWeather: DominantWeather,
        precipitation: Precipitation,
        cloudCover: Int
    ): Visibility {
        return if (shift == Shift.MORNING || shift == Shift.AFTERNOON) {
            getVisibilityDay(dominantWeather, precipitation)
        } else {
            getVisibilityNight(moonPhase, cloudCover, dominantWeather)
        }
    }

    fun getVisibilityDay(dominantWeather: DominantWeather, precipitation: Precipitation): Visibility {
        return when (dominantWeather) {
            DominantWeather.Clouds -> Visibility.CLEAR
            DominantWeather.Rain -> {
                when (precipitation) {
                    Rain.Heavy -> getDistanceForHeavyRain(rand)
                    Rain.Moderate -> getDistanceForModerateRain(rand)
                    else -> Visibility.CLEAR
                }

            }

            DominantWeather.Snow -> {
                when (precipitation) {
                    Snow.Heavy -> getDistanceForHeavySnow(rand)
                    Snow.Moderate -> getDistanceForModerateSnow(rand)
                    else -> getDistanceForLightSnow(rand)
                }
            }

            DominantWeather.Mist -> {
                when (precipitation) {
                    Rain.Heavy -> getDistanceForHeavyRain(rand)
                    Rain.Moderate -> getDistanceForModerateRain(rand)
                    Snow.Heavy -> getDistanceForHeavySnow(rand)
                    Snow.Moderate -> getDistanceForModerateSnow(rand)
                    Snow.Light -> getDistanceForLightSnow(rand)
                    else -> getDistanceForMist(rand)
                }
            }

            DominantWeather.Fog -> {
                when (precipitation) {
                    Rain.Heavy, Snow.Heavy -> Visibility.HEX(20, -1)
                    Rain.Moderate, Snow.Moderate -> Visibility.HEX(40, -1)
                    else -> getDistanceForFog(rand)
                }
            }

            DominantWeather.Thunderstorm -> {
                when (precipitation) {
                    Rain.Heavy -> getDistanceForHeavyRain(rand)
                    Rain.Moderate -> getDistanceForModerateRain(rand)
                    Snow.Heavy -> getDistanceForHeavySnow(rand)
                    Snow.Moderate -> getDistanceForModerateSnow(rand)
                    Snow.Light -> getDistanceForLightSnow(rand)
                    else -> Visibility.CLEAR
                }
            }
        }
    }

    fun getVisibilityNight(moonPhase: MoonPhase, cloudCover: Int, dominantWeather: DominantWeather): Visibility {
        val rawLightStep = when (moonPhase) {
            MoonPhase.FULL -> 0
            MoonPhase.HALF -> 1
            MoonPhase.CRESCENT -> 2
            MoonPhase.NEW -> 3
        }
        var lightStep = rawLightStep
        if (listOf(
                DominantWeather.Rain, DominantWeather.Snow, DominantWeather.Thunderstorm
            ).contains(dominantWeather)
        ) {
            lightStep++
        }
        if (listOf(DominantWeather.Mist, DominantWeather.Fog).contains(dominantWeather) || cloudCover > 0) {
            lightStep++
        }
        if (lightStep > 4) {
            lightStep = 4
        }
        return when (cloudCover) {
            25 -> Visibility.MULTIPLE(
                getVisibilityByLightingStep(rawLightStep), getVisibilityByLightingStep(lightStep)
            )

            in 50..75 -> Visibility.MULTIPLE(
                getVisibilityByLightingStep(lightStep), getVisibilityByLightingStep(rawLightStep)
            )

            else -> getVisibilityByLightingStep(lightStep)
        }
    }

    //        FULLMOON(0),
    //        MOON(1),
    //        CRESCENT(2),
    //        STARS(3),
    //        NONE(4),
    fun getVisibilityByLightingStep(lightStep: Int): Visibility.HEX {
        return when (lightStep) {
            0 -> Visibility.HEX(15, -1)
            1 -> Visibility.HEX(10, -1)
            2 -> Visibility.HEX(5, -2)
            3 -> Visibility.HEX(3, -3)
            else -> Visibility.HEX(0, -4)
        }
    }

    fun getDistanceForModerateRain(rand: IRandom): Visibility {
        val oneToSeven = ceil(getCurved1d10(rand.rand()) / (10.0 / 7))
        return Visibility.KM(oneToSeven * 0.5 + 1.5)
    }

    fun getDistanceForHeavyRain(rand: IRandom): Visibility {
        return Visibility.HEX((getCurved1d10(rand.rand()) + getCurved1d10(rand.rand())) * 10, -1)
    }

    fun getDistanceForLightSnow(rand: IRandom): Visibility {
        val oneToFive = ceil(getCurved1d10(rand.rand()) / 2.0)
        return Visibility.KM(oneToFive)
    }

    fun getDistanceForModerateSnow(rand: IRandom): Visibility {
        return Visibility.HEX(getCurved1d10(rand.rand()) * 5 + 45)
    }

    fun getDistanceForHeavySnow(rand: IRandom): Visibility {
        return Visibility.HEX(getCurved1d10(rand.rand()) * 5 - 5, -1)
    }

    fun getDistanceForMist(rand: IRandom): Visibility {
        return Visibility.HEX(100 + getCurved1d10(rand.rand()) * 10)
    }

    fun getDistanceForFog(rand: IRandom): Visibility {
        return Visibility.HEX(getCurved1d10(rand.rand()) * 10, -1)
    }

    fun getCurved1d10(num: Double): Int {
        return ceil(getCurvedValue(num) * 10).toInt()
    }

    private fun getCurvedValue(num: Double): Double {
        return sqrt(1 - (num - 1).pow(2.0))
    }
}

abstract class Visibility {

    abstract val desc: String

    object NOT_APPLICABLE : Visibility() {
        override val desc: String
            get() = "X"
    }

    object CLEAR : Visibility() {
        override val desc: String
            get() = "—"
    }

    /**
     * @property mod A mod of -4 counts as -3⃰
     */
    data class HEX(val hexes: Int, val mod: Int = 0) : Visibility() {

        override val desc: String
            get() {
                val hexValue = if (hexes in 0..1) {
                    "$hexes hex"
                } else {
                    "$hexes hexes"
                }
                val modValue = when (mod) {
                    0 -> ""
                    -4 -> " (-3 ⃰)"
                    else -> " (-${mod.absoluteValue})"
                }

                return "$hexValue$modValue"
            }
    }

    data class KM(val km: Double) : Visibility() {
        private val decimalFormat = DecimalFormat("0.#")
        override val desc: String
            get() = "${decimalFormat.format(km)} km"
    }

    data class MULTIPLE(val distMain: HEX, val distSec: HEX) : Visibility() {

        override val desc: String
            get() = "${makeDesc(distMain)}  |  ${makeDesc(distSec)}"

        companion object {
            fun makeDesc(hex: HEX): String {
                val modValue = when (hex.mod) {
                    0 -> ""
                    -4 -> " (-3 ⃰)"
                    else -> " (-${hex.mod.absoluteValue})"
                }

                return "${hex.hexes}$modValue"
            }
        }
    }
}