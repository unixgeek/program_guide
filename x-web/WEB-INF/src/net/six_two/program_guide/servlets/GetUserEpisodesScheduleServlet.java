/*
 * $Id: GetUserEpisodesScheduleServlet.java,v 1.8 2006-02-24 23:46:02 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.misc.Time;
import net.six_two.program_guide.Persistor;
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
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
                
        try {
            UserEpisode[] episodes = Persistor.
                selectAllEpisodesForUser(connection, user, -6, 6);
            
            TorrentSite site = Persistor.selectTorrentSite(connection);
            
            connection.close();
           
            UserEpisodeForSchedule[] episodesSchedule = 
                new UserEpisodeForSchedule[episodes.length];
            
            for (int i = 0; i != episodes.length; i++) {
                episodes[i].getEpisode().setTitle(
                    filterContent(episodes[i].getEpisode().getTitle()));
                episodesSchedule[i] = new UserEpisodeForSchedule(episodes[i]);
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(Time.datePart(
                    new Date(System.currentTimeMillis())));
            calendar.add(Calendar.DATE, -6);
            Date fromDate = calendar.getTime();
            calendar.add(Calendar.DATE, 12);
            Date toDate = calendar.getTime();
            
            request.setAttribute("fromDate", fromDate);
            request.setAttribute("toDate", toDate);
            request.setAttribute("episodesList", episodesSchedule);
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
}
