/*
 * $Id: GetUserEpisodesServlet.java,v 1.9 2005-11-01 20:40:36 gunter Exp $
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
        
        Program program = null;
        UserEpisode[] userEpisodes = null;
        try {
            program = Persistor.selectProgram(connection, program_id);
            userEpisodes = Persistor.
                selectAllEpisodesForUser(connection, user, program);
            
            connection.close();
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
            return;
        }
       
        for (int i = 0; i != userEpisodes.length; i++)
            userEpisodes[i].setTitle(filterContent(userEpisodes[i].getTitle()));

        request.setAttribute("userEpisodesList", userEpisodes);
        request.setAttribute("program", program);
        RequestDispatcher dispatcher = request.getRequestDispatcher("episodes.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
