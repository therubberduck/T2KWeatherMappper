package almanac

import model.SunriseSunset
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.io.File
import java.util.*

class SunriseCalc(file: File, val timeZoneAdjustment: Int) {

    private lateinit var sunriseSunsetList: Map<LocalDateTime, SunriseSunset>

    init {
        val rawDataRows = readCsv(file)
    }

    fun getSunriseSunsetFor(date: LocalDateTime): SunriseSunset {
        return sunriseSunsetList[date.withTime(0, 0, 0, 0)]
            ?: throw IndexOutOfBoundsException(date.toString("yyyy-MMM-dd HH:mm") + " not found")
    }

    fun readCsv(file: File) {
        val inputStream = file.inputStream()
        val reader = inputStream.bufferedReader()

        sunriseSunsetList = linesToData(reader.lineSequence())
    }

    fun linesToData(lines: Sequence<String>): Map<LocalDateTime, SunriseSunset> {
        val dateTimePattern = DateTimeFormat.forPattern("yyyy-MMM-dd HH:mm").withZoneUTC().withLocale(Locale.US)

        val tempSunriseSunsetList = mutableMapOf<LocalDateTime, SunriseSunset>()
        var sunriseDate: LocalDateTime? = null
        lines.filter { it.isNotBlank() }
            .forEachIndexed { index, line ->
                val entries = line.split(',', ignoreCase = false)
                val date = dateTimePattern.parseLocalDateTime(entries[0].trim())
                when {
                    index % 3 == 0 -> sunriseDate = date
                    index % 3 == 2 -> tempSunriseSunsetList.put(
                        date.withTime(0, 0, 0, 0),
                        SunriseSunset(
                            sunriseDate!!.toLocalTime().plusHours(timeZoneAdjustment),
                            date.toLocalTime().plusHours(timeZoneAdjustment)
                        )
                    )

                    else -> {}
                }
            }
        return tempSunriseSunsetList
    }
}