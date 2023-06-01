package org.siemac.metamac.statistical_resources.rest.common.impl.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;

public class CommonDatasetUtils {

    @Test
    public static void checkXlsDatasetResponse(String requestUri) throws Exception {
        WebClient webClient = WebClient.create(requestUri).accept("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        Response response = webClient.get();
        InputStream responseActual = (InputStream) response.getEntity();
        Workbook file = WorkbookFactory.create(responseActual);

        Sheet sheet = file.getSheetAt(0);

        Iterator<Row> rowIterator = sheet.rowIterator();
        Row row = rowIterator.next();
        int cellCount = row.getPhysicalNumberOfCells();

        assertEquals(19, cellCount);

        // Header row
        // For debug purposes: System.out.println(row.getCell(0).getStringCellValue();
        assertEquals("GEO_DIM#language01", row.getCell(0).getStringCellValue());
        assertEquals("GEO_DIM#language02", row.getCell(1).getStringCellValue());
        assertEquals("GEO_DIM#es", row.getCell(2).getStringCellValue());
        assertEquals("GEO_DIM_CODE", row.getCell(3).getStringCellValue());
        assertEquals("TIME_PERIOD#language01", row.getCell(4).getStringCellValue());
        assertEquals("TIME_PERIOD#language02", row.getCell(5).getStringCellValue());
        assertEquals("TIME_PERIOD#es", row.getCell(6).getStringCellValue());
        assertEquals("TIME_PERIOD_CODE", row.getCell(7).getStringCellValue());
        assertEquals("measure01#language01", row.getCell(8).getStringCellValue());
        assertEquals("measure01#language02", row.getCell(9).getStringCellValue());
        assertEquals("measure01#es", row.getCell(10).getStringCellValue());
        assertEquals("measure01_CODE", row.getCell(11).getStringCellValue());
        assertEquals("dim01#language01", row.getCell(12).getStringCellValue());
        assertEquals("dim01#language02", row.getCell(13).getStringCellValue());
        assertEquals("dim01#es", row.getCell(14).getStringCellValue());
        assertEquals("dim01_CODE", row.getCell(15).getStringCellValue());
        assertEquals("OBS_VALUE", row.getCell(16).getStringCellValue());
        assertEquals("at10", row.getCell(17).getStringCellValue());
        assertEquals("at11", row.getCell(18).getStringCellValue());

        // Data Row 1
        Row rowData1 = rowIterator.next();
        assertEquals(19, rowData1.getPhysicalNumberOfCells());

        // System.out.println(rowData1.getCell(0).getStringCellValue());
        assertTrue(checkIsEmptyCell(rowData1.getCell(0)));
        assertTrue(checkIsEmptyCell(rowData1.getCell(1)));
        assertEquals("santa-cruz-tenerife en Español", rowData1.getCell(2).getStringCellValue());
        assertEquals("santa-cruz-tenerife", rowData1.getCell(3).getStringCellValue());
        assertTrue(checkIsEmptyCell(rowData1.getCell(4)));
        assertTrue(checkIsEmptyCell(rowData1.getCell(5)));
        assertEquals("2011", rowData1.getCell(6).getStringCellValue());
        assertEquals("2011", rowData1.getCell(7).getStringCellValue());
        assertTrue(checkIsEmptyCell(rowData1.getCell(8)));
        assertTrue(checkIsEmptyCell(rowData1.getCell(9)));
        assertEquals("measure01-conceptScheme01-concept01 en Español", rowData1.getCell(10).getStringCellValue());
        assertEquals("measure01-conceptScheme01-concept01", rowData1.getCell(11).getStringCellValue());
        assertTrue(checkIsEmptyCell(rowData1.getCell(12)));
        assertTrue(checkIsEmptyCell(rowData1.getCell(13)));
        assertEquals("dim01-codelist01-code01 en Español", rowData1.getCell(14).getStringCellValue());
        assertEquals("dim01-codelist01-code01", rowData1.getCell(15).getStringCellValue());
        assertEquals("1", rowData1.getCell(16).getStringCellValue());
        assertEquals("Value 1", rowData1.getCell(17).getStringCellValue());
        assertEquals("Value 5", rowData1.getCell(18).getStringCellValue());

    }

    private static boolean checkIsEmptyCell(Cell cell) {
        if (cell == null) { // use row.getCell(x, Row.CREATE_NULL_AS_BLANK) to avoid null cells
            return true;
        }

        if (cell.getCellType() == CellType.BLANK) {
            return true;
        }

        if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty()) {
            return true;
        }

        return false;
    }

