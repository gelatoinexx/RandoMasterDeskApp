/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package randomasterdeskapp.backend.Login;
import randomasterdeskapp.pages.AdminHome;


/**
 *
 * @author Student Admin
 */
public class LoginController extends LoginService {
    @Override
    protected void redirectToAdminHome() {
        // Redirect to admin home frame
        AdminHome adminHome = new AdminHome();
        adminHome.setVisible(true);
        // this.setVisible(false); // hide the login frame (commented out since "this" is not defined in the separate class)
    }
}
