/*
 * $Id: Subscribed.java,v 1.1 2007-10-11 01:10:22 gunter Exp $
 */
package net.six_two.program_guide.model;

public class Subscribed {
    private Integer programId;
    private Integer userId;

    public Subscribed() {

    }

    public Subscribed(Integer userId, Integer programId) {
        this.programId = programId;
        this.userId = userId;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
