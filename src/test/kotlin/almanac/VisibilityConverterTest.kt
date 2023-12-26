package almanac

import mock.MockRandom

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.Test

class VisibilityConverterTest {

    companion object {
        @JvmStatic
        fun curvedVisValues(): List<Arguments> {
            return listOf(
                Arguments.of(0.005, 1),
                Arguments.of(0.02, 2),
                Arguments.of(0.04, 3),
                Arguments.of(0.08, 4),
                Arguments.of(0.13, 5),
                Arguments.of(0.2, 6),
                Arguments.of(0.28, 7),
                Arguments.of(0.4, 8),
                Arguments.of(0.5, 9),
                Arguments.of(0.575, 10),
            )
        }
    }

    val visibilityConverter = VisibilityConverter()

    @ParameterizedTest(name = "Random number {0} is converted to {1}")
    @MethodSource("curvedVisValues")
    fun getCurvedDistance(randomNumber: Double, expected: Int) {
        val distance = visibilityConverter.getCurved1d10(randomNumber)
        assertEquals(expected, distance)
    }

    @Test
    fun getVisibilityByLightingStep() {
        val result = visibilityConverter.getVisibilityByLightingStep(0)

        assertEquals(Visibility.HEX(15, -1), result)
    }

    @Test
    fun getDistanceForModerateRain() {
        val resultMin = visibilityConverter.getDistanceForModerateRain(MockRandom(0.00000001))
        val resultMax = visibilityConverter.getDistanceForModerateRain(MockRandom(1.0))

        assertEquals(Visibility.KM(2.0), resultMin)
        assertEquals(Visibility.KM(5.0), resultMax)
    }

    @Test
    fun getDistanceForHeavyRain() {
        val resultMin = visibilityConverter.getDistanceForHeavyRain(MockRandom(0.00000001))
        val resultMax = visibilityConverter.getDistanceForHeavyRain(MockRandom(1.0))

        assertEquals(Visibility.HEX(20, -1), resultMin)
        assertEquals(Visibility.HEX(200, -1), resultMax)
    }

    @Test
    fun getDistanceForLightSnow() {
        val resultMin = visibilityConverter.getDistanceForLightSnow(MockRandom(0.00000001))
        val resultMax = visibilityConverter.getDistanceForLightSnow(MockRandom(1.0))

        assertEquals(Visibility.KM(1.0), resultMin)
        assertEquals(Visibility.KM(5.0), resultMax)
    }

    @Test
    fun getDistanceForModerateSnow() {
        val resultMin = visibilityConverter.getDistanceForModerateSnow(MockRandom(0.00000001))
        val resultMax = visibilityConverter.getDistanceForModerateSnow(MockRandom(1.0))

        assertEquals(Visibility.HEX(50), resultMin)
        assertEquals(Visibility.HEX(95), resultMax)
    }

    @Test
    fun getDistanceForHeavySnow() {
        val resultMin = visibilityConverter.getDistanceForHeavySnow(MockRandom(0.00000001))
        val resultMax = visibilityConverter.getDistanceForHeavySnow(MockRandom(1.0))

        assertEquals(Visibility.HEX(0, -1), resultMin)
        assertEquals(Visibility.HEX(45, -1), resultMax)
    }

    @Test
    fun getDistanceForMist() {
        val resultMin = visibilityConverter.getDistanceForMist(MockRandom(0.00000001))
        val resultMax = visibilityConverter.getDistanceForMist(MockRandom(1.0))

        assertEquals(Visibility.HEX(110), resultMin)
        assertEquals(Visibility.HEX(200), resultMax)
    }

    @Test
    fun getDistanceForFog() {
        val resultMin = visibilityConverter.getDistanceForFog(MockRandom(0.00000001))
        val resultMax = visibilityConverter.getDistanceForFog(MockRandom(1.0))

        assertEquals(Visibility.HEX(10, -1), resultMin)
        assertEquals(Visibility.HEX(100, -1), resultMax)
    }
}