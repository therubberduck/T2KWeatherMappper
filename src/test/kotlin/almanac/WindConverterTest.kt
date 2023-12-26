package almanac

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class WindConverterTest {
    companion object {
        @JvmStatic
        fun getWindSpeedValues(): List<Arguments> {
            return listOf(
                Arguments.of(0f, WindSpeed.CalmL),
                Arguments.of(1f, WindSpeed.CalmL),
                Arguments.of(2f, WindSpeed.CalmS),
                Arguments.of(5f, WindSpeed.CalmS),
                Arguments.of(6f, WindSpeed.LightBreezeL),
                Arguments.of(11f, WindSpeed.LightBreezeL),
                Arguments.of(12f, WindSpeed.LightBreezeS),
                Arguments.of(19f, WindSpeed.LightBreezeS),
                Arguments.of(20f, WindSpeed.FreshBreezeL),
                Arguments.of(28f, WindSpeed.FreshBreezeL),
                Arguments.of(29f, WindSpeed.FreshBreezeS),
                Arguments.of(38f, WindSpeed.FreshBreezeS),
                Arguments.of(39f, WindSpeed.StrongBreezeL),
                Arguments.of(49f, WindSpeed.StrongBreezeL),
                Arguments.of(50f, WindSpeed.StrongBreezeS),
                Arguments.of(61f, WindSpeed.StrongBreezeS),
                Arguments.of(62f, WindSpeed.GaleL),
                Arguments.of(74f, WindSpeed.GaleL),
                Arguments.of(75f, WindSpeed.GaleS),
                Arguments.of(88f, WindSpeed.GaleS),
                Arguments.of(89f, WindSpeed.StormL),
                Arguments.of(102f, WindSpeed.StormL),
                Arguments.of(103f, WindSpeed.StormS),
                Arguments.of(117f, WindSpeed.StormS),
                Arguments.of(118f, WindSpeed.Hurricane),
            )
        }

        @JvmStatic
        fun getWindDirValues(): List<Arguments> {
            return listOf(
                Arguments.of(29, WindDir.N),
                Arguments.of(30, WindDir.NE),
                Arguments.of(89, WindDir.NE),
                Arguments.of(90, WindDir.SE),
                Arguments.of(149, WindDir.SE),
                Arguments.of(150, WindDir.S),
                Arguments.of(209, WindDir.S),
                Arguments.of(210, WindDir.SW),
                Arguments.of(269, WindDir.SW),
                Arguments.of(270, WindDir.NW),
                Arguments.of(329, WindDir.NW),
                Arguments.of(330, WindDir.N),
            )
        }
    }

    @ParameterizedTest(name = "Raw speed {0} is converted to {2}")
    @MethodSource("getWindSpeedValues")
    fun generateWindSpeed(rawWindSpeed: Float, expected: WindSpeed) {
        val wind = generateWindSpeed(rawWindSpeed)
        assertEquals(expected, wind)
    }

    @ParameterizedTest(name = "Raw direction {0} is converted to {2}")
    @MethodSource("getWindDirValues")
    fun generateWindDie(rawWindDir: Int, expected: WindDir) {
        val dir = generateWindDir(rawWindDir)
        assertEquals(expected, dir)
    }
}