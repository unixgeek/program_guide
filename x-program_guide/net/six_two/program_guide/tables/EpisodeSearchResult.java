/*
 * $Id$
 */
package net.six_two.program_guide.tables;

import java.text.NumberFormat;


public class EpisodeSearchResult {
    private String programName;
    private String episodeTitle;
    private double score;
    
    public EpisodeSearchResult(String programName, String episodeTitle, 
            double score) {
        this.programName = programName;
        this.episodeTitle = episodeTitle;
        this.score = score;
    }

    public String getEpisodeTitle() {
        return episodeTitle;
    }

    public String getProgramName() {
        return programName;
    }

    public double getScore() {
        return score;
    }
    
    public String getFormattedScore() {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
        formatter.setMinimumIntegerDigits(1);
        
        return formatter.format(score);
    }
}
