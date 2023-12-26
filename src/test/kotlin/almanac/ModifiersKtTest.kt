package almanac

import org.joda.time.DateTime
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class ModifiersKtTest {

    @Test
    fun reduceTempForNuclearWinter() {
        // Arrange
        val temp = 0.0
        val date = DateTime.parse("2000-01-01T00:00:00")

        // Act
        val newTemp = temp.reduceTempForNuclearWinter(date)

        // Assert
        assertEquals(-10.0, newTemp)
    }

    @Test
    fun reduceTempForNuclearWinter1() {
        // Arrange
        val temp = 20.0
        val date = DateTime.parse("2000-03-01T00:00:00")

        // Act
        val newTemp = temp.reduceTempForNuclearWinter(date)

        // Assert
        assertEquals(10.0, newTemp)
    }

    @Test
    fun reduceTempForNuclearWinter2() {
        // Arrange
        val temp = 0.0
        val date = DateTime.parse("2000-04-01T00:00:00")

        // Act
        val newTemp = temp.reduceTempForNuclearWinter(date)

        // Assert
        assertEquals(-5.0, newTemp)
    }

    @Test
    fun reduceTempForNuclearWinter3() {
        // Arrange
        val temp = 20.0
        val date = DateTime.parse("2000-09-01T00:00:00")

        // Act
        val newTemp = temp.reduceTempForNuclearWinter(date)

        // Assert
        assertEquals(15.0, newTemp)
    }

    @Test
    fun reduceTempForNuclearWinter4() {
        // Arrange
        val temp = 15.0
        val date = DateTime.parse("2000-10-01T00:00:00")

        // Act
        val newTemp = temp.reduceTempForNuclearWinter(date)

        // Assert
        assertEquals(5.0, newTemp)
    }
}