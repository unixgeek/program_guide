/*
 * $Id: AdminUsersServlet.java,v 1.3 2005-11-27 20:13:19 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Permissions;
import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class AdminUsersServlet extends GenericServlet {
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
        
        boolean canAddUser = UserManager.authorizeUser(user, 
                Permissions.ADD_USER);
        boolean canDeleteUser = UserManager.authorizeUser(user, 
                Permissions.DELETE_USER);
        boolean canEditUser = UserManager.authorizeUser(user, 
                Permissions.EDIT_USER);
        
        if (!canAddUser && !canDeleteUser && !canEditUser) {
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        try {
            User[] users = Persistor.selectAllUsers(connection);
            
            connection.close();
            
            request.setAttribute("usersList", users);
        } catch (SQLException e) {
            redirectError(request, response, e.getMessage());
        }
        
        request.setAttribute("canAdd", new Boolean(canAddUser));
        request.setAttribute("canDelete", new Boolean(canDeleteUser));
        request.setAttribute("canEdit", new Boolean(canEditUser));
        RequestDispatcher dispatcher = 
            request.getRequestDispatcher("admin_users.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
