package net.six_two.program_guide.web.actions;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.User;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SetUserProgramsSubmitAction extends GenericAction {
    public ActionForward execute(ActionMapping mapping, ActionForm form, 
            HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        ActionErrors errors = new ActionErrors();
        
        User user = getUserFromRequest(request);
        if (user == null) {
            errors.add(ActionMessages.GLOBAL_MESSAGE, 
                    new ActionMessage("error.login.required"));
            saveErrors(request, (ActionMessages) errors);
            return new ActionForward("/Error.do");
        }
        
        Connection connection = getConnection(request);
        
        Persistor.deleteAllSubscribedForUser(connection, user);
            
        String[] subscribed = request.getParameterValues("subscribed");
        if (subscribed != null) {
            for (int i = 0; i != subscribed.length; i++) {
                String tokens[] = subscribed[i].split("_");
                if (tokens[1].equals("1")) {
                    Program program = new Program();
                    program.setId(Integer.parseInt(tokens[0]));
                    Persistor.insertSubscribedForUser(connection, 
                            user, program);
                }
            }
        }
        
        connection.close();
        
        return mapping.findForward("success");
    }
}
