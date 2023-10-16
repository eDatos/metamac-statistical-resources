package org.siemac.metamac.statistical_resources.rest.common.impl.export;

import static org.siemac.metamac.core.common.exception.CommonServiceExceptionType.UNKNOWN;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.statistical_resources.rest.common.impl.export.utils.ExportUtils;

public class ExcelMapper {

    private static final int ROW_ACCESS_WINDOW_SIZE = 100;
    SXSSFWorkbook            workbook;
    SXSSFSheet               sheet;
    int                      rowActualPosition      = 1;

    public ExcelMapper() {
        workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE);
        sheet = workbook.createSheet("Sheet1");
    }

    public void dispose() throws MetamacException {
        if (workbook != null) {
            try {
                workbook.close();
                workbook.dispose();
            } catch (Exception e) {
                throw new MetamacException(e, UNKNOWN, "dispose excel workbook");
            }
        }
    }

    public void createHeaderRow(Map<String, String> line) {
        SXSSFRow rowHeader = sheet.createRow(0);

        int column = 0;
        for (Map.Entry<String, String> columnObservation : line.entrySet()) {
            addStringCell(rowHeader, column++, columnObservation.getKey());
        }
    }

    public void addObservationRow(Map<String, String> line) {

        int column = 0;
        SXSSFRow observationRow = sheet.createRow(rowActualPosition++);

        for (Map.Entry<String, String> columnObservation : line.entrySet()) {
            String value = ExportUtils.escapeNulls(columnObservation.getValue());
            addStringCell(observationRow, column++, value);
        }
    }

    public void writeExcelWorkBookToOutputStream(OutputStream outputStream) throws MetamacException {
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new MetamacException(e, UNKNOWN, "Error writing excel workbook to outputStream");
        }
    }

    private void addStringCell(SXSSFRow row, int column, String cellValue) {
        SXSSFCell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        cell.setCellType(CellType.STRING);
    }

}