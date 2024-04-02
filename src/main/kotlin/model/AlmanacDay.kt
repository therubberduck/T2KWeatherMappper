package model

import org.joda.time.DateTime

data class AlmanacDay(
    val dateTime: DateTime,
    val moonPhase: String,
    val night: AlmanacShift,
    val morning: AlmanacShift,
    val afternoon: AlmanacShift,
    val evening: AlmanacShift,
    val events: String
)
