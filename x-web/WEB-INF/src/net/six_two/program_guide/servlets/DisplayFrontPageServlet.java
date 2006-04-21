/*
 * $Id: DisplayFrontPageServlet.java,v 1.7 2006-04-21 06:44:40 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.tables.User;

public class DisplayFrontPageServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
   
        User user = getUserFromRequest(request);
        if (user == null) {
            /*
             * Don't use redirecLogin()--it's intended for redirection from
             * attempts to access pages that require authorization.  
             */ 
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("GetScheduleByMonth.do");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
