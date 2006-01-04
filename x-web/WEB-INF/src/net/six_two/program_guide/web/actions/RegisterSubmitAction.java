package net.six_two.program_guide.web.actions;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.web.forms.RegisterForm;
import net.six_two.program_guide.tables.User;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class RegisterSubmitAction extends GenericAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        ActionErrors errors = new ActionErrors();
        RegisterForm registerForm = (RegisterForm) form;
        
        Connection connection = getConnection(request);
        
        if (Persistor.selectUser(connection, registerForm.getUsername()) 
                != null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, 
                    new ActionMessage("error.username.exists"));
            saveErrors(request, (ActionMessages) errors);
            connection.close();
            
            return mapping.getInputForward();
        }   
            
        User user = UserManager.createUser(registerForm.getUsername(),
                registerForm.getPassword1());
        Persistor.insertUser(connection, user);
            
        connection.close();
        
        String action = (String) request.getAttribute("action");
        action = (action != null) ? action : "";        
            
        if (action.equals("nologin")) {
            return mapping.findForward("admin");
        }
        else {
            request.getSession().setAttribute("user", user);
            return mapping.findForward("success");
        }
    }
}
