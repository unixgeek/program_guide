/*
 * $Id: Episode.java,v 1.2 2005-10-28 18:26:49 gunter Exp $
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
    private int serialNumber;
    
    public Episode() {
        
    }
    
    public Episode(int programId, char season, int number,
            String productionCode, Date originalAirDate, String title,
            int serialNumber) {
        this.number = number;
        this.originalAirDate = originalAirDate;
        this.productionCode = productionCode;
        this.programId = programId;
        this.season = season;
        this.title = title;
        this.serialNumber = serialNumber;
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

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
