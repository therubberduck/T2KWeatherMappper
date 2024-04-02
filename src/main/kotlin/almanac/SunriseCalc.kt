package almanac

import model.MoonPhaseFull
import model.SunriseSunset
import org.joda.time.DateTime
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.io.File

class SunriseCalc(file: File, val timeZoneAdjustment: Int) {

    private lateinit var sunriseSunsetList: Map<DateTime, SunriseSunset>

    init {
        val rawDataRows = readCsv(file)
    }

    fun readCsv(file: File) {
        val inputStream = file.inputStream()
        val reader = inputStream.bufferedReader()

        // Skip Header
        reader.readLine()

        sunriseSunsetList = linesToData(reader.lineSequence())
    }

    fun linesToData(lines: Sequence<String>): Map<DateTime, SunriseSunset> {
        val dateTimePattern = DateTimeFormat.forPattern("yyyy-MMM-dd HH:mm")

        val tempSunriseSunsetList = mutableMapOf<DateTime, SunriseSunset>()
        var sunriseDate: DateTime? = null
        lines.filter { it.isNotBlank() }
            .forEachIndexed { index, line ->
                val entries = line.split(',', ignoreCase = false)
                val date = DateTime.parse(entries[0].trim(), dateTimePattern)
                when {
                    index % 3 == 0 -> sunriseDate = date
                    index % 3 == 2 -> tempSunriseSunsetList.put(
                        date.withTimeAtStartOfDay(),
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