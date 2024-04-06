package events

import model.Shift

data class EventShift(val shift: Shift, val events: List<Event>)
