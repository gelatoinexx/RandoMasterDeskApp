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

import java.sql.SQLException;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class ListScholarsClickListener implements ClickListener {
    private final ScholarController scholarController;
    private final JTable jTableListOfScholars;
    
    public ListScholarsClickListener(JTable jTableListOfScholars) throws SQLException {
        this.jTableListOfScholars = jTableListOfScholars;
        this.scholarController = new ScholarController();
    }
   
   @Override
   public void onClick() {
    try {
        List<Scholar> scholars = scholarController.getAllScholars();
        DefaultTableModel tableModel = (DefaultTableModel) jTableListOfScholars.getModel();
        tableModel.setRowCount(0);
        for (Scholar scholar : scholars) {
            Object[] rowData = {scholar.getScholarId(), scholar.getFirstName(), scholar.getLastName()};
            tableModel.addRow(rowData);
        }
        jTableListOfScholars.setModel(tableModel);
        jTableListOfScholars.repaint();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

    }


