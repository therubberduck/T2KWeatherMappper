package converters

import model.AlmanacDay
import model.AlmanacShift
import model.Shift
import org.apache.poi.hssf.usermodel.HSSFRichTextString
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.*
import java.io.File

class XlsMapper {
    fun writeXls(almanac: List<AlmanacDay>, outputFile: File) {
        val outputStream = outputFile.outputStream()
        val writer = outputStream.bufferedWriter(Charsets.UTF_8)

        val workBook = XSSFWorkbook()
        val workSheet = workBook.createSheet()

        val calibri9 = workBook.makeFont(
            fontName = "Calibri",
            fontSize = 9,
            isBold = false
        )
        val calibri10Bold = workBook.makeFont(
            fontName = "Calibri",
            fontSize = 10,
            isBold = true
        )
        val calibri11 = workBook.makeFont(
            fontName = "Calibri",
            fontSize = 11,
            isBold = false
        )
        val calibri11Bold = workBook.makeFont(
            fontName = "Calibri",
            fontSize = 11,
            isBold = true
        )
        val moonphases10 = workBook.makeFont(
            fontName = "Moonphases",
            fontSize = 10,
            isBold = false
        )

        val styles = Styles(
            commonLeftCellStyle = workBook.createCellStyle().apply {
                setFont(calibri11)
                alignment = HorizontalAlignment.LEFT
                verticalAlignment = VerticalAlignment.CENTER
            },
            commonCenterCellStyle = workBook.createCellStyle().apply {
                setFont(calibri11)
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
            },
            boldLeftCellStyle = workBook.createCellStyle().apply {
                setFont(calibri11Bold)
                alignment = HorizontalAlignment.LEFT
                verticalAlignment = VerticalAlignment.CENTER
            },
            boldCenterCellStyle = workBook.createCellStyle().apply {
                setFont(calibri11Bold)
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.CENTER
            },
            dateCellStyle = workBook.createCellStyle().apply {
                setFont(calibri10Bold)
                alignment = HorizontalAlignment.CENTER
                rotation = 90
                verticalAlignment = VerticalAlignment.CENTER
            },
            timeCellStyle = workBook.createCellStyle().apply {
                setFont(calibri9)
                alignment = HorizontalAlignment.CENTER
                rotation = 90
                verticalAlignment = VerticalAlignment.CENTER
            },
            moonPhaseCellStyle = workBook.createCellStyle().apply {
                setFont(moonphases10)
                alignment = HorizontalAlignment.CENTER
                verticalAlignment = VerticalAlignment.BOTTOM
            },
            shiftCellStyle = workBook.createCellStyle().apply {
                alignment = HorizontalAlignment.LEFT
                verticalAlignment = VerticalAlignment.CENTER
            },
            shiftUpperFont = workBook.createFont().apply {
                fontName = "Calibri"
                fontHeightInPoints = 11
                bold = false
            },
            shiftLowerFont = workBook.createFont().apply {
                fontName = "Cavolini"
                fontHeightInPoints = 9
                bold = true
            }
        )

        almanac.forEachIndexed { index, day ->
            val localDate = day.dateTime.toLocalDate()
            val date = localDate.toString("MMM d") + getDayOfMonthSuffix(localDate.dayOfMonth)

            val headerRow = workSheet.createRow(index * 5 + 0)
            addCell(headerRow, 0, date, styles.dateCellStyle)
            addCell(headerRow, 1, "", styles.commonCenterCellStyle)
            addCell(headerRow, 2, "Shift", styles.boldLeftCellStyle)
            addCell(headerRow, 3, "Felt Temp", styles.boldCenterCellStyle)
            addCell(headerRow, 4, "Temp", styles.boldCenterCellStyle)
            addCell(headerRow, 5, "Weather", styles.boldLeftCellStyle)
            addCell(headerRow, 6, "CC", styles.boldCenterCellStyle)
            addCell(headerRow, 7, "Wind", styles.boldLeftCellStyle)
            addCell(headerRow, 8, "Ground", styles.boldLeftCellStyle)
            addCell(headerRow, 9, "Visibility", styles.boldCenterCellStyle)
            addCell(headerRow, 10, "NV", styles.boldCenterCellStyle)

            workSheet.addDataRow(index * 5 + 1, day.night, day, styles)
            workSheet.addDataRow(index * 5 + 2, day.morning, day, styles)
            workSheet.addDataRow(index * 5 + 3, day.afternoon, day, styles)
            workSheet.addDataRow(index * 5 + 4, day.evening, day, styles)

            writer.write(",,\"${day.events}\"")
            writer.newLine()
        }

        workBook.write(outputFile.outputStream())
        workBook.close()

        println("Almanac saved to ${outputFile.name}")
    }

