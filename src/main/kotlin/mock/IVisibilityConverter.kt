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

    /**
     * Note that this visibility only returns worst visibility (I.E, during precipitation / cloud cover)
     */
    fun getVisibility(
        shift: Shift,
        moonPhase: MoonPhase,
        dominantWeather: DominantWeather,
        precipitation: Precipitation,
        cloudCover: Int
    ): Visibility
}