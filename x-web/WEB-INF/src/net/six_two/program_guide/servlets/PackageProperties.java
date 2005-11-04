/*
 * $Id: PackageProperties.java,v 1.1 2005-11-04 20:59:19 gunter Exp $
 */
package net.six_two.program_guide.servlets;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PackageProperties {
    private static final String PROPERTY_RESOURCE =
        "net/six_two/program_guide/servlets/program_guide.properties";
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
