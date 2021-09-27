package csci310.util;

import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.Assert.*;

public class DBUtilTest {

    @Test
    public void getConnection() {

        try {
            DBUtil db=new DBUtil("db");
            Connection connection = db.getConnection();

            assertTrue(connection!=null);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void getConnection_check_class(){

        try {
            DBUtil db=new DBUtil("test_db_class");
            Connection connection = db.getConnection();
            fail( "Should have thrown an exception" );

        } catch (ClassNotFoundException e) {
            final String expected = "Unkown";
            assertEquals( expected, e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void getConnection_check_login(){

        try {
            DBUtil db=new DBUtil("test_db_login");
            Connection connection = db.getConnection();
            fail( "Should have thrown an exception" );

        } catch (ClassNotFoundException e) {
            final String expected = "Unkown";
            assertEquals( expected, e.getMessage());
        } catch (SQLException e) {
            final String expected = "Access denied for user 'admin'@'108.60.34.131' (using password: YES)";

            assertEquals( expected.substring(0,22), e.getMessage().substring(0,22));
        }

    }
}