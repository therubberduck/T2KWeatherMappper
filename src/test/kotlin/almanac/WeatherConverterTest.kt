package almanac

import almanac.AlmanacWeatherGenerator.cloudCoverToClouds
import almanac.AlmanacWeatherGenerator.toRain
import almanac.AlmanacWeatherGenerator.toSnow
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class WeatherConverterTest {

    companion object {
        @JvmStatic
        fun cloudsValues(): List<Arguments> {
            return listOf(
                Arguments.of(0, "Clear"),
                Arguments.of(25, "Passing clouds"),
                Arguments.of(50, "Scattered clouds"),
                Arguments.of(75, "Broken clouds"),
                Arguments.of(100, "Overcast")
            )
        }

        @JvmStatic
        fun rainValues(): List<Arguments> {
            return listOf(
                Arguments.of(0.1f, Rain.Light),
                Arguments.of(0.9f, Rain.Light),
                Arguments.of(1.0f, Rain.Moderate),
                Arguments.of(3.9f, Rain.Moderate),
                Arguments.of(4.0f, Rain.Heavy)
            )
        }

        @JvmStatic
        fun snowValues(): List<Arguments> {
            return listOf(
                Arguments.of(0.5f, Snow.Light),
                Arguments.of(1.5f, Snow.Moderate),
                Arguments.of(2.5f, Snow.Heavy),
            )
        }
    }

    @ParameterizedTest(name = "Cloud coverage {0} is converted to {1}")
    @MethodSource("cloudsValues")
    fun cloudCoverToClouds(cloudCoverage: Int, expected: String) {
        val description = cloudCoverage.cloudCoverToClouds()
        assertEquals(expected, description)
    }

    @Test
    fun toRainNull() {
        val rainType = null.toRain()
        assertEquals(Rain.None, rainType)
    }

    @ParameterizedTest(name = "Rain amount {0} is converted to {1}")
    @MethodSource("rainValues")
    fun toRain(rainAmount: Float, expected: Rain) {
        val rainType = rainAmount.toRain()
        assertEquals(expected, rainType)
    }

    @Test
    fun toSnowNull() {
        val snowType = null.toSnow()
        assertEquals(Snow.None, snowType)
    }

    @ParameterizedTest(name = "Snow amount {0} and desc {1} is converted to {2}")
    @MethodSource("snowValues")
    fun toSnow(snowAmount: Float, expected: Snow) {
        val snowType = snowAmount.toSnow()
        assertEquals(expected, snowType)
    }
}