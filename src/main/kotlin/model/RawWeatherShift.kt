package model

data class RawWeatherShift(
    val hour0: RawWeatherHour,
    val hour1: RawWeatherHour,
    val hour2: RawWeatherHour,
    val hour3: RawWeatherHour,
    val hour4: RawWeatherHour,
    val hour5: RawWeatherHour
) : Iterable<RawWeatherHour> {
    override fun iterator(): Iterator<RawWeatherHour> {
        return listOf(hour0, hour1, hour2, hour3, hour4, hour5).iterator()
    }
}