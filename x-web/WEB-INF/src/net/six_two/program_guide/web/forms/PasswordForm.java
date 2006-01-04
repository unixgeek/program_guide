/*
 * $Id: PasswordForm.java,v 1.1.2.1 2006-01-04 05:09:12 gunter Exp $
 */
package net.six_two.program_guide.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class PasswordForm extends ActionForm {
    private String password1;
    private String password2;
    
    public PasswordForm() {
        
    }
    
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if ((password1 == null) || (password1.length() < 1))
            errors.add("password1", 
                    new ActionMessage("error.password.required"));
        
        if ((password2 == null) || (password2.length() < 1))
            errors.add("password2", 
                    new ActionMessage("error.password.required"));
        
        if (!password1.equals(password2))
            errors.add("password1",
                    new ActionMessage("error.password.mismatch"));
        
        return errors;
    }
    
    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getPassword1() {
        return password1;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }
}
