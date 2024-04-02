package events

import almanac.*
import almanac.KelvinToCelsius
import mock.IVisibilityConverter
import model.MoonPhase
import model.RawWeatherHour
import model.Shift
import kotlin.math.roundToInt

class DailyEventsMapper(
    private val visibilityConverter: IVisibilityConverter
) {

    fun mapDay(
        rawHours: List<RawWeatherHour>,
        shiftWeather: List<Weather>,
        moonPhases: List<MoonPhase>
    ): String {
        if (rawHours.size != 24) {
            throw IllegalStateException("Day data had wrong number of hours")
        }

        val mappedHours = rawHours.mapIndexed { index, rawWeatherHour -> mapHour(rawWeatherHour, index) }
        val events = collectEvents(mappedHours, shiftWeather, moonPhases)
        var prevEvent: WeatherEvent? = null
        var eventDescriptions = ""
        events.forEach {
            if(prevEvent == null) {
                eventDescriptions += Shift.fromHour(it.start).gameName + ":"
            }
            else if(Shift.fromHour(prevEvent?.end ?: 0) != Shift.fromHour(it.start)) {
                eventDescriptions = eventDescriptions.removeSuffix(", ")
                eventDescriptions += "\n" + Shift.fromHour(it.start).gameName + ":"
            }
            eventDescriptions += (mapEventCollectionToDescription(it)) + ", "
            prevEvent = it
        }
        return if(eventDescriptions.isNotEmpty()) {
            eventDescriptions.removeSuffix(", ")
        }
        else {
            "No weather events"
        }
    }

    fun mapHour(rawHour: RawWeatherHour, index: Int): WeatherEventHour {
        val tempC = rawHour.tempK.toDouble().reduceTempForNuclearWinter(rawHour.dto).KelvinToCelsius().roundToInt()
        val rain = rawHour.rain1h.toRain()
        val snow = rawHour.snow1h.toSnow()
        val precipitation = if (rain != Rain.None || snow != Snow.None) {
            AlmanacWeatherGenerator.convertRainSnowForTemp(tempC, rain, snow)
        } else {
            Rain.None
        }
        return WeatherEventHour(index, mapDominantWeather(rawHour, tempC), precipitation)
    }

    fun collectEvents(eventHours: List<WeatherEventHour>, shiftWeather: List<Weather>, moonPhases: List<MoonPhase>): List<WeatherEvent> {
        // Gather the events
        val events = mutableListOf<WeatherEvent>()
        for (eventHour in eventHours) {
            if (eventHour.weather != DominantWeather.Clouds) {
                if (events.lastOrNull() != null &&
                    eventHour.hour != 6 && eventHour.hour != 12 && eventHour.hour != 18 && // Beginning a new shift (with new visibility)
                    events.last().end == eventHour.hour - 1 && // Previous eventHour needs to be the same as the event we are looking at
                    events.last().weather == eventHour.weather && events.last().precipitation == eventHour.precipitation // Weather needs to be the same
                ) {
                    events.last().end = eventHour.hour
                } else {
                    val weatherOfShift = shiftWeather.fromHour(eventHour.hour)
                    val moonPhase = if(eventHour.hour < 12) {
                        moonPhases[0]
                    }
                    else {
                        moonPhases[1]
                    }
                    events.add(
                        WeatherEvent(
                            eventHour.hour,
                            eventHour.hour,
                            eventHour.weather,
                            eventHour.precipitation,
                            visibilityConverter.getVisibility(
                                Shift.fromHour(eventHour.hour),
                                moonPhase,
                                eventHour.weather,
                                eventHour.precipitation,
                                weatherOfShift.roundedCloudCover
                            )
                        )
                    )
                }
            }
        }

        // Compare to shift weather
        val finalEvents = mutableListOf<WeatherEvent>()
        for (event in events) {
            val weatherOfShift = shiftWeather.fromHour(event.start)
            if ((weatherOfShift.dominant != event.weather ||
                weatherOfShift.precipitation != event.precipitation) //&&
//                event.isSignificantEvent()
            ) {
                finalEvents.add(event)
            }
        }

        return finalEvents
    }

    private fun List<Weather>.fromHour(hour: Int): Weather {
        return when {
            (hour <= 5) -> this[0]
            (hour <= 11) -> this[1]
            (hour <= 17) -> this[2]
            else -> this[3]
        }
    }

    private fun WeatherEvent.isSignificantEvent(): Boolean {
        return if(
            (this.precipitation == Rain.Light || this.precipitation == Snow.Light) &&
            this.start == this.end
        ) {
            false
        }
        else {
            true
        }
    }

    fun mapEventCollectionToDescription(eventCollection: WeatherEvent): String {
        val startTime = String.format("%02d", eventCollection.start)
        val endTime = String.format("%02d", eventCollection.end + 1)

        val visibility = if(eventCollection.visibility != Visibility.CLEAR) {
            " (${eventCollection.visibility.desc})"
        }
        else {
            ""
        }

        return "$startTime:00-$endTime:00: ${eventCollection.precipitation.desc}$visibility"
    }

    private fun mapDominantWeather(rawHour: RawWeatherHour, temp: Int): DominantWeather {
        return when (rawHour.weatherMain) {
            DominantWeather.Thunderstorm.mainTag -> DominantWeather.Thunderstorm
            DominantWeather.Snow.mainTag, DominantWeather.Rain.mainTag -> if (temp > 0) {
                DominantWeather.Rain
            } else {
                DominantWeather.Snow
            }
            DominantWeather.Fog.mainTag -> DominantWeather.Fog
            DominantWeather.Mist.mainTag -> DominantWeather.Mist
            else -> DominantWeather.Clouds
        }
    }

    private fun Float?.toRain(): Rain {
        return when {
            this == null -> Rain.None
            this < 1 -> Rain.Light
            this < 4 -> Rain.Moderate
            else -> Rain.Heavy
        }
    }

    private fun Float?.toSnow(): Snow {
        return when {
            this == null -> Snow.None
            this < 1 -> Snow.Light
            this < 2 -> Snow.Moderate
            else -> Snow.Heavy
        }
    }

    data class WeatherEventHour(val hour: Int, val weather: DominantWeather, val precipitation: Precipitation)

    data class WeatherEvent(
        val start: Int,
        var end: Int,
        val weather: DominantWeather,
        val precipitation: Precipitation,
        val visibility: Visibility
    )
}