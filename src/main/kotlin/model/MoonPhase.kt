package model

enum class MoonPhase {
    FULL,
    HALF,
    CRESCENT,
    NEW
}

enum class MoonPhaseFull(val simple: MoonPhase) {
    NEW(MoonPhase.NEW),
    WAXING_CRESCENT(MoonPhase.CRESCENT),
    FIRST_QUARTER(MoonPhase.HALF),
    WAXING_GIBBOUS(MoonPhase.HALF),
    FULL(MoonPhase.FULL),
    WANING_GIBBOUS(MoonPhase.HALF),
    THIRD_QUARTER(MoonPhase.HALF),
    WANING_CRESCENT(MoonPhase.CRESCENT),
}