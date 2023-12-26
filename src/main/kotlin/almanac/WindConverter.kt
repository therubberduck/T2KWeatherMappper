package almanac

fun generateAlmanacWindEntry(wind: Wind): String {
    return if(wind.speed == WindSpeed.CalmL) {
        wind.speed.desc
    }
    else {
        wind.speed.desc + " " + wind.dir.desc
    }
}

fun generateWinds(rawWindSpeed: Float, rawWindDirection: Int): Wind {
    val wind = generateWindSpeed(rawWindSpeed)
    val dir = generateWindDir(rawWindDirection)
    return Wind(wind, dir)
}

fun generateWindSpeed(rawWindSpeed: Float): WindSpeed {
    return when {
        rawWindSpeed < 2 -> WindSpeed.CalmL
        rawWindSpeed <= 5 -> WindSpeed.CalmS
        rawWindSpeed <= 11 -> WindSpeed.LightBreezeL
        rawWindSpeed <= 19 -> WindSpeed.LightBreezeS
        rawWindSpeed <= 28 -> WindSpeed.FreshBreezeL
        rawWindSpeed <= 38 -> WindSpeed.FreshBreezeS
        rawWindSpeed <= 49 -> WindSpeed.StrongBreezeL
        rawWindSpeed <= 61 -> WindSpeed.StrongBreezeS
        rawWindSpeed <= 74 -> WindSpeed.GaleL
        rawWindSpeed <= 88 -> WindSpeed.GaleS
        rawWindSpeed <= 102 -> WindSpeed.StormL
        rawWindSpeed <= 117 -> WindSpeed.StormS
        else -> WindSpeed.Hurricane
    }
}

fun generateWindDir(rawWindDirection: Int): WindDir {
    return when {
        rawWindDirection < 30 -> WindDir.N
        rawWindDirection < 90 -> WindDir.NE
        rawWindDirection < 150 -> WindDir.SE
        rawWindDirection < 210 -> WindDir.S
        rawWindDirection < 270 -> WindDir.SW
        rawWindDirection < 330 -> WindDir.NW
        else -> WindDir.N
    }
}

data class Wind(val speed: WindSpeed, val dir: WindDir)

enum class WindSpeed(val desc: String) {
    CalmL("Calmˡ"),
    CalmS("Calmˢ"),
    LightBreezeL("Light Breezeˡ"),
    LightBreezeS("Light Breezeˢ"),
    FreshBreezeL("Fresh Breezeˡ"),
    FreshBreezeS("Fresh Breezeˢ"),
    StrongBreezeL("Strong Breezeˡ"),
    StrongBreezeS("Strong Breezeˢ"),
    GaleL("Galeˡ"),
    GaleS("Galeˢ"),
    StormL("Stormˡ"),
    StormS("Stormˢ"),
    Hurricane("Hurricane"),
}

enum class WindDir(val desc: String) {
    NW("from NW"),
    N("from N"),
    NE("from NE"),
    SE("from SE"),
    S("from S"),
    SW("from SW")
}