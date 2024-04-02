package model

import org.joda.time.LocalDateTime
import org.joda.time.LocalTime

data class AlmanacDay(
    val dateTime: LocalDateTime,
    val moonPhase: String,
    val sunrise: LocalTime,
    val sunset: LocalTime,
    val night: AlmanacShift,
    val morning: AlmanacShift,
    val afternoon: AlmanacShift,
    val evening: AlmanacShift,
    val events: String
)
