package almanac

fun Double.KelvinToCelsius(): Double {
    return this - 273.15
}

/**
 * Calc wind chill temperature from a Celsius temperature
 *
 * @param windSpeed in m/s
 * @return
 */
fun Double.calcFeltTemp(windSpeed: Float, humidity: Int): FeltTemp {
    val tempCelsius = this
    val windTemp = if (tempCelsius > 20) {
        val fahrenheit = (tempCelsius * 9 / 5) + 32
        val heatIndexFahrenheit = (-42.379
                + 2.04901523 * fahrenheit
                + 10.14333127 * humidity
                - 0.22475541 * fahrenheit * humidity
                - 0.00683783 * fahrenheit * fahrenheit
                - .05481717 * humidity * humidity
                + .00122874 * fahrenheit * fahrenheit * humidity
                + .00085282 * fahrenheit * humidity * humidity
                - .00000199 * fahrenheit * fahrenheit * humidity * humidity)
        Math.round((heatIndexFahrenheit - 32) * 5 / 9).toInt()
    } else if (windSpeed == 0f) {
        Math.round(tempCelsius).toInt()
    } else {
        val windKmH = windSpeed * 3600.0 / 1000.0
        Math.round(
            13.12 + 0.6215 * tempCelsius -
                    11.37 * Math.pow(windKmH, 0.16) +
                    0.3965 * tempCelsius * Math.pow(windKmH, 0.16)
        ).toInt()
    }
    return when {
        windTemp <= -50 -> FeltTemp.STYGIAN
        windTemp <= -30 -> FeltTemp.ARCTIC
        windTemp <= -5 -> FeltTemp.FREEZING
        windTemp <= 10 -> FeltTemp.CHILLY
        windTemp <= 17 -> FeltTemp.MILD
        windTemp <= 24 -> FeltTemp.COMFY
        windTemp <= 35 -> FeltTemp.WARM
        windTemp <= 45 -> FeltTemp.HOT
        else -> FeltTemp.INFERNAL
    }
}

enum class FeltTemp(val label: String) {
    STYGIAN("Stygian (4)"),
    ARCTIC("Arctic (3)"),
    FREEZING("Freezing (2)"),
    CHILLY("Chilly (1)"),
    MILD("Mild"),
    COMFY("Comfy"),
    WARM("Warm"),
    HOT("Hot"),
    INFERNAL("Infernal"),
}