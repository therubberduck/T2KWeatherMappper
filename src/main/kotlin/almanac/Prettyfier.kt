package almanac

import roundToFive

object Prettyfier {

    fun Double.celsiusToPrettyString(): String {
        return this.roundToFive() + " C⁰"
    }
}