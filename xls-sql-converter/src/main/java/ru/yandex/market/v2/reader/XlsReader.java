package ru.yandex.market.v2.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ru.yandex.market.v2.table.Field;
import ru.yandex.market.v2.table.Header;
import ru.yandex.market.v2.table.Row;
import ru.yandex.market.v2.table.Table;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

public class XlsReader {
    private static final String NULL = "null";
    private static final String SQL_PREFIX = "sql:";
    private static final String INFO_DEL = "del";
    private static final String INFO_TRUNCATE = "truncate";
    private static final String INFO_UPDATE = "update";
    private static final String SHEET_TRUNCATE = "TRUNCATE";


    private final HSSFWorkbook workbook;

    public XlsReader(String path) {
        if (!path.endsWith(".xls")) {
            throw new IllegalArgumentException("Input should be xls file");
        }

        try {
            this.workbook = new HSSFWorkbook(new FileInputStream(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<Table> read() {
        List<Table> tables = new ArrayList<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (SHEET_TRUNCATE.equalsIgnoreCase(sheet.getSheetName())) {
                tables.addAll(readTruncateSheet(sheet));
            } else {
                tables.addAll(readSheet(sheet));
            }
        }

        try {
            workbook.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return tables;
    }

    private List<Table> readSheet(HSSFSheet sheet) {
        String tableName = sheet.getSheetName();
        HSSFRow columnsRow = sheet.getRow(1);

        List<String> schemas = getSchemas(sheet);
        List<Header> headers = getColumns(sheet);
        List<Row> values = getRows(sheet, 3, columnsRow.getFirstCellNum(), columnsRow.getLastCellNum());
        return schemas.stream()
            .map(schema -> new Table(schema, tableName, headers, values))
            .collect(Collectors.toList());
    }

    private List<Table> readTruncateSheet(HSSFSheet sheet) {
        List<Row> rows = getRows(sheet, 1, 0, 2);
        LinkedList<Table> tables = new LinkedList<>();
        rows.stream()
            .map(row -> new Table(
                row.getField(0).getRawValue(),
                row.getField(1).getRawValue(),
                Collections.singletonList(new Header("dummy", false, true, false)),
                Collections.emptyList()))
            .forEach(tables::addFirst);
        return tables;
    }

    private List<String> getSchemas(HSSFSheet sheet) {
        String[] schemas = sheet.getRow(0).getCell(0).getStringCellValue().split(",");
        return Arrays.stream(schemas)
            .map(String::trim)
            .collect(Collectors.toList());
    }

    private List<Header> getColumns(HSSFSheet sheet) {
        HSSFRow columnsRow = sheet.getRow(1);
        HSSFRow infoRow = sheet.getRow(2);

        List<Header> headers = new ArrayList<>();

        for (int c = columnsRow.getFirstCellNum(); c <= columnsRow.getLastCellNum(); c++) {
            HSSFCell columnCell = columnsRow.getCell(c);
            if (columnCell == null || StringUtils.isBlank(columnCell.getStringCellValue())) {
                break;
            }
            String info = Optional.ofNullable(infoRow.getCell(c)).map(HSSFCell::getStringCellValue).orElse("");
            Header header = new Header(
                columnCell.getStringCellValue(),
                info.contains(INFO_DEL),
                info.contains(INFO_TRUNCATE),
                info.contains(INFO_UPDATE)
            );
            headers.add(header);
        }
        return headers;
    }

    private List<Row> getRows(HSSFSheet sheet, int firstRowIdx, int minIdx, int maxIdx) {
        List<Row> rows = new ArrayList<>();
        for (int r = firstRowIdx; r <= sheet.getLastRowNum(); r++) {
            HSSFRow sheetRow = sheet.getRow(r);
            if (sheetRow == null) {
                break;
            }
            List<Field> fields = new ArrayList<>();

            for (int c = minIdx; c < maxIdx; c++) {
                HSSFCell cell = sheetRow.getCell(c, CREATE_NULL_AS_BLANK);
                fields.add(getField(cell));
            }

            if (fields.stream().allMatch(s -> StringUtils.isBlank(s.getRawValue()))) {
                break;
            }
            rows.add(new Row(fields));
        }
        return rows;
    }

    private Field getField(HSSFCell cell) {
        switch (cell.getCellType()) {
            case BLANK:
            case STRING:
                String str = cell.getStringCellValue();
                if (NULL.equalsIgnoreCase(str)) {
                    return Field.of(str);
                } else if (str.startsWith(SQL_PREFIX)) {
                    return Field.of(str.substring(SQL_PREFIX.length()));
                } else {
                    return Field.quoted(str);
                }
            case NUMERIC:
                double dbl = cell.getNumericCellValue();
                return Double.compare(dbl, (int) dbl) == 0
                    ? Field.of((int) dbl)
                    : Field.of(dbl);
            default:
                throw new UnsupportedOperationException("Unsupported cell type: " + cell.getCellType());
        }
    }
}
