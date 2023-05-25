/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp.backend.profilebackend;

/**
 *
 * @author Student Admin
 */
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JFrame;
import randomasterdeskapp.backend.DatabaseConnection;
import randomasterdeskapp.pages.UserProfile;


public class ProfileController {
    public static void openUserProfile() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            // execute SQL query and retrieve result set
            if (connection != null) {
                String query = "SELECT first_name, last_name, email, image FROM accounts WHERE user_id = 1";

                // create a Statement object to execute the query
                Statement statement = connection.createStatement();

                // execute the query and retrieve the result set
                ResultSet rs = statement.executeQuery(query);

                // check if result set has a row
                if (rs.next()) {
                    // retrieve data from result set
                    String firstName = rs.getString("first_name");
                    String lastName = rs.getString("last_name");
                    String email = rs.getString("email");
                    byte[] imageBytes = rs.getBytes("image");

                    // create an instance of UserProfile and pass the required information as arguments
                    JFrame userProfile = new UserProfile(firstName, lastName, email, imageBytes);
                    userProfile.setVisible(true);
                } else {
                    // handle case where no data is returned for user with id = 1
                    // e.g., display error message
                    System.out.println("No data found for user with id = 1");
                }

                // close the result set, statement, and connection
                rs.close();
                statement.close();
                connection.close();
            }
        } catch (SQLException e) {
            // handle any SQL errors
            e.printStackTrace();
        }
    }
}