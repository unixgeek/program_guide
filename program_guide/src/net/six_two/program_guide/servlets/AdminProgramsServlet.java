/*
 * $Id: AdminProgramsServlet.java,v 1.1 2007-10-10 02:09:05 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Permissions;
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
        
        String prefix = request.getParameter("prefix");
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
        
        boolean canAddProgram = UserManager.authorizeUser(user, 
                Permissions.ADD_PROGRAM);
        boolean canDeleteProgram = UserManager.authorizeUser(user, 
                Permissions.DELETE_PROGRAM);
        boolean canEditProgram = UserManager.authorizeUser(user, 
                Permissions.EDIT_PROGRAM);
        
        if (!canAddProgram && !canDeleteProgram && !canEditProgram) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        try {
            String[] prefixes = 
                Persistor.selectProgramPrefixes(connection);
            
            Program[] programs = null;
            if (prefixes.length > 0) {
                if ((prefix == null) || (prefix.trim().length() < 1))
                    prefix = prefixes[0];
                programs = Persistor.selectAllPrograms(connection, prefix);
            }
            
            connection.close();
            
            request.setAttribute("prefixes", prefixes);
            request.setAttribute("currentPrefix", prefix);
            request.setAttribute("programsList", programs);
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, e.getMessage());
        }
        
        request.setAttribute("canAdd", new Boolean(canAddProgram));
        request.setAttribute("canDelete", new Boolean(canDeleteProgram));
        request.setAttribute("canEdit", new Boolean(canEditProgram));
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
