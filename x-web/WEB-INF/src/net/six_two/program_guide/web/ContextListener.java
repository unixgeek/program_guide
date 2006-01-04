/*
 * $Id: ContextListener.java,v 1.1.2.1 2006-01-04 05:06:15 gunter Exp $
 */
package net.six_two.program_guide.web;

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
