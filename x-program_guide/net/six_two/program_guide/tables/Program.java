/*
 * $Id: Program.java,v 1.2 2005-10-20 02:47:15 gunter Exp $
 */
package net.six_two.program_guide.tables;

import java.util.Date;

public class Program {
    private short doUpdate;
    private int id;
    private Date lastUpdate;
    private String name;
    private String url;
    
    public Program() {
        
    }
    
    public Program(int id, String name, String url, Date lastUpdate,
            short doUpdate) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.lastUpdate = lastUpdate;
        this.doUpdate = doUpdate;
    }
    
    public short getDoUpdate() {
        return doUpdate;
    }

    public void setDoUpdate(short doUpdate) {
        this.doUpdate = doUpdate;
    }
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
