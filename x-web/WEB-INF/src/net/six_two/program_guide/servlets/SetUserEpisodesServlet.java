/*
 * $Id: SetUserEpisodesServlet.java,v 1.1 2005-10-25 13:55:27 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.Episode;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.User;

public class SetUserEpisodesServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            ServletException {
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
                context.lookup("java:comp/env/jdbc/program_guide");
            Connection connection = source.getConnection();
            
            User user = Persistor.selectUser(connection, "gunter");
            UserManager.authenticateUser(user, "");
            
            int program_id = 
                Integer.parseInt(request.getParameter("program_id"));
            Program program = Persistor.selectProgram(connection, program_id);
            
            /*UserEpisode[] userEpisodes = Persistor.
                selectAllEpisodesForUser(connection, user, program);*/
            Persistor.deleteQueuedForUser(connection, user, program);
            
            String[] queued = request.getParameterValues("queued");
            if (queued != null) {
                for (int i = 0; i != queued.length; i++) {
                    String tokens[] = queued[i].split("_");
                    Episode episode = new Episode();
                    episode.setProgramId(program_id);
                    episode.setSeason(tokens[0].charAt(0));
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
                    episode.setSeason(tokens[0].charAt(0));
                    episode.setNumber(Integer.parseInt(tokens[1]));
                    
                    Persistor.insertViewedForUser(connection, user, episode);
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("GetUserEpisodes.do");
        dispatcher.forward(request, response);
    }
}