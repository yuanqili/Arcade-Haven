package networking;

import java.sql.*;

/**
 * {@link DBManage} is the universal database management class. It is used for
 * any purpose regarding to the central database. Currently it supports the
 * following usages:
 * <ul>
 * <li>login validation given username and password: {@link #userIdentityValidation(String, String)}</li>
 * </ul>
 */
public class DBManage {

    private static final String mysqlURL = "jdbc:mysql://mealplan.ck65g32d0fbs.us-west-1.rds.amazonaws.com:3306?useSSL=false";
    private static final String username = "yuanqili";
    private static final String password = "914116Mmc";

    private Connection conn = null;

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
            conn = DriverManager.getConnection(mysqlURL, username, password);
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
        }
    }

    /**
     * Validates login information.
     *
     * @param username login username
     * @param password login password
     * @return 0 if there is a (username, password) match in the database;
     *         -1 if password is wrong;
     *         -2 if there is no such a username;
     *         -3 if database related exception raised
     */
    public int userIdentityValidation(String username, String password) {
        if (conn == null)
            this.connect();

        PreparedStatement queryUser = null;
        ResultSet rs = null;
        int loginStatus = -3;

        try {
            queryUser = conn.prepareStatement("SELECT password, count(*) COUNT FROM g01.user WHERE username = ?");
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

    public boolean userRegistration(String username, String password) {
        if (conn == null)
            this.connect();

        try {
            PreparedStatement query = conn.prepareStatement("INSERT INTO g01.user (uid, username, password) VALUE (DEFAULT, ?, ?)");
            query.setString(1, username);
            query.setString(2, password);
            return query.executeUpdate() == 1;
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
        }

        return false;
    }

    public String leaderboard() {
        if (conn == null)
            this.connect();

        StringBuilder scores = new StringBuilder();

        try {
            PreparedStatement query = conn.prepareStatement("SELECT username, score FROM g01.user;");
            ResultSet rs = query.executeQuery();
            while (rs.next())
                scores.append(rs.getString(1)).append(" ").append(rs.getInt(2)).append(" ");
        } catch (SQLException ex) {
            SQLExceptionPrinter(ex);
        }

        return scores.toString();
    }

    public boolean updateUserScore(String username, int score) {
        if (conn == null)
            this.connect();

        boolean res = false;

        try {
            // query old score and compare with new score
            PreparedStatement oldScoreQuery = conn.prepareStatement("SELECT score FROM g01.user WHERE username=?");
            oldScoreQuery.setString(1, username);
            ResultSet oldScoreRS = oldScoreQuery.executeQuery();
            int oldScore = 0;
            if (oldScoreRS.next())
                oldScore = oldScoreRS.getInt(1);
            System.out.println("old score is " + oldScore);

            // new score is too low, return
            if (oldScore > score)
                return false;

            // update score if necessary (new score is greater to old score)
            PreparedStatement query = conn.prepareStatement("UPDATE g01.user SET score=? WHERE username=?");
            query.setInt(1, score);
            query.setString(2, username);
            res = query.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;
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
     * @param ex an {@link SQLException} instance
     */
    private void SQLExceptionPrinter(SQLException ex) {
        System.out.println("SQLException: " + ex.getMessage());
        System.out.println("SQLState: " + ex.getSQLState());
        System.out.println("VendorError: " + ex.getErrorCode());
    }
}
