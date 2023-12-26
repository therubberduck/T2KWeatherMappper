package model

import org.joda.time.DateTime

data class RawWeatherHour(
    val dto: DateTime,
    val tempK: Float,
    val dewPoint: Float,
    val feelsLikeK: Float,
    val humidity: Int,
    val windSpeed: Float,
    val windDegree: Int,
    val rain1h: Float?,
    val snow1h: Float?,
    val cloudCover: Int,
    val weatherMain: String,
    val weatherDesc: String
)
