/*
 * $Id: GetUserEpisodesScheduleServlet.java,v 1.11 2006-04-20 04:13:28 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        
        String level = request.getParameter("level");
        if ((level == null) || (level.trim().length() < 1)) {
            level = "month";
        }
        
        // Format: 20060304
        String targetDateString = request.getParameter("date");

        SimpleDateFormat targetDateFormatter = 
            new SimpleDateFormat("yyyyMMdd");
        
        Date targetDate = null;
        if (targetDateString != null) {
            try {
                targetDate = targetDateFormatter.parse(targetDateString);
            } catch (ParseException e1) {
                targetDate = null;
            }
        }
        
        if (targetDate == null) {
            targetDate = new Date(System.currentTimeMillis());
        }

        
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(targetDate);
        
        // Seek to the first day of the month.
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
        // Get the date for the first day of the month.
        Date startDate = Time.datePart(calendar.getTime());
        // Seek to the last day of the month.
        calendar.set(GregorianCalendar.DAY_OF_MONTH, 
                calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
        // Get the date for the last day of the month.
        Date endDate = Time.datePart(calendar.getTime());
        
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
            
            Date todaysDate = 
                Time.datePart(new Date(System.currentTimeMillis()));
            
            // Get the month name.
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM");
            String month = dateFormat.format(calendar.getTime());
            dateFormat = new SimpleDateFormat("yyyy");
            String year = dateFormat.format(calendar.getTime());
            int numberOfWeeks = 
                calendar.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
            
            CalendarDate[][] schedule = new CalendarDate[numberOfWeeks][7];
            
            // Set the calendar to the first day of the month;
            calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
            
            // Loop through the days and build the schedule array;
            int numberOfDays = 
                calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);
            for (int i = 0; i != numberOfDays; i++) {
                int weekOfMonth = 
                    calendar.get(GregorianCalendar.WEEK_OF_MONTH);
                int dayOfWeek = calendar.get(GregorianCalendar.DAY_OF_WEEK);
                int dayOfMonth = calendar.get(GregorianCalendar.DAY_OF_MONTH);
                Date thisDate = Time.datePart(calendar.getTime());
                boolean today = isEqual(todaysDate, thisDate);
                
                CalendarDate calendarDate = 
                    new CalendarDate(thisDate, dayOfMonth, today);
                
                // Add any episodes for this date.
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
            
            // Calculate next date and previous date string.
            calendar.set(GregorianCalendar.DAY_OF_MONTH, 1);
            String nextDateString = 
                targetDateFormatter.format(calendar.getTime());
            calendar.add(GregorianCalendar.MONTH, -2);
            /*calendar.set(GregorianCalendar.DAY_OF_MONTH, 
                    calendar.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));*/
            String previousDateString =
                targetDateFormatter.format(calendar.getTime());
            
            request.setAttribute("elapsedTime", timer.getElapsedTime());
            request.setAttribute("year", year);
            request.setAttribute("month", month);
            request.setAttribute("nextDate", nextDateString);
            request.setAttribute("previousDate", previousDateString);
            request.setAttribute("level", level);
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
