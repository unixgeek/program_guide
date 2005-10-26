/*
 * $Id: GetUserProgramsServlet.java,v 1.4 2005-10-26 22:33:22 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import javax.naming.Context;
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
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.Subscribed;
import net.six_two.program_guide.tables.User;

public class GetUserProgramsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
            
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
            context.lookup("java:comp/env/jdbc/program_guide");
            Connection connection = source.getConnection();
            
            Program[] programs = Persistor.
            selectAllProgramsForUser(connection, user);
            
            connection.close();
            
            request.setAttribute("programsList", programs);
            RequestDispatcher dispatcher = request.getRequestDispatcher("programs.jsp");
            dispatcher.forward(request, response);   
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
