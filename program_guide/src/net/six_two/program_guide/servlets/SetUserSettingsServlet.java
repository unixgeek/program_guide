/*
 * $Id: SetUserSettingsServlet.java,v 1.1 2007-10-10 02:09:05 gunter Exp $
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

public class SetUserSettingsServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("settings.jsp");
        dispatcher.forward(request, response);
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
        
        String username = (String) request.getParameter("username");
        String password1 = (String) request.getParameter("password1");
        String password2 = (String) request.getParameter("password2");
        String action = (String) request.getParameter("action");
        
        username = (username != null) ? username : "";
        password1 = (password1 != null) ? password1 : "";
        password2 = (password2 != null) ? password2 : "";
        
        if (username.equals("") && action.equals("username")) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            error(request, response, "Invalid username.");
            return;
        }
        
        if (password1.equals("") && action.equals("password")) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            request.setAttribute("username", username);
            error(request, response, "Invalid password.");
            return;
        }
        
        if (!password1.equals(password2) && action.equals("password")) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            request.setAttribute("username", username);
            error(request, response, "Passwords don't match.");
            return;
        }
        
        if (action.equals("username"))
            user.setUsername(username);
        else if (action.equals("password"))
            UserManager.setPasswordForUser(user, password1);
        
        try {
            Persistor.updateUser(connection, user);
            connection.close();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            error(request, response, e.getMessage());
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("DisplayFrontPage.do");
        dispatcher.forward(request, response);
    }
    
    private void error(HttpServletRequest request, HttpServletResponse response,
            String message) throws IOException, ServletException {
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("settings.jsp");
        dispatcher.forward(request, response);
    }
}
