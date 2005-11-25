import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.Episode;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.Subscribed;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

/*
 * $Id: ProgramGuideViewer.java,v 1.6 2005-10-22 22:46:35 gunter Exp $
 */

public class ProgramGuideViewer {
    public static void main(String[] args) {
        String dbUsername = args[0];
        String dbPassword = args[1];
        String pgUsername = args[2];
        String pgPassword = args[3];
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("E MMM d HH:mm:ss z yyyy");
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://outlands.six-two.net/program_guide",
                    dbUsername, dbPassword);
            
            User user = Persistor.selectUser(connection, pgUsername);
            
            if (!UserManager.authenticateUser(user, pgPassword)) {
                System.out.println("Invalid password.");
                System.exit(1);
            }
            
            /*UserEpisode[] episodes = Persistor.getUserEpisodes(connection, user);
            for (int i = 0; i != episodes.length; i++) {
                System.out.print(episodes[i].getProgram().getName() + " | ");
                System.out.print(episodes[i].getEpisode().getSeason() + " | ");
                System.out.print(episodes[i].getEpisode().getNumber() + " | ");
                System.out.print(episodes[i].getEpisode().getProductionCode() + " | ");
                System.out.print(episodes[i].getEpisode().getOriginalAirDate() + " | ");
                System.out.print(episodes[i].getEpisode().getTitle() + " | ");
                System.out.print(episodes[i].getQueued() + " | ");
                System.out.println(episodes[i].getViewed());
            }*/
            
            Program[] programs = Persistor.selectAllPrograms(connection);
            for (int i = 0; i != programs.length; i++) {
//                System.out.println(programs[i].getId() + "\t" 
//                        + programs[i].getName() + "\t"
//                        + dateFormat.format(programs[i].getLastUpdate()) + "\t"
//                        + programs[i].getDoUpdate());
                System.out.println(dateFormat.format(programs[i].getLastUpdate()));
            }
            
            /* SUBSCRIPTION TEST */
            /*Subscribed[] subscriptions;
            System.out.println("Subscriptions");
            subscriptions = Persistor.getSubscribed(connection, user);
            for (int i = 0; i != subscriptions.length; i++) {
                System.out.println(subscriptions[i].getUserId() + " | " 
                        + subscriptions[i].getProgramId());
            }
            Persistor.addSubscription(connection, user, programs[4]);
            System.out.println("Subscriptions (Add)");
            subscriptions = Persistor.getSubscribed(connection, user);
            for (int i = 0; i != subscriptions.length; i++) {
                System.out.println(subscriptions[i].getUserId() + " | " 
                        + subscriptions[i].getProgramId());
            }
            Persistor.deleteSubscription(connection, user, programs[4]);
            System.out.println("Subscriptions (Delete)");
            subscriptions = Persistor.getSubscribed(connection, user);
            for (int i = 0; i != subscriptions.length; i++) {
                System.out.println(subscriptions[i].getUserId() + " | " 
                        + subscriptions[i].getProgramId());
            }*/
            
            /* VIEWED / QUEUED TEST */
            //Episode episode = episodes[7].getEpisode();
            //Persistor.addUserQueuedEpisode(connection, user, episode);
            //Episode episode = episodes[7].getEpisode();
            //Persistor.deleteUserQueuedEpisode(connection, user, episode);
            
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}