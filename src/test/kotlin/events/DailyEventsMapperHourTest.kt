package events

import almanac.*
import mock.MockRandom
import mock.MockVisibilityConverter
import model.GroundCover
import model.RawWeatherHour
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.BeforeTest

class DailyEventsMapperHourTest {
    private lateinit var mapper: DailyEventsMapper

    @BeforeTest
    fun setUp() {
        mapper = DailyEventsMapper(MockVisibilityConverter(Visibility.CLEAR))
    }


    companion object {
        @JvmStatic
        fun groundValues(): List<Arguments> {
            return listOf(
                Arguments.of(hour(0, 0, 0, null, null, DominantWeather.Clouds), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Clouds, Rain.None)),
                Arguments.of(hour(5, 0, 0, 0.01, null, DominantWeather.Rain), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Rain, Rain.Light)),
                Arguments.of(hour(5, 0, 0, 0.16, null, DominantWeather.Rain), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Rain, Rain.Light)),
                Arguments.of(hour(5, 0, 0, 0.17, null, DominantWeather.Rain), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Rain, Rain.Moderate)),
                Arguments.of(hour(5, 0, 0, 0.66, null, DominantWeather.Rain), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Rain, Rain.Moderate)),
                Arguments.of(hour(5, 0, 0, 0.67, null, DominantWeather.Rain), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Rain, Rain.Heavy)),
                Arguments.of(hour(5, 0, 0, 5.0, null, DominantWeather.Rain), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Rain, Rain.Heavy)),
                Arguments.of(hour(0, 0, 0, null, 0.01, DominantWeather.Snow), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Snow, Snow.Light)),
                Arguments.of(hour(0, 0, 0, null, 0.16, DominantWeather.Snow), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Snow, Snow.Light)),
                Arguments.of(hour(0, 0, 0, null, 0.17, DominantWeather.Snow), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Snow, Snow.Moderate)),
                Arguments.of(hour(0, 0, 0, null, 0.33, DominantWeather.Snow), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Snow, Snow.Moderate)),
                Arguments.of(hour(0, 0, 0, null, 0.34, DominantWeather.Snow), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Snow, Snow.Heavy)),
                Arguments.of(hour(0, 0, 0, null, 5.00, DominantWeather.Snow), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Snow, Snow.Heavy)),
                Arguments.of(hour(0, 0, 0, null, null, DominantWeather.Fog), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Fog, Rain.None)),
                Arguments.of(hour(5, 0, 0, 0.2, null, DominantWeather.Fog), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Fog, Rain.Moderate)),
                Arguments.of(hour(0, 0, 0, null, null, DominantWeather.Mist), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Mist, Rain.None)),
                Arguments.of(hour(0, 0, 0, null, 0.2, DominantWeather.Mist), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Mist, Snow.Moderate)),
                Arguments.of(hour(0, 0, 0, null, null, DominantWeather.Thunderstorm), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Thunderstorm, Rain.None)),
                Arguments.of(hour(5, 0, 0, 2.0, null, DominantWeather.Thunderstorm), 0, DailyEventsMapper.WeatherEventHour(0, DominantWeather.Thunderstorm, Rain.Heavy)),
            )
        }

        private fun hour(tempC: Int, windSpeed: Int, windDegree: Int, rain1h: Double? = null, snow1h: Double? = null, weatherMain: DominantWeather): RawWeatherHour {
            val tempK = tempC + 273.15f
            return RawWeatherHour(
                dto = DateTime(),
                tempK = tempK,
                dewPoint = 0f,
                feelsLikeK = 0f,
                humidity = 0,
                windSpeed = windSpeed.toFloat(),
                windDegree = windDegree,
                rain1h = rain1h?.toFloat(),
                snow1h = snow1h?.toFloat(),
                cloudCover = 0,
                weatherMain = weatherMain.mainTag,
                weatherDesc = ""
            )
        }
    }

    @ParameterizedTest(name = "For data {0}, hour {1} result is {2}")
    @MethodSource("groundValues")
    fun makeString(data: RawWeatherHour, index: Int, expected: DailyEventsMapper.WeatherEventHour) {
        val result = mapper.mapHour(data, index)
        assertEquals(expected, result)
    }
}