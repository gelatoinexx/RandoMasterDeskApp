/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp.backend.taskingrandomizer;



import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class SaveandExportBackend {
    List<String> columns = new ArrayList<>();

    public void saveResult(String filePath,JTable jTableTaskingRandomizer, DefaultTableModel model,
            List<String> columns) {
        try {
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Task Assignments");

            int numRows = jTableTaskingRandomizer.getRowCount();
            int numCols = jTableTaskingRandomizer.getColumnCount();
            
            CellStyle cellStyle = workbook.createCellStyle();
            Font cellFont = workbook.createFont();
            cellFont.setFontName("Arial");
            cellFont.setFontHeightInPoints((short) 11);
            cellStyle.setFont(cellFont);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

            Font headerFont = workbook.createFont();
            headerFont.setFontName("Arial");
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setFont(headerFont);
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            

            // Write table header
            Row headerRow = sheet.createRow(0);
            for (int j = 0; j < numCols; j++) {
                String columnName = jTableTaskingRandomizer.getColumnName(j);
                Cell cell = headerRow.createCell(j);
                cell.setCellValue(columnName);
                cell.setCellStyle(headerCellStyle);
                
            }

            // Write table data
            for (int i = 0; i < numRows; i++) {
                Row dataRow = sheet.createRow(i + 1);
                for (int j = 0; j < numCols; j++) {
                    Object value = jTableTaskingRandomizer.getValueAt(i, j);
                    Cell cell = dataRow.createCell(j);
                    cell.setCellValue(value != null ? value.toString() : "");
                    cell.setCellStyle(cellStyle);
                }
            }

            // Autosize columns
            for (int j = 0; j < numCols; j++) {
                sheet.autoSizeColumn(j);
            }

            // Write the workbook to the Excel file
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            workbook.write(fileOutputStream);
            fileOutputStream.close();
            workbook.close();

            JOptionPane.showMessageDialog(null, "Tasking result exported to Excel successfully.");

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Error exporting tasking result to Excel: " + e.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
