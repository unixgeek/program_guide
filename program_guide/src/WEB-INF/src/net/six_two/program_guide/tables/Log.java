/*
 * $Id: Log.java,v 1.1 2006-05-05 22:38:23 gunter Exp $
 */
package net.six_two.program_guide.tables;

import java.sql.Timestamp;

public class Log {
    int id;
    String source;
    Timestamp createDate;
    String content;
    
    public Log() {
        
    }
    
    public Log(int id, String source, Timestamp createDate, String content) {
        this.id = id;
        this.source = source;
        this.createDate = createDate;
        this.content = content;
    }

    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String message) {
        content = message;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
