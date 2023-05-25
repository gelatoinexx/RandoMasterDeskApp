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
import java.sql.SQLException;
import java.util.List;


public class ScholarController {
    private final ScholarList scholarList;
    
    public ScholarController() throws SQLException {
       Connection conn = DatabaseConnection.getConnection();
        this.scholarList = new ScholarList(conn);
    }
    
    public List<Scholar> getAllScholars() throws SQLException {
        return scholarList.getAllScholars();
    }
}
