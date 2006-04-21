/*
 * $Id$
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

import net.six_two.misc.CalendarDay;
import net.six_two.program_guide.CalendarDate;
import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.Timer;
import net.six_two.program_guide.tables.TorrentSite;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

public class GetScheduleByDayServlet extends GenericServlet {
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

        CalendarDay calendar = new CalendarDay(targetDate);
        
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
                    new java.sql.Date(calendar.getDate().getTime()),
                    new java.sql.Date(calendar.getDate().getTime()));
            
            site = Persistor.selectTorrentSite(connection);
            
            connection.close();
            timer.stop();
        } catch (SQLException e) {
            timer.stop();
            redirectError(request, response, e.getMessage());
            return;
        }
                
        CalendarDate calendarDate = new CalendarDate(true);
                
        // Add any episodes for this date.
        for (int i = 0; i != episodes.length; i++) {
            calendarDate.addUserEpisode(episodes[i]);
        }
        
        calendar.add(calendarDate);
        
        // Calculate next date and previous date string.
        String nextDateString = dateFormat.format(calendar.getNextDay());
        String previousDateString = 
            dateFormat.format(calendar.getPreviousDay());
        
        request.setAttribute("elapsedTime", timer.getElapsedTime());
        request.setAttribute("date", calendar.getDate());
        request.setAttribute("nextDate", nextDateString);
        request.setAttribute("previousDate", previousDateString);
        request.setAttribute("schedule", calendar);
        request.setAttribute("site", site);
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("schedule_day.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
