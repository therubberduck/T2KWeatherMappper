package model

enum class Shift {
    NIGHT,
    MORNING,
    AFTERNOON,
    EVENING;

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