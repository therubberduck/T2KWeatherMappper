package almanac

import model.GroundCover
import model.MoonPhaseFull
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.File

class GroundToStringTest {
    companion object {
        @JvmStatic
        fun groundValues(): List<Arguments> {
            return listOf(
                Arguments.of(GroundCover(0f, 0f, 0f, 0f, 0f, false, false), "—"),
                Arguments.of(GroundCover(1f, 0f, 0f, 0f, 0f, false, false), "\"Puddles\""),
                Arguments.of(GroundCover(2f, 0f, 0f, 0f, 0f, false, false), "\"Puddles\""),
                Arguments.of(GroundCover(3f, 0f, 0f, 0f, 0f, false, false), "\"Light mud(0.3cm)\""),
                Arguments.of(GroundCover(9f, 0f, 0f, 0f, 0f, false, false), "\"Light mud(0.9cm)\""),
                Arguments.of(GroundCover(10f, 0f, 0f, 0f, 0f, false, false), "\"Medium mud(1cm)\""),
                Arguments.of(GroundCover(49f, 0f, 0f, 0f, 0f, false, false), "\"Medium mud(5cm)\""),
                Arguments.of(GroundCover(50f, 0f, 0f, 0f, 0f, false, false), "\"Deep mud(5cm)\""),
                Arguments.of(GroundCover(249f, 0f, 0f, 0f, 0f, false, false), "\"Deep mud(25cm)\""),
                Arguments.of(GroundCover(250f, 0f, 0f, 0f, 0f, false, false), "\"Very deep mud(25cm)\""),
                Arguments.of(GroundCover(0f, 1f, 0f, 0f, 0f, false, false), "\"Dusting of snow\""),
                Arguments.of(GroundCover(0f, 2f, 0f, 0f, 0f, false, false), "\"Dusting of snow\""),
                Arguments.of(GroundCover(0f, 3f, 0f, 0f, 0f, false, false), "\"Shallow snow(0.3cm)\""),
                Arguments.of(GroundCover(0f, 149f, 0f, 0f, 0f, false, false), "\"Shallow snow(15cm)\""),
                Arguments.of(GroundCover(0f, 150f, 0f, 0f, 0f, false, false), "\"Moderate snow(15cm)\""),
                Arguments.of(GroundCover(0f, 299f, 0f, 0f, 0f, false, false), "\"Moderate snow(30cm)\""),
                Arguments.of(GroundCover(0f, 300f, 0f, 0f, 0f, false, false), "\"Deep snow(30cm)\""),
                Arguments.of(GroundCover(0f, 599f, 0f, 0f, 0f, false, false), "\"Deep snow(60cm)\""),
                Arguments.of(GroundCover(0f, 600f, 0f, 0f, 0f, false, false), "\"Very deep snow(60cm)\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 9f, 0f, false, false), "\"Frozen shallow\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 10f, 0f, false, false), "\"Frozen\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 49f, 0f, false, false), "\"Frozen\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 50f, 0f, false, false), "\"Frozen deep\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 0f, 9f, false, false), "\"Frozen shallow\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 0f, 10f, false, false), "\"Frozen\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 0f, 49f, false, false), "\"Frozen\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 0f, 50f, false, false), "\"Frozen deep\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 4f, 5f, false, false), "\"Frozen shallow\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 5f, 5f, false, false), "\"Frozen\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 20f, 29f, false, false), "\"Frozen\""),
                Arguments.of(GroundCover(0f, 0f, 0f, 30f, 30f, false, false), "\"Frozen deep\""),
                Arguments.of(GroundCover(1f, 0f, 0f, 0f, 0f, true, false), "\"Frozen puddles\""),
                Arguments.of(GroundCover(9f, 0f, 0f, 0f, 0f, true, false), "\"Frozen light mud(0.9cm)\""),
                Arguments.of(GroundCover(49f, 0f, 0f, 0f, 0f, true, false), "\"Frozen medium mud(5cm)\""),
                Arguments.of(GroundCover(50f, 0f, 0f, 0f, 0f, true, false), "\"Frozen deep mud(5cm)\""),
                Arguments.of(GroundCover(0f, 300f, 0f, 30f, 30f, false, false), "\"Deep snow(30cm), frozen deep\""),
                Arguments.of(GroundCover(5f, 1f, 0f, 0f, 0f, false, false), "\"Dusting of snow, light mud(0.5cm)\""),
                Arguments.of(GroundCover(9f, 0f, 0f, 10f, 0f, true, false), "\"Frozen light mud(0.9cm), frozen\""),
                Arguments.of(
                    GroundCover(49f, 20f, 0f, 0f, 50f, false, false),
                    "\"Shallow snow(2cm), medium mud(5cm), frozen deep\""
                ),
                Arguments.of(
                    GroundCover(49f, 20f, 0f, 25f, 25f, true, false),
                    "\"Shallow snow(2cm), frozen medium mud(5cm), frozen deep\""
                ),
                Arguments.of(GroundCover(0f, 2f, 0f, 0f, 0f, false, true), "\"Dusting of snow(melting)\""),
                Arguments.of(GroundCover(0f, 149f, 0f, 0f, 0f, false, true), "\"Shallow snow(15cm,melting)\""),
                Arguments.of(GroundCover(0f, 150f, 0f, 0f, 0f, false, true), "\"Moderate snow(15cm,melting)\""),
                Arguments.of(GroundCover(0f, 300f, 0f, 0f, 0f, false, true), "\"Deep snow(30cm,melting)\""),
                Arguments.of(GroundCover(0f, 600f, 0f, 0f, 0f, false, true), "\"Very deep snow(60cm,melting)\""),
                Arguments.of(GroundCover(0f, 300f, 0f, 30f, 30f, false, true), "\"Deep snow(30cm,melting), frozen deep\""),
            )
        }
    }

    @ParameterizedTest(name = "For data {0} string is {1}")
    @MethodSource("groundValues")
    fun makeString(data: GroundCover, expected: String) {
        val result = GroundToString.makeString(data)
        assertEquals(expected, result)
    }
}