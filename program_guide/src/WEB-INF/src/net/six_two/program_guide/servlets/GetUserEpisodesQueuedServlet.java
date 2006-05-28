/*
 * $Id: GetUserEpisodesQueuedServlet.java,v 1.4 2006-05-28 20:44:58 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.Timer;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

public class GetUserEpisodesQueuedServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {

        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        Timer timer = new Timer();
        timer.start();
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
        
        int currentPage;
        try {
            currentPage =
                Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e2) {
            currentPage = 0;
        }
        
        try {
            int count = Persistor.selectQueuedEpisodeCountForUser(connection,
                    user);
            int pages = count / 10;
            if ((count % 10) > 0)
                pages++;
            
            String[] pageTitles = new String[pages];
            for (int i = 0; i != pages; i++) {
                pageTitles[i] = Integer.toString(i + 1);
            }
            
            if ((currentPage == 0) && (pageTitles.length > 0))
                currentPage = Integer.parseInt(pageTitles[0]);
            
            UserEpisode[] queuedEpisodes = null;
            if (currentPage != 0)
                queuedEpisodes = Persistor.selectAllQueuedEpisodesForUser(
                        connection, user, (currentPage - 1) * 10, 10); 
            
            connection.close();

            timer.stop();
            
            if (queuedEpisodes != null)
                for (int i = 0; i != queuedEpisodes.length; i++)
                    queuedEpisodes[i].getEpisode().setTitle(
                            filterContent(
                                    queuedEpisodes[i].getEpisode().getTitle()));

            request.setAttribute("pages", new Integer(pages));
            request.setAttribute("elapsedTime", timer.getElapsedTime());
            request.setAttribute("queuedEpisodesList", queuedEpisodes);
            request.setAttribute("pageTitles", pageTitles);
            request.setAttribute("currentPage", new Integer(currentPage));
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            timer.stop();
            redirectError(request, response, e.getMessage());
            return;
        }
        
        RequestDispatcher dispatcher = request.getRequestDispatcher("queued.jsp");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
