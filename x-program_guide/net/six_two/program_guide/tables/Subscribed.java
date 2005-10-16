/*
 * $Id: Subscribed.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class Subscribed {
    private int programId;
    private int userId;
    
    public Subscribed() {
        
    }
    
    public Subscribed(int userId, int programId) {
        this.programId = programId;
        this.userId = userId;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
