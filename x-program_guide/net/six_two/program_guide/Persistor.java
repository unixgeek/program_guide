/*
 * $Id: Persistor.java,v 1.2 2005-10-16 21:13:43 gunter Exp $
 */
package net.six_two.program_guide;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import net.six_two.program_guide.tables.*;

public class Persistor {
    public static User[] getAllUsers(Connection connection) 
            throws SQLException {
        ArrayList users = new ArrayList();
        
        String sql = "SELECT * FROM user";

        Statement statement = connection.createStatement();
        statement.execute(sql);
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            User user = new User();
            user.setId(result.getInt(1));
            user.setUsername(result.getString(2));
            user.setPassword(result.getString(3));
            user.setLastLoginDate(result.getDate(4));
            user.setRegistrationDate(result.getDate(5));
            
            users.add(user);
        }
        result.close();
        statement.close();
        
        User[] usersArray = new User[users.size()];
        for (int i = 0; i != users.size(); i++) {
            usersArray[i] = (User) users.get(i);
        }
        
        return usersArray;
    }
    
    public static int updateUser(Connection connection, User user) 
            throws SQLException {
        String sql = "UPDATE user "
            + "SET username = ?, "
            + "password = ?, "
            + "last_login_date = ?, "
            + "registration_date = ? "
            + "WHERE id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setDate(3, new Date(user.getLastLoginDate().getTime()));
        statement.setDate(4, new Date(user.getRegistrationDate().getTime()));
        statement.setInt(5, user.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteUser(Connection connection, User user) 
            throws SQLException {
        String sql = "DELETE FROM user "
            + "WHERE id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int addUser(Connection connection, User user) 
            throws SQLException {
        String sql = "INSERT INTO user VALUES (null, ?, ?, ?, ?)";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setDate(3, new Date(user.getLastLoginDate().getTime()));
        statement.setDate(4, new Date(user.getRegistrationDate().getTime()));
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        sql = "SELECT id FROM user WHERE username = ?";
        
        statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        result.next();
        user.setId(result.getInt(1));
        
        statement.close();
        
        return count;
    }
    
    public static User getUser(Connection connection, String username)
        throws SQLException {
            String sql = "SELECT * FROM user WHERE username = ?";

            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.execute();
            User user = new User();
            ResultSet result = statement.getResultSet();
            if (result.next()) {
                user.setId(result.getInt(1));
                user.setUsername(result.getString(2));
                user.setPassword(result.getString(3));
                user.setLastLoginDate(result.getDate(4));
                user.setRegistrationDate(result.getDate(5));
            }
            
            result.close();
            statement.close();
            
            return user;
    }
    
    public static User getUser(Connection connection, int id)
            throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        User user = new User();
        ResultSet result = statement.getResultSet();
        if (result.next()) {
            user.setId(result.getInt(1));
            user.setUsername(result.getString(2));
            user.setPassword(result.getString(3));
            user.setLastLoginDate(result.getDate(4));
            user.setRegistrationDate(result.getDate(5));
        }
        
        result.close();
        statement.close();
        
        return user;
    }
    
    public static UserEpisode[] getUserEpisodes(Connection connection, User user)
            throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        String sql = "SELECT p.*, e.*, "
            + "IFNULL(q.user_id, 0) AS queued, "
            + "IFNULL(v.user_id, 0) as viewed "
            + "FROM user u "
            + "LEFT JOIN subscribed s "
            + "ON u.id = s.user_id "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN queued q "
            + "ON (u.id = q.user_id "
            + "    AND q.program_id = e.program_id "
            + "    AND q.season = e.season "
            + "    AND q.episode_number = e.number) "
            + "LEFT JOIN viewed v "
            + "ON (u.id = v.user_id "
            + "    AND v.program_id = e.program_id "
            + "    AND v.season = e.season "
            + "    AND v.episode_number = e.number) "
            + "WHERE u.id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Program program = new Program(result.getInt("p.id"),
                    result.getString("p.name"));
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season").charAt(0),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"));
            userEpisodes.add(new UserEpisode(program, episode, 
                    result.getShort("queued"), result.getShort("viewed")));
        }
        result.close();
        statement.close();
        
        UserEpisode[] userEpisodesArray = new UserEpisode[userEpisodes.size()];
        for (int i = 0; i != userEpisodes.size(); i++) {
            userEpisodesArray[i] = (UserEpisode) userEpisodes.get(i);
        }
        
        return userEpisodesArray;
    }
    
    public static Program[] getAllPrograms(Connection connection) 
            throws SQLException {
        ArrayList programs = new ArrayList();
        
        String sql = "SELECT * FROM program";
        
        Statement statement = connection.createStatement();
        statement.execute(sql);
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Program program = new Program();
            program.setId(result.getInt("id"));
            program.setName(result.getString("name"));
            
            programs.add(program);
        }
        result.close();
        statement.close();
        
        Program[] programsArray = new Program[programs.size()];
        for (int i = 0; i != programs.size(); i++) {
            programsArray[i] = (Program) programs.get(i);
        }
        
        return programsArray;
    }
    
    public static Subscribed[] getSubscribed(Connection connection, User user) 
            throws SQLException {
        
        ArrayList subscriptions = new ArrayList();
        
        String sql = "SELECT * FROM subscribed "
            + "WHERE user_id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.execute();;
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Subscribed subscribed = new Subscribed();
            subscribed.setUserId(result.getInt("user_id"));
            subscribed.setProgramId(result.getInt("program_id"));
            
            subscriptions.add(subscribed);
        }
        result.close();
        statement.close();
        
        Subscribed[] subscriptionsArray = new Subscribed[subscriptions.size()];
        for (int i = 0; i != subscriptions.size(); i++) {
            subscriptionsArray[i] = (Subscribed) subscriptions.get(i);
        }
        
        return subscriptionsArray;
    }
    
    public static int addSubscription(Connection connection, User user, 
            Program program) throws SQLException {
        String sql = "INSERT INTO subscribed VALUES (?, ?)";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteSubscription(Connection connection, User user, 
            Program program) throws SQLException {
        String sql = "DELETE FROM subscribed "
            + "WHERE user_id = ? "
            + "AND program_id = ? ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int addUserQueuedEpisode(Connection connection, User user, 
            Episode episode) throws SQLException {
        String sql = "INSERT INTO queued VALUES (?, ?, ?, ?)";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, episode.getProgramId());
        statement.setString(3, Character.toString(episode.getSeason()));
        statement.setInt(4, episode.getNumber());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteUserQueuedEpisode(Connection connection, User user, 
            Episode episode) throws SQLException {
        String sql = "DELETE FROM viewed "
            + "WHERE user_id = ? "
            + "AND program_id = ? "
            + "AND season = ? "
            + "AND episode_number = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, episode.getProgramId());
        statement.setString(3, Character.toString(episode.getSeason()));
        statement.setInt(4, episode.getNumber());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int addUserViewedEpisode(Connection connection, User user, 
            Episode episode) throws SQLException {
        String sql = "INSERT INTO queued VALUES (?, ?, ?, ?)";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, episode.getProgramId());
        statement.setString(3, Character.toString(episode.getSeason()));
        statement.setInt(4, episode.getNumber());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteUserViewedEpisode(Connection connection, User user, 
            Episode episode) throws SQLException {
        String sql = "DELETE FROM viewed "
            + "WHERE user_id = ? "
            + "AND program_id = ? "
            + "AND season = ? "
            + "AND episode_number = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, episode.getProgramId());
        statement.setString(3, Character.toString(episode.getSeason()));
        statement.setInt(4, episode.getNumber());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
}
