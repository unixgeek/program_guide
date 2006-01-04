/*
 * $Id: LoginForm.java,v 1.1.2.1 2006-01-04 05:09:12 gunter Exp $
 */
package net.six_two.program_guide.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class LoginForm extends ActionForm {
    private String username;
    private String password;
    
    public LoginForm() {
        
    }
    
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if ((username == null) || (username.length() < 1))
            errors.add("username", 
                    new ActionMessage("error.username.required"));
        
        if ((password == null) || (password.length() < 1))
            errors.add("password", 
                    new ActionMessage("error.password.required"));
        
        return errors;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
