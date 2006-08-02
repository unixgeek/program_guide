/*
 * $Id: ShowAdminServlet.java,v 1.1 2006-08-02 03:07:47 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Permissions;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

public class ShowAdminServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        
        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        
        boolean canAddProgram = UserManager.authorizeUser(user, 
                Permissions.ADD_PROGRAM);
        boolean canDeleteProgram = UserManager.authorizeUser(user, 
                Permissions.DELETE_PROGRAM);
        boolean canEditProgram = UserManager.authorizeUser(user, 
                Permissions.EDIT_PROGRAM);
        
        boolean showAdminPrograms = canAddProgram || canDeleteProgram ||
                canEditProgram;
        
        boolean canAddUser = UserManager.authorizeUser(user, 
                Permissions.ADD_USER);
        boolean canDeleteUser = UserManager.authorizeUser(user, 
                Permissions.DELETE_USER);
        boolean canEditUser = UserManager.authorizeUser(user, 
                Permissions.EDIT_USER);
        
        boolean showAdminUser = canAddUser || canDeleteUser || canEditUser;
        
        boolean showAdminLog = UserManager.authorizeUser(user,
                Permissions.ADMIN_LOG);
        
        String forward;
        if (showAdminPrograms)
            forward = "AdminPrograms.do";
        else if (showAdminLog)
            forward = "GetLog.do";
        else if (showAdminUser)
            forward = "AdminUsers.do";
        else {
            redirectError(request, response, 
                    "You have insufficient rights to this resource.  Loser.");
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher(forward);
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
