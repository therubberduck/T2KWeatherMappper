import almanac.AlmanacMapper
import almanac.MoonPhaseCalc
import almanac.SunriseCalc
import almanac.VisibilityConverter
import converters.XlsMapper
import converters.writeCsv
import mock.Random
import model.GroundCover
import java.io.File

val timeZoneAdjustment = 1
val moonPhaseFileForYear = "DataFiles/moon-phases-2000-Europe_Stockholm.csv"
val sunriseFileForYear = "DataFiles/sunrise_sunset_2000_Stockholm_horizon.csv"
val inputFile = "DataFiles/2000-Europe-Stockholm.csv"
val outputFile = "Year2000.csv"
val outputFileXls = "DataFiles/Year2000_Stockholm.xlsx"

fun main() {
    val moonPhaseCalc = MoonPhaseCalc(File(moonPhaseFileForYear))
    val sunriseSunsetCalc = SunriseCalc(File(sunriseFileForYear), timeZoneAdjustment)
    val visibilityConverter = VisibilityConverter(Random())
    val almanac = AlmanacMapper(
        sunriseSunsetCalc = sunriseSunsetCalc,
        moonPhaseCalc = moonPhaseCalc,
        visibilityConverter = visibilityConverter,
        startingGroundCover = GroundCover(
            mudDepth = 0f,
            snowDepth = 586f,
            packedSnowDepth = 293f,
            frozenMudDepth = 24f,
            frostDepth = 215f,
            topMudFrozen = false,
            meltingSnow = false
        )
    ).makeAlmanac(File(inputFile))
//    writeCsv(almanac, File(outputFile))
    XlsMapper().writeXls(almanac, File(outputFileXls))
}




