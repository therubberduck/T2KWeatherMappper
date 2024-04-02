package model

import almanac.Weather

data class AlmanacShift(
    val shift: Shift,
    val feltTemp: String,
    val temp: String,
    val weather: String,
    val cloudCover: String,
    val wind: String,
    val ground: String,
    val visibility: String,
    val nightVision: String,
    val moonPhase: MoonPhaseFull,
    val weatherData: Weather
)
