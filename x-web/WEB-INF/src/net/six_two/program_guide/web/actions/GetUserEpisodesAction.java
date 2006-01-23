/*
 * $Id: GetUserEpisodesAction.java,v 1.1.2.1 2006-01-23 01:49:25 gunter Exp $
 */
package net.six_two.program_guide.web.actions;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.TorrentSite;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class GetUserEpisodesAction extends GenericAction {
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
        UserEpisode[] userEpisodes = Persistor.
            selectAllEpisodesForUser(connection, user, program);
        
        TorrentSite site = Persistor.selectTorrentSite(connection);
        
        connection.close();
        
        for (int i = 0; i != userEpisodes.length; i++)
            userEpisodes[i].getEpisode().setTitle(
                    filterContent(
                            userEpisodes[i].getEpisode().getTitle()));

        request.setAttribute("userEpisodesList", userEpisodes);
        request.setAttribute("program", program);
        request.setAttribute("site", site);
        
        return mapping.findForward("episodes");
    }
}
