/*
 * $Id$
 */
package net.six_two.program_guide;

import java.util.ArrayList;
import java.util.Date;

import net.six_two.program_guide.tables.UserEpisode;

public class CalendarDate {
    private Date date;
    private ArrayList userEpisodes;
    private int dayOfMonth;
    private boolean today;
    
    public CalendarDate(Date date, int dayOfMonth, boolean today) {
        this.date = date;
        this.userEpisodes = new ArrayList();
        this.dayOfMonth = dayOfMonth;
        this.today = today;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void addUserEpisode(UserEpisode userEpisode) {
        userEpisodes.add(userEpisode);
    }
    
    public ArrayList getUserEpisodes() {
        return userEpisodes;
    }
    
    public int getDayOfMonth() {
        return dayOfMonth;
    }
    
    public boolean getToday() {
        return today;
    }
}
