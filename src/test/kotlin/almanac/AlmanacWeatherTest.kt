package almanac

import almanac.AlmanacWeatherGenerator.generateAlmanacWeather
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class AlmanacWeatherTest {

    @Test
    fun generateAlmanacWeatherClouds() {
        val dominantWeather = DominantWeather.Clouds
        val cloudCover = 50
        val expected = Weather(dominantWeather, roundedCloudCover = 50, description = "Scattered clouds")

        val result = generateAlmanacWeather(0, null, null, cloudCover, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherRainDefault() {
        val dominantWeather = DominantWeather.Rain
        val temp = 5
        val rainAmount = 2f
        val expected = Weather(dominantWeather, Rain.Moderate, description = "Moderate rain")

        val result = generateAlmanacWeather(temp, rainAmount, null, 0, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherRainFlipped() {
        val dominantWeather = DominantWeather.Rain
        val temp = 5
        val snowAmount = 0.5f
        val expected = Weather(dominantWeather, Rain.Light, description = "Light rain")

        val result = generateAlmanacWeather(temp, null, snowAmount, 0, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherSnowDefault() {
        val dominantWeather = DominantWeather.Snow
        val temp = -5
        val snowAmount = 0.5f
        val expected = Weather(dominantWeather, Snow.Light, description = "Light snow")

        val result = generateAlmanacWeather(temp, null, snowAmount, 0, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherSnowFlipped() {
        val dominantWeather = DominantWeather.Snow
        val temp = -5
        val rainAmount = 2f
        val expected = Weather(dominantWeather, Snow.Moderate, description = "Snow")

        val result = generateAlmanacWeather(temp, rainAmount, null, 0, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherMist() {
        val dominantWeather = DominantWeather.Mist
        val expected = Weather(dominantWeather, description = "Mist")

        val result = generateAlmanacWeather(0, null, null, 0, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherFog() {
        val dominantWeather = DominantWeather.Fog
        val expected = Weather(dominantWeather, description = "Fog")

        val result = generateAlmanacWeather(0, null, null, 0, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherThunderstorm() {
        val dominantWeather = DominantWeather.Thunderstorm
        val expected = Weather(dominantWeather, description = "Thunderstorm")

        val result = generateAlmanacWeather(0, null, null, 0, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherFogAndRain() {
        val dominantWeather = DominantWeather.Fog
        val temp = 5
        val rainAmount = 0.5f
        val expected = Weather(dominantWeather, Rain.Light, description = "Light rain and fog")

        val result = generateAlmanacWeather(temp, rainAmount, null, 0, dominantWeather)
        assertEquals(expected, result)
    }

    @Test
    fun generateAlmanacWeatherThunderstormAndRain() {
        val dominantWeather = DominantWeather.Thunderstorm
        val temp = 5
        val rainAmount = 0.5f
        val expected = Weather(dominantWeather, Rain.Light, description = "Thunderstorm with light rain")

        val result = generateAlmanacWeather(temp, rainAmount, null, 0, dominantWeather)
        assertEquals(expected, result)
    }
}