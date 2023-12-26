import almanac.AlmanacMapper
import almanac.MoonPhaseCalc
import almanac.VisibilityConverter
import converters.writeCsv
import mock.Random
import model.GroundCover
import java.io.File

val moonPhaseFileForYear = "moon-phases-2000-Europe_Warsaw.csv"
//val moonPhaseFileForYear = "moon-phases-1999-Europe_Warsaw.csv"
//val inputFile = "1999-September-Dec-Europe-Warsaw.csv"
val inputFile = "2000-Europe-Warsaw.csv"
//val outputFile = "output99.csv"
val outputFile = "Year2000.csv"

fun main() {
    val moonPhaseCalc = MoonPhaseCalc(File(moonPhaseFileForYear))
    val visibilityConverter = VisibilityConverter(Random())
//    val almanac = AlmanacMapper(moonPhaseCalc, visibilityConverter, GroundCover(0f, 0f, 0f, 0f, false)).makeAlmanac(File(inputFile))
    val almanac = AlmanacMapper(moonPhaseCalc, visibilityConverter, GroundCover(0f, 586f, 24f, 215f, false)).makeAlmanac(File(inputFile))
    writeCsv(almanac, File(outputFile))
}




