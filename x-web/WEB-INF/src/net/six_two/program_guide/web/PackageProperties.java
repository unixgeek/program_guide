/*
 * $Id: PackageProperties.java,v 1.1.2.1 2006-01-04 05:06:15 gunter Exp $
 */
package net.six_two.program_guide.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PackageProperties {
    private static final String PROPERTY_RESOURCE =
        "net/six_two/program_guide/web/web.properties";
    private String name = "unknown";
    private String version = "unknown";
    
    public PackageProperties() {
        ClassLoader cl = this.getClass().getClassLoader(); 
        InputStream stream = cl.getResourceAsStream(PROPERTY_RESOURCE);
        
        if (stream != null) {
            Properties p = new Properties();
            try {
                p.load(stream);
                name = p.getProperty("package.name");
                version = p.getProperty("package.version");
            } catch (IOException e) {
            }
        }
    }
    
    public String getName() {
        return name;
    }
    
    public String getVersion() {
        return version;
    }
}
