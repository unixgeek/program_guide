/*
 * $Id: GetUserEpisodesScheduleAction.java,v 1.1.2.1 2006-01-04 05:06:38 gunter Exp $
 */
package net.six_two.program_guide.web.actions;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.tables.TorrentSite;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class GetUserEpisodesScheduleAction extends GenericAction {
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
        
        UserEpisode[] todaysEpisodes = Persistor.
        selectAllEpisodesForUser(connection, user, 0, 0);
        
        UserEpisode[] previousEpisodes = Persistor.
        selectAllEpisodesForUser(connection, user, -1, -6);
        
        UserEpisode[] nextEpisodes = Persistor.
        selectAllEpisodesForUser(connection, user, 1, 6);
        
        TorrentSite site = Persistor.selectTorrentSite(connection);
        
        connection.close();
        
        for (int i = 0; i != todaysEpisodes.length; i++)
            todaysEpisodes[i].getEpisode().setTitle(
                    filterContent(todaysEpisodes[i].getEpisode().getTitle()));
        
        for (int i = 0; i != previousEpisodes.length; i++)
            previousEpisodes[i].getEpisode().setTitle(
                    filterContent(
                            previousEpisodes[i].getEpisode().getTitle()));
        
        for (int i = 0; i != nextEpisodes.length; i++)
            nextEpisodes[i].getEpisode().setTitle(
                    filterContent(nextEpisodes[i].getEpisode().getTitle()));
        
        request.setAttribute("todaysEpisodesList", todaysEpisodes);
        request.setAttribute("nextEpisodesList", nextEpisodes);
        request.setAttribute("previousEpisodesList", previousEpisodes);
        request.setAttribute("site", site);
        
        return mapping.findForward("schedule");
    }
}
