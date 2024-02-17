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

class DailyEventsCollectionMapperTest {
    private lateinit var mapper: DailyEventsMapper

    @BeforeTest
    fun setUp() {
        mapper = DailyEventsMapper(MockVisibilityConverter(Visibility.CLEAR))
    }

    @Test
    fun collectEventSimpleRain() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(7, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(8, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,8, DominantWeather.Rain, Rain.Light, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleSnow() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Snow, precipitation = Snow.Light),
                hour(7, weather = DominantWeather.Snow, precipitation = Snow.Light),
                hour(8, weather = DominantWeather.Snow, precipitation = Snow.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,8, DominantWeather.Snow, Snow.Light, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleMist() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Mist, precipitation = Rain.None),
                hour(7, weather = DominantWeather.Mist, precipitation = Rain.None),
                hour(8, weather = DominantWeather.Mist, precipitation = Rain.None)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,8, DominantWeather.Mist, Rain.None, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleFog() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Fog, precipitation = Rain.None),
                hour(7, weather = DominantWeather.Fog, precipitation = Rain.None),
                hour(8, weather = DominantWeather.Fog, precipitation = Rain.None)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,8, DominantWeather.Fog, Rain.None, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleThunderstorm() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Thunderstorm, precipitation = Rain.None),
                hour(7, weather = DominantWeather.Thunderstorm, precipitation = Rain.None),
                hour(8, weather = DominantWeather.Thunderstorm, precipitation = Rain.None)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,8, DominantWeather.Thunderstorm, Rain.None, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleMistAndRain() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Mist, precipitation = Rain.Light),
                hour(7, weather = DominantWeather.Mist, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,7, DominantWeather.Mist, Rain.Light, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleFogAndSnow() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Fog, precipitation = Snow.Light),
                hour(7, weather = DominantWeather.Fog, precipitation = Snow.Light),
                hour(8, weather = DominantWeather.Fog, precipitation = Snow.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,8, DominantWeather.Fog, Snow.Light, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleThunderstormAndRain() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Thunderstorm, precipitation = Rain.Moderate),
                hour(7, weather = DominantWeather.Thunderstorm, precipitation = Rain.Moderate),
                hour(8, weather = DominantWeather.Thunderstorm, precipitation = Rain.Moderate)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,8, DominantWeather.Thunderstorm, Rain.Moderate, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimplePartialMatchShifts() {
        val eventHours = hourList(
            5,
            listOf(
                hour(5, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(7, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Rain, Rain.Light, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,7, DominantWeather.Rain, Rain.Light, Visibility.CLEAR)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleFullMatchShifts() {
        val eventHours = hourList(
            5,
            listOf(
                hour(5, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(7, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Rain, Rain.Light, description = ""),
            Weather(DominantWeather.Rain, Rain.Light, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = emptyList<WeatherEventCollection>()

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleFullMatchShift() {
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(7, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(8, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Rain, Rain.Light, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = emptyList<WeatherEventCollection>()

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventWithVisibility() {
        val customMapper = DailyEventsMapper(MockVisibilityConverter(Visibility.HEX(60)))
        val eventHours = hourList(
            6,
            listOf(
                hour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(7, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(8, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(6,8, DominantWeather.Rain, Rain.Light, Visibility.HEX(60))
        )

        val result = customMapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }
    @Test
    fun collectEventsAdvanced() {
        val eventHours = hourList(
            5,
            listOf(
                hour(5, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(7, weather = DominantWeather.Rain, precipitation = Rain.Light),
                emptyHour(),
                emptyHour(),
                emptyHour(),
                emptyHour(),
                emptyHour(),
                hour(13, weather = DominantWeather.Rain, precipitation = Rain.Light),
                hour(14, weather = DominantWeather.Rain, precipitation = Rain.Moderate),
                hour(15, weather = DominantWeather.Snow, precipitation = Snow.Light),
                hour(16, weather = DominantWeather.Snow, precipitation = Snow.Light),
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf<WeatherEventCollection>(
            WeatherEventCollection(5,5, DominantWeather.Rain, Rain.Light, Visibility.CLEAR),
            WeatherEventCollection(6,7, DominantWeather.Rain, Rain.Light, Visibility.CLEAR),
            WeatherEventCollection(13,13, DominantWeather.Rain, Rain.Light, Visibility.CLEAR),
            WeatherEventCollection(14,14, DominantWeather.Rain, Rain.Moderate, Visibility.CLEAR),
            WeatherEventCollection(15,16, DominantWeather.Snow, Snow.Light, Visibility.CLEAR),
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    private fun hourList(frontBuffer: Int, weatherToTest: List<WeatherEventHour>): List<WeatherEventHour> {
        val mutableList = mutableListOf<WeatherEventHour>()
        while (mutableList.size < frontBuffer) {
            mutableList.add(emptyHour())
        }
        mutableList.addAll(weatherToTest)
        while (mutableList.size < 24) {
            mutableList.add(emptyHour())
        }
        return mutableList
    }

    fun emptyHour(): WeatherEventHour {
        return hour(0, weather = DominantWeather.Clouds)
    }

    fun hour(
        hour: Int,
        weather: DominantWeather,
        precipitation: Precipitation = Rain.None
    ): WeatherEventHour {
        return WeatherEventHour(
            hour = hour,
            weather = weather,
            precipitation = precipitation
        )
    }
}