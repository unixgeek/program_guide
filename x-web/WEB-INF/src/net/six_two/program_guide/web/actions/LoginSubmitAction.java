package net.six_two.program_guide.web.actions;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.web.forms.LoginForm;
import net.six_two.program_guide.tables.User;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class LoginSubmitAction extends GenericAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        ActionErrors errors = new ActionErrors();
        LoginForm loginForm = (LoginForm) form;
        User user;
        try {
            Connection connection = getConnection(request);
            user = Persistor.selectUser(connection, loginForm.getUsername());
            connection.close();
        } catch (SQLException e) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, 
                    new ActionMessage(e.getMessage()));
            saveErrors(request, (ActionMessages) errors);
            return new ActionForward(mapping.getInput());
        }
        
        if (user == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, 
                    new ActionMessage("error.username.invalid"));
            saveErrors(request, (ActionMessages) errors);
            return new ActionForward(mapping.getInput());
        }
        
        boolean valid;
        valid = UserManager.authenticateUser(user, loginForm.getPassword());
        
        if (valid) {
            request.getSession().setAttribute("user", user);
            return mapping.findForward("success");
        }
        else {
            errors.add(ActionMessages.GLOBAL_MESSAGE, 
                new ActionMessage("error.password.invalid"));
            saveErrors(request, (ActionMessages) errors);
            return new ActionForward(mapping.getInput());
        }
    }
}
