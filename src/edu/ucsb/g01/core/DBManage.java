package edu.ucsb.g01.core;

import java.sql.*;

public class DBManage {

    private static final String mysqlURL = "jdbc:mysql://localhost:3306/test?useSSL=false";
    private static final String userName = "";
    private static final String password = "";

    Connection conn = null;

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.err.println("Cannot load MySQL driver");
            System.exit(1);
        }

        try {
            conn = DriverManager.getConnection(mysqlURL, userName, password);
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
            System.exit(1);
        }
    }

    public void query_test() {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM test.test");
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    if (i > 1) System.out.print(",  ");
                    String columnValue = rs.getString(i);
                    System.out.print(columnValue + " " + rsmd.getColumnName(i));
                }
                System.out.println("");
            }
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException sqlEx) { } // ignore
                rs = null;
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException sqlEx) { } // ignore
                stmt = null;
            }
        }
    }

    private void SQLExceptionPrinter(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

    public static void main(String[] args) {
        DBManage db = new DBManage();
        db.connect();
        db.query_test();
    }
}