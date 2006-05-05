/*
 * $Id: DeleteLogServlet.java,v 1.1.6.1 2006-05-05 03:44:39 gunter Exp $
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
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class DeleteLogServlet extends GenericServlet {
    protected void doPost(HttpServletRequest request, 
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
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
                
        try {
            Persistor.deleteLog(connection); 
            
            connection.close();;
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, e.getMessage());
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("GetLog.do");
        dispatcher.forward(request, response);
    }
}
