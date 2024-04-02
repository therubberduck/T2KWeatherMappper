package model

enum class MoonPhase {
    FULL,
    HALF,
    CRESCENT,
    NEW
}

enum class MoonPhaseFull(val simple: MoonPhase, val letter: String) {
    NEW(MoonPhase.NEW, "q"),
    WAXING_CRESCENT(MoonPhase.CRESCENT, "w"),
    FIRST_QUARTER(MoonPhase.HALF, "e"),
    WAXING_GIBBOUS(MoonPhase.HALF, "r"),
    FULL(MoonPhase.FULL, "t"),
    WANING_GIBBOUS(MoonPhase.HALF, "y"),
    THIRD_QUARTER(MoonPhase.HALF, "u"),
    WANING_CRESCENT(MoonPhase.CRESCENT, "i"),
}