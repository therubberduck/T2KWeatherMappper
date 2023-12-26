package almanac

import mock.MockRandom
import model.MoonPhaseFull
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File

class MoonPhaseCalcTest {


    companion object {
        lateinit var calculator: MoonPhaseCalc
        val testFile = File("moon-phases-2000-Europe_Warsaw.csv")

        @BeforeAll
        @JvmStatic
        fun setup() {
            calculator = MoonPhaseCalc(testFile)
        }

        @JvmStatic
        fun moonValues(): List<Arguments> {
            return listOf(
                Arguments.of(DateTime(2000, 1, 1, 0, 0), MoonPhaseFull.WANING_CRESCENT),
                Arguments.of(DateTime(2000, 1, 4, 0, 0), MoonPhaseFull.WANING_CRESCENT),
                Arguments.of(DateTime(2000, 1, 5, 0, 0), MoonPhaseFull.NEW),
                Arguments.of(DateTime(2000, 1, 6, 0, 0), MoonPhaseFull.NEW),
                Arguments.of(DateTime(2000, 1, 7, 0, 0), MoonPhaseFull.NEW),
                Arguments.of(DateTime(2000, 1, 8, 0, 0), MoonPhaseFull.WAXING_CRESCENT),
                Arguments.of(DateTime(2000, 1, 13, 0, 0), MoonPhaseFull.WAXING_CRESCENT),
                Arguments.of(DateTime(2000, 1, 14, 0, 0), MoonPhaseFull.FIRST_QUARTER),
                Arguments.of(DateTime(2000, 1, 20, 0, 0), MoonPhaseFull.FULL),
                Arguments.of(DateTime(2000, 1, 22, 0, 0), MoonPhaseFull.FULL),
                Arguments.of(DateTime(2000, 1, 28, 0, 0), MoonPhaseFull.THIRD_QUARTER),
                Arguments.of(DateTime(2000, 12, 10, 0, 0), MoonPhaseFull.FULL),
            )
        }
    }

    @Test
    fun setupCorrect() {
        val output = calculator.readCsv(testFile)

        assertEquals(DateTime(1999,12,29,0,0), output[0].dateTime)
        assertEquals(DateTime(2000,1,6,0,0), output[1].dateTime)
        assertEquals(MoonPhaseFull.THIRD_QUARTER, output[0].phase)
        assertEquals(MoonPhaseFull.NEW, output[1].phase)
    }

    @ParameterizedTest(name = "For date {0} moonphase is {1}")
    @MethodSource("moonValues")
    fun calc(date: DateTime, expected: MoonPhaseFull) {
        val result = calculator.calc(date)
        assertEquals(expected, result)
    }

}

