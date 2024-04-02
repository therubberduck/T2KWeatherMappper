package converters

import org.joda.time.LocalDateTime

fun LocalDateTime.withTimeAtStartOfDay(): LocalDateTime? {
    return this.withTime(0,0,0,0)
}