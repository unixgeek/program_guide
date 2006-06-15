/*
 * $Id: InsertProgramServlet.java,v 1.2 2006-06-15 00:58:04 gunter Exp $
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

public class InsertProgramServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            ServletException {

        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        
        if (!UserManager.authorizeUser(user, Permissions.ADD_PROGRAM)) {
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("insert_program.jsp");
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
        
        if (!UserManager.authorizeUser(user, Permissions.ADD_PROGRAM)) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        Program program = new Program();
        program.setName(request.getParameter("name"));
        program.setUrl(request.getParameter("url"));
        
        String doUpdate = request.getParameter("do_update");
        if (doUpdate == null)
            program.setDoUpdate((short) 0);
        else
            program.setDoUpdate(Short.parseShort(doUpdate));
        
        try {
            Persistor.insertProgram(connection, program);
            
            connection.close();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, e.getMessage());
        }
        
        String prefix = program.getName().substring(0, 1);
        RequestDispatcher dispatcher = request.getRequestDispatcher(
                "AdminPrograms.do?prefix=" + prefix);
        dispatcher.forward(request, response);
    }
}
