/*
 * $Id: Program.java,v 1.1 2006-05-05 22:38:23 gunter Exp $
 */
package net.six_two.program_guide.tables;

import java.sql.Timestamp;

public class Program {
    private short doUpdate;
    private int id;
    private Timestamp lastUpdate;
    private String name;
    private String url;
    private int tvMazeId;
    private String network;
    
    public Program() {
        
    }
    
    public Program(int id, String name, String url, Timestamp lastUpdate,
            short doUpdate, int tvMazeId, String network) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.lastUpdate = lastUpdate;
        this.doUpdate = doUpdate;
        this.network = network;
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

    public int getTvMazeId() {
        return tvMazeId;
    }

    public void setTvMazeId(int tvMazeId) {
        this.tvMazeId = tvMazeId;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}
