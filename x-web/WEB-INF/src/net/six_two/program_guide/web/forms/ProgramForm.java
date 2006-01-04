/*
 * $Id: ProgramForm.java,v 1.1.2.1 2006-01-04 05:09:12 gunter Exp $
 */
package net.six_two.program_guide.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

public class ProgramForm extends ActionForm {
    private int id;
    private String name;
    private String url;
    private boolean update;
    
    public ProgramForm() {
        
    }

    public ActionErrors validate(ActionMapping mapping,
            HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        
        if ((name == null) || (name.length() < 1))
            errors.add("name", 
                    new ActionMessage("error.program.name.required"));
        
        if ((url == null) || (url.length() < 1))
            errors.add("url", 
                    new ActionMessage("error.program.url.required"));
        
        return errors;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isUpdate() {
        return update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
