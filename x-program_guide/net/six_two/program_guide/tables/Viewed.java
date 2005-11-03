/*
 * $Id: Viewed.java,v 1.3 2005-11-03 01:44:05 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class Viewed extends UserEpisodeRelation {
    public Viewed() {
        super();
    }
    
    public Viewed(User user, Episode episode) {
        super(user, episode);
    }

    public Viewed(int userId, int programId, String season, 
            int episodeNumber) {
        super(userId, programId, season, episodeNumber);
    }
}
