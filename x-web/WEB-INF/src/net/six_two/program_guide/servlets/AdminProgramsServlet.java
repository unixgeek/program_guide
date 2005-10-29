/*
 * $Id: AdminProgramsServlet.java,v 1.2 2005-10-29 00:59:41 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

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
import net.six_two.program_guide.tables.User;

public class AdminProgramsServlet extends GenericServlet {
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
        
        try {
            Program[] programs = Persistor.selectAllPrograms(connection);
            
            connection.close();
            
            request.setAttribute("programsList", programs);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
        }
        
        RequestDispatcher dispatcher = 
            request.getRequestDispatcher("admin_programs.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
