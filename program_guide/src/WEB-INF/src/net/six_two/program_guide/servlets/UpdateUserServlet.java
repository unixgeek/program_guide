/*
 * $Id: UpdateUserServlet.java,v 1.1 2006-05-05 22:38:23 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Permissions;
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
        
        if (!UserManager.authorizeUser(user, Permissions.EDIT_USER)) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        int user_id = 
            Integer.parseInt(request.getParameter("user_id"));
        
        try {
            User candidateUser = Persistor.selectUser(connection, user_id);
            
            connection.close();
            
            boolean canUse = UserManager.authorizeUser(candidateUser, 
                    Permissions.USAGE);
            boolean canAddProgram = UserManager.authorizeUser(candidateUser, 
                    Permissions.ADD_PROGRAM);
            boolean canDeleteProgram = UserManager.authorizeUser(candidateUser, 
                    Permissions.DELETE_PROGRAM);
            boolean canEditProgram = UserManager.authorizeUser(candidateUser,
                    Permissions.EDIT_PROGRAM);
            boolean canAddUser = UserManager.authorizeUser(candidateUser, 
                    Permissions.ADD_USER);
            boolean canDeleteUser = UserManager.authorizeUser(candidateUser, 
                    Permissions.DELETE_USER);
            boolean canEditUser = UserManager.authorizeUser(candidateUser, 
                    Permissions.EDIT_USER);
            boolean canAdminLog = UserManager.authorizeUser(candidateUser, 
                    Permissions.ADMIN_LOG);
            
            /*
             * TODO Do permissions correctly...I need a barf bag.
             */
            request.setAttribute("canUse", new Boolean(canUse));
            request.setAttribute("canAddProgram", new Boolean(canAddProgram));
            request.setAttribute("canDeleteProgram", new Boolean(canDeleteProgram));
            request.setAttribute("canEditProgram", new Boolean(canEditProgram));
            request.setAttribute("canAddUser", new Boolean(canAddUser));
            request.setAttribute("canDeleteUser", new Boolean(canDeleteUser));
            request.setAttribute("canEditUser", new Boolean(canEditUser));
            request.setAttribute("canAdminLog", new Boolean(canAdminLog));
            request.setAttribute("candidateUser", candidateUser);
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
        
        if (!UserManager.authorizeUser(user, Permissions.EDIT_USER)) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        String username = (String) request.getParameter("username");
        String password1 = (String) request.getParameter("password1");
        String password2 = (String) request.getParameter("password2");
        String action = (String) request.getParameter("action");
        
        username = (username != null) ? username : "";
        password1 = (password1 != null) ? password1 : "";
        password2 = (password2 != null) ? password2 : "";
        
        int user_id = 
            Integer.parseInt(request.getParameter("user_id"));
        
        try {
            User candidateUser = Persistor.selectUser(connection, user_id);
            
            if (username.equals("") && action.equals("username")) {
                connection.close();
                error(request, response, "Invalid username.");
                return;
            }
            
            if (password1.equals("") && action.equals("password")) {
                connection.close();
                request.setAttribute("candidateUser", candidateUser);
                error(request, response, "Invalid password.");
                return;
            }
            
            if (!password1.equals(password2) && action.equals("password")) {
                connection.close();
                request.setAttribute("candidateUser", candidateUser);
                error(request, response, "Passwords don't match.");
                return;
            }
            
            if (action.equals("username")) {
                User testUser = Persistor.selectUser(connection, username);
                
                if (testUser != null) {
                    connection.close();
                    request.setAttribute("candidateUser", candidateUser);
                    error(request, response, "User exists.");
                    return;
                }
                candidateUser.setUsername(username);
            }
            else if (action.equals("password"))
                UserManager.setPasswordForUser(candidateUser, password1);
            else if (action.equals("permissions")) {
                String[] grants = request.getParameterValues("granted");
                if (grants != null) {
                    Permissions p = new Permissions();
                    HashMap map = p.getPermissionsMap();
                    int permissions = 0;
                    for (int i = 0; i != grants.length; i++) {
                        String tokens[] = grants[i].split("\\|");
                        if (tokens[1].equals("1")) {
                            permissions |= ((Integer) map.get(tokens[0])).intValue();
                        }
                    }
                    candidateUser.setPermissions(permissions);
                }
                else
                    candidateUser.setPermissions(0);
            }

            Persistor.updateUser(connection, candidateUser);
            
            connection.close();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
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
