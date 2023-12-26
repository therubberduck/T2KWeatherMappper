package almanac

import org.joda.time.DateTime

fun Double.reduceTempForNuclearWinter(date: DateTime): Double {
    return if(date.monthOfYear in 4..9) {
        this - 5.0
    }
    else {
        this - 10.0
    }
}