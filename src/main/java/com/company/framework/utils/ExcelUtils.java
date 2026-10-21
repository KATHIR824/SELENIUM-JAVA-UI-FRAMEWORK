package com.company.framework.utils;

import com.company.framework.exceptions.FrameworkException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Utility class for reading test data from Excel spreadsheets (.xlsx, .xls) using Apache POI.
 */
public final class ExcelUtils {

    private ExcelUtils() {
        // Prevent instantiation
    }

    /**
     * Reads rows from an Excel worksheet into a List of Maps, where each Map represents a row keyed by column headers.
     *
     * @param excelFilePath path to the Excel file relative to classpath (e.g. "testdata/sample.xlsx")
     * @param sheetName     name of the worksheet to read
     * @return List of Map containing key-value data per row
     */
    public static List<Map<String, String>> getExcelData(String excelFilePath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try (InputStream is = ExcelUtils.class.getClassLoader().getResourceAsStream(excelFilePath)) {
            if (is == null) {
                throw new FrameworkException("Excel test data file not found in classpath: " + excelFilePath);
            }
            Workbook workbook = WorkbookFactory.create(is);
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new FrameworkException("Sheet '" + sheetName + "' not found in Excel file: " + excelFilePath);
            }

            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                return dataList;
            }

            int cols = headerRow.getLastCellNum();
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < cols; j++) {
                    Cell headerCell = headerRow.getCell(j);
                    Cell dataCell = row.getCell(j);

                    String key = headerCell != null ? headerCell.getStringCellValue().trim() : "col" + j;
                    String value = (dataCell != null) ? formatter.formatCellValue(dataCell).trim() : "";
                    rowData.put(key, value);
                }
                dataList.add(rowData);
            }
        } catch (Exception e) {
            throw new FrameworkException("Failed to read Excel data from: " + excelFilePath, e);
        }
        return dataList;
    }
}
