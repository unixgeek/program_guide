/*
 * $Id: UserManager.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
 */
package net.six_two.program_guide;

import java.util.Date;

import net.six_two.misc.MD5MessageDigest;
import net.six_two.program_guide.tables.User;

public class UserManager {
    public static User createUser(String username, String password) {
        User user = new User();
        Date date = new Date(System.currentTimeMillis());
        
        user.setUsername(username);
        user.setRegistrationDate(date);
        user.setLastLoginDate(date);
        setPasswordForUser(user, password);
        
        return user;
    }
    
    public static void setPasswordForUser(User user, String password) {
        MD5MessageDigest md5 = new MD5MessageDigest();
        user.setPassword(md5.getDigestAsHex(password));
    }
    
    public static boolean authenticateUser(User user, String password) {
        MD5MessageDigest md5 = new MD5MessageDigest();
        
        if (md5.getDigestAsHex(password).equals(user.getPassword()))
            return true;
        else
            return false;
    }
}
