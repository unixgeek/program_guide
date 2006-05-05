/*
 * $Id: DeleteProgramServlet.java,v 1.4.6.1 2006-05-05 03:44:39 gunter Exp $
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

public class DeleteProgramServlet extends GenericServlet {
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
        
        if (!UserManager.authorizeUser(user, Permissions.DELETE_PROGRAM)) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        int program_id = 
            Integer.parseInt(request.getParameter("program_id"));
        
        try {
            Program program = Persistor.selectProgram(connection, program_id);
            
            connection.close();
            
            request.setAttribute("program", program);
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, e.getMessage());
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("delete_program.jsp");
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
        
        if (!UserManager.authorizeUser(user, Permissions.DELETE_PROGRAM)) {
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        Program program = new Program();
        program.setId(Integer.parseInt(request.getParameter("program_id")));

        try {
            Persistor.deleteProgram(connection, program);
            
            connection.close();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, e.getMessage());
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPrograms.do");
        dispatcher.forward(request, response);
    }
}
