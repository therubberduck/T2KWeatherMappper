package almanac

import model.GroundCover
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class GroundCoverBucketTest {

    @Test
    fun addGroundCoverForNoPrecipitation() {
        val noGroundCover = createStartingConditions(mudDepth = 0f, snowDepth = 0f)
        val expected = createExpectedConditions(mudDepth = 0f, snowDepth = 0f)

        val result = noGroundCover.addGroundCoverTest()

        assertEquals(expected, result)
    }

    @Test
    fun addMudForRain() {
        val muddyGround = createStartingConditions(mudDepth = 5f)
        val newRain = 3f // Makes twice as much mud
        val expected = createExpectedConditions(mudDepth = 8f)

        val result = muddyGround.addGroundCoverTest(rainAmount = newRain, positiveTemp = true)

        assertEquals(expected, result)
    }

    @Test
    fun checkMax500UnitsMud() {
        val frozenMud = createStartingConditions(mudDepth = 490f)
        val newRain = 20f
        val expected = createExpectedConditions(mudDepth = 500f)

        val result = frozenMud.addGroundCoverTest(rainAmount = newRain)

        assertEquals(expected, result)
    }

    @Test
    fun removeMudAfterRain() {
        val muddyGround = createStartingConditions(mudDepth = 5f)
        val newRain = 0f
        val tempC = 5.0 // Dries 2 unit
        val expected = createExpectedConditions(mudDepth = 3f)

        val result = muddyGround.addGroundCoverTest(rainAmount = newRain, tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun removeMudAfterRainCheckNeverNegative() {
        val muddyGround = createStartingConditions(mudDepth = 5f)
        val tempC = 30.0 // Dries 25 units
        val expected = createExpectedConditions(mudDepth = 0f)

        val result = muddyGround.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun addSnowForSnow() {
        val snowyGround = createStartingConditions(snowDepth = 5f)
        val newSnow = 3f
        val expected = createExpectedConditions(snowDepth = 8f, negativeTemp = true)

        val result = snowyGround.addGroundCoverTest(snowAmount = newSnow, negativeTemp = true)

        assertEquals(expected, result)
    }

    @Test
    fun addSnowForFrozenRain() {
        val snowyGround = createStartingConditions(snowDepth = 5f)
        val newRain = 3f
        val expected = createExpectedConditions(snowDepth = 35f, negativeTemp = true)

        val result = snowyGround.addGroundCoverTest(rainAmount = newRain, negativeTemp = true)

        assertEquals(expected, result)
    }

    @Test
    fun removeSnowDuringThaw() {
        val snowyGround = createStartingConditions(snowDepth = 30f)
        val tempC = 5.0 // Thaws 20 units snow, dries 1 unit mud

        // Mud equals 20 units thawed snow / 5 for snow to mud - 1 unit drying = 3
        val expected = createExpectedConditions(mudDepth = 2f, snowDepth = 10f)

        val result = snowyGround.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun removeSnowDuringThawCheckNeverNegative() {
        val snowyGround = createStartingConditions(snowDepth = 5f)
        val tempC = 30.0 // Thaws 75 units snow, dries 25 unit mud
        val expected = createExpectedConditions(mudDepth = 0f, snowDepth = 0f)

        val result = snowyGround.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun removeSnowDuringRain() {
        val snowyGround = createStartingConditions(snowDepth = 51f)
        val rainAmount = 1f
        val tempC = 5.0 // Thaws 20 units snow, doubled for rain to 40, all mud stays during rain

        // Mud equals 20 units thawed snow / 5 for snow to mud * 2 for wet weather + 1 for rain falling = 9
        val expected = createExpectedConditions(mudDepth = 9f, snowDepth = 11f)

        val result = snowyGround.addGroundCoverTest(rainAmount = rainAmount, tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun removeSnowDuringRainCheckOverflow() {
        val snowyGround = createStartingConditions(snowDepth = 3f)
        val rainAmount = 1f
        val tempC = 5.0 // Thaws 20 units snow, doubled for rain to 40, all mud stays during rain

        // Mud equals 3 units thawed snow (since that is all there is) / 5 for snow to mud + 1 rain falling = 1.6
        val expected = createExpectedConditions(mudDepth = 1.6f, snowDepth = 0f)

        val result = snowyGround.addGroundCoverTest(rainAmount = rainAmount, tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun packSnowWhenNotSnowing() {
        val snowyCover = createStartingConditions(snowDepth = 1000f, packedSnowDepth = 0f)
        val tempC = -5.0 // Freezes 3 units

        // Ground frozen = 3 / 4 for slower freeze = 0.75
        val expected = createExpectedConditions(snowDepth = 950f, packedSnowDepth = 50f, frostDepth =  0.75f)

        val result = snowyCover.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun addFrozenGroundNoMud() {
        val noGroundCover = createStartingConditions()
        val tempC = -5.0 // Freezes 3 units

        // Ground frozen = 3 / 4 for slower freeze = 0.75
        val expected = createExpectedConditions(frostDepth =  0.75f)

        val result = noGroundCover.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun checkMax250UnitsFrozenGround() {
        val frozenMud = createStartingConditions(frostDepth = 245f)
        val tempC = -25.0 // Freezes 25 unit mud
        val expected = createExpectedConditions(mudDepth = 0f, frostDepth = 250f)

        val result = frozenMud.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun removeFrozenGroundNoMud() {
        val frozenGround = createStartingConditions(frostDepth = 8f)
        val tempC = 5.0 // Thaws 3 unit
        val expected = createExpectedConditions(frostDepth = 5f)

        val result = frozenGround.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun removeFrozenGroundCheckNeverNegative() {
        val frozenGround = createStartingConditions(frostDepth = 2f)
        val tempC = 30.0 // Thaws 35 units
        val expected = createExpectedConditions(frostDepth = 0f)

        val result = frozenGround.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun addFrozenMud() {
        val muddyGround = createStartingConditions(mudDepth = 5f)
        val tempC = -5.0 // Freezes 3 units
        val expected = createExpectedConditions(mudDepth = 2f, frozenMudDepth = 3f, topMudFrozen = true)

        val result = muddyGround.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun addFrozenMudAndGroundCombined() {
        val muddyGround = createStartingConditions(mudDepth = 1f)
        val tempC = -5.0 // Freezes 3 units

        // Ground frozen = 3 - 1 mud / 4 for slower freeze = 0.5
        val expected = createExpectedConditions(mudDepth = 0f, frozenMudDepth = 1f, frostDepth = 0.5f, topMudFrozen = false)

        val result = muddyGround.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun removeFrozenMud() {
        val frozenMud = createStartingConditions(frozenMudDepth = 5f)
        val tempC = 5.0 // Thaws 3 unit, dries 2 units
        val expected = createExpectedConditions(mudDepth = 1f, frozenMudDepth = 2f)

        val result = frozenMud.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun checkMax250UnitsFrozenMud() {
        val frozenMud = createStartingConditions(mudDepth = 20f, frozenMudDepth = 240f)
        val tempC = -25.0 // Freezes 25 unit mud
        val expected = createExpectedConditions(mudDepth = 0f, frozenMudDepth = 250f, topMudFrozen = false)

        val result = frozenMud.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun removeFrozenMudAndGroundCombined() {
        val frozenGround = createStartingConditions(frozenMudDepth = 2f, frostDepth = 8f)
        val tempC = 5.0 // Thaws 3 unit, dries 2 unit
        val expected = createExpectedConditions(mudDepth = 0f, frostDepth = 7f)

        val result = frozenGround.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun checkMax250UnitsCombinedFrozenWhenFreezingMud() {
        val frozenMud = createStartingConditions(mudDepth = 20f, frozenMudDepth = 100f, frostDepth = 150f)
        val tempC = -25.0 // Freezes 25 unit mud
        val expected = createExpectedConditions(mudDepth = 0f, frozenMudDepth = 120f, frostDepth = 130f)

        val result = frozenMud.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    @Test
    fun checkMax250UnitsCombinedFrozenWhenFreezingGround() {
        val frozenMud = createStartingConditions(mudDepth = 0f, frozenMudDepth = 100f, frostDepth = 145f)
        val tempC = -25.0 // Freezes 25 unit ground

        // Ground frozen = 25 / 4 for slower freeze = 6.25
        val expected = createExpectedConditions(mudDepth = 0f, frozenMudDepth = 100f, frostDepth = 150f)

        val result = frozenMud.addGroundCoverTest(tempC = tempC)

        assertEquals(expected, result)
    }

    ///
    /// Helper Test Functions
    ///

    private fun GroundCoverBucket.addGroundCoverTest(
        rainAmount: Float = 0f,
        snowAmount: Float = 0f,
        tempC: Double? = null,
        positiveTemp: Boolean = true,
        negativeTemp: Boolean = false
    ): GroundCover {
        val temp = if(tempC != null) {
            tempC
        }
        else if(negativeTemp == true) {
            -5.0
        }
        else {
            5.0
        }
        return this.addGroundCoverWith(rainAmount, snowAmount, temp, -5f)
    }
    // With dewPointC = -5f
    // 5C  = -2
    // 10C = -5
    // 15C = -8
    // 20C = -11
    // 25C = -15
    // 30C = -19

    private fun createStartingConditions(
        mudDepth: Float = 0f,
        snowDepth: Float = 0f,
        packedSnowDepth: Float = 0f,
        frozenMudDepth: Float = 0f,
        frostDepth: Float = 0f
    ): GroundCoverBucket {
        return GroundCoverBucket(GroundCover(mudDepth, snowDepth, packedSnowDepth, frozenMudDepth, frostDepth, false))
    }

    private fun createExpectedConditions(
        mudDepth: Float = 0f,
        snowDepth: Float = 0f,
        packedSnowDepth: Float = 0f,
        frozenMudDepth: Float = 0f,
        frostDepth: Float = 0f,
        topMudFrozen: Boolean = false,
        negativeTemp: Boolean = false
    ): GroundCover {
        val defaultFrostDepth = if(negativeTemp) {
            0.75f
        }
        else {
            frostDepth
        }
        return GroundCover(mudDepth, snowDepth, packedSnowDepth, frozenMudDepth, defaultFrostDepth, topMudFrozen)
    }
}