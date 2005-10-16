/*
 * $Id: Queued.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class Queued extends UserEpisodeRelation {
    public Queued() {
        super();
    }
    
    public Queued(int userId, int programId, char season,
            int episodeNumber) {
        super(userId, programId, season, episodeNumber);
    }
}
