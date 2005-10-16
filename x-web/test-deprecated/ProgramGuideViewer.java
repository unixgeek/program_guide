import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;
import net.six_two.program_guide.tables.UserEpisode;

/*
 * $Id: ProgramGuideViewer.java,v 1.1 2005-10-16 05:39:57 gunter Exp $
 */

public class ProgramGuideViewer {
    public static void main(String[] args) {
        String dbUsername = args[0];
        String dbPassword = args[1];
        String pgUsername = args[2];
        String pgPassword = args[3];
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://outlands.six-two.net/program_guide",
                    dbUsername, dbPassword);
            
            User user = Persistor.getUser(connection, pgUsername);
            
            if (!UserManager.authenticateUser(user, pgPassword)) {
                System.out.println("Invalid password.");
                System.exit(1);
            }
            
            UserEpisode[] episodes = Persistor.getUserEpisodes(connection, user);
            for (int i = 0; i != episodes.length; i++) {
                System.out.print(episodes[i].getProgram().getName() + " | ");
                System.out.print(episodes[i].getEpisode().getSeason() + " | ");
                System.out.print(episodes[i].getEpisode().getNumber() + " | ");
                System.out.print(episodes[i].getEpisode().getProductionCode() + " | ");
                System.out.print(episodes[i].getEpisode().getOriginalAirDate() + " | ");
                System.out.print(episodes[i].getEpisode().getTitle() + " | ");
                System.out.print(episodes[i].getQueued() + " | ");
                System.out.println(episodes[i].getViewed());
            }
            
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
