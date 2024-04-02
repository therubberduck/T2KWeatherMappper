package almanac

import converters.withTimeAtStartOfDay
import model.MoonPhaseFull
import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat
import java.io.File


class MoonPhaseCalc(file: File) {

    private lateinit var moonPhaseList: List<Pair<LocalDateTime, MoonPhaseFull>>

    init {
        val rawMoonRows = readCsv(file)
        convertRawMoonRows(rawMoonRows)
    }

    fun readCsv(file: File): MutableList<RawMoonRow> {
        val inputStream = file.inputStream()
        val reader = inputStream.bufferedReader()

        // Skip Header
        reader.readLine()

        // Convert remaining lines to model.RawWeatherHour
        val dateTimePattern = DateTimeFormat.forPattern("MM/dd/yyyy")
        return reader.lineSequence()
            .filter { it.isNotBlank() }
            .map {
                val entries = it.split(',', ignoreCase = false)
                RawMoonRow(
                    dateTime = LocalDateTime.parse(entries[0], dateTimePattern),
                    phase = when (entries[3]) {
                        "1" -> MoonPhaseFull.NEW
                        "2" -> MoonPhaseFull.FIRST_QUARTER
                        "3" -> MoonPhaseFull.FULL
                        "4" -> MoonPhaseFull.THIRD_QUARTER
                        else -> throw NotImplementedError()
                    }
                )
            }.toMutableList()
    }

    private fun convertRawMoonRows(rawRows: List<RawMoonRow>) {
        moonPhaseList = mutableListOf()
        rawRows.forEach { rawMoonRow ->
            val currentPhaseStartsAt = rawMoonRow.dateTime
            when (rawMoonRow.phase) {
                MoonPhaseFull.NEW -> {
                    moonPhaseList.addLast(currentPhaseStartsAt.minusDays(1) to MoonPhaseFull.NEW)
                    moonPhaseList.addLast(currentPhaseStartsAt.plusDays(2) to MoonPhaseFull.WAXING_CRESCENT)
                }

                MoonPhaseFull.FIRST_QUARTER -> {
                    moonPhaseList.addLast(currentPhaseStartsAt to MoonPhaseFull.FIRST_QUARTER)
                    moonPhaseList.addLast(currentPhaseStartsAt.plusDays(1) to MoonPhaseFull.WAXING_GIBBOUS)
                }

                MoonPhaseFull.FULL -> {
                    moonPhaseList.addLast(currentPhaseStartsAt.minusDays(1) to MoonPhaseFull.FULL)
                    moonPhaseList.addLast(currentPhaseStartsAt.plusDays(2) to MoonPhaseFull.WANING_GIBBOUS)
                }

                MoonPhaseFull.THIRD_QUARTER -> {
                    moonPhaseList.addLast(currentPhaseStartsAt to MoonPhaseFull.THIRD_QUARTER)
                    moonPhaseList.addLast(currentPhaseStartsAt.plusDays(1) to MoonPhaseFull.WANING_CRESCENT)
                }

                else -> throw NotImplementedError()
            }
        }
    }

    fun calc(dateTime: LocalDateTime): MoonPhaseFull {
        val justDate = dateTime.withTimeAtStartOfDay()
        val matchingMoonPhase = try {
            moonPhaseList.last { it.first <= justDate }.second
        } catch (e: NoSuchElementException) {
            throw NoSuchElementException(justDate.toString(), e)
        }

        return matchingMoonPhase
    }

    data class RawMoonRow(val dateTime: LocalDateTime, val phase: MoonPhaseFull)
}