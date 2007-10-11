/*
 * $Id: ProgramSubscribed.java,v 1.1 2007-10-11 01:10:22 gunter Exp $
 */
package net.six_two.program_guide.model;

public class ProgramSubscribed {
    private Program program;
    private Boolean subscribed;
    
    public ProgramSubscribed() {
        
    }
    
    public ProgramSubscribed(Program program, Boolean subscribed) {
        this.program = program;
        this.subscribed = subscribed;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Boolean getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(Boolean subscribed) {
        this.subscribed = subscribed;
    }
}
