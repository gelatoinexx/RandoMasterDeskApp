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
public class Scholar {

    private int scholarId;
    private String firstName;
    private String lastName;

    public Scholar(int scholarId, String firstName, String lastName) {
        this.scholarId = scholarId;
        this.firstName = firstName;
        this.lastName = lastName;

    }

    public int getScholarId() {
        return scholarId;
    }

    public void setScholarId(int scholarId) {
        this.scholarId = scholarId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

}
