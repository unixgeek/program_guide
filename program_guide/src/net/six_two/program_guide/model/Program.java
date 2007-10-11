/*
 * $Id: Program.java,v 1.1 2007-10-11 01:10:22 gunter Exp $
 */
package net.six_two.program_guide.model;

import java.sql.Timestamp;

public class Program {
    private Integer id;
    private Boolean doUpdate;
    private Timestamp lastUpdate;
    private String name;
    private String url;
    
    public Program() {
        
    }
    
    public Program(Integer id, String name, String url, Timestamp lastUpdate,
            Boolean doUpdate) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.lastUpdate = lastUpdate;
        this.doUpdate = doUpdate;
    }
    
    public Boolean getDoUpdate() {
        return doUpdate;
    }

    public void setDoUpdate(Boolean doUpdate) {
        this.doUpdate = doUpdate;
    }
    
    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public Timestamp getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Timestamp lastUpdate) {
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
