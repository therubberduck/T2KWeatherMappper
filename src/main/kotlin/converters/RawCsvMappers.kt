package converters

import model.AlmanacDay
import model.RawWeatherHour
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormatterBuilder
import org.joda.time.format.ISODateTimeFormat
import java.io.File
import java.io.InputStream

fun readCsv(inputStream: InputStream): List<RawWeatherHour> {
    val reader = inputStream.bufferedReader()

    // Skip Header
    reader.readLine()

    // Convert remaining lines to model.RawWeatherHour
    return reader.lineSequence()
        .filter { it.isNotBlank() }
        .map {
            val entries = it.split(',', ignoreCase = false, limit = 28)
            RawWeatherHour(
                dto = convertCsvStringToDateTime(entries[1]),
                tempK = entries[6].toFloat(),
                dewPoint = entries[8].toFloat(),
                feelsLikeK = entries[9].toFloat(),
                humidity = entries[15].toInt(),
                windSpeed = entries[16].toFloat(),
                windDegree = entries[17].toInt(),
                rain1h = entries[19].toFloatOrNull(),
                snow1h = entries[21].toFloatOrNull(),
                cloudCover = entries[23].toInt(),
                weatherMain = entries[25],
                weatherDesc = entries[26]
            )
        }.toList()
}

fun writeCsv(almanac: List<AlmanacDay>, outputFile: File) {
    val outputStream = outputFile.outputStream()
    val writer = outputStream.bufferedWriter(Charsets.UTF_8)

    val builder = DateTimeFormatterBuilder()

    almanac.forEach {day ->
        val localDate = day.dateTime.toLocalDate()
        val date = localDate.toString("MMM d") + getDayOfMonthSuffix(localDate.dayOfMonth)
        writer.write("${date},,Shift,Felt Temp,Temp,Weather,CC,Wind,Ground,Visibility,NV")
        writer.newLine()
        writer.write(",,Night,${day.night.feltTemp},${day.night.temp},${day.night.weather},${day.night.cloudCover},${day.night.wind},${day.night.ground},${day.night.visibility},${day.night.nightVision}")
        writer.newLine()
        writer.write(",,Morning,${day.morning.feltTemp},${day.morning.temp},${day.morning.weather},${day.morning.cloudCover},${day.morning.wind},${day.morning.ground},${day.morning.visibility},${day.morning.nightVision}")
        writer.newLine()
        writer.write(",,Day,${day.afternoon.feltTemp},${day.afternoon.temp},${day.afternoon.weather},${day.afternoon.cloudCover},${day.afternoon.wind},${day.afternoon.ground},${day.afternoon.visibility},${day.afternoon.nightVision}")
        writer.newLine()
        writer.write("${day.moonPhase},,Evening,${day.evening.feltTemp},${day.evening.temp},${day.evening.weather},${day.evening.cloudCover},${day.evening.wind},${day.evening.ground},${day.evening.visibility},${day.evening.nightVision}")
        writer.newLine()
        writer.write(",,\"${day.events}\"")
        writer.newLine()
    }
    writer.close()
    println("Almanac saved to ${outputFile.name}")
}

private fun getDayOfMonthSuffix(n: Int): String {
    if (n in 11..13) {
        return "th"
    }
    return when (n % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}

private fun convertCsvStringToDateTime(textDate: String): DateTime {
    val adjustedDateString = textDate.dropLast(10).replaceRange(10, 11, "T") + ".000Z"
    return ISODateTimeFormat.dateTime().withZoneUTC().parseDateTime(adjustedDateString)
}