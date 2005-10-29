/*
 * $Id: GenericServlet.java,v 1.1 2005-10-29 00:59:41 gunter Exp $
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

import net.six_two.program_guide.tables.User;

public class GenericServlet extends HttpServlet {
    /*
     * Returns the User object from the session, or null if it
     * doesn't exist.
     */
    protected User getUserFromRequest(HttpServletRequest request) {
        // Attempt to get a session, but don't create one.
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        
        return (User) session.getAttribute("user");
    }
    
    /*
     * Returns a connection from the pool.
     */
    protected Connection getConnection() {
        Connection connection = null;
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
                context.lookup("java:comp/env/jdbc/program_guide");
            connection = source.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    /*
     * Handle the session-less.
     */
    protected void redirectLogin(HttpServletRequest request, 
            HttpServletResponse response) {
        try {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Handle errors.
     */
    protected void redirectError(HttpServletRequest request, 
            HttpServletResponse response, String message) {
        try {
            request.setAttribute("message", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
