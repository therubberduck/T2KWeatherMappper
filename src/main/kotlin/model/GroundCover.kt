package model

data class GroundCover(val mudDepth: Float, val snowDepth: Float, val frozenMudDepth: Float, val frostDepth: Float, val topMudFrozen: Boolean, val evaporation: Int = 0, val mudAdjust: Float = 0f)
