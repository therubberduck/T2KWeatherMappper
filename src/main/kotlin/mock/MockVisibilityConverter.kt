package mock

import almanac.DominantWeather
import almanac.Precipitation
import almanac.Visibility
import almanac.Weather
import model.MoonPhase
import model.Shift

class MockVisibilityConverter(val visibility: Visibility): IVisibilityConverter {
    override fun getVisibility(
        shift: Shift,
        moonPhase: MoonPhase,
        weather: Weather
    ): Visibility {
        return visibility
    }

    override fun getVisibility(
        shift: Shift,
        moonPhase: MoonPhase,
        dominantWeather: DominantWeather,
        precipitation: Precipitation,
        cloudCover: Int,
    ): Visibility {
        return visibility
    }
}