/*
 * $Id: Episode.java,v 1.1 2007-10-11 01:10:22 gunter Exp $
 */
package net.six_two.program_guide.model;

import java.util.Date;

public class Episode {
    private Integer programId;
    private String season;
    private Integer number;
    private String productionCode;
    private Date originalAirDate;
    private String title;
    private Integer serialNumber;
    private String summaryUrl;

    public Episode() {

    }

    public Episode(Integer programId, String season, Integer number, String productionCode,
            Date originalAirDate, String title, Integer serialNumber, String summaryUrl) {
        this.number = number;
        this.originalAirDate = originalAirDate;
        this.productionCode = productionCode;
        this.programId = programId;
        this.season = season;
        this.title = title;
        this.serialNumber = serialNumber;
        this.summaryUrl = summaryUrl;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
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

    public Integer getProgramId() {
        return programId;
    }

    public void setProgramId(Integer programId) {
        this.programId = programId;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Integer serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSummaryUrl() {
        return summaryUrl;
    }

    public void setSummaryUrl(String url) {
        this.summaryUrl = url;
    }
}
