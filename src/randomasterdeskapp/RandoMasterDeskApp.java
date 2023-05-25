/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp;

/**
 *
 * @author Student Admin
 */

import javax.swing.*;
import randomasterdeskapp.pages.LoginPage;


public class RandoMasterDeskApp {



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage login = new LoginPage();
            login.setVisible(true);
        });
    }
    
}
    
