package converters

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import roundToFive
import roundUpToFive

class DoubleMappersKtTest {

    @Test
    fun roundUpToFive0() {
        val value = 0.0
        val converted = value.roundUpToFive()
        assertEquals("0", converted)
    }
    @Test
    fun roundUpToFive1() {
        val value = 1.1
        val converted = value.roundUpToFive()
        assertEquals("5", converted)
    }
    @Test
    fun roundUpToFive9() {
        val value = 9.0
        val converted = value.roundUpToFive()
        assertEquals("10", converted)
    }
    @Test
    fun roundUpToFive36() {
        val value = 36.4
        val converted = value.roundUpToFive()
        assertEquals("40", converted)
    }
    @Test
    fun roundUpToFiveNeg1() {
        val value = -1.1
        val converted = value.roundUpToFive()
        assertEquals("0", converted)
    }
    @Test
    fun roundUpToFiveNeg19() {
        val value = -19.3
        val converted = value.roundUpToFive()
        assertEquals("-15", converted)
    }

    @Test
    fun roundToFive0() {
        val value = 0.0
        val converted = value.roundToFive()
        assertEquals("0", converted)
    }
    @Test
    fun roundToFive1() {
        val value = 1.1
        val converted = value.roundToFive()
        assertEquals("0", converted)
    }
    @Test
    fun roundToFive9() {
        val value = 9.0
        val converted = value.roundToFive()
        assertEquals("10", converted)
    }
    @Test
    fun roundToFive36() {
        val value = 36.4
        val converted = value.roundToFive()
        assertEquals("35", converted)
    }
    @Test
    fun roundToFiveNeg1() {
        val value = -1.1
        val converted = value.roundToFive()
        assertEquals("0", converted)
    }
    @Test
    fun roundToFiveNeg19() {
        val value = -19.3
        val converted = value.roundToFive()
        assertEquals("-20", converted)
    }
}