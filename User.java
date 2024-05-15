package main.java.com.example.myloginpage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class User {
    private static final String URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String USER = "root";
    private static final String PASS = "Aryan@08";
    private String name;
    private String password;
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }
    public String getName() {
        return name;
    }
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
    public static void close(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
