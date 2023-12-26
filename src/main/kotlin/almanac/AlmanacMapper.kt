import converters.readCsv
import java.io.File

object AlmanacMapper {
    fun makeAlmanac(file: File): List<AlmanacDay> {
        val fileInputStream = file.inputStream()
        val rawHourData = readCsv(fileInputStream)
        val rawDayData = rawHourData.chunkToDays()
        return rawDayData.map { rawDayData ->
            val shifts = rawDayData.map {rawShift -> rawShift.convertToAlmanac() }
            AlmanacDay(rawDayData.night.hour0.dto.withTimeAtStartOfDay(), shifts[0], shifts[1], shifts[2], shifts[3], "")
        }
    }

    fun List<RawWeatherHour>.chunkToDays(): List<RawWeatherDay> {
        val rawShiftData = this.chunked(6).map {
            RawWeatherShift(it[0], it[1], it[2], it[3], it[4], it[5])
        }
        return rawShiftData.chunked(4).map {
            RawWeatherDay(it[0], it[1], it[2], it[3])
        }
    }

    fun RawWeatherShift.convertToAlmanac(): AlmanacShift {
        val temp = ((this.sumOf { it.tempK.toDouble() } / 6) - 273.15).roundUpToFive() + " C⁰"

        return AlmanacShift("", temp, "", "", "", "", "", "", "")
    }
}