package events

import almanac.*
import events.DailyEventsMapper.WeatherEvent
import events.DailyEventsMapper.WeatherEventHour
import mock.MockVisibilityConverter
import model.MoonPhase
import org.junit.jupiter.api.Test

import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class DailyEventsItemMapperTest {
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
                eventHour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(7, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(8, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Rain, Rain.Light)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleSnow() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Snow, precipitation = Snow.Light),
                eventHour(7, weather = DominantWeather.Snow, precipitation = Snow.Light),
                eventHour(8, weather = DominantWeather.Snow, precipitation = Snow.Light)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Snow, Snow.Light)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleMist() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Mist, precipitation = None),
                eventHour(7, weather = DominantWeather.Mist, precipitation = None),
                eventHour(8, weather = DominantWeather.Mist, precipitation = None)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Mist)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleFog() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Fog, precipitation = None),
                eventHour(7, weather = DominantWeather.Fog, precipitation = None),
                eventHour(8, weather = DominantWeather.Fog, precipitation = None)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Fog)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleThunderstorm() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Thunderstorm, precipitation = None),
                eventHour(7, weather = DominantWeather.Thunderstorm, precipitation = None),
                eventHour(8, weather = DominantWeather.Thunderstorm, precipitation = None)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Thunderstorm)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleMistAndRain() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Mist, precipitation = Rain.Light),
                eventHour(7, weather = DominantWeather.Mist, precipitation = Rain.Light)
            )
        )
        val expected = listOf(
            weatherEvent(6,7, DominantWeather.Mist, Rain.Light)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleFogAndSnow() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Fog, precipitation = Snow.Light),
                eventHour(7, weather = DominantWeather.Fog, precipitation = Snow.Light),
                eventHour(8, weather = DominantWeather.Fog, precipitation = Snow.Light)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Fog, Snow.Light)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleThunderstormAndRain() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Thunderstorm, precipitation = Rain.Moderate),
                eventHour(7, weather = DominantWeather.Thunderstorm, precipitation = Rain.Moderate),
                eventHour(8, weather = DominantWeather.Thunderstorm, precipitation = Rain.Moderate)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Thunderstorm, Rain.Moderate)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleRainAndWind() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Rain, precipitation = Rain.Light, WindSpeed.FreshBreezeL),
                eventHour(7, weather = DominantWeather.Rain, precipitation = Rain.Light, WindSpeed.FreshBreezeL),
                eventHour(8, weather = DominantWeather.Rain, precipitation = Rain.Light, WindSpeed.FreshBreezeL)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Rain, Rain.Light, WindSpeed.FreshBreezeL)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleWindBlockedByShift() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Rain, precipitation = Rain.Light, WindSpeed.FreshBreezeL),
                eventHour(7, weather = DominantWeather.Rain, precipitation = Rain.Light, WindSpeed.FreshBreezeL),
                eventHour(8, weather = DominantWeather.Rain, precipitation = Rain.Light, WindSpeed.FreshBreezeL)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Rain, Rain.Light, WindSpeed.FreshBreezeL)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleWindBlockedByStrongerShift() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Clouds, precipitation = None, WindSpeed.FreshBreezeL),
                eventHour(7, weather = DominantWeather.Clouds, precipitation = None, WindSpeed.FreshBreezeL),
                eventHour(8, weather = DominantWeather.Clouds, precipitation = None, WindSpeed.FreshBreezeL)
            )
        )
        val windOfShift = listOf(
            WindSpeed.StormS,
            WindSpeed.StormS,
            WindSpeed.StormS,
            WindSpeed.StormS
        )
        val expected = emptyList<WeatherEvent>()

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, windOfShift, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleWindNotBlockedByWeakerShift() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Clouds, precipitation = None, WindSpeed.StormS),
                eventHour(7, weather = DominantWeather.Clouds, precipitation = None, WindSpeed.StormS),
                eventHour(8, weather = DominantWeather.Clouds, precipitation = None, WindSpeed.StormS)
            )
        )
        val windOfShift = listOf(
            WindSpeed.FreshBreezeL,
            WindSpeed.FreshBreezeL,
            WindSpeed.FreshBreezeL,
            WindSpeed.FreshBreezeL
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Clouds, windSpeed = WindSpeed.StormS)
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, windOfShift, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimplePartialMatchShifts() {
        val eventHours = hourList(
            5,
            listOf(
                eventHour(5, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(7, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Rain, Rain.Light, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = listOf(
            weatherEvent(6,7, DominantWeather.Rain, Rain.Light)
        )

        val result = mapper.collectEvents(eventHours, shiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleFullMatchShifts() {
        val eventHours = hourList(
            5,
            listOf(
                eventHour(5, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(7, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Rain, Rain.Light, description = ""),
            Weather(DominantWeather.Rain, Rain.Light, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = emptyList<WeatherEvent>()

        val result = mapper.collectEvents(eventHours, shiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventSimpleFullMatchShift() {
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(7, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(8, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val shiftWeather = listOf(
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Rain, Rain.Light, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
            Weather(DominantWeather.Clouds, description = ""),
        )
        val expected = emptyList<WeatherEvent>()

        val result = mapper.collectEvents(eventHours, shiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }

    @Test
    fun collectEventWithVisibility() {
        val customMapper = DailyEventsMapper(MockVisibilityConverter(Visibility.HEX(60)))
        val eventHours = hourList(
            6,
            listOf(
                eventHour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(7, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(8, weather = DominantWeather.Rain, precipitation = Rain.Light)
            )
        )
        val expected = listOf(
            weatherEvent(6,8, DominantWeather.Rain, Rain.Light, visibility = Visibility.HEX(60))
        )

        val result = customMapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

        assertEquals(expected, result)
    }
    @Test
    fun collectEventsAdvanced() {
        val eventHours = hourList(
            5,
            listOf(
                eventHour(5, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(6, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(7, weather = DominantWeather.Rain, precipitation = Rain.Light),
                emptyHour(),
                emptyHour(),
                emptyHour(),
                emptyHour(),
                emptyHour(),
                eventHour(13, weather = DominantWeather.Rain, precipitation = Rain.Light),
                eventHour(14, weather = DominantWeather.Rain, precipitation = Rain.Moderate),
                eventHour(15, weather = DominantWeather.Snow, precipitation = Snow.Light),
                eventHour(16, weather = DominantWeather.Snow, precipitation = Snow.Light),
            )
        )
        val expected = listOf(
            weatherEvent(5,5, DominantWeather.Rain, Rain.Light),
            weatherEvent(6,7, DominantWeather.Rain, Rain.Light),
            weatherEvent(13,13, DominantWeather.Rain, Rain.Light),
            weatherEvent(14,14, DominantWeather.Rain, Rain.Moderate),
            weatherEvent(15,16, DominantWeather.Snow, Snow.Light),
        )

        val result = mapper.collectEvents(eventHours, defaultShiftWeather, defaultShiftWind, listOf(MoonPhase.FULL, MoonPhase.FULL))

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

    private fun emptyHour(): WeatherEventHour {
        return eventHour(0, weather = DominantWeather.Clouds)
    }

    private fun eventHour(
        hour: Int,
        weather: DominantWeather,
        precipitation: Precipitation = None,
        wind: WindSpeed = WindSpeed.CalmL
    ): WeatherEventHour {
        return WeatherEventHour(
            hour = hour,
            weather = weather,
            precipitation = precipitation,
            wind = wind
        )
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

    private val defaultShiftWeather = listOf(
        Weather(DominantWeather.Clouds, description = ""),
        Weather(DominantWeather.Clouds, description = ""),
        Weather(DominantWeather.Clouds, description = ""),
        Weather(DominantWeather.Clouds, description = "")
    )

    private val defaultShiftWind = listOf(
        WindSpeed.CalmL,
        WindSpeed.CalmL,
        WindSpeed.CalmL,
        WindSpeed.CalmL
    )
}