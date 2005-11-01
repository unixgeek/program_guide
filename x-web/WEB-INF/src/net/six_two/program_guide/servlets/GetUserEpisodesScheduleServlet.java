/*
 * $Id: GetUserEpisodesScheduleServlet.java,v 1.3 2005-11-01 23:34:44 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
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
            UserEpisode[] previousEpisodes = Persistor.
                selectAllEpisodesForUser(connection, user, -6);
            
            UserEpisode[] nextEpisodes = Persistor.
                selectAllEpisodesForUser(connection, user, 6);
            
            connection.close();
           
            for (int i = 0; i != previousEpisodes.length; i++)
                previousEpisodes[i].getEpisode().setTitle(
                    filterContent(
                            previousEpisodes[i].getEpisode().getTitle()));

            for (int i = 0; i != previousEpisodes.length; i++)
                nextEpisodes[i].getEpisode().setTitle(
                    filterContent(nextEpisodes[i].getEpisode().getTitle()));

            request.setAttribute("nextEpisodesList", nextEpisodes);
            request.setAttribute("previousEpisodesList", previousEpisodes);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
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
