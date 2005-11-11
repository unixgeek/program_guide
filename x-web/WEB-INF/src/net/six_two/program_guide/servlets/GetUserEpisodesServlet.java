/*
 * $Id: GetUserEpisodesServlet.java,v 1.11 2005-11-11 16:37:45 gunter Exp $
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
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.TorrentSite;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

public class GetUserEpisodesServlet extends GenericServlet {
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
            
        int program_id = 
            Integer.parseInt(request.getParameter("program_id"));
        
        try {
            Program program = Persistor.selectProgram(connection, program_id);
            UserEpisode[] userEpisodes = Persistor.
                selectAllEpisodesForUser(connection, user, program);
            
            TorrentSite site = Persistor.selectTorrentSite(connection);
            
            connection.close();
            
            for (int i = 0; i != userEpisodes.length; i++)
                userEpisodes[i].getEpisode().setTitle(
                        filterContent(
                                userEpisodes[i].getEpisode().getTitle()));

            request.setAttribute("userEpisodesList", userEpisodes);
            request.setAttribute("program", program);
            request.setAttribute("site", site);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
            return;
        }

        RequestDispatcher dispatcher = 
            request.getRequestDispatcher("episodes.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
