import almanac.AlmanacMapper
import almanac.VisibilityConverter
import model.GroundCover
import model.RawWeatherHour
import org.junit.jupiter.api.Test

import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class AlmanacMapperMiscTest {

    private lateinit var rawCsvHours: List<RawWeatherHour>
    private lateinit var mapper: AlmanacMapper

    @BeforeTest
    fun setUp() {
        val fileInputStream = File("test.csv").inputStream()
        rawCsvHours = converters.readCsv(fileInputStream)

        mapper = AlmanacMapper(null, VisibilityConverter(), GroundCover(0f, 0f, 0f, 0f, 0f, false))
    }

    @Test
    fun chunkToDays() {
        // Act
        val rawDays = mapper.chunkToDays(rawCsvHours)

        // Assert

        // Correct number of days
        assertEquals(2, rawDays.size)

        // Correct number of shifts
        assertEquals(4, rawDays[0].count())
        assertEquals(4, rawDays[1].count())

        // Correct number of hours in shifts
        assertEquals(6, rawDays[0].night.count())
        assertEquals(6, rawDays[0].morning.count())
        assertEquals(6, rawDays[0].afternoon.count())
        assertEquals(6, rawDays[0].evening.count())
        assertEquals(6, rawDays[1].night.count())
        assertEquals(6, rawDays[1].morning.count())
        assertEquals(6, rawDays[1].afternoon.count())
        assertEquals(6, rawDays[1].evening.count())

        // Correct Order
        assertEquals(0, rawDays[0].night.hour0.dto.hourOfDay)
        assertEquals(1, rawDays[0].night.hour1.dto.hourOfDay)
        assertEquals(2, rawDays[0].night.hour2.dto.hourOfDay)
        assertEquals(3, rawDays[0].night.hour3.dto.hourOfDay)
        assertEquals(4, rawDays[0].night.hour4.dto.hourOfDay)
        assertEquals(5, rawDays[0].night.hour5.dto.hourOfDay)
        assertEquals(6, rawDays[0].morning.hour0.dto.hourOfDay)
        assertEquals(7, rawDays[0].morning.hour1.dto.hourOfDay)
        assertEquals(8, rawDays[0].morning.hour2.dto.hourOfDay)
        assertEquals(9, rawDays[0].morning.hour3.dto.hourOfDay)
        assertEquals(10, rawDays[0].morning.hour4.dto.hourOfDay)
        assertEquals(11, rawDays[0].morning.hour5.dto.hourOfDay)

        assertEquals(0, rawDays[1].night.hour0.dto.hourOfDay)
        assertEquals(11, rawDays[1].morning.hour5.dto.hourOfDay)
    }
}