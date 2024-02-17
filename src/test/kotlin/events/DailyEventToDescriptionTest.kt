package events

import almanac.*
import events.DailyEventsMapper.WeatherEvent
import mock.MockVisibilityConverter
import org.junit.jupiter.api.Test

import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class DailyEventToDescriptionTest {
    private lateinit var mapper: DailyEventsMapper

    @BeforeTest
    fun setUp() {
        mapper = DailyEventsMapper(MockVisibilityConverter(Visibility.CLEAR))
    }

    @Test
    fun lightRainEventToDescription() {
        val event = weatherEvent(6, 6, DominantWeather.Rain, Rain.Light)
        val expected = "06:00-07:00: Light rain"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun thunderstormEventToDescription() {
        val event = weatherEvent(6, 6, DominantWeather.Thunderstorm)
        val expected = "06:00-07:00: Thunderstorm"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun windEventToDescription() {
        val event = weatherEvent(6, 6, weather = DominantWeather.Clouds, windSpeed =  WindSpeed.FreshBreezeL)
        val expected = "06:00-07:00: Fresh Breezeˡ"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainEventToDescription() {
        val event = weatherEvent(6, 6, DominantWeather.Rain, Rain.Moderate, visibility = Visibility.HEX(60))
        val expected = "06:00-07:00: Moderate rain (60 hexes)"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainWithMistEventToDescription() {
        val event = weatherEvent(6, 6, DominantWeather.Mist, Rain.Moderate, visibility = Visibility.HEX(60))
        val expected = "06:00-07:00: Moderate rain and mist (60 hexes)"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainWithFogEventToDescription() {
        val event = weatherEvent(6, 6, DominantWeather.Fog, Rain.Moderate, visibility = Visibility.HEX(60))
        val expected = "06:00-07:00: Moderate rain and fog (60 hexes)"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainWithThunderstormEventToDescription() {
        val event = weatherEvent(6, 6, DominantWeather.Thunderstorm, Rain.Moderate, visibility = Visibility.HEX(60))
        val expected = "06:00-07:00: Thunderstorm with moderate rain (60 hexes)"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainNightEventToDescription() {
        val event = weatherEvent(
            2,
            5,
            DominantWeather.Rain,
            Rain.Moderate,
            visibility = Visibility.MULTIPLE(Visibility.HEX(5, -2), Visibility.HEX(10, -1))
        )
        val expected = "02:00-06:00: Moderate rain (5 (-2)  |  10 (-1))"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainWithWindEventToDescription() {
        val event = weatherEvent(6, 6, DominantWeather.Rain, Rain.Moderate, WindSpeed.FreshBreezeL, Visibility.HEX(60))
        val expected = "06:00-07:00: Moderate rain (60 hexes), Fresh Breezeˡ"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainWithWindNightEventToDescription() {
        val event = weatherEvent(
            2,
            5,
            DominantWeather.Rain,
            Rain.Moderate,
            WindSpeed.StormS,
            Visibility.MULTIPLE(Visibility.HEX(5, -2), Visibility.HEX(10, -1))
        )
        val expected = "02:00-06:00: Moderate rain (5 (-2)  |  10 (-1)), Stormˢ"

        val result = mapper.mapEventToDescription(event)

        assertEquals(expected, result)
    }

    private fun weatherEvent(
        start: Int,
        end: Int,
        weather: DominantWeather,
        precipitation: Precipitation = None,
        windSpeed: WindSpeed = WindSpeed.CalmL,
        visibility: Visibility = Visibility.CLEAR
    ): WeatherEvent {
        return WeatherEvent(start, end, weather, precipitation, windSpeed, windSpeed != WindSpeed.CalmL, visibility)
    }
}