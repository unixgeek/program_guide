/*
 * $Id: GetUserProgramsServlet.java,v 1.2 2006-05-15 04:07:52 gunter Exp $
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

public class GetUserProgramsServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {

        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        
        String prefix = request.getParameter("prefix");
        
        Timer timer = new Timer();
        timer.start();
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
            
        try {
            Program[] programs = null;
            
            String[] prefixes = 
                Persistor.selectProgramPrefixesForUser(connection, user);
            
            if (prefixes.length > 0) {
                if ((prefix == null) || (prefix.trim().length() < 1))
                    prefix = prefixes[0];
                programs = Persistor.selectAllProgramsForUser(connection, 
                        user, prefix);
            }
            
            TorrentSite site = Persistor.selectTorrentSite(connection);
            
            connection.close();
            
            timer.stop();
            
            request.setAttribute("prefixes", prefixes);
            request.setAttribute("currentPrefix", prefix);
            request.setAttribute("elapsedTime", timer.getElapsedTime());
            request.setAttribute("programsList", programs);
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
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("programs.jsp");
        dispatcher.forward(request, response); 
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
