/*
 * $Id: RegisterServlet.java,v 1.4 2005-11-01 23:49:49 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class RegisterServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
   
        String username = (String) request.getParameter("username");
        String password1 = (String) request.getParameter("password1");
        String password2 = (String) request.getParameter("password2");
        
        username = (username != null) ? username : "";
        password1 = (password1 != null) ? password1 : "";
        password2 = (password2 != null) ? password2 : "";
        
        if (username.equals("")) {
            error(request, response, "Invalid username.");
            return;
        }
        
        if (password1.equals("")) {
            request.setAttribute("username", username);
            error(request, response, "Invalid password.");
            return;
        }
        
        if (!password1.equals(password2)) {
            request.setAttribute("username", username);
            error(request, response, "Passwords don't match.");
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
            
            if (user != null) {
                error(request, response, "User exists.");
                return;
            }
            
            user = UserManager.createUser(username, password1);
            Persistor.insertUser(connection, user);
            
            connection.close();
            
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("DisplayFrontPage.do");
        dispatcher.forward(request, response);
    }
    
    private void error(HttpServletRequest request, HttpServletResponse response,
            String message) throws IOException, ServletException {
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }
}
