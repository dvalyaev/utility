package app.converter.reader

import app.converter.table.Field
import app.converter.table.Header
import app.converter.table.Row
import app.converter.table.Table
import org.apache.poi.hssf.usermodel.HSSFCell
import org.apache.poi.hssf.usermodel.HSSFSheet
import org.apache.poi.hssf.usermodel.HSSFWorkbook
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy
import java.io.FileInputStream


class XlsReader(val path: String) {

    companion object {
        private const val NULL = "null"
        private const val SQL_PREFIX = "sql:" // example:  sql:select wmwhse1.LocalDayBeginInUtc(-10)
        private const val INFO_DEL = "del"
        private const val INFO_TRUNCATE = "truncate"
        private const val INFO_UPDATE = "update"
        private const val INFO_IDENTITY_INSERT = "identity-insert"
        private const val SHEET_TRUNCATE = "TRUNCATE"
    }

    init {
        require(path.endsWith(".xls")) { "Input should be xls file" }
    }

    fun read(): List<Table> = HSSFWorkbook(FileInputStream(path)).use { workbook ->
        (0 until workbook.numberOfSheets).asSequence()
            .map { workbook.getSheetAt(it) }
            .flatMap {
                if (SHEET_TRUNCATE.equals(it.sheetName, ignoreCase = true))
                    readTruncateSheet(it)
                else readSheet(it)
            }.toList()
    }


    private fun readSheet(sheet: HSSFSheet): List<Table> {
        val columnsRow = sheet.getRow(1)
        return getSchemas(sheet).map {
            Table(
                schema = it,
                name = sheet.sheetName,
                headers = getHeaders(sheet),
                rows = getRows(sheet, 3, columnsRow.firstCellNum.toInt(), columnsRow.lastCellNum.toInt() - 1)
            )
        }
    }

    private fun readTruncateSheet(sheet: HSSFSheet): List<Table> =
        getRows(sheet, 1, 0, 1)
            .map { row ->
                Table(
                    row.field(0).rawValue,
                    row.field(1).rawValue,
                    listOf(Header("dummy", isTruncate = true)),
                    listOf()
                )
            }.asReversed()

    private fun getSchemas(sheet: HSSFSheet): List<String> =
        sheet.getRow(0).getCell(0).stringCellValue.split(",").map { it.trim() }

    private fun getHeaders(sheet: HSSFSheet): List<Header> {
        val columnsRow = sheet.getRow(1)
        val infoRow = sheet.getRow(2)
        val headers = mutableListOf<Header>()
        for (c in columnsRow.firstCellNum..columnsRow.lastCellNum) {
            val columnCell = columnsRow.getCell(c)?.stringCellValue?.takeIf { it.isNotBlank() } ?: break
            val info = infoRow?.getCell(c)?.stringCellValue ?: ""
            headers += Header(
                name = columnCell,
                isDelete = INFO_DEL in info,
                isTruncate = INFO_TRUNCATE in info,
                isUpdate = INFO_UPDATE in info,
                isIdentityInsert = INFO_IDENTITY_INSERT in info
            )
        }
        return headers
    }

    private fun getRows(sheet: HSSFSheet, firstRowIdx: Int, minIdx: Int, maxIdx: Int): List<Row> {
        val rows = mutableListOf<Row>()
        for (r in firstRowIdx..sheet.lastRowNum) {
            val sheetRow = sheet.getRow(r) ?: break
            val fields = (minIdx..maxIdx)
                .map { getField(sheetRow.getCell(it, MissingCellPolicy.CREATE_NULL_AS_BLANK)) }
            if (fields.all { it.rawValue.isBlank() }) {
                break
            }
            rows += Row(fields)
        }
        return rows
    }

    private fun getField(cell: HSSFCell): Field {
        return when (cell.cellType) {
            CellType.BLANK, CellType.STRING -> {
                val str = cell.stringCellValue
                when {
                    NULL.equals(str, ignoreCase = true) -> Field.of(str)
                    str.startsWith(SQL_PREFIX) -> Field.of("(" + str.substring(SQL_PREFIX.length) + ")")
                    else -> Field.quoted(str)
                }
            }
            CellType.NUMERIC -> {
                val dbl: Double = cell.numericCellValue
                if (dbl == dbl.toInt().toDouble()) Field.of(dbl.toInt()) else Field.of(dbl)
            }
            CellType.BOOLEAN -> Field.of(cell.booleanCellValue)
            else -> throw UnsupportedOperationException("Unsupported cell type: ${cell.cellType}. ${cell.sheet.sheetName} ${cell.address}")
        }
    }
}
