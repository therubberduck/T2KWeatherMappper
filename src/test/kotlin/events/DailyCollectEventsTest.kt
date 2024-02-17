package events

import almanac.*
import mock.MockVisibilityConverter
import model.RawWeatherHour
import org.joda.time.DateTime
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import kotlin.test.BeforeTest

class DailyCollectEventsTest {
    private lateinit var mapper: DailyEventsMapper

    @BeforeTest
    fun setUp() {
        mapper = DailyEventsMapper(MockVisibilityConverter(Visibility.CLEAR))
    }


    companion object {
        @JvmStatic
        fun groundValues(): List<Arguments> {
            return listOf(
                Arguments.of(hour(0, weatherMain = DominantWeather.Clouds),                         0, weatherEventHour(DominantWeather.Clouds, None)),
                Arguments.of(hour(5, rain1h = 0.1, weatherMain = DominantWeather.Rain),             0, weatherEventHour(DominantWeather.Rain, Rain.Light)),
                Arguments.of(hour(5,  rain1h = 0.9, weatherMain = DominantWeather.Rain),            0, weatherEventHour(DominantWeather.Rain, Rain.Light)),
                Arguments.of(hour(5,  rain1h = 1.0, weatherMain = DominantWeather.Rain),            0, weatherEventHour(DominantWeather.Rain, Rain.Moderate)),
                Arguments.of(hour(5,  rain1h = 3.9, weatherMain = DominantWeather.Rain),            0, weatherEventHour(DominantWeather.Rain, Rain.Moderate)),
                Arguments.of(hour(5,  rain1h = 4.0, weatherMain = DominantWeather.Rain),            0, weatherEventHour(DominantWeather.Rain, Rain.Heavy)),
                Arguments.of(hour(5,  rain1h = 5.0, weatherMain = DominantWeather.Rain),            0, weatherEventHour(DominantWeather.Rain, Rain.Heavy)),
                Arguments.of(hour(0, snow1h = 0.1, weatherMain = DominantWeather.Snow),             0, weatherEventHour(DominantWeather.Snow, Snow.Light)),
                Arguments.of(hour(0,snow1h = 0.9, weatherMain = DominantWeather.Snow),              0, weatherEventHour(DominantWeather.Snow, Snow.Light)),
                Arguments.of(hour(0, snow1h = 1.0, weatherMain = DominantWeather.Snow),             0, weatherEventHour(DominantWeather.Snow, Snow.Moderate)),
                Arguments.of(hour(0, snow1h = 1.9, weatherMain = DominantWeather.Snow),             0, weatherEventHour(DominantWeather.Snow, Snow.Moderate)),
                Arguments.of(hour(0, snow1h = 2.99, weatherMain = DominantWeather.Snow),            0, weatherEventHour(DominantWeather.Snow, Snow.Heavy)),
                Arguments.of(hour(0, snow1h = 5.00, weatherMain = DominantWeather.Snow),            0, weatherEventHour(DominantWeather.Snow, Snow.Heavy)),
                Arguments.of(hour(0, weatherMain = DominantWeather.Fog),                            0, weatherEventHour(DominantWeather.Fog, None)),
                Arguments.of(hour(5,  rain1h = 1.2, weatherMain = DominantWeather.Fog),             0, weatherEventHour(DominantWeather.Fog, Rain.Moderate)),
                Arguments.of(hour(0, weatherMain = DominantWeather.Mist),                           0, weatherEventHour(DominantWeather.Mist, None)),
                Arguments.of(hour(0, snow1h = 1.2, weatherMain = DominantWeather.Mist),             0, weatherEventHour(DominantWeather.Mist, Snow.Moderate)),
                Arguments.of(hour(0, weatherMain = DominantWeather.Thunderstorm),                   0, weatherEventHour(DominantWeather.Thunderstorm, None)),
                Arguments.of(hour(5,  rain1h = 5.0, weatherMain = DominantWeather.Thunderstorm),    0, weatherEventHour(DominantWeather.Thunderstorm, Rain.Heavy)),
                Arguments.of(hour(0, weatherMain = DominantWeather.Clouds, windSpeed = 11),         0, weatherEventHour(DominantWeather.Clouds, None, WindSpeed.LightBreezeL)),
                Arguments.of(hour(5, rain1h = 0.1, weatherMain = DominantWeather.Rain, windSpeed = 11), 0, weatherEventHour(DominantWeather.Rain, Rain.Light, WindSpeed.LightBreezeL)),
            )
        }

        private fun hour(tempC: Int, windSpeed: Int = 0, windDegree: Int = 0, rain1h: Double? = null, snow1h: Double? = null, weatherMain: DominantWeather): RawWeatherHour {
            val tempK = tempC + 273.15f
            return RawWeatherHour(
                dto = DateTime(6,6,6,6,6),
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

        private fun weatherEventHour(weatherMain: DominantWeather, precipitation: Precipitation, windSpeed: WindSpeed = WindSpeed.CalmL): DailyEventsMapper.WeatherEventHour {
            return DailyEventsMapper.WeatherEventHour(0, weatherMain, precipitation, windSpeed)
        }
    }

    @ParameterizedTest(name = "For data {0}, hour {1} result is {2}")
    @MethodSource("groundValues")
    fun makeString(data: RawWeatherHour, index: Int, expected: DailyEventsMapper.WeatherEventHour) {
        val result = mapper.mapHour(data, index)
        assertEquals(expected, result)
    }
}