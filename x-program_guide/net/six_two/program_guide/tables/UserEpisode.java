/*
 * $Id: UserEpisode.java,v 1.3 2005-11-04 03:55:37 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class UserEpisode {
    private Episode episode;
    private Program program;
    private String status;
    
    public UserEpisode() {
        
    }
    
    public UserEpisode(Program program, Episode episode, String status) {
        this.program = program;
        this.episode = episode;
        this.status = status;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
