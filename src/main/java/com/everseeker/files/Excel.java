package com.everseeker.files;

import com.everseeker.App;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by everseeker on 16/9/6.
 */
public class Excel {
    private static Map excelCache = App.getCache();

    public static void readFileToMemory(String filePath) {
        File file = new File(filePath);
        try (Workbook book = WorkbookFactory.create(new FileInputStream(file))) {
            int sheetCount = book.getNumberOfSheets();
            for (int sheetNum = 0; sheetNum < sheetCount; sheetNum++) {
                //sheet表
                Sheet sheet = book.getSheetAt(sheetNum);
                String sheetName = sheet.getSheetName();
                //row行
                Iterator<Row> rowIterator = sheet.iterator();

                Map<String, String> rowMap = new LinkedHashMap<String, String>();
                int rowNum = 0;
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    //cell单元格
                    Iterator<Cell> cellIterator = row.cellIterator();
                    StringBuffer rowContent = new StringBuffer();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getCellType()) {
                            case Cell.CELL_TYPE_STRING:
                                rowContent.append(cell.getStringCellValue() + "\t");
                                break;
                            case Cell.CELL_TYPE_NUMERIC:
                                if (DateUtil.isCellDateFormatted(cell)) {
                                    rowContent.append(cell.getDateCellValue().toString() + "\t");
                                } else {
                                    rowContent.append(String.valueOf(cell.getNumericCellValue()) + "\t");
                                }
                                break;
                            case Cell.CELL_TYPE_BOOLEAN:
                                rowContent.append(String.valueOf(cell.getBooleanCellValue()) + "\t");
                                break;
                        }
                    }
                    rowMap.put("Row " + (++rowNum), rowContent.toString().toLowerCase());
                }
                excelCache.put(filePath + " --- " + sheetName, rowMap);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
    }
}
