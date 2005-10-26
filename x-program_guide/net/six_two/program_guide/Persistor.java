/*
 * $Id: Persistor.java,v 1.16 2005-10-26 22:20:36 gunter Exp $
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
    /* episode table *********************************************************/
    public static UserEpisode[] selectAllEpisodesForUser(Connection connection,
            User user, Program program) throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        String sql = "SELECT e.*, "
            + "IFNULL(q.user_id, 0) AS queued, "
            + "IFNULL(v.user_id, 0) as viewed "
            + "FROM user u "
            + "LEFT JOIN subscribed s "
            + "ON u.id = s.user_id "
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
            + "WHERE u.id = ? "
            + "AND e.program_id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
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
    
    public static UserEpisode[] selectAllEpisodesForUser(Connection connection,
            User user) throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT p.*, e.*, "
            + "IFNULL(q.user_id, 0) AS queued, "
            + "IFNULL(v.user_id, 0) AS viewed "
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
                    result.getString("p.name"),
                    result.getString("p.url"),
                    result.getTimestamp("p.last_update"),
                    result.getShort("p.do_update"));
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
    
    public static UserEpisode[] selectAllEpisodesForUser(Connection connection,
            User user, int dayRange) throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT p.*, e.* "
            + "FROM user u "
            + "LEFT JOIN subscribed s "
            + "ON u.id = s.user_id "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "WHERE u.id = ? "
            + "AND original_air_date >= (CURRENT_DATE() - INTERVAL ? DAY) "
            + "AND original_air_date <= (CURRENT_DATE() + INTERVAL ? DAY) "
            + "ORDER BY e.original_air_date DESC";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, dayRange);
        statement.setInt(3, dayRange);
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Program program = new Program(result.getInt("p.id"),
                    result.getString("p.name"),
                    result.getString("p.url"),
                    result.getTimestamp("p.last_update"),
                    result.getShort("p.do_update"));
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season").charAt(0),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"));
            userEpisodes.add(new UserEpisode(program, episode, (short) -1, 
                    (short) -1));
        }
        result.close();
        statement.close();
        
        UserEpisode[] userEpisodesArray = new UserEpisode[userEpisodes.size()];
        for (int i = 0; i != userEpisodes.size(); i++) {
            userEpisodesArray[i] = (UserEpisode) userEpisodes.get(i);
        }
        
        return userEpisodesArray;
    }
    /* episode table *********************************************************/
    
    /* program table *********************************************************/
    public static Program selectProgram(Connection connection, int id) 
            throws SQLException {
        String sql = "SELECT * FROM program WHERE id = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        Program program = new Program();
        if (result.next()) {
            program.setId(result.getInt("id"));
            program.setName(result.getString("name"));
            program.setUrl(result.getString("url"));
            program.setLastUpdate(result.getTimestamp("last_update"));
            program.setDoUpdate(result.getShort("do_update"));
        }
        result.close();
        statement.close();
        
        return program;
    }
    
    public static Program[] selectAllPrograms(Connection connection) 
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
            program.setUrl(result.getString("url"));
            program.setLastUpdate(result.getTimestamp("last_update"));
            program.setDoUpdate(result.getShort("do_update"));
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
    
    public static Program[] selectAllProgramsForUser(Connection connection,
                User user) throws SQLException {
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT * "
            + "FROM subscribed s "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "WHERE user_id = ? "
            + "ORDER BY p.name";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        ArrayList programs = new ArrayList();
        while (result.next()) {
            Program program = new Program();
            program.setId(result.getInt("id"));
            program.setName(result.getString("name"));
            program.setUrl(result.getString("url"));
            program.setLastUpdate(result.getTimestamp("last_update"));
            program.setDoUpdate(result.getShort("do_update"));
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
    
    public static int updateProgram(Connection connection, Program program)
            throws SQLException {
        String sql = "UPDATE program "
                + "SET name = ?, "
                + "url = ?, "
                + "do_update = ? "
                + "WHERE id = ?";
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, program.getName());
        statement.setString(2, program.getUrl());
        statement.setShort(3, program.getDoUpdate());
        statement.setInt(4, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        statement.close();
        
        return count;
    }
    
    public static int deleteProgram(Connection connection, Program program)
            throws SQLException {
        String sql = "DELETE FROM program WHERE id = ?";
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        statement.close();
        
        count += deleteQueuedForProgram(connection, program);
        count += deleteViewedForProgram(connection, program);
        count += deleteSubscribedForProgram(connection, program);
        
        return count;
    }
    
    public static int insertProgram(Connection connection, Program program)
            throws SQLException {
        String sql = "INSERT INTO program VALUES (null, ?, ?, null, ?)";
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, program.getName());
        statement.setString(2, program.getUrl());
        statement.setShort(3, program.getDoUpdate());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        sql = "SELECT LAST_INSERT_ID()";
        
        statement.execute();
        ResultSet result = statement.getResultSet();
        result.next();
        program.setId(result.getInt(1));
        
        result.close();
        statement.close();
        
        return count;
    }
    
    public static ProgramSubscribed[] selectAllProgramsAndSubscribedForUser(
            Connection connection, User user) 
            throws SQLException {
        ArrayList subscriptions = new ArrayList();
        
        String sql = "SELECT p.*, IFNULL(s.program_id, 0) as subscribed "
            + "FROM program p "
            + "LEFT JOIN subscribed s "
            + "ON (p.id = s.program_id "
            + "    AND s.user_id = ?)";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.execute();;
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Program program = new Program();
            program.setId(result.getInt("p.id"));
            program.setName(result.getString("p.name"));
            program.setUrl(result.getString("p.url"));
            program.setLastUpdate(result.getTimestamp("p.last_update"));
            program.setDoUpdate(result.getShort("p.do_update"));
            short subscribed = result.getShort("subscribed");
            if (subscribed != 0) {
                /*
                 *  The query will return program id for subscribed, if it's not
                 * null.  Make that 1 for true.
                 */
                subscribed = 1;
            }            

            subscriptions.add(new ProgramSubscribed(program, subscribed));
        }
        result.close();
        statement.close();
        
        ProgramSubscribed[] subscriptionsArray = new ProgramSubscribed[subscriptions.size()];
        for (int i = 0; i != subscriptions.size(); i++) {
            subscriptionsArray[i] = (ProgramSubscribed) subscriptions.get(i);
        }
        
        return subscriptionsArray;
    }
    /* program table *********************************************************/
    
    /* queued table **********************************************************/
    public static int insertQueuedForUser(Connection connection, User user, 
            Episode episode) throws SQLException {
        String sql = "INSERT INTO queued VALUES (?, ?, ?, ?)";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (episode == null)
            throw new SQLException("Attempted operation with a null episode.");
        
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
    
    public static int deleteQueuedForUser(Connection connection, User user, 
            Episode episode) throws SQLException {
        String sql = "DELETE FROM queued "
            + "WHERE user_id = ? "
            + "AND program_id = ? "
            + "AND season = ? "
            + "AND episode_number = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (episode == null)
            throw new SQLException("Attempted operation with a null episode.");
        
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
    
    public static int deleteQueuedForUser(Connection connection, User user, 
            Program program) throws SQLException {
        String sql = "DELETE FROM queued "
            + "WHERE user_id = ? "
            + "AND program_id = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteAllQueuedForUser(Connection connection, User user) 
            throws SQLException {
        String sql = "DELETE FROM queued "
            + "WHERE user_id = ? ";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteQueuedForProgram(Connection connection, 
            Program program) throws SQLException {
        String sql = "DELETE FROM queued WHERE program_id = ? ";
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    /* queued table **********************************************************/
    
    /* subscribed table ******************************************************/    
    public static Subscribed[] selectSubscribedForUser(Connection connection,
            User user) throws SQLException {
        ArrayList subscriptions = new ArrayList();
        
        String sql = "SELECT * FROM subscribed "
            + "WHERE user_id = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
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
    
    public static int insertSubscribedForUser(Connection connection, 
            User user, Program program) throws SQLException {
        String sql = "INSERT INTO subscribed VALUES (?, ?)";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteSubscribedForUser(Connection connection, User user, 
            Program program) throws SQLException {
        String sql = "DELETE FROM subscribed "
            + "WHERE user_id = ? "
            + "AND program_id = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteAllSubscribedForUser(Connection connection, 
            User user) throws SQLException {
        String sql = "DELETE FROM subscribed "
            + "WHERE user_id = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteSubscribedForProgram(Connection connection, 
            Program program) throws SQLException {
        String sql = "DELETE FROM subscribed WHERE program_id = ? ";
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    /* subscribed table ******************************************************/
    
    /* user table ************************************************************/
    public static User selectUser(Connection connection, String username)
            throws SQLException {
        String sql = "SELECT * FROM user WHERE username = ?";

        if (username == null) 
            throw new SQLException("Attempted operation with a null"
                    + " username.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        User user = null;
        if (result.next()) {
            user = new User();
            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));
            user.setLastLoginDate(result.getTimestamp("last_login_date"));
            user.setRegistrationDate(result.getTimestamp("registration_date"));
            user.setLevel(result.getShort("level"));
        }
        
        result.close();
        statement.close();
        
        return user;
    }

    public static User selectUser(Connection connection, int id)
            throws SQLException {
        String sql = "SELECT * FROM user WHERE id = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        User user = null;
        if (result.next()) {
            user = new User();
            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));
            user.setLastLoginDate(result.getTimestamp("last_login_date"));
            user.setRegistrationDate(result.getTimestamp("registration_date"));
            user.setLevel(result.getShort("level"));
        }

        result.close();
        statement.close();

        return user;
    }
    
    public static User[] selectAllUsers(Connection connection) 
            throws SQLException {
        ArrayList users = new ArrayList();
        
        String sql = "SELECT * FROM user";

        Statement statement = connection.createStatement();
        statement.execute(sql);
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            User user = new User();
            user.setId(result.getInt("id"));
            user.setUsername(result.getString("username"));
            user.setPassword(result.getString("password"));
            user.setLastLoginDate(result.getTimestamp("last_login_date"));
            user.setRegistrationDate(result.getTimestamp("registration_date"));
            user.setLevel(result.getShort("level"));;
            
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
            + "registration_date = ?, "
            + "level = ? "
            + "WHERE id = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation on a null user.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setDate(3, new Date(user.getLastLoginDate().getTime()));
        statement.setDate(4, new Date(user.getRegistrationDate().getTime()));
        statement.setShort(5, user.getLevel());
        statement.setInt(6, user.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteUser(Connection connection, User user) 
            throws SQLException {
        String sql = "DELETE FROM user "
            + "WHERE id = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation on a null user.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        deleteAllSubscribedForUser(connection, user);
        deleteAllQueuedForUser(connection, user);
        deleteAllViewedForUser(connection, user);
        return count;
    }
    
    public static int insertUser(Connection connection, User user) 
            throws SQLException {
        String sql = "INSERT INTO user VALUES (null, ?, ?, ?, ?, ?)";
        
        if (user == null) 
            throw new SQLException("Attempted operation on a null user.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setDate(3, new Date(user.getLastLoginDate().getTime()));
        statement.setDate(4, new Date(user.getRegistrationDate().getTime()));
        statement.setShort(5, user.getLevel());
        
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
        
        result.close();
        statement.close();
        
        return count;
    }
    /* user table ************************************************************/
    
    /* viewed table **********************************************************/
    public static int insertViewedForUser(Connection connection, User user, 
            Episode episode) throws SQLException {
        String sql = "INSERT INTO viewed VALUES (?, ?, ?, ?)";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (episode == null)
            throw new SQLException("Attempted operation with a null episode");
        
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
    
    public static int deleteViewedForUser(Connection connection, User user, 
            Episode episode) throws SQLException {
        String sql = "DELETE FROM viewed "
            + "WHERE user_id = ? "
            + "AND program_id = ? "
            + "AND season = ? "
            + "AND episode_number = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (episode == null)
            throw new SQLException("Attempted operation with a null episode");
        
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
    
    public static int deleteViewedForUser(Connection connection, User user, 
            Program program) throws SQLException {
        String sql = "DELETE FROM viewed "
            + "WHERE user_id = ? "
            + "AND program_id = ?";
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (program == null)
            throw new SQLException("Attempted operation with a null program");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteAllViewedForUser(Connection connection, User user)
            throws SQLException {
        String sql = "DELETE FROM viewed "
            + "WHERE user_id = ? ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteViewedForProgram(Connection connection, 
            Program program) throws SQLException {
        String sql = "DELETE FROM viewed WHERE program_id = ? ";
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    /* viewed table **********************************************************/
}
