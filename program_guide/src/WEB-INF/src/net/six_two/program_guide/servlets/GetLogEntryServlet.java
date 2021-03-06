/*
 * $Id: GetLogEntryServlet.java,v 1.1 2006-05-05 22:38:23 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Permissions;
import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.Timer;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.Log;
import net.six_two.program_guide.tables.User;

public class GetLogEntryServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {

        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        
        if (!UserManager.authorizeUser(user, Permissions.ADMIN_LOG)) {
            redirectError(request, response, 
                "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        Timer timer = new Timer();
        timer.start();
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
         
        int log_id = Integer.parseInt(request.getParameter("log_id"));
        
        try {
            Log log = Persistor.selectLogEntry(connection, log_id); 
            
            connection.close();;
            
            timer.stop();
            
            request.setAttribute("elapsedTime", timer.getElapsedTime());
            request.setAttribute("log",log);
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
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("log_entry.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
