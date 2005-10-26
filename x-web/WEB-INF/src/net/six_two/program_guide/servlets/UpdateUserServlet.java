/*
 * $Id: UpdateUserServlet.java,v 1.1 2005-10-26 22:31:36 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.Episode;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.User;

public class UpdateUserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            ServletException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (user.getLevel() != 0) {
            request.setAttribute("message", "You must login with admin rights first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        int user_id = 
            Integer.parseInt(request.getParameter("user_id"));
        
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
                context.lookup("java:comp/env/jdbc/program_guide");
            Connection connection = source.getConnection();
            
            User theUser = Persistor.selectUser(connection, user_id);
            
            request.setAttribute("theUser", theUser);
            connection.close();
            RequestDispatcher dispatcher = request.getRequestDispatcher("update_user.jsp");
            dispatcher.forward(request, response);
        } catch (NamingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws IOException,
            ServletException {
        
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        User user = (User) session.getAttribute("user");
        if (user == null) {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        if (user.getLevel() != 0) {
            request.setAttribute("message", 
                    "You must login with admin rights first.");
            RequestDispatcher dispatcher = 
                request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
            return;
        }
        
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
                context.lookup("java:comp/env/jdbc/program_guide");
            Connection connection = source.getConnection();
            
            int user_id = 
                Integer.parseInt(request.getParameter("user_id"));
            
            User theUser = Persistor.selectUser(connection, user_id);
            theUser.setUsername(request.getParameter("username"));
            theUser.setLevel(Short.parseShort(request.getParameter("level")));
            UserManager.setPasswordForUser(theUser, request.getParameter("password"));

            Persistor.updateUser(connection, theUser);
            
            connection.close();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminUsers.do");
        dispatcher.forward(request, response);
    }
}
