package model

enum class Shift(val gameName: String) {
    NIGHT("Night"),
    MORNING("Morning"),
    AFTERNOON("Day"),
    EVENING("Evening");

    companion object {
        fun fromHour(hour: Int): Shift {
            return when {
                hour <= 5 -> NIGHT
                hour <= 11 -> MORNING
                hour <= 17 -> AFTERNOON
                hour <= 23 -> EVENING
                else -> throw NotImplementedError()
            }
        }
    }
}