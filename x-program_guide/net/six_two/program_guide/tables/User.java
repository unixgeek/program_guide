/*
 * $Id: User.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
 */
package net.six_two.program_guide.tables;

import java.util.Date;

public class User {
    private int id;
    private String username;
    private String password;
    private Date lastLoginDate;
    private Date registrationDate;
    
    public User() {
        
    }
    
    public User(int id, String username, String password, 
            Date lastLoginDate, Date registrationDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastLoginDate = lastLoginDate;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getLastLoginDate() {
        return lastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
