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
                Arguments.of(GroundCover(0f,0f,0f,0f,false), "—"),
                Arguments.of(GroundCover(1f,0f,0f,0f,false), "\"Puddles\""),
                Arguments.of(GroundCover(2f,0f,0f,0f,false), "\"Puddles\""),
                Arguments.of(GroundCover(3f,0f,0f,0f,false), "\"Light mud\""),
                Arguments.of(GroundCover(9f,0f,0f,0f,false), "\"Light mud\""),
                Arguments.of(GroundCover(10f,0f,0f,0f,false), "\"Medium mud\""),
                Arguments.of(GroundCover(49f,0f,0f,0f,false), "\"Medium mud\""),
                Arguments.of(GroundCover(50f,0f,0f,0f,false), "\"Deep mud\""),
                Arguments.of(GroundCover(249f,0f,0f,0f,false), "\"Deep mud\""),
                Arguments.of(GroundCover(250f,0f,0f,0f,false), "\"Very deep mud\""),
                Arguments.of(GroundCover(0f,1f,0f,0f,false), "\"Dusting of snow\""),
                Arguments.of(GroundCover(0f,2f,0f,0f,false), "\"Dusting of snow\""),
                Arguments.of(GroundCover(0f,3f,0f,0f,false), "\"Shallow snow\""),
                Arguments.of(GroundCover(0f,149f,0f,0f,false), "\"Shallow snow\""),
                Arguments.of(GroundCover(0f,150f,0f,0f,false), "\"Moderate snow\""),
                Arguments.of(GroundCover(0f,299f,0f,0f,false), "\"Moderate snow\""),
                Arguments.of(GroundCover(0f,300f,0f,0f,false), "\"Deep snow\""),
                Arguments.of(GroundCover(0f,599f,0f,0f,false), "\"Deep snow\""),
                Arguments.of(GroundCover(0f,600f,0f,0f,false), "\"Very deep snow\""),
                Arguments.of(GroundCover(0f,0f,9f,0f,false), "\"Frozen shallow\""),
                Arguments.of(GroundCover(0f,0f,10f,0f,false), "\"Frozen\""),
                Arguments.of(GroundCover(0f,0f,49f,0f,false), "\"Frozen\""),
                Arguments.of(GroundCover(0f,0f,50f,0f,false), "\"Frozen deep\""),
                Arguments.of(GroundCover(0f,0f,0f,9f,false), "\"Frozen shallow\""),
                Arguments.of(GroundCover(0f,0f,0f,10f,false), "\"Frozen\""),
                Arguments.of(GroundCover(0f,0f,0f,49f,false), "\"Frozen\""),
                Arguments.of(GroundCover(0f,0f,0f,50f,false), "\"Frozen deep\""),
                Arguments.of(GroundCover(0f,0f,4f,5f,false), "\"Frozen shallow\""),
                Arguments.of(GroundCover(0f,0f,5f,5f,false), "\"Frozen\""),
                Arguments.of(GroundCover(0f,0f,20f,29f,false), "\"Frozen\""),
                Arguments.of(GroundCover(0f,0f,30f,30f,false), "\"Frozen deep\""),
                Arguments.of(GroundCover(1f,0f,0f,0f,true), "\"Frozen puddles\""),
                Arguments.of(GroundCover(9f,0f,0f,0f,true), "\"Frozen light mud\""),
                Arguments.of(GroundCover(49f,0f,0f,0f,true), "\"Frozen medium mud\""),
                Arguments.of(GroundCover(50f,0f,0f,0f,true), "\"Frozen deep mud\""),
                Arguments.of(GroundCover(0f,300f,30f,30f,false), "\"Deep snow, frozen deep\""),
                Arguments.of(GroundCover(5f,1f,0f,0f,false), "\"Dusting of snow, light mud\""),
                Arguments.of(GroundCover(9f,0f,10f,0f,true), "\"Frozen light mud, frozen\""),
                Arguments.of(GroundCover(49f,20f,0f,50f,false), "\"Shallow snow, medium mud, frozen deep\""),
                Arguments.of(GroundCover(49f,20f,25f,25f,true), "\"Shallow snow, frozen medium mud, frozen deep\""),
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