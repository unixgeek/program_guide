/*
 * $Id$
 */
package net.six_two.program_guide;

import java.text.NumberFormat;

public class Timer {
    private long start;
    private long stop;
    
    public Timer() {
        
    }
    
    public void start() {
        this.start = System.currentTimeMillis();
    }
    
    public void stop() {
        this.stop = System.currentTimeMillis();
    }
    
    public String getElapsedTime() {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMinimumFractionDigits(3);
        formatter.setMaximumFractionDigits(3);
        formatter.setMinimumIntegerDigits(1);
        
        double time = (((double) stop) - ((double) start)) / 1000d;
        String elapsedTime = formatter.format(time);
        
        return elapsedTime;
    }
}
