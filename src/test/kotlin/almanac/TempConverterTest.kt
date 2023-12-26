package almanac

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class TempConverterTest {
    companion object {
        @JvmStatic
        fun getKelvinToCelsius(): List<Arguments> {
            return listOf(
                Arguments.of(0.0, -273.15),
                Arguments.of(273.0, -0.15),
                Arguments.of(303.0, 29.85)
            )
        }

        @JvmStatic
        fun getFeltTempValues(): List<Arguments> {
            return listOf(
                Arguments.of(-50.0, 0f, FeltTemp.STYGIAN),
                Arguments.of(-49.0, 0f, FeltTemp.ARCTIC),
                Arguments.of(-30.0, 0f, FeltTemp.ARCTIC),
                Arguments.of(-29.0, 0f, FeltTemp.FREEZING),
                Arguments.of(-5.0, 0f, FeltTemp.FREEZING),
                Arguments.of(-4.0, 0f, FeltTemp.CHILLY),
                Arguments.of(10.0, 0f, FeltTemp.CHILLY),
                Arguments.of(11.0, 0f, FeltTemp.MILD),
                Arguments.of(17.0, 0f, FeltTemp.MILD),
                Arguments.of(18.0, 0f, FeltTemp.COMFY),
                Arguments.of(24.0, 0f, FeltTemp.COMFY),
                Arguments.of(25.0, 0f, FeltTemp.WARM),
                Arguments.of(35.0, 0f, FeltTemp.WARM),
                Arguments.of(36.0, 0f, FeltTemp.WARM),
                Arguments.of(45.0, 0f, FeltTemp.INFERNAL),
                Arguments.of(46.0, 0f, FeltTemp.INFERNAL),
                Arguments.of(-50.0, 15f, FeltTemp.STYGIAN),
                Arguments.of(-30.0, 15f, FeltTemp.STYGIAN),
                Arguments.of(-15.0, 15f, FeltTemp.FREEZING),
                Arguments.of(-5.0, 15f, FeltTemp.FREEZING),
                Arguments.of(0.0, 15f, FeltTemp.FREEZING),
                Arguments.of(10.0, 15f, FeltTemp.CHILLY),
                Arguments.of(17.0, 15f, FeltTemp.MILD),
                Arguments.of(20.0, 15f, FeltTemp.COMFY),
                Arguments.of(24.0, 15f, FeltTemp.COMFY),
                Arguments.of(35.0, 15f, FeltTemp.WARM),
                Arguments.of(45.0, 15f, FeltTemp.INFERNAL),
                Arguments.of(55.0, 15f, FeltTemp.INFERNAL),
                Arguments.of(-50.0, 50f, FeltTemp.STYGIAN),
                Arguments.of(-30.0, 50f, FeltTemp.STYGIAN),
                Arguments.of(-15.0, 50f, FeltTemp.ARCTIC),
                Arguments.of(-5.0, 50f, FeltTemp.FREEZING),
                Arguments.of(0.0, 50f, FeltTemp.FREEZING),
                Arguments.of(10.0, 50f, FeltTemp.CHILLY),
                Arguments.of(17.0, 50f, FeltTemp.MILD),
                Arguments.of(20.0, 50f, FeltTemp.COMFY),
                Arguments.of(24.0, 50f, FeltTemp.COMFY),
                Arguments.of(35.0, 50f, FeltTemp.WARM),
                Arguments.of(45.0, 50f, FeltTemp.INFERNAL),
                Arguments.of(55.0, 50f, FeltTemp.INFERNAL),
                Arguments.of(-50.0, 100f, FeltTemp.STYGIAN),
                Arguments.of(-30.0, 100f, FeltTemp.STYGIAN),
                Arguments.of(-15.0, 100f, FeltTemp.ARCTIC),
                Arguments.of(-5.0, 100f, FeltTemp.FREEZING),
                Arguments.of(0.0, 100f, FeltTemp.FREEZING),
                Arguments.of(10.0, 100f, FeltTemp.CHILLY),
                Arguments.of(17.0, 100f, FeltTemp.MILD),
                Arguments.of(20.0, 100f, FeltTemp.MILD),
                Arguments.of(24.0, 100f, FeltTemp.COMFY),
                Arguments.of(35.0, 100f, FeltTemp.WARM),
                Arguments.of(45.0, 100f, FeltTemp.INFERNAL),
                Arguments.of(55.0, 100f, FeltTemp.INFERNAL),
                Arguments.of(5.0, 8.6f, FeltTemp.CHILLY),
            )
        }
    }

    @ParameterizedTest(name = "Return Celsius for {0} should be {1}")
    @MethodSource("getKelvinToCelsius")
    fun kelvinToCelsius0(kelvinValue: Double, expectedCelsius: Double) {
        val celsius = kelvinValue.KelvinToCelsius()
        assertEquals(expectedCelsius, celsius, 0.01)
    }

    @ParameterizedTest(name = "Felt Temp for {0}C and {1}m/s should be {2}")
    @MethodSource("getFeltTempValues")
    fun calcFeltTemp(celsius: Double, windSpeed: Float, expectedResult: FeltTemp) {
        val feltTemp = celsius.calcFeltTemp(windSpeed, 20)
        assertEquals(expectedResult, feltTemp)
    }

    @Test
    fun simpleWindTest() {
        val tempCelsius = 24
        val humidity = 0.20
        val fahrenheit = (tempCelsius * 9.0 / 5.0) + 32.0

        assertEquals(75.2, fahrenheit)

        val heatIndexFahrenheit = (-42.379
                + 2.04901523 * fahrenheit
                + 10.14333127 * humidity
                - 0.22475541 * fahrenheit * humidity
                - 0.00683783 * fahrenheit * fahrenheit
                - .05481717 * humidity * humidity
                + .00122874 * fahrenheit * fahrenheit * humidity
                + .00085282 * fahrenheit * humidity * humidity
                - .00000199 * fahrenheit * fahrenheit * humidity * humidity)



        val result = Math.round((heatIndexFahrenheit - 32) * 5 / 9).toInt()
        assertEquals(23, result)
    }
}