/*
 * $Id: FetchEpisodesServlet.java,v 1.1 2006-05-05 22:38:23 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.six_two.program_guide.ScrapeRunnable;
import net.six_two.program_guide.tables.User;

public class FetchEpisodesServlet extends GenericServlet {
    protected void doGet(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {

        User user = getUserFromRequest(request);
        if (user == null) {
            redirectLogin(request, response);
            return;
        }
        
        Connection connection = getConnection();
        if (connection == null) {
            redirectError(request, response, 
                    "Couldn't connect to the database.");
            return;
        }
            
        int programId = 
            Integer.parseInt(request.getParameter("program_id"));
            
            String path = 
                getServletConfig().getServletContext().getRealPath("/scripts");

        ScrapeRunnable scrape = new ScrapeRunnable(connection, new File(path),
                programId);
        Thread scrapeThread = new Thread(scrape);
        scrapeThread.start();
        
        RequestDispatcher dispatcher = 
            request.getRequestDispatcher("/AdminPrograms.do");
        dispatcher.forward(request, response);
    }
    
    protected void doPost(HttpServletRequest request, 
            HttpServletResponse response) throws IOException, 
            ServletException {
        doGet(request, response);
    }
}
