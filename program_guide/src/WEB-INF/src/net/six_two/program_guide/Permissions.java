/*
 * $Id: Permissions.java,v 1.1 2006-05-05 22:38:22 gunter Exp $
 */
package net.six_two.program_guide;

import java.util.HashMap;

public class Permissions {
    public static final int USAGE = 1;
    public static final int ADD_PROGRAM = 2;
    public static final int DELETE_PROGRAM = 4;
    public static final int EDIT_PROGRAM = 8;
    public static final int ADD_USER = 16;
    public static final int DELETE_USER = 32;
    public static final int EDIT_USER = 64;
    public static final int ADMIN_LOG = 128;
    private HashMap map;
    
    public Permissions() {
        map = new HashMap(8);
        map.put("USAGE", new Integer(USAGE));
        map.put("ADD_PROGRAM", new Integer(ADD_PROGRAM));
        map.put("DELETE_PROGRAM", new Integer(DELETE_PROGRAM));
        map.put("EDIT_PROGRAM", new Integer(EDIT_PROGRAM));
        map.put("ADD_USER", new Integer(ADD_USER));
        map.put("DELETE_USER", new Integer(DELETE_USER));
        map.put("EDIT_USER", new Integer(EDIT_USER));
        map.put("ADMIN_LOG", new Integer(ADMIN_LOG));
    }
    
    public HashMap getPermissionsMap() {
        return map;
    }
}
