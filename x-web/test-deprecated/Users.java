import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;

import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.User;

/*
 * $Id: Users.java,v 1.3 2005-10-22 22:46:35 gunter Exp $
 */

public class Users {
    public static void main(String[] args) {
        String dbUsername = args[0];
        String dbPassword = args[1];
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://outlands.six-two.net/program_guide",
                    dbUsername, dbPassword);
            User[] users = Persistor.selectAllUsers(connection);
            for (int i = 0; i != users.length; i++) {
                System.out.println("=> " + users[i].getUsername());
                System.out.println("==> " + users[i].getId()); 
                System.out.println("==> " + users[i].getPassword());
                System.out.println("==> " + DateFormat.getDateTimeInstance()
                        .format(users[i].getLastLoginDate()));
                System.out.println("==> " + DateFormat.getDateTimeInstance()
                        .format(users[i].getRegistrationDate()));
            }
            
            User somebody = Persistor.selectUser(connection, "gunter");
            System.out.println("=> " + somebody.getUsername());
            System.out.println("==> " + somebody.getId()); 
            System.out.println("==> " + somebody.getPassword());
            System.out.println("==> " + DateFormat.getDateTimeInstance()
                    .format(somebody.getLastLoginDate()));
            System.out.println("==> " + DateFormat.getDateTimeInstance()
                    .format(somebody.getRegistrationDate()));
            
            somebody.setLastLoginDate(new Timestamp(System.currentTimeMillis()));
            Persistor.updateUser(connection, somebody);
            
            User foo = UserManager.createUser("kari", "elizabeth");
            Persistor.insertUser(connection, foo);
            
            /*User foo = Persistor.getUser(connection, "kari");
            Persistor.deleteUser(connection, foo);*/
            
            connection.close();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
