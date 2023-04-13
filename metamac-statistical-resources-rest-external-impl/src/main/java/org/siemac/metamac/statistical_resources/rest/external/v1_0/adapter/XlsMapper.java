package org.siemac.metamac.statistical_resources.rest.external.v1_0.adapter;

import static org.siemac.metamac.statistical_resources.rest.common.service.utils.StatisticalResourcesRestImplCommonUtils.manageException;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.siemac.metamac.rest.exception.RestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XlsMapper {

    private static Logger log = LoggerFactory.getLogger(XlsMapper.class);
    private static final int ROW_ACCESS_WINDOW_SIZE = 100;

    public void writeValue(List<Map<String, ?>> data, List<String> header, OutputStream outputStream) {
        try {
            exportToExcel(data, header, outputStream);
        } catch (Exception e) {
            throw manageException(e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    private void exportToExcel(List<Map<String, ?>> data, List<String> header, OutputStream outputStream) throws Exception {
        if (!data.isEmpty()) {
            try (SXSSFWorkbook workbook = new SXSSFWorkbook(ROW_ACCESS_WINDOW_SIZE)) {
                SXSSFSheet sheet = workbook.createSheet();

                createHeaderRow(header, sheet);
                int row = 1;
                for (Map<String, ?> observation : data) {
                    addObservationRow(observation, sheet, row++);
                }

                writeExcelWorkBookToOutputStream(outputStream, workbook);
            }
        }
    }

    private void writeExcelWorkBookToOutputStream(OutputStream outputStream, SXSSFWorkbook workbook) throws RestException {
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            log.error("Error writing excel to OutputStream", e);
            throw manageException(e);
        }
    }

    private void addObservationRow(Map<String, ?> data, SXSSFSheet sheet, int rowPosition) {

        int column = 0;
        SXSSFRow row = sheet.createRow(rowPosition);
        for (Entry<String, ?> entry : data.entrySet()) {
            addStringCell(row, column++, entry.getValue() != null ? entry.getValue().toString() : null);
        }
    }

    private void createHeaderRow(List<String> header, SXSSFSheet sheet) {
        SXSSFRow row = sheet.createRow(0);

        int column = 0;
        for (String headerColumnName : header) {
            addStringCell(row, column++, headerColumnName);
        }
    }

    private void addStringCell(SXSSFRow row, int column, String cellValue) {
        SXSSFCell cell = row.createCell(column);
        cell.setCellValue(cellValue);
        cell.setCellType(CellType.STRING);
    }

}
