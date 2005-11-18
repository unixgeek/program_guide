/*
 * $Id: SetUserEpisodesServlet.java,v 1.6 2005-11-18 03:14:44 gunter Exp $
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
import net.six_two.program_guide.tables.Episode;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.User;

public class SetUserEpisodesServlet extends GenericServlet {
    protected void doPost(HttpServletRequest request,
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
            
            Persistor.deleteStatusForUser(connection, user, program);
            
            String[] episodeStatus = request.getParameterValues("status");
            if (episodeStatus != null) {
                for (int i = 0; i != episodeStatus.length; i++) {
                    String tokens[] = episodeStatus[i].split("_");
                    Episode episode = new Episode();
                    episode.setProgramId(program_id);
                    episode.setSeason(tokens[0]);
                    episode.setNumber(Integer.parseInt(tokens[1]));
                    
                    if (tokens[2].equals("queued") || tokens[2].equals("viewed"))
                        Persistor.insertStatusForUser(connection, user, 
                                episode, tokens[2]);
                }
            }
            
            connection.close();
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("GetUserEpisodes.do");
        dispatcher.forward(request, response);
    }
}
