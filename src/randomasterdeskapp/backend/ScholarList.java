/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp.backend;

/**
 *
 * @author Student Admin
 */


import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ScholarList {
    private Connection conn;
    
    public ScholarList(Connection conn) {
        this.conn = conn;
    }
    
    public List<Scholar> getAllScholars() throws SQLException {
        List<Scholar> scholars = new ArrayList<>();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT Scholar_Id, FirstName, LastName FROM Scholars");
        while (rs.next()) {
            int id = rs.getInt("Scholar_Id");
            String firstName = rs.getString("FirstName");
            String lastName = rs.getString("LastName");
            Scholar scholar = new Scholar(id, firstName, lastName);
            scholars.add(scholar);
        }
        return scholars;
    }
}
