/*
 * $Id: Status.java,v 1.1 2005-11-04 03:54:39 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class Status {
    private int episodeNumber;
    private int programId;
    private String season;
    private String status;
    private int userId;
    
    public Status() {
        
    }
    
    public Status(User user, Episode episode, String status) {
        this.episodeNumber = episode.getNumber();
        this.programId = episode.getProgramId();
        this.season = episode.getSeason();
        this.status = status;
        this.userId = user.getId();
    }
    
    public Status(int userId, int programId, String status,
            String season, int episodeNumber) {
        this.episodeNumber = episodeNumber;
        this.programId = programId;
        this.season = season;
        this.status = status;
        this.userId = userId;
    }

    public int getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(int episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public int getProgramId() {
        return programId;
    }

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
