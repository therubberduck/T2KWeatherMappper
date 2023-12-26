package almanac

import model.RawWeatherShift

object AlmanacWeatherGenerator {
    fun generateAlmanacWeather(
        tempC: Double,
        rainAmount: Float?,
        snowAmount: Float?,
        cloudCover: Int,
        dominant: DominantWeather
    ): Weather {
        val rain = rainAmount.toRain()
        val snow = snowAmount.toSnow()

        return when (dominant) {
            DominantWeather.Clouds -> Weather(dominant, description = cloudCover.cloudCoverToClouds())
            DominantWeather.Rain,
            DominantWeather.Snow -> {
                val precipitation = convertRainSnowForTemp(tempC, rain, snow)
                getWeatherForPrecipitation(dominant, precipitation)
            }

            DominantWeather.Mist -> {
                if (rain != Rain.None || snow != Snow.None) {
                    val precipitation = convertRainSnowForTemp(tempC, rain, snow)
                    getWeatherForPrecipitation(dominant, precipitation, suffix = "and mist")
                } else {
                    Weather(dominant, description = "Mist")
                }
            }

            DominantWeather.Fog -> {
                if (rain != Rain.None || snow != Snow.None) {
                    val precipitation = convertRainSnowForTemp(tempC, rain, snow)
                    getWeatherForPrecipitation(dominant, precipitation, suffix = "and fog")
                } else {
                    Weather(dominant, description = "Fog")
                }
            }

            DominantWeather.Thunderstorm -> {
                if (rain != Rain.None || snow != Snow.None) {
                    val precipitation = convertRainSnowForTemp(tempC, rain, snow)
                    getWeatherForPrecipitation(dominant, precipitation, prefix = "Thunderstorm with")
                } else {
                    Weather(dominant, description = "Thunderstorm")
                }
            }
        }
    }

    fun DominantWeather(rawWeatherShift: RawWeatherShift, shiftTemp: Double): DominantWeather {
        return when {
            rawWeatherShift.fourPlusMatchesMainTag(DominantWeather.Thunderstorm.mainTag) -> DominantWeather.Thunderstorm
            rawWeatherShift.fourPlusMatchesMainTag(DominantWeather.Snow.mainTag) -> {
                if (shiftTemp > 0.0) {
                    DominantWeather.Rain
                } else {
                    DominantWeather.Snow
                }
            }

            rawWeatherShift.fourPlusMatchesMainTag(DominantWeather.Rain.mainTag) -> {
                if (shiftTemp > 0.0) {
                    DominantWeather.Rain
                } else {
                    DominantWeather.Snow
                }
            }

            rawWeatherShift.fourPlusMatchesMainTag(DominantWeather.Fog.mainTag) -> DominantWeather.Fog
            rawWeatherShift.fourPlusMatchesMainTag(DominantWeather.Mist.mainTag) -> DominantWeather.Mist
            else -> DominantWeather.Clouds
        }
    }

    private fun RawWeatherShift.fourPlusMatchesMainTag(match: String): Boolean {
        val count = this.count { hour ->
            hour.weatherMain == match
        }
        return count >= 4
    }

    fun String.weatherMainToSpecialWeather(): String? {
        return when (this) {
            "Mist" -> "Mist"
            "Fog" -> "Fog"
            "Thunderstorm" -> "Thunderstorm"
            else -> null
        }
    }

    fun Int.cloudCoverToClouds(): String {
        return when (this) {
            25 -> "Passing clouds"
            50 -> "Scattered clouds"
            75 -> "Broken clouds"
            100 -> "Overcast"
            else -> "Clear"
        }
    }

    fun getWeatherForPrecipitation(
        dominant: DominantWeather,
        precipitation: Precipitation,
        prefix: String? = null,
        suffix: String? = null
    ): Weather {
        val description = when {
            prefix != null -> prefix + " " + precipitation.desc.lowercase()
            suffix != null -> precipitation.desc + " " + suffix.lowercase()
            else -> precipitation.desc
        }
        return Weather(dominant, precipitation, description)
    }

    fun convertRainSnowForTemp(tempC: Double, rain: Rain, snow: Snow): Precipitation {
        return if (tempC <= 0) {
            if (snow != Snow.None) {
                snow
            } else {
                when (rain) {
                    Rain.None -> throw IllegalStateException()
                    Rain.Light -> Snow.Light
                    Rain.Moderate -> Snow.Moderate
                    Rain.Heavy -> Snow.Heavy
                }
            }
        } else {
            if (rain != Rain.None) {
                rain
            } else {
                when (snow) {
                    Snow.None -> throw IllegalStateException()
                    Snow.Light -> Rain.Light
                    Snow.Moderate -> Rain.Moderate
                    Snow.Heavy -> Rain.Heavy
                }
            }
        }
    }

    fun Float?.toRain(): Rain {
        return when {
            this == null -> Rain.None
            this < 1 -> Rain.Light
            this < 4 -> Rain.Moderate
            else -> Rain.Heavy
        }
    }

    fun Float?.toSnow(): Snow {
        return when {
            this == null -> Snow.None
            this < 1 -> Snow.Light
            this < 2 -> Snow.Moderate
            else -> Snow.Heavy
        }
    }
}

data class Weather(
    val dominant: DominantWeather,
    val precipitation: Precipitation = None,
    val description: String
)

interface Precipitation {
    val desc: String
}

object None : Precipitation{
    override val desc: String = ""
}

enum class Rain(override val desc: String) : Precipitation {
    None(""),
    Light("Light rain"),
    Moderate("Moderate rain"),
    Heavy("Heavy rain")
}

enum class Snow(override val desc: String) : Precipitation {
    None(""),
    Light("Light snow"),
    Moderate("Snow"),
    Heavy("Heavy snow")
}

enum class DominantWeather(val mainTag: String) {
    Clouds("Clouds"),
    Rain("Rain"),
    Snow("Snow"),
    Mist("Mist"),
    Fog("Fog"),
    Thunderstorm("Thunderstorm")
}