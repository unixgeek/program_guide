/*
 * $Id: UpdateUserServlet.java,v 1.3 2005-11-02 04:17:21 gunter Exp $
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
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class UpdateUserServlet extends GenericServlet {
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
        
        int user_id = 
            Integer.parseInt(request.getParameter("user_id"));
        
        try {
            User candidateUser = Persistor.selectUser(connection, user_id);
            
            connection.close();
            
            request.setAttribute("candidateUser", candidateUser);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("update_user.jsp");
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
        
        String username = (String) request.getParameter("username");
        String password1 = (String) request.getParameter("password1");
        String password2 = (String) request.getParameter("password2");
        String level = (String) request.getParameter("level");
        String action = (String) request.getParameter("action");
        
        username = (username != null) ? username : "";
        password1 = (password1 != null) ? password1 : "";
        password2 = (password2 != null) ? password2 : "";
        level = (level != null) ? level : "";
        
        int user_id = 
            Integer.parseInt(request.getParameter("user_id"));
        
        try {
            User candidateUser = Persistor.selectUser(connection, user_id);
            
            if (username.equals("") && action.equals("username")) {
                error(request, response, "Invalid username.");
                return;
            }
            
            if (password1.equals("") && action.equals("password")) {
                request.setAttribute("candidateUser", candidateUser);
                error(request, response, "Invalid password.");
                return;
            }
            
            if (!password1.equals(password2) && action.equals("password")) {
                request.setAttribute("candidateUser", candidateUser);
                error(request, response, "Passwords don't match.");
                return;
            }
            
            if (level.equals("") && action.equals("level")) {
                request.setAttribute("candidateUser", candidateUser);
                error(request, response, "Invalid level.");
                return;
            }
            
            if (action.equals("username")) {
                User testUser = Persistor.selectUser(connection, username);
                
                if (testUser != null) {
                    request.setAttribute("candidateUser", candidateUser);
                    error(request, response, "User exists.");
                    return;
                }
                candidateUser.setUsername(username);
            }
            else if (action.equals("password"))
                UserManager.setPasswordForUser(candidateUser, password1);
            else if (action.equals("level")) {
                candidateUser.setLevel(Short.parseShort(level));
            }

            Persistor.updateUser(connection, candidateUser);
            
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("AdminUsers.do");
        dispatcher.forward(request, response);
    }
    
    private void error(HttpServletRequest request, HttpServletResponse response,
            String message) throws IOException, ServletException {
        request.setAttribute("message", message);
        RequestDispatcher dispatcher = request.getRequestDispatcher("update_user.jsp");
        dispatcher.forward(request, response);
    }
}
