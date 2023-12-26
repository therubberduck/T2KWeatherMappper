package almanac

import model.GroundCover
import java.lang.Math.round
import java.text.DecimalFormat

object GroundToString {
    fun makeString(groundData: GroundCover): String {
        val snowString = when {
            groundData.snowDepth == 0f -> null
            groundData.snowDepth < 3f ->  "dusting of snow"
            groundData.snowDepth < 150f ->  "shallow snow"
            groundData.snowDepth < 300f ->  "moderate snow"
            groundData.snowDepth < 600f ->  "deep snow"
            else ->  "very deep snow"
        }
        var mudString = when {
            groundData.mudDepth == 0f -> null
            groundData.mudDepth < 3f ->  "puddles"
            groundData.mudDepth < 10f ->  "light mud"
            groundData.mudDepth < 50f ->  "medium mud"
            groundData.mudDepth < 250f ->  "deep mud"
            else ->  "very deep mud"
        }
        if(mudString != null && groundData.topMudFrozen) {
            mudString = "frozen $mudString"
        }
        val frozenGround = when {
            groundData.frozenMudDepth + groundData.frostDepth == 0f -> null
            groundData.frozenMudDepth + groundData.frostDepth < 10f -> "frozen shallow"
            groundData.frozenMudDepth + groundData.frostDepth < 50f -> "frozen"
            else ->  "frozen deep"
        }

        if(snowString == null && mudString == null && frozenGround == null) {
            return "—"
        }
        else {
            var output = ""
            if(snowString != null) {
                output += snowString
                if(groundData.snowDepth >= 3) {
                    output += "(${groundData.snowDepth.mmToCm()})"
                }
            }
            if(mudString != null && output.isNotEmpty()) {
                output += ", "
            }
            if(mudString != null) {
                output += mudString
                if(groundData.mudDepth >= 3) {
                    output += "(${groundData.mudDepth.mmToCm()})"
                }
            }
            if(frozenGround != null && output.isNotEmpty()) {
                output += ", "
            }
            if(frozenGround != null) {
                output += frozenGround
            }
            return "\"" + output.replaceFirstChar { it.uppercase() } + "\""
        }
    }

    val decimalFormat = DecimalFormat("0.#")
    private fun Float.mmToCm(): String {
        return if(this >= 1000) {
            "${decimalFormat.format(this/1000)}m"
        }
        else if(this >= 10f) {
            "${round(this/10)}cm"
        }
        else {
            decimalFormat.format(this/10) + "cm"
        }
    }
}