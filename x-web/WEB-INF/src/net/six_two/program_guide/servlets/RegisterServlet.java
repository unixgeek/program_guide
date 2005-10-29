/*
 * $Id: RegisterServlet.java,v 1.2 2005-10-29 00:59:41 gunter Exp $
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
            
            if (user != null) {
                error(request, response, "User exists.");
                return;
            }
            
            user = UserManager.createUser(username, password);
            Persistor.insertUser(connection, user);
            
            connection.close();
            
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            request.setAttribute("user", user);
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
