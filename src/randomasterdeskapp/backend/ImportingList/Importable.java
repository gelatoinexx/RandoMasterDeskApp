/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp.backend.ImportingList;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author Student Admin
 */
public interface Importable {
    public void importFile(File file) throws IOException, SQLException;
}
