/*
 * $Id: LogoutServlet.java,v 1.1 2005-10-25 22:10:03 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        }
        else {
            session.invalidate();
            
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);   
        }
    }
}
