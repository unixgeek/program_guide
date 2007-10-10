/*
 * $Id: PackageProperties.java,v 1.1 2007-10-10 02:09:04 gunter Exp $
 */
package net.six_two.program_guide;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PackageProperties {
    private static final String PROPERTY_RESOURCE =
        "net/six_two/program_guide/program_guide.properties";
    private String name = "unknown";
    private String version = "unknown";
    private String dateDisplayFormat = "yyyy-MM-dd";
    private String timestampDisplayFormat = "yyyy-MM-dd hh:mm:ss.S";
    
    public PackageProperties() {
        ClassLoader cl = this.getClass().getClassLoader(); 
        InputStream stream = cl.getResourceAsStream(PROPERTY_RESOURCE);
        
        if (stream != null) {
            Properties p = new Properties();
            try {
                p.load(stream);
                name = p.getProperty("package.name");
                version = p.getProperty("package.version");
                dateDisplayFormat = p.getProperty("date.displayFormat");
                timestampDisplayFormat = p.getProperty("timestamp.displayFormat");
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

    public String getDateDisplayFormat() {
        return dateDisplayFormat;
    }

    public String getTimestampDisplayFormat() {
        return timestampDisplayFormat;
    }
}