    public static void checkCsvDatasetResponse(String requestUri) throws Exception {
        List<String> lines = getResponse(requestUri, "text/csv");

        Iterator<String> iterator = lines.iterator();

        assertEquals(
                "GEO_DIM#language01,GEO_DIM#language02,GEO_DIM#es,GEO_DIM_CODE,TIME_PERIOD#language01,TIME_PERIOD#language02,TIME_PERIOD#es,TIME_PERIOD_CODE,measure01#language01,measure01#language02,measure01#es,measure01_CODE,dim01#language01,dim01#language02,dim01#es,dim01_CODE,OBS_VALUE,at10,at11",
                iterator.next());
        assertTrue(lines.contains(
                ",,santa-cruz-tenerife en Español,santa-cruz-tenerife,,,2011,2011,,,measure01-conceptScheme01-concept01 en Español,measure01-conceptScheme01-concept01,,,dim01-codelist01-code01 en Español,dim01-codelist01-code01,1,Value 1,Value 5"));
        assertTrue(lines.contains(
                ",,santa-cruz-tenerife en Español,santa-cruz-tenerife,,,2011,2011,,,measure01-conceptScheme01-concept01 en Español,measure01-conceptScheme01-concept01,,,dim01-codelist01-code03 en Español,dim01-codelist01-code03,2,Value 2,"));

    }

    public static void checkTsvDatasetResponse(String requestUri) throws Exception {
        List<String> lines = getResponse(requestUri, "text/tab-separated-values");

        Iterator<String> iterator = lines.iterator();

        assertEquals(
                "GEO_DIM#language01\tGEO_DIM#language02\tGEO_DIM#es\tGEO_DIM_CODE\tTIME_PERIOD#language01\tTIME_PERIOD#language02\tTIME_PERIOD#es\tTIME_PERIOD_CODE\tmeasure01#language01\tmeasure01#language02\tmeasure01#es\tmeasure01_CODE\tdim01#language01\tdim01#language02\tdim01#es\tdim01_CODE\tOBS_VALUE\tat10\tat11",
                iterator.next());
        assertTrue(lines.contains(
                "\t\tsanta-cruz-tenerife en Español\tsanta-cruz-tenerife\t\t\t2011\t2011\t\t\tmeasure01-conceptScheme01-concept01 en Español\tmeasure01-conceptScheme01-concept01\t\t\tdim01-codelist01-code01 en Español\tdim01-codelist01-code01\t1\tValue 1\tValue 5"));
        assertTrue(lines.contains(
                "\t\tsanta-cruz-tenerife en Español\tsanta-cruz-tenerife\t\t\t2011\t2011\t\t\tmeasure01-conceptScheme01-concept01 en Español\tmeasure01-conceptScheme01-concept01\t\t\tdim01-codelist01-code03 en Español\tdim01-codelist01-code03\t2\tValue 2\t"));

    }

    private static List<String> getResponse(String requestUri, String mediaType) throws Exception {
        WebClient webClient = WebClient.create(requestUri).accept(mediaType);
        Response response = webClient.get();

        InputStream responseActual = (InputStream) response.getEntity();
        BufferedReader br = new BufferedReader(new InputStreamReader(responseActual, "UTF-8"));

        List<String> lines = new ArrayList<String>();
        String line = null;
        while ((line = br.readLine()) != null) {
            // System.out.println(line.replaceAll("\t", "\\\\t"));
            lines.add(line);
        }

        return lines;
    }
}
