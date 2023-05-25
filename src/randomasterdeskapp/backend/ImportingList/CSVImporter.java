/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp.backend.ImportingList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import randomasterdeskapp.backend.Scholar;
import java.sql.ResultSet;

/**
 *
 * @author Student Admin
 */
public class CSVImporter implements Importable {

    private Connection conn;

    public CSVImporter(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void importFile(File file) throws IOException, SQLException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line;
        while ((line = br.readLine()) != null) {
            String[] data = line.split(",");
            if (data.length >= 3) {
                String idString = data[0];
                if (idString.matches("\\d+")) {
                    int id = Integer.parseInt(idString);
                    String firstName = data[1];
                    String lastName = data[2];
                    Scholar scholar = new Scholar(id, firstName, lastName);
                    insertScholar(scholar);
                } else {
                    // Handle invalid integer value
                    System.err.println("Invalid integer value for Scholar_Id: " + idString);
                }
            }
        }
        br.close();
    }

    private void insertScholar(Scholar scholar) {
        try {
            // check if the scholar already exists
            String selectSql = "SELECT Scholar_Id FROM scholars WHERE Scholar_Id = ?";
            PreparedStatement selectStmt = conn.prepareStatement(selectSql);
            selectStmt.setInt(1, scholar.getScholarId());
            ResultSet rs = selectStmt.executeQuery();
            if (rs.next()) {
                // scholar already exists, do not insert
                return;
            }
            // insert the new scholar
            String insertSql = "INSERT INTO scholars (Scholar_Id, FirstName, LastName) VALUES (?, ?, ?)";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, scholar.getScholarId());
            insertStmt.setString(2, scholar.getFirstName());
            insertStmt.setString(3, scholar.getLastName());
            insertStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // add any additional error handling or logging here
        }
    }

}
