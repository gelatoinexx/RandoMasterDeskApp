/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp.backend.Login;

/**
 *
 * @author Student Admin
 */
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import randomasterdeskapp.backend.DatabaseConnection;
import java.sql.SQLException;

public abstract class LoginService {
    public void login(String enteredEmail, String enteredPassword) {
        if (enteredEmail.isEmpty() && enteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Both email and password are required!");
            return;
        }

        if (enteredEmail.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Email is required!");
            return;
        }

        if (enteredPassword.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Password is required!");
            return;
        }

        // Check if email is in a valid format
        if (!enteredEmail.matches("\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b")) {
            JOptionPane.showMessageDialog(null, "Please enter a valid email address");
            return;
        }

        try {
            Connection connection = DatabaseConnection.getConnection();
            if (connection != null) {
                String query = "SELECT * FROM accounts WHERE email = ? AND password = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, enteredEmail);
                preparedStatement.setString(2, enteredPassword);

                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    // Redirect to admin home frame
                    redirectToAdminHome();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid email or password!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Unable to connect to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "An error occurred while processing your request");
        }
    }

    protected abstract void redirectToAdminHome();
}