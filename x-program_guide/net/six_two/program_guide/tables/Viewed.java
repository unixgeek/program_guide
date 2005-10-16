/*
 * $Id: Viewed.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class Viewed extends UserEpisodeRelation {
    public Viewed() {
        super();
    }
    
    public Viewed(int userId, int programId, char season, 
            int episodeNumber) {
        super(userId, programId, season, episodeNumber);
    }
}
