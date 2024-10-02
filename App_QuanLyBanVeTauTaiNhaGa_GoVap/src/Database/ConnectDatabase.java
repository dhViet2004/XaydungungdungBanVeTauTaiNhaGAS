package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDatabase {
    public static Connection con = null;
    private static ConnectDatabase instance = new ConnectDatabase();

    public static ConnectDatabase getInstance() {
        return instance;
    }

    public void connect()  {
        String url = "jdbc:sqlserver://localhost:1433;DatabaseName=UngDungQuanLyBanVeTauTaiGaGoVap";
        String user = "sa";
        String password = "sapassword";
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            con = DriverManager.getConnection(url, user, password);

        } catch(SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void disconnect() {
        if(con != null) {
            try {
                con.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static Connection getConnection() {
        return con;
    }
}
