/*
 * $Id: LoginServlet.java,v 1.8.6.1 2006-05-05 03:44:39 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.six_two.program_guide.Permissions;
import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class LoginServlet extends GenericServlet {
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
   
        String username = (String) request.getParameter("username");
        String password = (String) request.getParameter("password");
        
        username = (username != null) ? username : "";
        password = (password != null) ? password : "";
        
        if (username.equals("")) {
            error(request, response, "Invalid username.");
            return;
        }
        
        if (password.equals("")) {
            error(request, response, "Invalid password.");
            return;
        }
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
           return;
        }
        
        try {           
            User user = Persistor.selectUser(connection, username);
            
            if (user == null) {
                connection.close();
                error(request, response, "User doesn't exist.");
                return;
            }
            
            if (!UserManager.authenticateUser(user, password)) {
                connection.close();
                request.setAttribute("username", username);
                error(request, response, "Incorrect password.");
                return;
            }
            
            if (!UserManager.authorizeUser(user, Permissions.USAGE)) {
                connection.close();
                request.setAttribute("username", username);
                error(request, response, 
                        "You're account is disabled.  Loser.");
                return;
            }
            
            user.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
            Persistor.updateUser(connection, user);
            
            connection.close();
            
            HttpSession session = request.getSession();
            session.setMaxInactiveInterval(-1);
            session.setAttribute("user", user);
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, e.getMessage());
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("DisplayFrontPage.do");
        dispatcher.forward(request, response);
    }
    
    private void error(HttpServletRequest request, HttpServletResponse response,
            String message) throws IOException, ServletException {
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request, response);
    }
}
