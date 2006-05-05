/*
 * $Id: ContextListener.java,v 1.1 2006-05-05 22:38:22 gunter Exp $
 */
package net.six_two.program_guide;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.six_two.program_guide.PackageProperties;

public class ContextListener implements ServletContextListener {

    public void contextDestroyed(ServletContextEvent event) {
        
    }

    public void contextInitialized(ServletContextEvent event) {
        PackageProperties p = new PackageProperties();
        ServletContext context = event.getServletContext();
        context.setAttribute("packageName", p.getName());
        context.setAttribute("packageVersion", p.getVersion());
        context.setAttribute("dateDisplayFormat", p.getDateDisplayFormat());
        context.setAttribute("timestampDisplayFormat", 
                p.getTimestampDisplayFormat());

    }
}
