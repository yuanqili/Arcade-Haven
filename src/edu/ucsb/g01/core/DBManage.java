package edu.ucsb.g01.core;

import java.sql.*;

/**
 * {@link DBManage} is the universal database management class. It is used for
 * any purpose regarding to the central database. Currently it supports the
 * following usages:
 * <p><ul>
 * <li>login validation given username and password: {@link #loginValidation(String, String)}</li>
 * </ul></p>
 */
public class DBManage {

    private static final String mysqlURL = "jdbc:mysql://mealplan.ck65g32d0fbs.us-west-1.rds.amazonaws.com:3306?useSSL=false";
    private static final String userName = "yuanqili";
    private static final String password = "914116Mmc";

    Connection conn = null;

    /**
     * Connects to the database.
     */
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (Exception ex) {
            System.err.println("Cannot load MySQL driver");
        }

        try {
            conn = DriverManager.getConnection(mysqlURL, userName, password);
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
        }
    }

    /**
     * Validates login information.
     *
     * TODO: don't require password from database to local
     * TODO: maybe return a User object is a better choice
     *
     * @param username login username
     * @param password login password
     * @return 0 if there is a (username, password) match in the database;
     *         -1 if password is wrong;
     *         -2 if there is no such a username;
     *         -3 if database related exception raised
     */
    public int loginValidation(String username, String password) {
        if (conn == null)
            this.connect();

        PreparedStatement queryUser = null;
        ResultSet rs = null;
        int loginStatus = -3;

        try {
            queryUser = conn.prepareCall("SELECT password, count(*) COUNT FROM g01.user WHERE name = ?");
            queryUser.setString(1, username);
            rs = queryUser.executeQuery();
            rs.first();
            if (rs.getInt("COUNT") == 0)
                loginStatus = -2;
            else
                loginStatus = rs.getString("password").equals(password) ? 0 : -1;
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
            loginStatus = -3;
        } finally {
            if (queryUser != null) {
                try { queryUser.close(); } catch (SQLException ex) { /* ignore*/ }
                queryUser = null;
            }
        }

        return loginStatus;
    }

    /**
     * Registers a new user.
     *
     * TODO: provide more specific user registration information and error code
     *
     * @param newUser a {@link User} instance filled with registration info
     * @return 0 if registration succeeded;
     *         -1 if registration fails;
     *         -2 if database related exception raised
     */
    public int userRegistration(User newUser) {
        if (conn == null)
            this.connect();

        PreparedStatement insertUser = null;
        int insertStatus;

        try {
            insertUser = conn.prepareCall("INSERT INTO g01.user VALUE (NULL, ?, ?, ?)");
            insertUser.setString(1, newUser.name);
            insertUser.setString(2, newUser.password);
            insertUser.setString(3, newUser.email);
            insertStatus = insertUser.execute() ? 0 : -1;
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
            insertStatus = -2;
        } finally {
            if (insertUser != null) {
                try { insertUser.close(); } catch (SQLException ex) { /* ignore*/ }
                insertUser = null;
            }
        }

        return insertStatus;
    }

    /**
     * Disconnects from the database.
     */
    public void disconnect() {
        try {
            conn.close();
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
        }
    }

    /**
     * Prints {@link SQLException} related information.
     *
     * @param ex an {@link SQLException} instance
     */
    private void SQLExceptionPrinter(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }

    public static void main(String[] args) {
        DBManage db = new DBManage();

        User user = new User();
        user.name = "ricky li";
        user.password = "12345678";
        user.email = "cs48@ucsb.edu";
        db.userRegistration(user);
    }
}