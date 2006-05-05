/*
 * $Id$
 */
package net.six_two.program_guide;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;

import net.six_two.misc.ExternalProcess;
import net.six_two.program_guide.tables.Log;

public class ScrapeRunnable implements Runnable {
    private int programId;
    private File workingDirectory;
    private Connection connection;
    
    public ScrapeRunnable(Connection connection, File workingDirectory, 
            int programId) {
        this.connection = connection;
        this.programId = programId;
        this.workingDirectory = workingDirectory;
    }
    
    public void run() {
        String[] update = new String[4];
        update[0] = "/bin/sh";
        update[1] = workingDirectory.getAbsolutePath() + "/database_wrapper.sh";
        update[2] = workingDirectory.getAbsolutePath() + "/scrape.sh";
        update[3] = Integer.toString(programId);
        
        ExternalProcess process = new ExternalProcess(update, workingDirectory);
        
        String newLine = System.getProperty("line.separator");
        
        StringBuffer log = new StringBuffer();
        log.append("programId: ");
        log.append(programId);
        log.append(newLine);
        log.append("workingDirectory: ");
        log.append(workingDirectory);
        log.append(newLine);
        
        int exitStatus = -1;
        try {
            exitStatus = process.execute();
        } catch (IOException e) {
            log.append(e.getMessage());
        } catch (InterruptedException e) {
            log.append(e.getMessage());
        }
        
        log.append("exitStatus: ");
        log.append(exitStatus);
        log.append(newLine);
        log.append("==== stderr ====");
        log.append(newLine);
        log.append(new String(process.getStderr()));
        log.append(newLine);
        log.append("==== stdout ====");
        log.append(newLine);
        log.append(new String(process.getStdout()));
        
        Log logEntry = new Log();
        logEntry.setSource("ScrapeRunnable");
        logEntry.setCreateDate(new Timestamp(System.currentTimeMillis()));
        logEntry.setContent(log.toString());
        
        try {
            Persistor.createLogEntry(connection, logEntry);
            connection.close();
        } catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }
    }
}
