package networking;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * {@link SBServer2} is the main server that validates user login and user
 * message forwarding. When it starts, it listens to a specific port for all
 * incoming messages.
 *
 * Every connection is handled by a separated {@link SBServer2.ClientConnector}
 * object, which extends {@link Thread} so can run on another thread. It talks
 * to each clients using a specific SBTalk protocol. Each message is of the
 * format:
 *
 *   Message = Type Action Data
 *
 * There are two Type for now: MSG and CTRL. MSG is used for user's dialog, and
 * CTRL is for general purpose, e.g., login, logoff, register, etc.
 */
public class SBServer2 {

    /** The port the server will listen on. */
    private final int port;

    /** The socket that used to listen on {@code port}. */
    private ServerSocket listener;

    /** Contains all online users and their input stream (so you can write to them easily). */
    private final Map<String, PrintWriter> clientWriters = new Hashtable<>();

    /** Writes server's log. */
    private final Logger logger = Logger.getLogger(SBServer2.class.getName());

    /** A database connection that is used in various methods. */
    private final DBManage db = new DBManage();

    /**
     * Initializes the server. Only the port number is set. {@link SBServer2#run()}
     * needs to be called explicitly so the server will be able to listen to
     * incoming messages.
     *
     * @param port the port number this server will listen on
     */
    public SBServer2(int port) {
        this.port = port;
        logger.info("Server binds to port " + port);
    }

    /**
     * Runs the server so it listens for incoming messages and dispatches them
     * to a separate handler thread.
     */
    public void run() {
        try {
            listener = new ServerSocket(this.port);
        } catch (IOException e) {
            logger.severe("Server cannot start. Unable to create a ServerSocket");
            return;
        }
        while (true) {
            try {
                Socket socket = listener.accept();
                logger.info("Accepted a new connection");
                new ClientConnector(socket).start();
            } catch (IOException e) {
                logger.warning("Error on accepting a new connection");
                close();
            }
        }
    }

    /**
     * Closes the server. This method should only be called on a running server.
     */
    public void close() {
        db.disconnect();
        clientWriters.forEach((k, v) -> v.close());
        try {
            listener.close();
        } catch (IOException e) {
            logger.warning("Error on closing the server's ServerSocket");
        } finally {
            listener = null;
        }
    }

    /**
     * A {@link ClientConnector} is a user connection thread. It handles
     * the communication between a user and the server. It is a inner class of
     * server because it has to share states with the server.
     */
    private class ClientConnector extends Thread {

        /** The user's name it connects to. */
        private String name;

        /** The connection to this user. */
        private Socket socket;

        /** Reader of socket. */
        private Scanner in;

        /** Writer of socket. */
        private PrintWriter out;

        /**
         * Creates a {@link ClientConnector} given a newly accepted socket.
         * @param socket a newly accepted socket to be handled
         */
        public ClientConnector(Socket socket) {
            this.socket = socket;
        }

        /**
         * Responses a client message accordingly.
         * @param msg incoming client message
         * @throws Exception error on parsing messages
         */
        private void response(String msg) throws Exception {

            System.out.println("\033[33mreceived message: " + msg + "\033[0m");

            String[] token = msg.split(" ");
            String type = token[0];

            String username, password, receiver;
            StringBuilder sb = new StringBuilder();
            PrintWriter receiverPrinter = null;

            switch (type) {
                case "login":
                    username = token[1];
                    password = token[2];
                    if (db.userIdentityValidation(username, password) != 0) {
                        out.println("login fail");
                        return;
                    }
                    out.println("login succeed");
                    name = username;
                    clientWriters.put(name, out);
                    logger.info("user <" + name + "> log in");
                    break;

                case "signup":
                    username = token[1];
                    password = token[2];
                    out.println("signup " + (db.userRegistration(username, password) ? "succeed" : "fail"));
                    break;

                case "logout":
                    out.println("logout bye");
                    clientWriters.remove(name);
                    try {
                        socket.close();
                    } catch (IOException e) {
                        logger.warning("Error on closing socket");
                    }
                    break;

                case "userlist":
                    clientWriters.forEach((name, out) -> sb.append(name).append(" "));
                    out.println("userlist " + sb.toString());
                    break;

                case "score":
                    out.println("score " + db.leaderboard());
                    break;

                case "updatescore":
                    db.updateUserScore(token[1], Integer.parseInt(token[2]));
                    break;

                case "message":
                    receiver = token[1];
                    try {
                        receiverPrinter = clientWriters.get(receiver);
                    } catch (Exception e) {
                        out.println("message notfound");
                        return;
                    }
                    if (receiverPrinter != null) {
                        for (int i = 2; i < token.length; i++)
                            sb.append(token[i] + " ");
                        receiverPrinter.println("recvmsg " + name + " " + sb.toString());
                        out.println("message sent");
                    } else {
                        out.println("message notfound");
                    }
                    break;

                case "info":
                    out.println("info received");
                    break;

                default:
                    out.println("info cannot understand message");
            }
        }

        /**
         * A new {@link ClientConnector} is spawned for every connection
         * accepted by the server. It first validates user login, then put user
         * into server's online user list. It listens to user messages and call
         * {@link ClientConnector#response(String)} to parse user message and
         * make response.
         */
        @Override
        public void run() {
            try {
                out = new PrintWriter(socket.getOutputStream(), true);
                in = new Scanner(socket.getInputStream());
                while (true) {
                    String message = in.nextLine();
                    try {
                        response(message);
                    } catch (Exception e) {
                        out.println("info error on receiving message");
                    }
                }
            } catch (IOException e) {
                logger.info("user <" + name + "> log out");
            } catch (NoSuchElementException e) {
                clientWriters.remove(name);
                logger.info("user <" + name + "> log out");
            } finally {
                if (name != null)
                    clientWriters.remove(name);
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SBServer2 server = new SBServer2(23333);
        server.run();
    }
}
