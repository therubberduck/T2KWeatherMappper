fun Double.roundUpToFive(): String {
    return (Math.ceil(this / 5) * 5).toInt().toString()
}

fun Double.roundToFive(): String {
    return (Math.round(this / 5) * 5).toInt().toString()
}