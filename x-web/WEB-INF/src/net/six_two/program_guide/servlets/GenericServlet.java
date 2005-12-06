/*
 * $Id: GenericServlet.java,v 1.7 2005-12-06 05:34:13 gunter Exp $
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

import net.six_two.program_guide.Permissions;
import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class GenericServlet extends HttpServlet {
    /*
     * Returns the User object from the session, or null if it
     * doesn't exist.
     */
    protected User getUserFromRequest(HttpServletRequest request) {
        // Attempt to get a session, but don't create one.
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        
        User user = (User) session.getAttribute("user");
        if (user != null) {
            try {
                Connection connection = getConnection();
                int programCount = 
                    Persistor.selectProgramCountForUser(connection, user);
                int queueCount = 
                    Persistor.selectQueuedEpisodeCountForUser(connection, user);
                int todayCount =
                    Persistor.selectEpisodeCountForUser(connection, user, 0, 0);
                
                boolean canAddProgram = UserManager.authorizeUser(user, 
                        Permissions.ADD_PROGRAM);
                boolean canDeleteProgram = UserManager.authorizeUser(user, 
                        Permissions.DELETE_PROGRAM);
                boolean canEditProgram = UserManager.authorizeUser(user, 
                        Permissions.EDIT_PROGRAM);
                boolean showAdminLog = UserManager.authorizeUser(user,
                        Permissions.ADMIN_LOG);
                
                boolean showAdminPrograms;
                if (canAddProgram || canDeleteProgram || canEditProgram) {
                    showAdminPrograms = true;
                }
                else
                    showAdminPrograms = false;
                
                boolean canAddUser = UserManager.authorizeUser(user, 
                        Permissions.ADD_USER);
                boolean canDeleteUser = UserManager.authorizeUser(user, 
                        Permissions.DELETE_USER);
                boolean canEditUser = UserManager.authorizeUser(user, 
                        Permissions.EDIT_USER);
                
                boolean showAdminUsers;
                if (canAddUser || canDeleteUser || canEditUser) {
                    showAdminUsers = true;
                }
                else
                    showAdminUsers = false;
                
                session.setAttribute("showAdminPrograms", new Boolean(showAdminPrograms));
                session.setAttribute("showAdminUsers", new Boolean(showAdminUsers));
                session.setAttribute("showAdminLog", new Boolean(showAdminLog));
                session.setAttribute("programCount", 
                        new Integer(programCount));
                session.setAttribute("queueCount", new Integer(queueCount));
                session.setAttribute("todayCount", new Integer(todayCount));
                connection.close();
            } catch (SQLException e) {
            }
        }
        return (User) session.getAttribute("user");
    }
    
    /*
     * Returns a connection from the pool.
     */
    protected Connection getConnection() {
        Connection connection = null;
        try {
            InitialContext context = new InitialContext();
            DataSource source = (DataSource) 
                context.lookup("java:comp/env/jdbc/program_guide");
            connection = source.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    /*
     * Handle the session-less.
     */
    protected void redirectLogin(HttpServletRequest request, 
            HttpServletResponse response) {
        try {
            request.setAttribute("message", "You must login first.");
            RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Handle errors.
     */
    protected void redirectError(HttpServletRequest request, 
            HttpServletResponse response, String message) {
        try {
            request.setAttribute("message", message);
            RequestDispatcher dispatcher = request.getRequestDispatcher("error.jsp");
            dispatcher.forward(request, response);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Filter special characters for HTML.
     */
    protected String filterContent(String content) {
        if (content == null)
            return "&nbsp;";
        
        StringBuffer filteredContent = new StringBuffer();
        
        char[] characters = content.trim().toCharArray();
        
        for (int i = 0; i != characters.length; i++) {
            switch (characters[i]) {
                case '<':
                    filteredContent.append("&lt;");
                    break;
                case '>':
                    filteredContent.append("&gt;");
                    break;
                case '&':
                    filteredContent.append("&amp;");
                    break;
                case '"':
                    filteredContent.append("&quot;");
                    break;
                default:
                    filteredContent.append(characters[i]);
            }
        }
        
        return filteredContent.toString();
    }
}

