/*
 * $Id: User.java,v 1.1 2007-10-11 01:10:22 gunter Exp $
 */
package net.six_two.program_guide.model;

import java.sql.Timestamp;

public class User {
    private Integer id;
    private String username;
    private String password;
    private Timestamp lastLoginDate;
    private Timestamp registrationDate;
    private Integer permissions;
    
    public User() {
        
    }
    
    public User(Integer id, String username, String password, 
            Timestamp lastLoginDate, Timestamp registrationDate, 
            Integer permissions) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastLoginDate = lastLoginDate;
        this.registrationDate = registrationDate;
        this.permissions = permissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Timestamp lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Timestamp registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getPermissions() {
        return permissions;
    }

    public void setPermissions(Integer permissions) {
        this.permissions = permissions;
    }
}
