/*
 * $Id$
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
import net.six_two.program_guide.tables.TorrentSite;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

public class SearchEpisodesServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
   
        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        
        String query = (String) request.getParameter("query");
        query = (query != null) ? query : "";
        if (query.equals("")) {
            error(request, response, "Invalid query.");
            return;
        }
        
        String type = (String) request.getParameter("type");
        type = (type != null) ? type : "";
        if (type.equals("")) {
            error(request, response, "Invalid type.");
            return;
        }
        
        int searchType = Persistor.NATURAL_LANGUAGE_SEARCH;
        if (type.equals("natural"))
            searchType = Persistor.NATURAL_LANGUAGE_SEARCH;
        else if (type.equals("boolean"))
            searchType = Persistor.BOOLEAN_SEARCH;
        else if (type.equals("expansion"))
            searchType = Persistor.QUERY_EXPANSION_SEARCH;
        
        Timer timer = new Timer();
        timer.start();
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
        
        try {
            TorrentSite site = Persistor.selectTorrentSite(connection);
            
            UserEpisode[] userEpisodesList = 
                Persistor.searchEpisodes(connection, query, searchType, user);
            connection.close();
            timer.stop();
            
            request.setAttribute("query", query);
            request.setAttribute("type", type);
            request.setAttribute("elapsedTime", timer.getElapsedTime());
            request.setAttribute("site", site);
            request.setAttribute("userEpisodesList", userEpisodesList);
            request.setAttribute("count", 
                    new Integer(userEpisodesList.length));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
        dispatcher.forward(request, response);
    }
    
    private void error(HttpServletRequest request, HttpServletResponse response,
            String message) throws IOException, ServletException {
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("search.jsp");
        dispatcher.forward(request, response);
    }
}
