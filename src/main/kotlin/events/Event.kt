package events

import model.Shift

data class Event(val shift: Shift, val time: String, val eventText: String)
