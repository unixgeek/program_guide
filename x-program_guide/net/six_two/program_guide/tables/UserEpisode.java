/*
 * $Id: UserEpisode.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class UserEpisode {
    private Episode episode;
    private Program program;
    private short queued;
    private short viewed;
    
    public UserEpisode() {
        
    }
    
    public UserEpisode(Program program, Episode episode, short queued, 
            short viewed) {
        this.program = program;
        this.episode = episode;
        this.queued = queued;
        this.viewed = viewed;
    }

    public Episode getEpisode() {
        return episode;
    }

    public void setEpisode(Episode episode) {
        this.episode = episode;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public short getQueued() {
        return queued;
    }

    public void setQueued(short queued) {
        this.queued = queued;
    }

    public short getViewed() {
        return viewed;
    }

    public void setViewed(short viewed) {
        this.viewed = viewed;
    }
}
