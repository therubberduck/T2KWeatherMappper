package events

import almanac.*
import events.DailyEventsMapper.WeatherEvent
import mock.MockVisibilityConverter
import org.junit.jupiter.api.Test

import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class DailyEventsCollectionDescriptionTest {
    private lateinit var mapper: DailyEventsMapper

    @BeforeTest
    fun setUp() {
        mapper = DailyEventsMapper(MockVisibilityConverter(Visibility.CLEAR))
    }

    @Test
    fun lightRainEventCollectionToDescription() {
        val eventCollection = WeatherEvent(6, 6, DominantWeather.Rain, Rain.Light, Visibility.CLEAR)
        val expected = "06:00-07:00: Light rain"

        val result = mapper.mapEventCollectionToDescription(eventCollection)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainEventCollectionToDescription() {
        val eventCollection = WeatherEvent(6, 6, DominantWeather.Rain, Rain.Moderate, Visibility.HEX(60))
        val expected = "06:00-07:00: Moderate rain (60 hexes)"

        val result = mapper.mapEventCollectionToDescription(eventCollection)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainNightEventCollectionToDescription() {
        val eventCollection = WeatherEvent(2, 5, DominantWeather.Rain, Rain.Moderate, Visibility.MULTIPLE(Visibility.HEX(5, -2), Visibility.HEX(10, -1)))
        val expected = "02:00-06:00: Moderate rain (5 (-2)  |  10 (-1))"

        val result = mapper.mapEventCollectionToDescription(eventCollection)

        assertEquals(expected, result)
    }
}