/*
 * $Id: GetUserEpisodesServlet.java,v 1.3 2006-05-15 04:09:37 gunter Exp $
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
import net.six_two.program_guide.Timer;
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
        
        String season = request.getParameter("season");
        
        Timer timer = new Timer();
        timer.start();
        
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
            String[] seasons = 
                Persistor.selectSeasonsForProgram(connection, program);
            
            UserEpisode[] userEpisodes = null;
            if (seasons.length > 0) {
                if ((season == null) || (season.trim().length() < 1))
                    season = seasons[0];
                    userEpisodes = Persistor.selectEpisodesBySeasonForUser(
                        connection, user, program, season);
            }
        
            TorrentSite site = Persistor.selectTorrentSite(connection);
            
            connection.close();
            
            timer.stop();
            
            if (userEpisodes != null)
                for (int i = 0; i != userEpisodes.length; i++)
                    userEpisodes[i].getEpisode().setTitle(
                        filterContent(
                                userEpisodes[i].getEpisode().getTitle()));

            request.setAttribute("elapsedTime", timer.getElapsedTime());
            request.setAttribute("userEpisodesList", userEpisodes);
            request.setAttribute("currentSeason", season);
            request.setAttribute("seasons", seasons);
            request.setAttribute("program", program);
            request.setAttribute("site", site);
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
