/*
 * $Id: SetUserProgramsServlet.java,v 1.1 2006-05-05 22:38:22 gunter Exp $
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
import net.six_two.program_guide.tables.ProgramSubscribed;
import net.six_two.program_guide.tables.User;

public class SetUserProgramsServlet extends GenericServlet {
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
        
        try {
            ProgramSubscribed[] programs = Persistor.
                selectAllProgramsAndSubscribedForUser(connection, user);
            
            connection.close();
            
            request.setAttribute("programsList", programs);
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
             redirectError(request, response, e.getMessage());
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("subscription.jsp");
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
        
        try {
            Persistor.deleteAllSubscribedForUser(connection, user);
            
            String[] subscribed = request.getParameterValues("subscribed");
            if (subscribed != null) {
                for (int i = 0; i != subscribed.length; i++) {
                    String tokens[] = subscribed[i].split("_");
                    if (tokens[1].equals("1")) {
                        Program program = new Program();
                        program.setId(Integer.parseInt(tokens[0]));
                        Persistor.insertSubscribedForUser(connection, 
                                user, program);
                    }
                }
            }
            
            connection.close();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("GetUserPrograms.do");
        dispatcher.forward(request, response);
    }
}
