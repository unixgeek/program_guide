/*
 * $Id: GetUserProgramsServlet.java,v 1.6 2005-11-11 16:37:45 gunter Exp $
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

public class GetUserProgramsServlet extends GenericServlet {
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
            Program[] programs = Persistor.
            selectAllProgramsForUser(connection, user);
            
            TorrentSite site = Persistor.selectTorrentSite(connection);
            
            connection.close();
            
            request.setAttribute("programsList", programs);
            request.setAttribute("site", site);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
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
