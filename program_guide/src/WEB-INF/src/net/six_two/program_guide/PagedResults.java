/*
 * $Id: PagedResults.java,v 1.1 2006-05-13 20:04:42 gunter Exp $
 */
package net.six_two.program_guide;

import net.six_two.program_guide.tables.UserEpisode;

public class PagedResults {
    private static final int PAGE_SIZE = 10;
    private UserEpisode[] results;
    private int previousIndex;
    private int nextIndex;
    
    public PagedResults(UserEpisode[] results, int startIndex) throws Exception {
        if (startIndex > (results.length - 1)) {
            throw new Exception("No results.");
        }
        
        int endIndex = startIndex + PAGE_SIZE;
        if (endIndex > (results.length - 1)) {
            endIndex = results.length - 1;
        }
        
        nextIndex = endIndex + 1;
        
        if (nextIndex > (results.length - 1)) {
            nextIndex = -1;
        }
        
        previousIndex = startIndex - PAGE_SIZE;
        if (previousIndex < 0)
            previousIndex = -1;
        
        this.results = new UserEpisode[startIndex - endIndex + 1];
        System.arraycopy(results, startIndex, this.results, 0, endIndex + 1);        
    }
    
    public int getPreviousIndex() {
        return previousIndex;
    }
    
    public int getNextIndex() {
        return nextIndex;
    }
    
    public UserEpisode[] getResults() {
        return results;
    }
}
