/*
 * $Id: TorrentSite.java,v 1.1 2005-11-11 16:37:12 gunter Exp $
 */
package net.six_two.program_guide.tables;

public class TorrentSite {
    private int id;
    private String name;
    private String url;
    private String searchString;
    
    public TorrentSite() {
        
    }

    public TorrentSite(int id, String name, String searchString, String url) {
        super();
        this.id = id;
        this.name = name;
        this.searchString = searchString;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
