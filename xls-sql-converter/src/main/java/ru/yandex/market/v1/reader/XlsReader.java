package ru.yandex.market.v1.reader;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import ru.yandex.market.v1.table.Field;
import ru.yandex.market.v1.table.Header;
import ru.yandex.market.v1.table.Row;
import ru.yandex.market.v1.table.Table;

import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;

public class XlsReader {
    private static final String NULL = "null";
    private static final String SQL_PREFIX = "sql:";
    private static final String INFO_DEL = "del";
    private static final String INFO_TRUNCATE = "truncate";

    private final HSSFWorkbook workbook;
    private final String schema;


    public XlsReader(String path) {
        if (!path.endsWith(".xls")) {
            throw new IllegalArgumentException("Input should be xls file");
        }
        String fileName = Paths.get(path).getFileName().toString().replace(".xls", "");
        int lastDotIdx = fileName.lastIndexOf(".");
        this.schema = lastDotIdx > 0 ? fileName.substring(lastDotIdx + 1) : fileName;

        try {
            this.workbook = new HSSFWorkbook(new FileInputStream(path));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public List<Table> read() {
        List<Table> tables = new ArrayList<>();

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            tables.add(readSheet(i));
        }

        try {
            workbook.close();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        return tables;
    }

    private Table readSheet(int index) {
        HSSFSheet sheet = workbook.getSheetAt(index);
        String tableName = sheet.getSheetName();
        HSSFRow columnsRow = sheet.getRow(0);

        List<Header> headers = getColumns(sheet);
        List<Row> values = getRows(sheet, columnsRow.getFirstCellNum(), columnsRow.getLastCellNum());

        return new Table(schema, tableName, headers, values);
    }

    private List<Header> getColumns(HSSFSheet sheet) {
        HSSFRow columnsRow = sheet.getRow(0);
        HSSFRow infoRow = sheet.getRow(1);

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
                info.contains(INFO_TRUNCATE)
            );
            headers.add(header);
        }
        return headers;
    }

    private List<Row> getRows(HSSFSheet sheet, int minIdx, int maxIdx) {
        List<Row> rows = new ArrayList<>();
        for (int r = 2; r <= sheet.getLastRowNum(); r++) {
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
