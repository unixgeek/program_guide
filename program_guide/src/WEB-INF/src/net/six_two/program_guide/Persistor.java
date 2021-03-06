/*
 * $Id: Persistor.java,v 1.8.2.1 2006-08-16 00:18:59 gunter Exp $
 */
package net.six_two.program_guide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.sql.Date;

import net.six_two.program_guide.tables.*;

public class Persistor {
    public static final int NATURAL_LANGUAGE_SEARCH = 0;
    public static final int BOOLEAN_SEARCH = 1;
    public static final int QUERY_EXPANSION_SEARCH = 2;
    
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
            + "FROM subscribed s "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (u.id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE s.user_id = ? "
            + "AND e.program_id = ? "
            + "ORDER BY program_id, serial_number DESC ";
        
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
                    result.getInt("e.serial_number"),
                    result.getString("e.summary_url"));
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
    
    public static UserEpisode[] selectEpisodesBySeasonForUser(Connection connection,
            User user, Program program, String season) throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        String sql = "SELECT e.*, "
            + "IFNULL(t.status, 'none') AS status "
            + "FROM subscribed s "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (s.user_id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE s.user_id = ? "
            + "AND e.program_id = ? "
            + "AND e.season = ? "
            + "ORDER BY e.season, e.number, e.original_air_date ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, program.getId());
        statement.setString(3, season);
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season"),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"),
                    result.getInt("e.serial_number"),
                    result.getString("e.summary_url"));
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
            + "FROM subscribed s "
            + "ON u.id = s.user_id "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (s.user_id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE s.user_id = ? "
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
                    result.getShort("p.do_update"),
                    result.getInt("p.tvmaze_id"),
                    result.getString("p.network"));
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season"),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"),
                    result.getInt("e.serial_number"),
                    result.getString("e.summary_url"));
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
            User user, Date fromDate, Date toDate) throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT p.*, e.*, "
            + "IFNULL(t.status, 'none') AS status "
            + "FROM subscribed s "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (s.user_id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE s.user_id = ? "
            + "AND original_air_date >= ? "
            + "AND original_air_date <= ? ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setDate(2, fromDate);
        statement.setDate(3, toDate);
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Program program = new Program(result.getInt("p.id"),
                    result.getString("p.name"),
                    result.getString("p.url"),
                    result.getTimestamp("p.last_update"),
                    result.getShort("p.do_update"),
                    result.getInt("p.tvmaze_id"),
                    result.getString("p.network"));
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season"),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"),
                    result.getInt("e.serial_number"),
                    result.getString("e.summary_url"));
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
            + "FROM subscribed s "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "WHERE s.user_id = ? ";
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
            User user, int startIndex, int length) throws SQLException {
        ArrayList userEpisodes = new ArrayList();
        
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT p.*, e.*, "
            + "IFNULL(st.status, 'none') AS status "    
            + "    FROM `user` u " 
            + "    LEFT JOIN subscribed s "  
            + "        ON u.id = s.user_id "  
            + "    LEFT JOIN program p " 
            + "        ON p.id = s.program_id "  
            + "    LEFT JOIN episode e " 
            + "        ON e.program_id = s.program_id "  
            + "    LEFT JOIN status st " 
            + "        ON st.user_id = u.id " 
            + "        AND st.program_id = e.program_id "  
            + "        AND st.season = e.season "  
            + "        AND st.episode_number = e.`number` " 
            + "WHERE u.id = ? " 
            + "AND (((st.status IS NULL OR st.status != 'viewed') " 
            + "       AND CURDATE() >= e.original_air_date) " 
            + "     OR st.status = 'queued') " 
            + "ORDER BY e.original_air_date ASC, p.name ASC "
            + "LIMIT ?,? ";
            
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, startIndex);
        statement.setInt(3, length);
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        while (result.next()) {
            Program program = new Program(result.getInt("p.id"),
                    result.getString("p.name"),
                    result.getString("p.url"),
                    result.getTimestamp("p.last_update"),
                    result.getShort("p.do_update"),
                    result.getInt("p.tvmaze_id"),
                    result.getString("p.network"));
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season"),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"),
                    result.getInt("e.serial_number"),
                    result.getString("e.summary_url"));
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
                + "    FROM `user` u " 
                + "    LEFT JOIN subscribed s "  
                + "        ON u.id = s.user_id "  
                + "    LEFT JOIN program p " 
                + "        ON p.id = s.program_id "  
                + "    LEFT JOIN episode e " 
                + "        ON e.program_id = s.program_id "  
                + "    LEFT JOIN status st " 
                + "        ON st.user_id = u.id " 
                + "        AND st.program_id = e.program_id "  
                + "        AND st.season = e.season "  
                + "        AND st.episode_number = e.`number` " 
                + "WHERE u.id = ? " 
                + "AND (((st.status IS NULL OR st.status != 'viewed') " 
                + "       AND CURDATE() >= e.original_air_date) " 
                + "     OR st.status = 'queued') " ;
            
        
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
    
    public static UserEpisode[] searchEpisodes(Connection connection,
            String query, int searchType, User user) throws SQLException {
        if (query == null)
            throw new SQLException("Attempted operation with a null query.");
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        if ((searchType != BOOLEAN_SEARCH) 
                && (searchType != NATURAL_LANGUAGE_SEARCH)
                && (searchType != QUERY_EXPANSION_SEARCH)) {
            throw new SQLException("Invalid search type.");
        }
        
        String type;
        switch (searchType) {
            case BOOLEAN_SEARCH:
                type = " IN BOOLEAN MODE";
                break;
            case QUERY_EXPANSION_SEARCH:
                type = " WITH QUERY EXPANSION";
                break;
            case NATURAL_LANGUAGE_SEARCH:
            default:
                type = "";
                break;
        }
        
        String sql = "SELECT p.*, e.*, "
            + "IFNULL(t.status, 'none') AS status, "
            + "MATCH(e.title) AGAINST(?" + type + ") AS score "
            + "FROM subscribed s "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "LEFT JOIN episode e "
            + "ON s.program_id = e.program_id "
            + "LEFT JOIN status t "
            + "ON (s.user_id = t.user_id "
            + "    AND t.program_id = e.program_id "
            + "    AND t.season = e.season "
            + "    AND t.episode_number = e.number) "
            + "WHERE s.user_id = ? "
            + "AND MATCH(e.title) AGAINST(?" + type + ") "
            + "ORDER BY score DESC ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, query);
        statement.setInt(2, user.getId());
        statement.setString(3, query);
        
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        ArrayList userEpisodes = new ArrayList();
        while (result.next()) {
            Program program = new Program(result.getInt("p.id"),
                    result.getString("p.name"),
                    result.getString("p.url"),
                    result.getTimestamp("p.last_update"),
                    result.getShort("p.do_update"),
                    result.getInt("p.tvmaze_id"),
                    result.getString("p.network"));
            Episode episode = new Episode(result.getInt("e.program_id"),
                    result.getString("e.season"),
                    result.getInt("e.number"),
                    result.getString("e.production_code"),
                    result.getDate("e.original_air_date"),
                    result.getString("e.title"),
                    result.getInt("e.serial_number"),
                    result.getString("e.summary_url"));
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
    
    public static String[] selectSeasonsForProgram(Connection connection,
            Program program) throws SQLException {
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        String sql = "SELECT DISTINCT season AS season "
            + "FROM episode "
            + "WHERE program_id = ? "
            + "ORDER BY 0+season ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, program.getId());
        statement.execute();
        
        ResultSet result = statement.getResultSet();
        
        ArrayList seasons = new ArrayList();
        
        while (result.next()) {
            String season = result.getString("season");
            
            seasons.add(season);
        }
        result.close();
        statement.close();
        
        String[] seasonsArray = new String[seasons.size()];
        for (int i = 0; i != seasons.size(); i++) {
            seasonsArray[i] = (String) seasons.get(i);
        }
        
        return seasonsArray;
    }
    
    public static int deleteEpisodesForProgram(Connection connection, 
            Program program) throws SQLException {
        String sql = "DELETE FROM episode where program_id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, program.getId());
        
        statement.executeUpdate();
        int count = statement.getUpdateCount();
        
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
            program.setTvMazeId(result.getInt("tvmaze_id"));
            program.setNetwork(result.getString("network"));
        }
        result.close();
        statement.close();
        
        return program;
    }
    
    public static String[] selectProgramPrefixes(Connection connection) 
            throws SQLException {
        
        String sql = "SELECT DISTINCT SUBSTRING(name, 1, 1) AS prefix "
            + "FROM program "
            + "ORDER BY prefix ";
        
        Statement statement = connection.createStatement();
        statement.execute(sql);
        ResultSet result = statement.getResultSet();
        
        ArrayList prefixes = new ArrayList();
        while (result.next()) {
            prefixes.add(result.getString("prefix"));
        }
        result.close();
        statement.close();
        
        String[] prefixesArray = new String[prefixes.size()];
        for (int i = 0; i != prefixes.size(); i++) {
            prefixesArray[i] = (String) prefixes.get(i);
        }
        
        return prefixesArray;
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
 
    public static Program[] selectAllPrograms(Connection connection,
            String prefix) throws SQLException {
        
        String sql = "SELECT * "
            + "FROM program p "
            + "WHERE SUBSTRING(name, 1, 1) = ? "
            + "ORDER BY name ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, prefix);
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
            program.setTvMazeId(result.getInt("tvmaze_id"));
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
        
        String sql = "SELECT p.* "
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

    public static Program[] selectAllProgramsForUser(Connection connection,
            User user, String prefix) throws SQLException {
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT p.* "
            + "FROM subscribed s "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "WHERE s.user_id = ? "
            + "AND SUBSTRING(name, 1, 1) = ? "
            + "ORDER BY p.name ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setString(2, prefix);
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
    
    public static String[] selectProgramPrefixesForUser(Connection connection,
            User user) throws SQLException {
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT DISTINCT SUBSTRING(p.name, 1, 1) AS prefix "
            + "FROM subscribed s "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "WHERE s.user_id = ? "
            + "ORDER BY prefix ";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        ArrayList prefixes = new ArrayList();
        while (result.next()) {
            prefixes.add(result.getString("prefix"));
        }
        result.close();
        statement.close();
        
        String[] prefixesArray = new String[prefixes.size()];
        for (int i = 0; i != prefixes.size(); i++) {
            prefixesArray[i] = (String) prefixes.get(i);
        }
        
        return prefixesArray;
    }
    
    public static int selectProgramCountForUser(Connection connection,
            User user) throws SQLException {
        if (user == null)
            throw new SQLException("Attempted operation with a null user.");
        
        String sql = "SELECT COUNT(*) "
            + "FROM subscribed s "
            + "LEFT JOIN program p "
            + "ON s.program_id = p.id "
            + "WHERE user_id = ? ";
        
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
        count += deleteEpisodesForProgram(connection, program);
        
        return count;
    }
    
    public static int insertProgram(Connection connection, Program program)
            throws SQLException {
        String sql = "INSERT INTO program (name, tvmaze_id, do_update, url)  VALUES (?, ?, ?, 'TBD')";
        
        if (program == null)
            throw new SQLException("Attempted operation with a null program.");
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, program.getName());
        statement.setInt(2, program.getTvMazeId());
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
    
    public static int deleteStatusForEpisode(Connection connection, 
            User user, Episode episode) 
            throws SQLException {
        String sql = "DELETE FROM status "
            + "WHERE user_id = ? "
            + "AND program_id = ? "
            + "AND season = ? "
            + "AND episode_number = ?";
                
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, user.getId());
        statement.setInt(2, episode.getProgramId());
        statement.setString(3, episode.getSeason());
        statement.setInt(4, episode.getNumber());
        
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
    public static Log[] getLogEntries(Connection connection, int startIndex,
            int length) throws SQLException {
        String sql = "SELECT id, source, create_date FROM log LIMIT ?,?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, startIndex);
        statement.setInt(2, length);
        
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        ArrayList logEntries = new ArrayList();
        Log log;
        while (result.next()) {
            log = new Log(result.getInt("id"), result.getString("source"), 
                    result.getTimestamp("create_date"), 
                    null);
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
    
    public static int getLogCount(Connection connection) throws SQLException {
        String sql = "SELECT COUNT(*) FROM log";
        
        Statement statement = connection.createStatement();
        
        statement.execute(sql);
        ResultSet result = statement.getResultSet();
        
        int count = 0;
        if (result.next()) {
            count = result.getInt(1);
        }
        
        result.close();
        statement.close();
        
        return count;
    }
    
    public static Log selectLogEntry(Connection connection, int id)
            throws SQLException {
        String sql = "SELECT * FROM log WHERE id = ?";
        
        PreparedStatement statement = connection.prepareStatement(sql);
        
        statement.setInt(1, id);
        statement.execute();
        ResultSet result = statement.getResultSet();
        
        Log log = null;
        byte[] buffer = new byte[1024];
        if (result.next()) {
            // Decompress the log content.
            String content = "";
            InputStream inputStream = null;
            GZIPInputStream gzipStream = null;
            try {
                inputStream = result.getBinaryStream("content");
                gzipStream = new GZIPInputStream(inputStream);
                int read = 0;
                while ((read = gzipStream.read(buffer, 0, 1024)) != -1) {
                    byte[] data = new byte[read];
                    System.arraycopy(buffer, 0, data, 0, read);
                    content += new String(data);
                }
                gzipStream.close();
                inputStream.close();
            } catch (IOException e) {
                try {
                    if (inputStream != null)
                        inputStream.close();
                    if (gzipStream != null)
                        gzipStream.close();
                } catch (IOException e1) {
                }
                result.close();
                statement.close();
                throw new SQLException(e.getMessage());
            }
            log = new Log(result.getInt("id"), result.getString("source"), 
                    result.getTimestamp("create_date"), 
                    content);
        }
        
        result.close();
        statement.close();
        
        return log;
    }
    
    public static void createLogEntry(Connection connection, Log entry) 
            throws SQLException {
        String sql = "INSERT INTO log VALUES (null, ?, ?, ?)";
        
        // Compress the log content.
        ByteArrayOutputStream outputStream = null;
        GZIPOutputStream gzipStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            gzipStream = new GZIPOutputStream(outputStream);
            gzipStream.write(entry.getContent().getBytes());
            gzipStream.close();
        } catch (IOException e) {
            try {
                if (gzipStream != null)
                    gzipStream.close();
            } catch (IOException e1) {
            }
            throw new SQLException(e.getMessage());
        }
            
        PreparedStatement statement = connection.prepareStatement(sql);
        
        statement.setString(1, entry.getSource());
        statement.setTimestamp(2, entry.getCreateDate());
        statement.setBytes(3, outputStream.toByteArray());
        statement.execute();
        statement.close();
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
