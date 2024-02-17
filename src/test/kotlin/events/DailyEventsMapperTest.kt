package events

import almanac.DominantWeather
import almanac.Visibility
import mock.MockRandom
import mock.MockVisibilityConverter
import model.RawWeatherHour
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

import kotlin.test.BeforeTest

class DailyEventsMapperTest {
    private lateinit var mapper: DailyEventsMapper

    @BeforeTest
    fun setUp() {
        mapper = DailyEventsMapper(MockVisibilityConverter(Visibility.CLEAR))
    }

//    @Test
//    fun mapDay() {
//        val inputList = hourList(
//            5,
//            listOf(
//                hour(5, rain1h = 0.1, weatherMain = DominantWeather.Rain),
//                hour(5, rain1h = 0.1, weatherMain = DominantWeather.Rain),
//                hour(5, rain1h = 0.1, weatherMain = DominantWeather.Rain)
//            )
//        )
//        val expected = ""
//
//        val result = mapper.mapDay(inputList)
//    }

    private fun hourList(frontBuffer: Int, weatherToTest: List<RawWeatherHour>): List<RawWeatherHour> {
        val mutableList = mutableListOf<RawWeatherHour>()
        while (mutableList.size < frontBuffer) {
            mutableList.add(emptyHour())
        }
        mutableList.addAll(weatherToTest)
        while (mutableList.size < 24) {
            mutableList.add(emptyHour())
        }
        return mutableList
    }

    fun emptyHour(): RawWeatherHour {
        return hour(0, weatherMain = DominantWeather.Clouds)
    }

    fun hour(
        tempC: Int,
        windSpeed: Int = 0,
        windDegree: Int = 0,
        rain1h: Double? = null,
        snow1h: Double? = null,
        weatherMain: DominantWeather
    ): RawWeatherHour {
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