/*
 * $Id: UpdateUserServlet.java,v 1.2 2005-10-29 00:59:41 gunter Exp $
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
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class UpdateUserServlet extends GenericServlet {
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
        
        if (user.getLevel() != 0) {
            redirectError(request, response, 
                    "You must login with admin rights first.");
            return;
        }
        
        int user_id = 
            Integer.parseInt(request.getParameter("user_id"));
        
        try {
            User userCandidate = Persistor.selectUser(connection, user_id);
            
            request.setAttribute("userCandidate", userCandidate);
            connection.close();
            RequestDispatcher dispatcher = request.getRequestDispatcher("update_user.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
        }
    }
    
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
        
        if (user.getLevel() != 0) {
            redirectError(request, response, 
                "You must login with admin rights first.");
            return;
        }
        
        int user_id = 
            Integer.parseInt(request.getParameter("user_id"));
        
        try {            
            User theUser = Persistor.selectUser(connection, user_id);
            theUser.setUsername(request.getParameter("username"));
            theUser.setLevel(Short.parseShort(request.getParameter("level")));
            UserManager.setPasswordForUser(theUser, request.getParameter("password"));

            Persistor.updateUser(connection, theUser);
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminUsers.do");
        dispatcher.forward(request, response);
    }
}
