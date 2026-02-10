package com.delivery.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {
    public static Connection getDBConnection() throws Exception {
        Class.forName("oracle.jdbc.OracleDriver");
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String username = "system";
        String password = "root";
        Connection conn = DriverManager.getConnection(url, username, password);
        conn.setAutoCommit(false);

        return conn;
    }
}
