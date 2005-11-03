/*
 * $Id: UserEpisodeRelation.java,v 1.3 2005-11-03 01:44:05 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class UserEpisodeRelation {
    private int episodeNumber;
    private int programId;
    private String season;
    private int userId;
    
    public UserEpisodeRelation() {
        
    }
    
    public UserEpisodeRelation(User user, Episode episode) {
        this.episodeNumber = episode.getNumber();
        this.programId = episode.getProgramId();
        this.season = episode.getSeason();
        this.userId = user.getId();
    }
    
    public UserEpisodeRelation(int userId, int programId, String season,
            int episodeNumber) {
        this.episodeNumber = episodeNumber;
        this.programId = programId;
        this.season = season;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
