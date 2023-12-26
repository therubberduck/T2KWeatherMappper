package almanac

import model.GroundCover

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
            }
            if(mudString != null && output.isNotEmpty()) {
                output += ", "
            }
            if(mudString != null) {
                output += mudString
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
}