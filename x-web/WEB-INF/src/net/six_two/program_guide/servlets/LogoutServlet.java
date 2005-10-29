/*
 * $Id: LogoutServlet.java,v 1.2 2005-10-29 00:59:41 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.tables.User;

public class LogoutServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        
        User user = getUserFromRequest(request);
        // There isn't a session.
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        else {
            request.getSession().invalidate();
            redirectLogin(request, response);   
        }
    }
}
