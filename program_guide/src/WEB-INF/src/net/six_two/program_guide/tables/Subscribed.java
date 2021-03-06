/*
 * $Id: Subscribed.java,v 1.1 2006-05-05 22:38:23 gunter Exp $
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
