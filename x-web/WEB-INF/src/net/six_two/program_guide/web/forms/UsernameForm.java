/*
 * $Id: UsernameForm.java,v 1.1.2.1 2006-01-04 05:09:12 gunter Exp $
 */
package net.six_two.program_guide.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class UsernameForm extends ActionForm {
    private String username;
    
    public UsernameForm() {
        
    }
    
    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if ((username == null) || (username.length() < 1))
            errors.add("username", 
                    new ActionMessage("error.username.required"));
        
        return errors;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
