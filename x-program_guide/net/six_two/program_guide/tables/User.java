/*
 * $Id: User.java,v 1.3 2005-10-23 05:56:58 gunter Exp $
 */
package net.six_two.program_guide.tables;

import java.sql.Timestamp;

public class User {
    private int id;
    private String username;
    private String password;
    private Timestamp lastLoginDate;
    private Timestamp registrationDate;
    private short level;
    
    public User() {
        
    }
    
    public User(int id, String username, String password, 
            Timestamp lastLoginDate, Timestamp registrationDate, short level) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.lastLoginDate = lastLoginDate;
        this.registrationDate = registrationDate;
        this.level = level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public short getLevel() {
        return level;
    }

    public void setLevel(short level) {
        this.level = level;
    }
}
