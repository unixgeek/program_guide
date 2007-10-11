/*
 * $Id: Status.java,v 1.1 2007-10-11 01:10:22 gunter Exp $
 */
package net.six_two.program_guide.model;

public class Status {
    private Integer episodeNumber;
    private Integer programId;
    private String season;
    private String status;
    private Integer userId;
    
    public Status() {
        
    }
    
    public Status(User user, Episode episode, String status) {
        this.episodeNumber = episode.getNumber();
        this.programId = episode.getProgramId();
        this.season = episode.getSeason();
        this.status = status;
        this.userId = user.getId();
    }
    
    public Status(Integer userId, Integer programId, String status,
            String season, Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
        this.programId = programId;
        this.season = season;
        this.status = status;
        this.userId = userId;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
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

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
