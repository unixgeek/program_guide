/*
 * $Id$
 */
package net.six_two.program_guide;

import java.util.ArrayList;

import net.six_two.program_guide.tables.UserEpisode;

public class CalendarDate {
    private ArrayList userEpisodes;
    private boolean today;
    
    public CalendarDate(boolean today) {
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
