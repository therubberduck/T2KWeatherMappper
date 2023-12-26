package almanac

import org.joda.time.DateTime

fun Double.reduceTempForNuclearWinter(date: DateTime): Double {
    return if(date.monthOfYear in 5..8) {
        this - 2.5
    }
    else if(date.monthOfYear in 3..10) {
        this - 5.0
    }
    else {
        this - 7.5
    }
}