package csci310.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
public class DBUtil {
    private String driverClass;
    private String url;
    private String username;
    private String password;
    public DBUtil(String bundle){
        ResourceBundle rb = ResourceBundle.getBundle(bundle);
        driverClass = rb.getString("driverClass");
        url = rb.getString("url");
        username = rb.getString("username");
        password = rb.getString("password");

    }


    public Connection getConnection() throws SQLException, ClassNotFoundException {

        Class.forName(driverClass);

        return DriverManager.getConnection(url, username, password);

    }


}
