/*
 * $Id: GetProgramsServlet.java,v 1.1 2005-10-20 05:09:14 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.tables.Program;

public class GetProgramsServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) 
            throws ServletException, IOException {
        
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
                context.lookup("java:comp/env/jdbc/program_guide");
            Connection connection = source.getConnection();
            
            response.setContentType("text/html");
            PrintWriter writer = response.getWriter();
            writer.println("<html>");
            writer.println("<body>");
            writer.println("<table border=\"1\">");
            Program[] programs = Persistor.getAllPrograms(connection);
            for (int i = 0; i != programs.length; i++) {
                writer.println("<tr>");
                writer.print("<td>" + programs[i].getId() + "</td>");
                writer.print("<td>" + programs[i].getName() + "</td>");
                writer.print("<td>" + programs[i].getLastUpdate() + "</td>");
                writer.print("<td>" + programs[i].getDoUpdate() + "</td>");
                writer.println("</tr>");
            }
            writer.println("</table>");
            connection.close();
        } catch (NamingException e) {
            response.setContentType("text");
            response.getWriter().println(e.getMessage());
        } catch (SQLException e) {
            response.setContentType("text");
            response.getWriter().println(e.getMessage());
        }
    }
}
