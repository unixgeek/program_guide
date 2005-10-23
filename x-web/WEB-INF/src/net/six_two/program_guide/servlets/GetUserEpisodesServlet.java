/*
 * $Id: GetUserEpisodesServlet.java,v 1.2 2005-10-23 23:31:55 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
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
import net.six_two.program_guide.tables.UserEpisode;

public class GetUserEpisodesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, 
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
            UserEpisode[] userEpisodes = Persistor.
                selectAllEpisodesForUser(connection, user, program);
            
            connection.close();
            
            request.setAttribute("userEpisodesList", userEpisodes);
            request.setAttribute("program", program);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
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
            
            UserEpisode[] userEpisodes = Persistor.
                selectAllEpisodesForUser(connection, user, program);
            
            Enumeration parameters = request.getParameterNames();
            while (parameters.hasMoreElements()) {
                String parameterName = (String) parameters.nextElement();
                log(parameterName);
                
                String tokens[] = parameterName.split("_");
                if (tokens[0].equals("queued")) {
                    Episode episode = new Episode();
                    episode.setProgramId(program_id);
                    episode.setSeason(tokens[1].charAt(0));
                    episode.setNumber(Integer.parseInt(tokens[2]));
                    
                    Persistor.insertQueuedForUser(connection, user, episode);
                }
                else if (tokens[0].equals("viewed")) {
                    
                }
            }
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        doGet(request, response);
    }
}
