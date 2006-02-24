/*
 * $Id$
 */
package net.six_two.program_guide;

import java.util.Calendar;
import java.util.Date;

import net.six_two.misc.Time;
import net.six_two.program_guide.tables.UserEpisode;

public class UserEpisodeForSchedule extends net.six_two.program_guide.tables.UserEpisode {
    private boolean today;
    private boolean tomorrow;
    private boolean yesterday;
    
    public UserEpisodeForSchedule(UserEpisode userEpisode) {
        super(userEpisode.getProgram(), userEpisode.getEpisode(), 
                userEpisode.getStatus());
        
        Date airDate = getEpisode().getOriginalAirDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Time.datePart(new Date(System.currentTimeMillis())));
        
        today = isEqual(calendar.getTime(), airDate);
        
        // Add a day.
        calendar.add(Calendar.DATE, 1);
        tomorrow = isEqual(calendar.getTime(), airDate);
        
        // Subtract two days.
        calendar.add(Calendar.DATE, -2);
        yesterday = isEqual(calendar.getTime(), airDate);
    }
    
    private boolean isEqual(Date date1, Date date2) {
        int difference = date1.compareTo(date2);
        
        if (difference == 0)
            return true;
        else
            return false;
    }
    
    public boolean getToday() {
        return today;
    }
    
    public boolean isToday() {
        return today;
    }

    public boolean getTomorrow() {
        return tomorrow;
    }
    
    public boolean isTomorrow() {
        return tomorrow;
    }
    public boolean getYesterday() {
        return yesterday;
    }
    
    public boolean isYesterday() {
        return yesterday;
    }
}
