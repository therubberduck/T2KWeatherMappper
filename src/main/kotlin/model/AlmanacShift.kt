package model

data class AlmanacShift(
    val feltTemp: String,
    val temp: String,
    val weather: String,
    val cloudCover: String,
    val wind: String,
    val ground: String,
    val visibility: String,
    val nightVision: String,
    val rainMm: String,
    val evaporation: String,
    val mudAdjust: String,
    val mudDepth: String,
    val frostDepth: String,
    val snowDepth: String
)
