/*
 * $Id: ProgramSubscribed.java,v 1.1 2006-05-05 22:38:23 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class ProgramSubscribed {
    private Program program;
    private short subscribed;
    
    public ProgramSubscribed() {
        
    }
    
    public ProgramSubscribed(Program program, short subscribed) {
        this.program = program;
        this.subscribed = subscribed;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public short getSubscribed() {
        return subscribed;
    }

    public void setSubscribed(short subscribed) {
        this.subscribed = subscribed;
    }
}
