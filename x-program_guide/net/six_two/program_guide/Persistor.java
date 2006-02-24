/*
 * $Id: Persistor.java,v 1.36 2006-02-24 21:50:09 gunter Exp $
 */
package net.six_two.program_guide;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
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
            + "IFNULL(t.status, 'none') AS status "
            + "FROM user u "
            + "LEFT JOIN subscribed s "
            + "ON u.id = s.user_id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (u.id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE u.id = ? "
            + "AND e.program_id = ? "
            + "ORDER BY program_id, serial_number DESC";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season"),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"),
                    result.getInt("e.serial_number"));
            String status = result.getString("status");
            
            userEpisodes.add(new UserEpisode(program, episode, status));
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
            User user, int fromDay, int toDay) throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT p.*, e.*, "
            + "IFNULL(t.status, 'none') AS status "
            + "FROM user u "
            + "LEFT JOIN subscribed s "
            + "ON u.id = s.user_id "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (u.id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE u.id = ? "
            + "AND original_air_date >= (CURRENT_DATE() + INTERVAL ? DAY) "
            + "AND original_air_date <= (CURRENT_DATE() + INTERVAL ? DAY) "
            + "ORDER BY e.original_air_date DESC";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, fromDay);
        statement.setInt(3, toDay);
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Program program = new Program(result.getInt("p.id"),
                    result.getString("p.name"),
                    result.getString("p.url"),
                    result.getTimestamp("p.last_update"),
                    result.getShort("p.do_update"));
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season"),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"),
                    result.getInt("e.serial_number"));
            String status = result.getString("status");
            userEpisodes.add(new UserEpisode(program, episode, status));
        }
        result.close();
        statement.close();
        
        UserEpisode[] userEpisodesArray = new UserEpisode[userEpisodes.size()];
        for (int i = 0; i != userEpisodes.size(); i++) {
            userEpisodesArray[i] = (UserEpisode) userEpisodes.get(i);
        }
        
        return userEpisodesArray;
    }
    
    public static int selectEpisodeCountForUser(Connection connection,
            User user, int fromDay, int toDay) throws SQLException {
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT COUNT(*) "
            + "FROM user u "
            + "LEFT JOIN subscribed s "
            + "ON u.id = s.user_id "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (u.id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE u.id = ? ";
        if (fromDay >= 0)
            sql += "AND original_air_date >= (CURRENT_DATE() + INTERVAL ? DAY) "
                 + "AND original_air_date <= (CURRENT_DATE() + INTERVAL ? DAY) ";
        else
            sql += "AND original_air_date <= (CURRENT_DATE() + INTERVAL ? DAY) "
                 + "AND original_air_date >= (CURRENT_DATE() + INTERVAL ? DAY) ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, fromDay);
        statement.setInt(3, toDay);
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        int count = 0;
        if (result.next()) {
            count = result.getInt(1);
        }
        result.close();
        statement.close();
        
        return count;
    }
    
    public static UserEpisode[] selectAllQueuedEpisodesForUser(Connection connection,
            User user) throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT p.*, e.*, "
            + "IFNULL(t.status, 'none') AS status "
            + "FROM user u "
            + "LEFT JOIN subscribed s "
            + "ON u.id = s.user_id "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (u.id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE u.id = ? "
            + "AND status = 'queued' "
            + "ORDER BY p.name, e.serial_number DESC";
            
        
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
                    result.getString("e.season"),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"),
                    result.getInt("e.serial_number"));
            String status = result.getString("status");
            userEpisodes.add(new UserEpisode(program, episode, status));
        }
        result.close();
        statement.close();
        
        UserEpisode[] userEpisodesArray = new UserEpisode[userEpisodes.size()];
        for (int i = 0; i != userEpisodes.size(); i++) {
            userEpisodesArray[i] = (UserEpisode) userEpisodes.get(i);
        }
        
        return userEpisodesArray;
    }
    
    public static int selectQueuedEpisodeCountForUser(Connection connection,
            User user) throws SQLException {
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT COUNT(*) "
            + "FROM user u "
            + "LEFT JOIN subscribed s "
            + "ON u.id = s.user_id "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (u.id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE u.id = ? "
            + "AND IFNULL(t.status, 'none') = 'queued'";
            
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        int count = 0;
        if (result.next()) {
            count = result.getInt(1);
        }
        result.close();
        statement.close();
        
        return count;
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
        
        String sql = "SELECT * "
            + "FROM program "
            + "ORDER BY name";
        
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
    
    public static int selectProgramCountForUser(Connection connection,
            User user) throws SQLException {
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT COUNT(*) "
            + "FROM subscribed s "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "WHERE user_id = ? "
            + "ORDER BY p.name";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        int count = 0;
        if (result.next()) {
            count = result.getInt(1);
        }
        result.close();
        statement.close();
        
        return count;
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
        
        count += deleteStatusForProgram(connection, program);
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
        
        statement = connection.prepareStatement(sql);
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
            + "    AND s.user_id = ?) "
            + "ORDER BY p.name";
        
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
    
    /* status table **********************************************************/
    public static int insertStatusForUser(Connection connection, User user, 
            Episode episode, String status) throws SQLException {
        String sql = "INSERT INTO status VALUES (?, ?, ?, ?, ?, ?)";
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (episode == null)
            throw new SQLException("Attempted operation with a null episode.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, episode.getProgramId());
        statement.setString(3, episode.getSeason());
        statement.setInt(4, episode.getNumber());
        statement.setString(5, status);
        statement.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    
    public static int deleteStatusForUser(Connection connection, User user, 
            Program program) throws SQLException {
        String sql = "DELETE FROM status "
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
    
    public static int deleteAllStatusForUser(Connection connection, User user) 
            throws SQLException {
        String sql = "DELETE FROM status "
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
    
    public static int deleteStatusForProgram(Connection connection, 
            Program program) throws SQLException {
        String sql = "DELETE FROM status WHERE program_id = ? ";
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, program.getId());
        
        statement.execute();
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    /* status table **********************************************************/
    
    /* subscribed table ******************************************************/    
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
    
    /* torrent_site table ****************************************************/
    public static TorrentSite selectTorrentSite(Connection connection) 
            throws SQLException {
        String sql = "SELECT * FROM torrent_site";
        Statement statement = connection.createStatement();
        
        statement.execute(sql);
        ResultSet result = statement.getResultSet();
        
        TorrentSite site = null;
        if (result.next()) {
            site = new TorrentSite(result.getInt("id"), 
                    result.getString("name"),
                    result.getString("search_string"),
                    result.getString("url"));
        }
        
        return site;
    }
    /* torrent_site table ****************************************************/
    
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
            user.setPermissions(result.getInt("permissions"));
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
            user.setPermissions(result.getInt("permissions"));
        }

        result.close();
        statement.close();

        return user;
    }
    
    public static User[] selectAllUsers(Connection connection) 
            throws SQLException {
        ArrayList users = new ArrayList();
        
        String sql = "SELECT * "
            + "FROM user "
            + "ORDER BY username";

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
            user.setPermissions(result.getInt("permissions"));;
            
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
            + "permissions = ? "
            + "WHERE id = ?";
        
        if (user == null)
            throw new SQLException("Attempted operation on a null user.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());
        statement.setTimestamp(3, user.getLastLoginDate());
        statement.setTimestamp(4, user.getRegistrationDate());
        statement.setInt(5, user.getPermissions());
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
        deleteAllStatusForUser(connection, user);
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
        statement.setTimestamp(3, user.getLastLoginDate());
        statement.setTimestamp(4, user.getRegistrationDate());
        statement.setInt(5, user.getPermissions());
        
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
    
    /* log table *************************************************************/
    public static Log[] selectAllLogEntries(Connection connection)
            throws SQLException {
        String sql = "SELECT * FROM log";
        
        Statement statement = connection.createStatement();
        
        statement.execute(sql);
        ResultSet result = statement.getResultSet();
        
        ArrayList logEntries = new ArrayList();
        Log log;
        while (result.next()) {
            log = new Log(result.getInt("id"), result.getString("source"), 
                    result.getTimestamp("create_date"), 
                    result.getString("content"));
            logEntries.add(log);
        }
        
        result.close();
        statement.close();
        
        Log[] logEntriesArray = new Log[logEntries.size()];
        for (int i = 0; i != logEntries.size(); i++) {
            logEntriesArray[i] = (Log) logEntries.get(i);
        }
        
        return logEntriesArray;
    }
    
    public static Log selectLogEntry(Connection connection, int id)
            throws SQLException {
        String sql = "SELECT * FROM log WHERE id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        
        statement.setInt(1, id);
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        Log log = null;
        if (result.next()) {
            log = new Log(result.getInt("id"), result.getString("source"), 
                    result.getTimestamp("create_date"), 
                    result.getString("content"));
        }
        
        result.close();
        statement.close();
        
        return log;
    }
    
    public static int deleteLog(Connection connection)
            throws SQLException {
        String sql = "DELETE FROM log";
        
        Statement statement = connection.createStatement();
        
        statement.execute(sql);
        int count = statement.getUpdateCount();
        
        statement.close();
        
        return count;
    }
    /* log table *************************************************************/
}
