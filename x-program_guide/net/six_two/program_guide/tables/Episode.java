/*
 * $Id: Episode.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
 */
package net.six_two.program_guide.tables;

import java.util.Date;

public class Episode {
    private int programId;
    private char season;
    private int number;
    private String productionCode;
    private Date originalAirDate;
    private String title;
    
    public Episode() {
        
    }
    
    public Episode(int programId, char season, int number,
            String productionCode, Date originalAirDate, String title) {
        this.number = number;
        this.originalAirDate = originalAirDate;
        this.productionCode = productionCode;
        this.programId = programId;
        this.season = season;
        this.title = title;
    }
    
    public int getNumber() {
        return number;
    }
    
    public void setNumber(int number) {
        this.number = number;
    }
    
    public Date getOriginalAirDate() {
        return originalAirDate;
    }
    
    public void setOriginalAirDate(Date originalAirDate) {
        this.originalAirDate = originalAirDate;
    }
    
    public String getProductionCode() {
        return productionCode;
    }
    
    public void setProductionCode(String productionCode) {
        this.productionCode = productionCode;
    }
    
    public int getProgramId() {
        return programId;
    }
    public void setProgramId(int programId) {
        this.programId = programId;
    }
    
    public char getSeason() {
        return season;
    }
    
    public void setSeason(char season) {
        this.season = season;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
}
