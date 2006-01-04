/*
 * $Id: DeleteForm.java,v 1.1.2.1 2006-01-04 05:09:12 gunter Exp $
 */
package net.six_two.program_guide.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class DeleteForm extends ActionForm {
    private int id;
    
    public DeleteForm() {
        
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if (id < 0)
            errors.add("id", 
                    new ActionMessage("error.id.invalid"));
        
        return errors;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
