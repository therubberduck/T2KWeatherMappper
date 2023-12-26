package almanac

import mock.IRandom
import mock.MockRandom
import model.MoonPhase

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class VisibilityConverterHighTest {

    companion object {
        @JvmStatic
        fun dayValues(): List<Arguments> {
            return listOf(
                Arguments.of(DominantWeather.Clouds, None, Visibility.CLEAR),
                Arguments.of(DominantWeather.Rain, Rain.Light, Visibility.CLEAR),
                Arguments.of(DominantWeather.Rain, Rain.Moderate, Visibility.KM(5.0)),
                Arguments.of(DominantWeather.Rain, Rain.Heavy, Visibility.HEX(200, -1)),
                Arguments.of(DominantWeather.Snow, Snow.Light, Visibility.KM(5.0)),
                Arguments.of(DominantWeather.Snow, Snow.Moderate, Visibility.HEX(95)),
                Arguments.of(DominantWeather.Snow, Snow.Heavy, Visibility.HEX(45, -1)),
                Arguments.of(DominantWeather.Mist, None, Visibility.HEX(200)),
                Arguments.of(DominantWeather.Mist, Rain.Moderate, Visibility.KM(5.0)),
                Arguments.of(DominantWeather.Mist, Rain.Heavy, Visibility.HEX(200, -1)),
                Arguments.of(DominantWeather.Mist, Snow.Light, Visibility.KM(5.0)),
                Arguments.of(DominantWeather.Mist, Snow.Moderate, Visibility.HEX(95)),
                Arguments.of(DominantWeather.Mist, Snow.Heavy, Visibility.HEX(45, -1)),
                Arguments.of(DominantWeather.Fog, Rain.Light, Visibility.HEX(100, -1)),
                Arguments.of(DominantWeather.Fog, Rain.Moderate, Visibility.HEX(40, -1)),
                Arguments.of(DominantWeather.Fog, Snow.Moderate, Visibility.HEX(40, -1)),
                Arguments.of(DominantWeather.Fog, Rain.Heavy, Visibility.HEX(20, -1)),
                Arguments.of(DominantWeather.Fog, Snow.Heavy, Visibility.HEX(20, -1)),
                Arguments.of(DominantWeather.Thunderstorm, None, Visibility.CLEAR),
                Arguments.of(DominantWeather.Thunderstorm, Rain.Moderate, Visibility.KM(5.0)),
                Arguments.of(DominantWeather.Thunderstorm, Rain.Heavy, Visibility.HEX(200, -1)),
                Arguments.of(DominantWeather.Thunderstorm, Snow.Light, Visibility.KM(5.0)),
                Arguments.of(DominantWeather.Thunderstorm, Snow.Moderate, Visibility.HEX(95)),
                Arguments.of(DominantWeather.Thunderstorm, Snow.Heavy, Visibility.HEX(45, -1)),
            )
        }

        @JvmStatic
        fun nightValues(): List<Arguments> {
            return listOf(
                Arguments.of(MoonPhase.FULL, 0, DominantWeather.Clouds, Visibility.HEX(15, -1)),
                Arguments.of(MoonPhase.HALF, 0, DominantWeather.Clouds, Visibility.HEX(10, -1)),
                Arguments.of(MoonPhase.CRESCENT, 0, DominantWeather.Clouds, Visibility.HEX(5, -2)),
                Arguments.of(MoonPhase.NEW, 0, DominantWeather.Clouds, Visibility.HEX(3, -3)),
                Arguments.of(MoonPhase.FULL, 100, DominantWeather.Clouds, Visibility.HEX(10, -1)),
                Arguments.of(MoonPhase.HALF, 100, DominantWeather.Clouds, Visibility.HEX(5, -2)),
                Arguments.of(MoonPhase.CRESCENT, 100, DominantWeather.Clouds, Visibility.HEX(3, -3)),
                Arguments.of(MoonPhase.NEW, 100, DominantWeather.Clouds, Visibility.HEX(0, -4)),
                Arguments.of(MoonPhase.FULL, 100, DominantWeather.Rain, Visibility.HEX(5, -2)),
                Arguments.of(MoonPhase.HALF, 100, DominantWeather.Rain, Visibility.HEX(3, -3)),
                Arguments.of(MoonPhase.CRESCENT, 100, DominantWeather.Rain, Visibility.HEX(0, -4)),
                Arguments.of(MoonPhase.NEW, 100, DominantWeather.Rain, Visibility.HEX(0, -4)),
                Arguments.of(MoonPhase.FULL, 100, DominantWeather.Mist, Visibility.HEX(10, -1)),
                Arguments.of(MoonPhase.HALF, 100, DominantWeather.Mist, Visibility.HEX(5, -2)),
                Arguments.of(MoonPhase.CRESCENT, 100, DominantWeather.Mist, Visibility.HEX(3, -3)),
                Arguments.of(MoonPhase.NEW, 100, DominantWeather.Mist, Visibility.HEX(0, -4)),
                Arguments.of(MoonPhase.FULL, 0, DominantWeather.Mist, Visibility.HEX(10, -1)),
                Arguments.of(MoonPhase.HALF, 0, DominantWeather.Mist, Visibility.HEX(5, -2)),
                Arguments.of(MoonPhase.CRESCENT, 0, DominantWeather.Mist, Visibility.HEX(3, -3)),
                Arguments.of(MoonPhase.NEW, 0, DominantWeather.Mist, Visibility.HEX(0, -4)),
                Arguments.of(MoonPhase.FULL, 25, DominantWeather.Clouds, Visibility.MULTIPLE(Visibility.HEX(15, -1),Visibility.HEX(10, -1))),
                Arguments.of(MoonPhase.HALF, 25, DominantWeather.Clouds, Visibility.MULTIPLE(Visibility.HEX(10, -1),Visibility.HEX(5, -2))),
                Arguments.of(MoonPhase.CRESCENT, 25, DominantWeather.Clouds, Visibility.MULTIPLE(Visibility.HEX(5, -2),Visibility.HEX(3, -3))),
                Arguments.of(MoonPhase.NEW, 25, DominantWeather.Clouds, Visibility.MULTIPLE(Visibility.HEX(3, -3),Visibility.HEX(0, -4))),
                Arguments.of(MoonPhase.FULL, 50, DominantWeather.Rain, Visibility.MULTIPLE(Visibility.HEX(5, -2),Visibility.HEX(15, -1))),
                Arguments.of(MoonPhase.HALF, 50, DominantWeather.Rain, Visibility.MULTIPLE(Visibility.HEX(3, -3),Visibility.HEX(10, -1))),
                Arguments.of(MoonPhase.CRESCENT, 50, DominantWeather.Rain, Visibility.MULTIPLE(Visibility.HEX(0, -4),Visibility.HEX(5, -2))),
                Arguments.of(MoonPhase.NEW, 50, DominantWeather.Rain, Visibility.MULTIPLE(Visibility.HEX(0, -4),Visibility.HEX(3, -3))),
            )
        }
    }

    @ParameterizedTest(name = "Visibility for {0} and {1} converted to {2}")
    @MethodSource("dayValues")
    fun getVisibilityDay(dominantWeather: DominantWeather, precipitation: Precipitation, expected: Visibility) {
        val visibilityConverter = VisibilityConverter(MockRandom(1.0))
        val result = visibilityConverter.getVisibilityDay(dominantWeather, precipitation)
        assertEquals(expected, result)
    }

    @ParameterizedTest(name = "Visibility for {0} and {2} with {1} cloudcover converted to {2}")
    @MethodSource("nightValues")
    fun getVisibilityNight(moonPhase: MoonPhase, cloudCover: Int, dominantWeather: DominantWeather, expected: Visibility) {
        val visibilityConverter = VisibilityConverter(MockRandom(1.0))
        val result = visibilityConverter.getVisibilityNight(moonPhase, cloudCover, dominantWeather)
        assertEquals(expected, result)
    }
}