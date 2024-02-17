package mock

import almanac.DominantWeather
import almanac.Precipitation
import almanac.Visibility
import almanac.Weather
import model.MoonPhase
import model.Shift

interface IVisibilityConverter {
    fun getVisibility(
        shift: Shift,
        moonPhase: MoonPhase,
        weather: Weather
    ): Visibility
    fun getVisibility(
        shift: Shift,
        moonPhase: MoonPhase,
        dominantWeather: DominantWeather,
        precipitation: Precipitation,
        cloudCover: Int
    ): Visibility
}