package banking;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection getConnection (String username, String password, String database) {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            return DriverManager.getConnection("jdbc:mysql://localhost:3306/" + database, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    public static Connection getAppConnection () {
        return getConnection("weka_banking", "weka_banking", "weka_banking");
    }
}
