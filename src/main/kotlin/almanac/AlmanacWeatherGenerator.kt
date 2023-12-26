package almanac

fun generateAlmanacWeather(tempC: Double, rainAmount: Float?, snowAmount: Float?, cloudCover: Int, weatherMain: String): String {
    val rain = rainAmount.toRain()
    val snow = snowAmount.toSnow()
    val specialWeather = weatherMain.weatherMainToSpecialWeather()
    return if(rain != Rain.None || snow != Snow.None) {
        val rainSnow = getRainSnowDescription(tempC, rain, snow)
        if(specialWeather != null) {
            if(specialWeather == "Thunderstorm") {
                "Thunderstorm with ${rainSnow.lowercase()}"
            }
            else {
                "$rainSnow and ${specialWeather.lowercase()}"
            }
        }
        else {
            rainSnow
        }
    }
    else if(specialWeather != null) {
        specialWeather
    }
    else {
        cloudCover.cloudCoverToClouds()
    }
}

fun String.weatherMainToSpecialWeather(): String? {
    return when(this) {
        "Mist" -> "Mist"
        "Fog" -> "Fog"
        "Thunderstorm" -> "Thunderstorm"
        else -> null
    }
}

fun Int.cloudCoverToClouds(): String {
    return when(this) {
        25 -> "Passing clouds"
        50 -> "Scattered clouds"
        75 -> "Broken clouds"
        100 -> "Overcast"
        else -> "Clear"
    }
}

fun getRainSnowDescription(tempC: Double, rain: Rain, snow: Snow): String {
    return if(tempC <= 0) {
        if(snow != Snow.None) {
            snow.desc
        }
        else {
            when(rain) {
                Rain.None -> throw IllegalStateException()
                Rain.Light -> Snow.Light.desc
                Rain.Moderate -> Snow.Moderate.desc
                Rain.Heavy -> Snow.Heavy.desc
            }
        }
    }
    else {
        if(rain != Rain.None) {
            rain.desc
        }
        else {
            when(snow) {
                Snow.None -> throw IllegalStateException()
                Snow.Light -> Rain.Light.desc
                Snow.Moderate -> Rain.Moderate.desc
                Snow.Heavy -> Rain.Heavy.desc
            }
        }
    }
}

fun Float?.toRain(): Rain {
    return when{
        this == null -> Rain.None
        this < 1 -> Rain.Light
        this < 4 -> Rain.Moderate
        else -> Rain.Heavy
    }
}

fun Float?.toSnow(): Snow {
    return when () {
        "light snow" -> Snow.Light
        "snow" -> Snow.Moderate
        "heavy snow" -> Snow.Heavy
        else -> {
            println("Failed snow with $snowDescription")
            Snow.None
        }
    }
}

enum class Rain(val desc: String) {
    None(""),
    Light("Light rain"),
    Moderate("Moderate rain"),
    Heavy("Heavy rain")
}

enum class Snow(val desc: String) {
    None(""),
    Light("Light snow"),
    Moderate("Snow"),
    Heavy("Heavy snow")
}