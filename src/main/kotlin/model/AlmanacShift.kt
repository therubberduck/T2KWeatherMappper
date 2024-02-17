package model

import almanac.Weather
import almanac.WindSpeed

data class AlmanacShift(
    val feltTemp: String,
    val temp: String,
    val weather: String,
    val cloudCover: String,
    val wind: String,
    val ground: String,
    val visibility: String,
    val nightVision: String,
    val moonPhase: MoonPhase,
    val weatherData: Weather,
    val windData: WindSpeed,
)
