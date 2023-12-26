data class RawWeatherDay(
    val night: RawWeatherShift,
    val morning: RawWeatherShift,
    val afternoon: RawWeatherShift,
    val evening: RawWeatherShift,
) : Iterable<RawWeatherShift> {
    override fun iterator(): Iterator<RawWeatherShift> {
        return listOf(night, morning, afternoon, evening).iterator()
    }
}
