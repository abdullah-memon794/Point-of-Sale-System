package pos.pro;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class db {

    public static Connection mycon() {
        Connection con = null;

        try {
            // Updated to the modern MySQL driver class
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connects to your local MySQL Workbench database named 'pos'
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pos", "root", "root");
            return con;

        } catch (ClassNotFoundException | SQLException e) {
            // Improved error logging to help you and Meharban debug if the connection fails
            System.err.println("Database Connection Failed!");
            e.printStackTrace();
            return null;
        }
    }
}