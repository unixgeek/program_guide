/*
 * $Id: Queued.java,v 1.2 2005-10-16 21:14:22 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class Queued extends UserEpisodeRelation {
    public Queued() {
        super();
    }
    
    public Queued(User user, Episode episode) {
        super(user, episode);
    }

    public Queued(int userId, int programId, char season,
            int episodeNumber) {
        super(userId, programId, season, episodeNumber);
    }
}
