package almanac

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class CloudsConverterTest {
    companion object {
        @JvmStatic
        fun getCloudCoverageValues(): List<Arguments> {
            return listOf(
                Arguments.of(0, 0),
                Arguments.of(5, 0),
                Arguments.of(12, 0),
                Arguments.of(13, 25),
                Arguments.of(25, 25),
                Arguments.of(37, 25),
                Arguments.of(38, 50),
                Arguments.of(62, 50),
                Arguments.of(63, 75),
                Arguments.of(87, 75),
                Arguments.of(88, 100),
                Arguments.of(100, 100),
            )
        }
    }

    @ParameterizedTest(name = "Cloud coverage {0} is rounded to {1}")
    @MethodSource("getCloudCoverageValues")
    fun roundCloudsCoverageTo25(oldCoverage: Int, expected: Int) {
        val newCoverage = oldCoverage.roundCloudsCoverageTo25()
        assertEquals(expected, newCoverage)
    }
}