package converters

import org.junit.jupiter.api.Assertions.*
import java.io.File
import kotlin.test.Test

class RawCsvMappersTest {
    @Test
    fun readCsv() {
        // Arrange
        val fileInputStream = File("test.csv").inputStream()

        // Act
        val rawCsvHours = converters.readCsv(fileInputStream)

        // Assert
        assertEquals(48, rawCsvHours.size)

        val firstRow = rawCsvHours[0]
        // dto
        assertEquals(1990, firstRow.dto.year().get())
        assertEquals(1, firstRow.dto.monthOfYear().get())
        assertEquals(1, firstRow.dto.dayOfMonth().get())
        assertEquals(0, firstRow.dto.hourOfDay().get())
        assertEquals(0, firstRow.dto.minuteOfHour().get())
        assertEquals(0, firstRow.dto.secondOfMinute().get())

        assertEquals(271.31f, firstRow.tempK)
        assertEquals(268.19f, firstRow.feelsLikeK)
        assertEquals(82, firstRow.humidity)
        assertEquals(2.29f, firstRow.windSpeed)
        assertEquals(152, firstRow.windDegree)

        assertEquals(null, firstRow.rain1h)
        assertEquals(null, firstRow.snow1h)

        assertEquals(97, firstRow.cloudCover)
        assertEquals("Clouds", firstRow.weatherMain)
        assertEquals("overcast clouds", firstRow.weatherDesc)

        // Test Rain
        assertEquals(0.24f, rawCsvHours[7].rain1h)

        // Test Snow
        assertEquals(0.18f, rawCsvHours[4].snow1h)

        // Random time dto
        val row2d7h = rawCsvHours[31]
        assertEquals(1990, row2d7h.dto.year().get())
        assertEquals(1, row2d7h.dto.monthOfYear().get())
        assertEquals(2, row2d7h.dto.dayOfMonth().get())
        assertEquals(7, row2d7h.dto.hourOfDay().get())
        assertEquals(0, row2d7h.dto.minuteOfHour().get())
        assertEquals(0, row2d7h.dto.secondOfMinute().get())
    }
}