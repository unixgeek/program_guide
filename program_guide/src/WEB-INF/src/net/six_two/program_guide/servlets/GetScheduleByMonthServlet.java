/*
 * $Id: GetScheduleByMonthServlet.java,v 1.2 2006-07-16 18:07:29 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.commons.calendar.CalendarDay;
import net.six_two.commons.calendar.MonthlyCalendar;
import net.six_two.commons.datetime.DateTimeUtilities;
import net.six_two.program_guide.CalendarEntry;
import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.Timer;
import net.six_two.program_guide.tables.TorrentSite;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

public class GetScheduleByMonthServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {

        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        
        // Format: 20060304
        String targetDateString = request.getParameter("date");

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        
        Date targetDate = null;
        if (targetDateString != null) {
            try {
                targetDate = dateFormat.parse(targetDateString);
            } catch (ParseException e1) {
                targetDate = null;
            }
        }
        
        if (targetDate == null) {
            targetDate = new Date(System.currentTimeMillis());
        }

        MonthlyCalendar calendar = new MonthlyCalendar(targetDate);
        
        Timer timer = new Timer();
        timer.start();
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
        
        UserEpisode[] episodes;
        TorrentSite site;
        try {
            episodes = Persistor.selectAllEpisodesForUser(connection, user, 
                    new java.sql.Date(calendar.getFirstDay().getTime()),
                    new java.sql.Date(calendar.getLastDay().getTime()));
            
            site = Persistor.selectTorrentSite(connection);
            
            connection.close();
            timer.stop();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            timer.stop();
            redirectError(request, response, e.getMessage());
            return;
        }
        
        Date todaysDate = DateTimeUtilities.datePart(
                new Date(System.currentTimeMillis()));
        
        CalendarDay[][] schedule = calendar.getCalendarDays();
        
        // Loop through the days and build the schedule array.
        for (int week = 0; week != calendar.getNumberOfWeeks(); week++) {
            for (int day = 0; day != 7; day++) {
                if (schedule[week][day] != null) {
                    boolean today = 
                        DateTimeUtilities.isEqual(todaysDate, 
                                schedule[week][day].getDate());
                    
                    CalendarEntry calendarDate = new CalendarEntry(today);
                    
                    // Add any episodes for this date.
                    for (int i = 0; i != episodes.length; i++) {
                        if (DateTimeUtilities.isEqual(
                                schedule[week][day].getDate(), 
                                episodes[i].getEpisode().getOriginalAirDate())) {
                            calendarDate.addUserEpisode(episodes[i]);
                        }
                        
                    }
                    schedule[week][day].setUserObject(calendarDate);
                }
            }
        }
        
        // Calculate next date and previous date string.
        String nextDateString = dateFormat.format(calendar.getNextMonth());
        String previousDateString = 
            dateFormat.format(calendar.getPreviousMonth());
        
        request.setAttribute("elapsedTime", timer.getElapsedTime());
        request.setAttribute("year", calendar.getYearName());
        request.setAttribute("month", calendar.getMonthName());
        request.setAttribute("nextDate", nextDateString);
        request.setAttribute("previousDate", previousDateString);
        request.setAttribute("schedule", schedule);
        request.setAttribute("site", site);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("schedule_month.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
