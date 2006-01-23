package net.six_two.program_guide.web.actions;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.tables.Episode;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.User;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class SetUserEpisodesSubmitAction extends GenericAction {
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
        int program_id = 
            Integer.parseInt(request.getParameter("program_id"));
        
        Program program = Persistor.selectProgram(connection, program_id);
        
        Persistor.deleteStatusForUser(connection, user, program);
        
        String[] episodeStatus = request.getParameterValues("status");
        if (episodeStatus != null) {
            for (int i = 0; i != episodeStatus.length; i++) {
                String tokens[] = episodeStatus[i].split("_");
                Episode episode = new Episode();
                episode.setProgramId(program_id);
                episode.setSeason(tokens[0]);
                episode.setNumber(Integer.parseInt(tokens[1]));
                
                if (tokens[2].equals("queued") || tokens[2].equals("viewed"))
                    Persistor.insertStatusForUser(connection, user, 
                            episode, tokens[2]);
            }
        }
        
        connection.close();
        
        return mapping.findForward("episodes");
    }
}
