/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp.backend.Archiving;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class ScholarsTableArchiver {
    private Connection connection;

    public ScholarsTableArchiver(Connection connection) {
        this.connection = connection;
    }

   public void archive() throws SQLException {
    // retrieve the scholars table data
    Statement statement = connection.createStatement();
    ResultSet resultSet = statement.executeQuery("SELECT * FROM scholars");

    // check if the table is empty
    if (!resultSet.next()) {
        System.out.println("The table in the database is empty, please import a new list of scholars!");
        return; // exit the method
    }

    // insert the data into the archive table
    PreparedStatement insertStatement = connection.prepareStatement("INSERT INTO scholars_archive (Scholar_Id, FirstName, LastName, archived_at, expiry_date) VALUES (?, ?, ?, ?, ?)");
    while (resultSet.next()) {
        int scholarId = resultSet.getInt("Scholar_Id");
        String firstName = resultSet.getString("FirstName");
        String lastName = resultSet.getString("LastName");
        Timestamp archiveAt = Timestamp.valueOf(LocalDateTime.now()); // set archived_at to current date and time
        Timestamp expiryDate = Timestamp.valueOf(LocalDateTime.now().plusYears(3)); // set expiry_date to 3 years from now
        insertStatement.setInt(1, scholarId);
        insertStatement.setString(2, firstName);
        insertStatement.setString(3, lastName);
        insertStatement.setTimestamp(4, archiveAt);
        insertStatement.setTimestamp(5, expiryDate);
        insertStatement.executeUpdate();
    }

    // delete the expired data from the archive table
    LocalDateTime currentDateTime = LocalDateTime.now();
    PreparedStatement deleteStatement = connection.prepareStatement("DELETE FROM scholars_archive WHERE expiry_date <= ?");
    deleteStatement.setTimestamp(1, Timestamp.valueOf(currentDateTime));
    deleteStatement.executeUpdate();

    // delete the data from the original table
    statement.execute("DELETE FROM scholars");

    // commit the changes
    connection.commit();

   
}
}
