package almanac

import almanac.AlmanacWeatherGenerator.DominantWeather
import almanac.AlmanacWeatherGenerator.generateAlmanacWeather
import almanac.Prettyfier.celsiusToPrettyString
import converters.readCsv
import events.DailyEventsMapper
import model.*
import roundToFiveInt
import java.io.File

class AlmanacMapper(private val moonPhaseCalc: MoonPhaseCalc? = null, private val visibilityConverter: VisibilityConverter, startingGroundCover: GroundCover) {

    private val groundCoverBucket = GroundCoverBucket(startingGroundCover)
    private val dailyEventsMapper = DailyEventsMapper(visibilityConverter)

    fun makeAlmanac(file: File): List<AlmanacDay> {
        val fileInputStream = file.inputStream()
        val rawHourData = readCsv(fileInputStream)
        val rawDaysData = chunkToDays(rawHourData)
        return rawDaysData.map { rawDayData ->
            val shifts = rawDayData.map {rawShift -> convertToAlmanac(rawShift) }
            val collectedRawHoursForDay = rawDayData.night + rawDayData.morning + rawDayData.afternoon + rawDayData.evening
            val weatherOfDay = shifts.map { it.weatherData }
            val moonPhases = listOf(shifts.first().moonPhase, shifts.last().moonPhase)
            AlmanacDay(
                rawDayData.night.hour0.dto.withTimeAtStartOfDay(),
                shifts[0],
                shifts[1],
                shifts[2],
                shifts[3],
                dailyEventsMapper.mapDay(collectedRawHoursForDay, weatherOfDay, moonPhases)
            )
        }
    }

    fun chunkToDays(hours: List<RawWeatherHour>): List<RawWeatherDay> {
        val rawShiftData = hours.chunked(6).map {
            RawWeatherShift(it[0], it[1], it[2], it[3], it[4], it[5])
        }
        return rawShiftData.chunked(4).map {
            RawWeatherDay(it[0], it[1], it[2], it[3])
        }
    }

    fun convertToAlmanac(rawShift: RawWeatherShift): AlmanacShift {
        // Calculate shift
        val currentShift = Shift.fromHour(rawShift.hour0.dto.hourOfDay)

        // Calculate moonphase
        // Move night / morning shift to previous day, since moons are the moon of "the night of"
        val moonDate = rawShift.hour0.dto.minusHours(12).withTimeAtStartOfDay()
        val moonPhase = moonPhaseCalc?.calc(moonDate) ?: MoonPhaseFull.FULL

        // Generate Temperature
        val shiftTemp = rawShift.averageShiftHours { it.tempK.toDouble() }.reduceTempForNuclearWinter(rawShift.hour0.dto).KelvinToCelsius()
        val tempString = shiftTemp.celsiusToPrettyString()
        val roundedTemp = shiftTemp.roundToFiveInt()

        val shiftWind = rawShift.averageShiftHours { it.windSpeed.toDouble() }.toFloat()
        val shiftHumidity = rawShift.averageShiftHours { it.humidity.toDouble() }.toInt()

        val feltTemp = shiftTemp.calcFeltTemp(shiftWind, shiftHumidity)

        // Generate Cloud Cover
        val shiftCover = rawShift.averageShiftHours { it.cloudCover.toDouble() }.toInt()

        val roundedCover = shiftCover.roundCloudsCoverageTo25()

        // Generate Weather
        val shiftRainAmount = rawShift.averageShiftHours { it.rain1h?.toDouble() }.zeroEqualsNull()
        val shiftSnowAmount = rawShift.averageShiftHours { it.snow1h?.toDouble() }.zeroEqualsNull()

        val dominantWeather = DominantWeather(rawShift, roundedTemp)

        val weatherData = generateAlmanacWeather(roundedTemp, shiftRainAmount, shiftSnowAmount, roundedCover, dominantWeather)

        // Generate Wind
        val shiftWindSpeed = rawShift.averageShiftHours { it.windSpeed.toDouble() }.toFloat()
        val shiftWindDir = rawShift.averageShiftHours { it.windDegree.toDouble() }.toInt()

        val windValue = generateWinds(shiftWindSpeed, shiftWindDir)
        val windDescription = generateAlmanacWindEntry(windValue)

        // Visibility
        val visibility = visibilityConverter.getVisibility(currentShift, moonPhase.simple, weatherData)

        val nvVision = if(currentShift == Shift.EVENING || currentShift == Shift.NIGHT) {
            visibilityConverter.getVisibilityDay(weatherData.dominant, weatherData.precipitation)
        }
        else {
            Visibility.NOT_APPLICABLE
        }

        // Ground Cover
        val dewPoint = rawShift.averageShiftHours { it.dewPoint.toDouble() }.KelvinToCelsius().toFloat()
        val shiftTotalRainAmount = rawShift.sumOf { it.rain1h?.toDouble() ?: 0.0 }.toFloat()
        val shiftTotalSnowAmount = rawShift.sumOf { it.snow1h?.toDouble() ?: 0.0 }.toFloat()
        val rawGroundCover = groundCoverBucket.addGroundCoverWith(shiftTotalRainAmount, shiftTotalSnowAmount, tempC = shiftTemp, dewPoint)
        val groundCoverString = GroundToString.makeString(rawGroundCover)

        return AlmanacShift(
            feltTemp = feltTemp.label,
            temp = tempString,
            weather = weatherData.description,
            cloudCover = "$roundedCover%",
            wind = windDescription,
            ground = groundCoverString,
            visibility = visibility.desc,
            nightVision = nvVision.desc,
            moonPhase = moonPhase.simple,
            weatherData = weatherData
        )
    }

    private fun RawWeatherShift.averageShiftHours(variable: (RawWeatherHour) -> Double?): Double {
        return this.sumOf { variable(it) ?: 0.0 } / 6
    }

    private fun Double.zeroEqualsNull(): Float? {
        return if(this == 0.0) {
            null
        }
        else {
            this.toFloat()
        }
    }
}