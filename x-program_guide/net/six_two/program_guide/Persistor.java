/*
 * $Id: Persistor.java,v 1.1 2005-10-16 05:02:59 gunter Exp $
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
        
        String sql = "SELECT p.name, e.season, e.number, e.production_code, "
            + "e.original_air_date, e.title, IFNULL(q.user_id, 0) AS queued, "
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
            Program program = new Program();
            program.setName(result.getString("name"));
            Episode episode = new Episode();
            episode.setSeason(result.getString("season").charAt(0));
            episode.setNumber(result.getInt("number"));
            episode.setProductionCode(result.getString("production_code"));
            episode.setOriginalAirDate(result.getDate("original_air_date"));
            episode.setTitle(result.getString("title"));
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
}
