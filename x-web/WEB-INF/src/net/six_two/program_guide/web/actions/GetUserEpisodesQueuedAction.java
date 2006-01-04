/*
 * $Id: GetUserEpisodesQueuedAction.java,v 1.1.2.1 2006-01-04 05:06:38 gunter Exp $
 */
package net.six_two.program_guide.web.actions;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class GetUserEpisodesQueuedAction extends GenericAction {
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
        
        UserEpisode[] queuedEpisodes = 
            Persistor.selectAllQueuedEpisodesForUser(connection, user); 
        
        connection.close();

        for (int i = 0; i != queuedEpisodes.length; i++)
            queuedEpisodes[i].getEpisode().setTitle(
                filterContent(queuedEpisodes[i].getEpisode().getTitle()));
        
        request.setAttribute("queuedEpisodesList", queuedEpisodes);
        
        return mapping.findForward("queued");
    }
}
