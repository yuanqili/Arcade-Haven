package server;

import edu.ucsb.g01.core.DBManage;

/**
 * This is class is used by a client end to support user activity related to the
 * server end. Currently it supports the following operations:
 *
 * <p><ul>
 *     <li>user login</li>
 *     <li>new user registration</li>
 * </ul></p>
 *
 * Database operations are delegated to {@link edu.ucsb.g01.core.DBManage},
 * while {@link UserActivity} only works as a wrapper class. It also talks to
 * the IM server {@link ServerTalker} so that after a user login, the IM server
 * will know he/she is online, and accepts his/her connection requests.
 */
public class UserActivity {

    DBManage conn = null;
    ServerTalker imServer = null;

    private void connectDB() {
        if (conn == null) {
            conn = new DBManage();
            conn.connect();
        }
    }

    private int login(String username, String password) {
        if (conn == null)
            this.connectDB();

        int loginStatus = conn.loginValidation(username, password);
        switch (loginStatus) {
            case 0:
                System.err.println("Login validation succeeded");
                imServer.userOnline(username);
                break;
            case -1:
                System.err.println("Check your username/password");
                break;
            case -2:
                System.err.println("Unable to connect to the database, try again later");
                break;
            default:
                break;
        }

        return loginStatus;
    }
}
