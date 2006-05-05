/*
 * $Id: CalendarEntry.java,v 1.1 2006-05-05 22:38:22 gunter Exp $
 */
package net.six_two.program_guide;

import java.util.ArrayList;

import net.six_two.program_guide.tables.UserEpisode;

public class CalendarEntry {
    private ArrayList userEpisodes;
    private boolean today;
    
    public CalendarEntry(boolean today) {
        this.userEpisodes = new ArrayList();
        this.today = today;
    }
    
    public void addUserEpisode(UserEpisode userEpisode) {
        userEpisodes.add(userEpisode);
    }
    
    public ArrayList getUserEpisodes() {
        return userEpisodes;
    }
    
    public boolean getToday() {
        return today;
    }
}
