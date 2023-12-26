package almanac

fun Int.roundCloudsCoverageTo25(): Int {
    return (Math. round(this / 25.0) * 25).toInt()
}