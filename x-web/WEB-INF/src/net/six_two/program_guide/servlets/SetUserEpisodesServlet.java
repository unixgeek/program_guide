/*
 * $Id: SetUserEpisodesServlet.java,v 1.4 2005-11-03 03:29:05 gunter Exp $
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
            
            Persistor.deleteQueuedForUser(connection, user, program);
            
            String[] queued = request.getParameterValues("queued");
            if (queued != null) {
                for (int i = 0; i != queued.length; i++) {
                    String tokens[] = queued[i].split("_");
                    Episode episode = new Episode();
                    episode.setProgramId(program_id);
                    episode.setSeason(tokens[0]);
                    episode.setNumber(Integer.parseInt(tokens[1]));
                    
                    Persistor.insertQueuedForUser(connection, user, episode);
                }
            }
            
            Persistor.deleteViewedForUser(connection, user, program);
            
            String[] viewed = request.getParameterValues("viewed");
            if (viewed != null) {
                for (int i = 0; i != viewed.length; i++) {
                    String tokens[] = viewed[i].split("_");
                    Episode episode = new Episode();
                    episode.setProgramId(program_id);
                    episode.setSeason(tokens[0]);
                    episode.setNumber(Integer.parseInt(tokens[1]));
                    
                    Persistor.insertViewedForUser(connection, user, episode);
                }
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("GetUserEpisodes.do");
        dispatcher.forward(request, response);
    }
}
