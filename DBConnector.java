import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnector {

    private static final String URL = "jdbc:mysql://localhost:3306/crm_system";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    public static Connection connect() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully!");
        } catch (ClassNotFoundException e) {
            System.err.println("**MySQL JDBC Driver not found**");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("**Failed to connect to the database**");
            e.printStackTrace();
        }
        return conn;
    }
}
