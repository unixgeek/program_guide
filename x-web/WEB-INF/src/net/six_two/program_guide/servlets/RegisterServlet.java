/*
 * $Id: RegisterServlet.java,v 1.5 2005-11-02 04:16:29 gunter Exp $
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
        String action = (String) request.getParameter("action");
        
        username = (username != null) ? username : "";
        password1 = (password1 != null) ? password1 : "";
        password2 = (password2 != null) ? password2 : "";
        action = (action != null) ? action : "";
        
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
            if (!action.equals("nologin"))
                request.getSession().setAttribute("user", user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!action.equals("nologin")) {
            RequestDispatcher dispatcher = request.getRequestDispatcher("DisplayFrontPage.do");
            dispatcher.forward(request, response);
        }
        else {
            RequestDispatcher dispatcher = request.getRequestDispatcher("AdminUsers.do");
            dispatcher.forward(request, response);
        }
    }
    
    private void error(HttpServletRequest request, HttpServletResponse response,
            String message) throws IOException, ServletException {
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("register.jsp");
        dispatcher.forward(request, response);
    }
}
