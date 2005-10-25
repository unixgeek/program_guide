/*
 * $Id: LoginServlet.java,v 1.1 2005-10-25 22:09:45 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
                context.lookup("java:comp/env/jdbc/program_guide");
            Connection connection = source.getConnection();
            
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
            
            User user = Persistor.selectUser(connection, username);
            
            if (user == null) {
                error(request, response, "User doesn't exist.");
                return;
            }
            
            if (!UserManager.authenticateUser(user, password)) {
                request.setAttribute("username", username);
                error(request, response, "Incorrect password.");
                return;
            }
            
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            request.setAttribute("user", user);
            RequestDispatcher dispatcher = request.getRequestDispatcher("recent.jsp");
            dispatcher.forward(request, response);
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
        
    private void error(HttpServletRequest request, HttpServletResponse response,
            String message) throws IOException, ServletException {
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
        dispatcher.forward(request, response);
    }
}
