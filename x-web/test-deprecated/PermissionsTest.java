import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import net.six_two.program_guide.Permissions;
import net.six_two.program_guide.Persistor;
import net.six_two.program_guide.UserManager;
import net.six_two.program_guide.tables.Program;
import net.six_two.program_guide.tables.User;

public class PermissionsTest {
    public static void main(String[] args) {
        String dbUsername = args[0];
        String dbPassword = args[1];
        
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://six-two.net/program_guide",
                    dbUsername, dbPassword);
            
            /*User user = UserManager.createUser("test", "test");
            
            Persistor.insertUser(connection, user);
            
            Permissions permissions = new Permissions();
            HashMap map = permissions.getPermissionsMap();
            Set keys = map.keySet();
            Iterator iterator = keys.iterator();
            String key;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                System.out.print(key + " = ");
                System.out.println(UserManager.authorizeUser(user, ((Integer) map.get(key)).intValue()));
            }
            
            //user.setPermissions(Permissions.USAGE | Permissions.DELETE_USER);*/
            User user = Persistor.selectUser(connection, 1);
            
            Permissions permissions = new Permissions();
            HashMap map = permissions.getPermissionsMap();
            Set keys = map.keySet();
            Iterator iterator = keys.iterator();
            String key;
            while (iterator.hasNext()) {
                key = (String) iterator.next();
                System.out.print(key + " = ");
                System.out.println(UserManager.authorizeUser(user, ((Integer) map.get(key)).intValue()));
            }
            
            //Persistor.deleteUser(connection, user);
            
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
