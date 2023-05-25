package randomasterdeskapp.backend.TableArrangement;

import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

import javax.swing.JFileChooser;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import randomasterdeskapp.backend.DatabaseConnection;
import randomasterdeskapp.backend.Scholar;

// This class provides methods for generating random table arrangements for scholars.

public class Randomizer {
    
    // This method saves the table arrangement to an Excel file.

    public static void saveTableArrangement(String title, String tableName, int tableSize) {
        
        // initializes an empty list tables to store the table arrangements
        List<List<String>> tables = generateGroupings(tableSize);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Table Arrangement");

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

            //Create the header row.
            Row headerRow = sheet.createRow(0);
            int columnIndex = 0;
            for (int i = 0; i < tables.size(); i++) {
                String tableHeader = "Table " + (i + 1);
                Cell headerCell = headerRow.createCell(columnIndex);
                headerCell.setCellValue(tableHeader);
                headerCell.setCellStyle(headerCellStyle);

                columnIndex++;
            }
            
            //Create the data rows.
            int maxTableSize = getMaxTableSize(tables);
            for (int i = 0; i < maxTableSize; i++) {
                Row dataRow = sheet.createRow(i + 1);
                columnIndex = 0;
                for (int j = 0; j < tables.size(); j++) {
                    List<String> table = tables.get(j);
                    if (i < table.size()) {
                        String fullName = table.get(i);
                        Cell dataCell = dataRow.createCell(columnIndex);
                        dataCell.setCellValue(fullName);
                        dataCell.setCellStyle(cellStyle);
                    } else {
                        Cell dataCell = dataRow.createCell(columnIndex);
                        dataCell.setCellValue("");
                        dataCell.setCellStyle(cellStyle);
                    }
                    columnIndex++;
                }

                /* placing the autosize column code inside the loop, for this to be executed after each column is filled with data, ensuring
that the column widths are adjusted properly based on the content.*/
                // Autosize columns
                for (int j = 0; j < tables.size(); j++) {
                    sheet.autoSizeColumn(j);
                }
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setSelectedFile(new File(title + ".xlsx"));
            int userChoice = fileChooser.showSaveDialog(null);
            if (userChoice == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                String filePath = file.getAbsolutePath();
                if (!filePath.endsWith(".xlsx")) {
                    file = new File(filePath + ".xlsx");
                }

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                    JOptionPane.showMessageDialog(null, "Table assignment exported successfully.");
                } catch (IOException e) {
                    if (e instanceof java.nio.file.FileAlreadyExistsException) {
                        JOptionPane.showMessageDialog(null, "The file already exists.", "Export Error", JOptionPane.ERROR_MESSAGE);
                    } else if (e instanceof java.io.FileNotFoundException && e.getMessage().contains("used by another process")) {
                        JOptionPane.showMessageDialog(null, "The file is being used by another process.", "Export Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(null, "Failed to export the table assignment.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

 // This method gets the total number of scholars in the database.
    public static int getNumScholars() {

        int numScholars = 0;

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement()) {
            String query = "SELECT COUNT(*) FROM scholars";
            ResultSet rs = stmt.executeQuery(query);
            if (rs.next()) {
                numScholars = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return numScholars;
    }

    // Method to get the maximum table size to be used in the table arrangements frame also.
    private static int getMaxTableSize(List<List<String>> tables) {
        int maxTableSize = 0;
        for (List<String> table : tables) {
            if (table.size() > maxTableSize) {
                maxTableSize = table.size();
            }
        }
        return maxTableSize;
    }

    public static List<List<String>> generateGroupings(int tableSize) {
        // get a list of all scholars from the database
        String sql = "SELECT * FROM scholars";
        List<Scholar> scholars = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("Scholar_Id");
                String firstName = rs.getString("FirstName");
                String lastName = rs.getString("LastName");
                Scholar scholar = new Scholar(id, firstName, lastName);
                scholars.add(scholar);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // randomly shuffle the scholars
        Collections.shuffle(scholars);

       // Randomly group the scholars into tables of the specified size.
        List<List<String>> tables = new ArrayList<>();
        
        Set<Integer> usedIndexes = new HashSet<>();// to keep track of the indexes of scholars that have already been assigned to a table.
        int numTables = (int) Math.ceil((double) scholars.size() / tableSize); // calculate the number of tables needed
        for (int i = 0; i < numTables; i++) {
            List<String> table = new ArrayList<>();
            int startIndex = i * tableSize; // calculate the starting index of the current table
            int endIndex = Math.min(startIndex + tableSize, scholars.size()); // calculate the ending index of the current table
            
            // Loop to assign scholars to the current table
            for (int j = startIndex; j < endIndex; j++) {
                if (usedIndexes.contains(j)) {
                    // Skip if scholar already added to another table.
                    continue;
                }
                Scholar scholar = scholars.get(j);
                String fullName = scholar.getFirstName() + " " + scholar.getLastName();
                table.add(fullName);
                usedIndexes.add(j);
            }
            tables.add(table);
        }

        return tables;
    }
}
