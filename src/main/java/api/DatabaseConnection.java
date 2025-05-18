package api;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

    private static Connection instance;

    public static synchronized Connection getInstance() {

        try {
            String url = "jdbc:postgresql://localhost:5432/sisweb?user=admin&password=admin&ssl=true";
            
            //not thread safe but ok
            if(instance == null) {
                instance = DriverManager.getConnection(url);
            }

            return instance;
        } catch (Exception e) {
            System.err.println(e);
        }
        return null;
    }
}