    private fun XSSFSheet.addDataRow(rowIndex: Int, shift: AlmanacShift, day: AlmanacDay, styles: Styles) {
        val row = createRow(rowIndex)
        when (shift.shift) {
            Shift.EVENING -> addCell(row, 0, day.moonPhase, styles.moonPhaseCellStyle)
            else -> addCell(row, 0, "", styles.commonCenterCellStyle)
        }
        when (shift.shift) {
            Shift.NIGHT -> addCell(row, 1, day.sunrise.toString("HH:mm"), styles.timeCellStyle)
            Shift.AFTERNOON -> addCell(row, 1, day.sunrise.toString("HH:mm"), styles.timeCellStyle)
            else -> addCell(row, 1, "", styles.commonCenterCellStyle)
        }
        when (shift.shift) {
            Shift.NIGHT -> addShiftTag(
                row,
                2,
                "Night",
                styles.shiftCellStyle,
                styles.shiftUpperFont,
                styles.shiftLowerFont
            )

            Shift.MORNING -> addShiftTag(
                row,
                2,
                "Morning",
                styles.shiftCellStyle,
                styles.shiftUpperFont,
                styles.shiftLowerFont
            )

            Shift.AFTERNOON -> addShiftTag(
                row,
                2,
                "Day",
                styles.shiftCellStyle,
                styles.shiftUpperFont,
                styles.shiftLowerFont
            )

            Shift.EVENING -> addShiftTag(
                row,
                2,
                "Evening",
                styles.shiftCellStyle,
                styles.shiftUpperFont,
                styles.shiftLowerFont
            )
        }
        addCell(row, 3, shift.feltTemp, styles.commonCenterCellStyle)
        addCell(row, 4, shift.temp, styles.commonCenterCellStyle)
        addCell(row, 5, shift.weather, styles.commonLeftCellStyle)
        addCell(row, 6, shift.cloudCover, styles.commonCenterCellStyle)
        addCell(row, 7, shift.wind, styles.commonLeftCellStyle)
        addCell(row, 8, shift.ground, styles.commonLeftCellStyle)
        addCell(row, 9, shift.visibility, styles.commonCenterCellStyle)
        addCell(row, 10, shift.nightVision, styles.commonCenterCellStyle)
    }

    private fun addCell(row: XSSFRow, column: Int, value: String, cellStyle: XSSFCellStyle) {
        row.createCell(column).apply {
            setCellStyle(cellStyle)
            setCellValue(value)
        }
    }

    private fun addShiftTag(
        row: XSSFRow,
        column: Int,
        value: String,
        cellStyle: XSSFCellStyle,
        upperFont: XSSFFont,
        lowerFont: XSSFFont
    ) {
        val richText = HSSFRichTextString(value)
        richText.applyFont(0, 1, upperFont)
        richText.applyFont(1, value.length, lowerFont)
        row.createCell(column).apply {
            setCellStyle(cellStyle)
            setCellValue(richText)
        }
    }

    private fun XSSFWorkbook.makeFont(fontName: String, fontSize: Short, isBold: Boolean = false): XSSFFont? {
        return createFont().apply {
            setFontName(fontName)
            fontHeightInPoints = fontSize
            bold = isBold
        }
    }

    private fun getDayOfMonthSuffix(n: Int): String {
        if (n in 11..13) {
            return "th"
        }
        return when (n % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    data class Styles(
        val commonLeftCellStyle: XSSFCellStyle,
        val commonCenterCellStyle: XSSFCellStyle,
        val boldLeftCellStyle: XSSFCellStyle,
        val boldCenterCellStyle: XSSFCellStyle,
        val dateCellStyle: XSSFCellStyle,
        val timeCellStyle: XSSFCellStyle,
        val moonPhaseCellStyle: XSSFCellStyle,
        val shiftCellStyle: XSSFCellStyle,
        val shiftUpperFont: XSSFFont,
        val shiftLowerFont: XSSFFont
    )
}