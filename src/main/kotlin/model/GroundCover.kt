package model

data class GroundCover(
    val mudDepth: Float,
    val snowDepth: Float, // Full snow height in mm
    val packedSnowDepth: Float, // Mm of the snow that is packed. Never more than half height.
    val frozenMudDepth: Float,
    val frostDepth: Float,
    val topMudFrozen: Boolean
)
