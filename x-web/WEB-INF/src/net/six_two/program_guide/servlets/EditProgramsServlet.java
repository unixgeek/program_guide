/*
 * $Id: EditProgramsServlet.java,v 1.2 2005-10-23 23:31:55 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.User;

public class EditProgramsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
                context.lookup("java:comp/env/jdbc/program_guide");
            Connection connection = source.getConnection();
            
            User user = Persistor.selectUser(connection, "gunter");
            UserManager.authenticateUser(user, "");
            //user.getLevel() == 0;
            Program[] programs = Persistor.selectAllPrograms(connection);
            
            request.setAttribute("programsList", programs);
        } catch (NamingException e) {
            log("error", e);
        } catch (SQLException e) {
            log("error", e);
        }
    }
    
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            ServletException {
        Enumeration attributes = request.getAttributeNames();
        while (attributes.hasMoreElements()) {
            log(attributes.nextElement().toString());
        }
        
        doGet(request, response);
    }
}
