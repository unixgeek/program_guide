/*
 * $Id: LogoutServlet.java,v 1.3 2005-11-01 03:46:45 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.tables.User;

public class LogoutServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        
        User user = getUserFromRequest(request);
            request.getSession().invalidate();
            /*
             * Don't use redirecLogin()--it's intended for redirection from
             * attempts to access pages that require authorization.  
             */ 
            RequestDispatcher dispatcher = request.getRequestDispatcher("DisplayFrontPage.do");
            dispatcher.forward(request, response);
            return;
    }
}
