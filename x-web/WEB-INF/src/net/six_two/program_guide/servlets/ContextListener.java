/*
 * $Id: ContextListener.java,v 1.1 2005-11-04 20:51:10 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent event) {
        
    }

    public void contextInitialized(ServletContextEvent event) {
        PackageProperties p = new PackageProperties();
        ServletContext context = event.getServletContext();
        context.setAttribute("packageName", p.getName());
        context.setAttribute("packageVersion", p.getVersion());
    }
}
