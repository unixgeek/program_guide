/*
 * $Id: UpdateProgramServlet.java,v 1.3 2005-10-29 00:59:41 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.User;

public class UpdateProgramServlet extends GenericServlet {
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
        
        int program_id = 
            Integer.parseInt(request.getParameter("program_id"));
        
        try {
            Program program = Persistor.selectProgram(connection, program_id);
            
            request.setAttribute("program", program);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("update_program.jsp");
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
        
        if (user.getLevel() != 0) {
            redirectError(request, response, 
                    "You must login with admin rights first.");
            return;
        }
        
        Program program = new Program();
        program.setId(Integer.parseInt(request.getParameter("program_id")));
        program.setName(request.getParameter("name"));
        program.setUrl(request.getParameter("url"));

        String doUpdate = request.getParameter("do_update");
        if (doUpdate == null)
            program.setDoUpdate((short) 0);
        else
            program.setDoUpdate(Short.parseShort(doUpdate));
        
        try {
            Persistor.updateProgram(connection, program);
            
            connection.close();
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminPrograms.do");
        dispatcher.forward(request, response);
    }
}
