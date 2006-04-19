/*
 * $Id: GetUserEpisodesScheduleServlet.java,v 1.10 2006-04-19 05:36:24 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.misc.Time;
import net.six_two.program_guide.CalendarDate;
import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.Timer;
import net.six_two.program_guide.UserEpisodeForSchedule;
import net.six_two.program_guide.tables.TorrentSite;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

public class GetUserEpisodesScheduleServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {

        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        
        // 20060304
        /*String month = request.getParameter("month");
        if ((month != null) && (!month.trim().equals("")) {
            
        }*/
        
        GregorianCalendar target = new GregorianCalendar();
        target.set(GregorianCalendar.DAY_OF_MONTH, 1);
        Date startDate = Time.datePart(target.getTime());
        int lastDay = target.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
        target.set(GregorianCalendar.DAY_OF_MONTH, lastDay);
        Date endDate = Time.datePart(target.getTime());
        
        Timer timer = new Timer();
        timer.start();
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
                
        try {
            UserEpisode[] episodes = Persistor.
                selectAllEpisodesForUser(connection, user, 
                        new java.sql.Date(startDate.getTime()),
                        new java.sql.Date(endDate.getTime()));
            
            TorrentSite site = Persistor.selectTorrentSite(connection);
            
            connection.close();
            timer.stop();

            GregorianCalendar calendar = new GregorianCalendar();
            
            // Set the calendar to the first day of the month;
            calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
            
            // Get the month name.
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
            String month = dateFormat.format(calendar.getTime());
            
            // Create the calendar for this month.
            int numberOfWeeks = 
                calendar.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
            int numberOfDays = 
                calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            
            CalendarDate[][] schedule = new CalendarDate[numberOfWeeks][7];
            
            // Loop through the days and build the schedule array;
            for (int i = 0; i != numberOfDays; i++) {
                int weekOfMonth = 
                    calendar.get(GregorianCalendar.WEEK_OF_MONTH);
                int dayOfWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK);
                int dayOfMonth = calendar.get(GregorianCalendar.DAY_OF_MONTH);
                
                CalendarDate calendarDate = 
                    new CalendarDate(Time.datePart(calendar.getTime()), 
                            dayOfMonth);
                for (int j = 0; j != episodes.length; j++) {
                    if (isEqual(calendarDate.getDate(), 
                            episodes[j].getEpisode().getOriginalAirDate())) {
                        calendarDate.addUserEpisode(episodes[j]);
                    }
                    
                }
                schedule[weekOfMonth - 1][dayOfWeek - 1] = calendarDate;
                // Increment to the next day of the month.
                calendar.add(GregorianCalendar.DAY_OF_MONTH, 1);
            }
            
            request.setAttribute("elapsedTime", timer.getElapsedTime());
            request.setAttribute("month", month);
            request.setAttribute("schedule", schedule);
            request.setAttribute("site", site);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("schedule.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
    
    private boolean isEqual(Date date1, Date date2) {
        int difference = date1.compareTo(date2);
        
        if (difference == 0)
            return true;
        else
            return false;
    }
}
