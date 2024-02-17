package almanac

import model.GroundCover
import model.RawWeatherHour
import model.RawWeatherShift
import org.joda.time.DateTime
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.io.File
import kotlin.test.BeforeTest

class AlmanacMapperTest {
    private lateinit var mapper: AlmanacMapper

    @BeforeTest
    fun setUp() {
        mapper = AlmanacMapper(null, VisibilityConverter(), GroundCover(0f, 0f, 0f, 0f, 0f, false, false))
    }

    @Test
    fun convertToAlmanacTemp() {
        val input = RawWeatherShift(
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 271.31f, 0f, 0f, 82, 2.29f, 0, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 271.31f, 0f, 0f, 82, 2.29f, 0, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 271.31f, 0f, 0f, 82, 2.29f, 0, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 301.31f, 0f, 0f, 20, 15f, 0, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 301.31f, 0f, 0f, 20, 15f, 0, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 301.31f, 0f, 0f, 20, 15f, 0, null, null, 0, "", ""),
        )

        val result = mapper.convertToAlmanac(input)

        assertEquals("5 C⁰", result.temp)
        assertEquals("Chilly (1)", result.feltTemp)
    }

    @Test
    fun convertCloudCover() {
        val input = RawWeatherShift(
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 5, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 15, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 15, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 30, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 30, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 50, "", ""),
        )

        val result = mapper.convertToAlmanac(input)

        assertEquals("25%", result.cloudCover)
    }

    @Test
    fun convertWind() {
        val input = RawWeatherShift(
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 5f, 20, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 15f, 15, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 15f, 30, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 15f, 35, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 15f, 40, null, null, 0, "", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 25f, 45, null, null, 0, "", ""),
        )

        val result = mapper.convertToAlmanac(input)

        assertEquals("Light Breezeˢ from NE", result.wind)
    }

    @Test
    fun convertWeather() {
        val input = RawWeatherShift(
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 5, "Thunderstorm", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 15, "Rain", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 25, "Clouds", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 25, "Clouds", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 35, "Rain", ""),
            RawWeatherHour(DateTime(2000, 1, 1, 0, 0), 270f, 0f, 0f, 0, 0f, 0, null, null, 45, "Clouds", ""),
        )

        val result = mapper.convertToAlmanac(input)

        assertEquals("Passing clouds", result.weather)
    }
}