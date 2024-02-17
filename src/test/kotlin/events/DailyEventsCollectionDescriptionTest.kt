package events

import almanac.*
import events.DailyEventsMapper.WeatherEventCollection
import events.DailyEventsMapper.WeatherEventHour
import mock.MockRandom
import mock.MockVisibilityConverter
import model.MoonPhase
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
        val eventCollection = WeatherEventCollection(6, 6, DominantWeather.Rain, Rain.Light, Visibility.CLEAR)
        val expected = "06-07: Light rain"

        val result = mapper.mapEventCollectionToDescription(eventCollection)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainEventCollectionToDescription() {
        val eventCollection = WeatherEventCollection(6, 6, DominantWeather.Rain, Rain.Moderate, Visibility.HEX(60))
        val expected = "06-07: Moderate rain (60 hexes)"

        val result = mapper.mapEventCollectionToDescription(eventCollection)

        assertEquals(expected, result)
    }

    @Test
    fun moderateRainNightEventCollectionToDescription() {
        val eventCollection = WeatherEventCollection(2, 5, DominantWeather.Rain, Rain.Moderate, Visibility.MULTIPLE(Visibility.HEX(5, -2), Visibility.HEX(10, -1)))
        val expected = "02-06: Moderate rain (5 (-2)  |  10 (-1))"

        val result = mapper.mapEventCollectionToDescription(eventCollection)

        assertEquals(expected, result)
    }
}