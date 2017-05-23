package networking;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Logger;

/**
 * {@link SBServer} is the main server that validates user login and user
 * message forwarding. When it starts, it listens to a specific port for all
 * incoming messages.
 *
 * Every connection is handled by a separated {@link SBServer.ClientConnector}
 * object, which extends {@link Thread} so can run on another thread. It talks
 * to each clients using a specific SBTalk protocol. Each message is of the
 * format:
 *
 *   Message = Type Action Data
 *
 * There are two Type for now: MSG and CTRL. MSG is used for user's dialog, and
 * CTRL is for general purpose, e.g., login, logoff, register, etc.
 */
public class SBServer {

    /** The port the server will listen on. */
    private final int port;

    /** The socket that used to listen on {@code port}. */
    private ServerSocket listener;

    /** Contains all online users and their input stream (so you can write to them easily). */
    private final Map<String, ObjectOutputStream> clientWriters = new Hashtable<>();

    /** Writes server's log. */
    private final Logger logger = Logger.getLogger(SBServer.class.getName());

    /** A database connection that is used in various methods. */
    private final DBManage db = new DBManage();

    /**
     * Initializes the server. Only the port number is set. {@link SBServer#run()}
     * needs to be called explicitly so the server will be able to listen to
     * incoming messages.
     *
     * @param port the port number this server will listen on
     */
    public SBServer(int port) {
        this.port = port;
        logger.info("Server binds to port " + port);
        logger.info("Server starts");
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
        clientWriters.forEach((k, v) -> {
            try {
                v.close();
            } catch (IOException e) {
                logger.warning("Error on closing connection to " + k);
            }
        });
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
        private ObjectInputStream in;

        /** Writer of socket. */
        private ObjectOutputStream out;

        /**
         * Creates a {@link ClientConnector} given a newly accepted socket.
         * @param socket a newly accepted socket to be handled
         */
        public ClientConnector(Socket socket) {
            this.socket = socket;
        }

        /**
         * Responses to each incoming message based on its type and action.
         * @param msg message sent from the user
         * @throws IOException all exceptions related to socket
         */
        private void response(SBMessage msg) throws IOException {

            System.out.println("\033[33mreceived message: " + msg + "\033[0m");

            Type type = msg.getType();
            Action action = msg.getAction();

            if (type == Type.message) {

                msg.setSender(name);
                ObjectOutputStream receiverWriter = clientWriters.get(msg.receiver);
                if (receiverWriter != null) {
                    receiverWriter.writeObject(msg);
                    out.writeObject(serverInfo("sent", true));
                } else {
                    out.writeObject(serverInfo("receiver not online", false));
                }

            } else if (type == Type.control) {
                if (action == Action.login) {

                    try {
                        String[] loginInfo = msg.getBody().split(" ");
                        if (db.userIdentityValidation(loginInfo[0], loginInfo[1]) == 0) {
                            name = loginInfo[0];
                            clientWriters.put(name, out);
                            out.writeObject(serverInfo("succeed", true, msg.getSequence()));
                            logger.info("user <" + loginInfo[0] + "> log in");
                        } else {
                            out.writeObject(serverInfo("fail", false, msg.getSequence()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject(serverInfo("wrong format", false, msg.getSequence()));
                    }

                } else if (action == Action.bye) {

                    clientWriters.remove(name);
                    out.writeObject(serverInfo("bye", true, msg.getSequence()));
                    try {
                        socket.close();
                    } catch (IOException e) {
                        logger.warning("Error on closing socket");
                    }

                } else if (action == Action.signup) {

                    String[] signupInfo = msg.getBody().split(" ");
                    if (db.userRegistration(signupInfo[0], signupInfo[1])) {
                        out.writeObject(serverInfo("succeed", true, msg.getSequence()));
                    } else {
                        out.writeObject(serverInfo("fail", false, msg.getSequence()));
                    }

                } else if (action == Action.userlist) {

                    StringBuilder sb = new StringBuilder();
                    clientWriters.forEach((username, out) -> sb.append(username).append(" "));
                    logger.info("userlist: " + sb.toString());
                    out.writeObject(serverInfo(sb.toString(), true, msg.getSequence()));

                } else {
                    out.writeObject(serverInfo("cannot understand message", false, msg.getSequence()));
                }
            } else if (type == Type.info) {
                out.writeObject(serverInfo("received", true, msg.getSequence()));
            } else {
                out.writeObject(serverInfo("cannot understand message", false, msg.getSequence()));
            }
        }

        /**
         * Creates a {@link SBMessage} of {@link Type#info} type with given body.
         * @param body message body
         * @return a newly created {@link SBMessage}
         */
        private SBMessage serverInfo(String body) {
            return new SBMessage().setType(Type.info).setBody(body);
        }

        /**
         * Creates a {@link SBMessage} of {@link Type#info} type with given body
         * and a given flag indicating success or not.
         * @param body message body
         * @param flag flag indicating success or not of last action
         * @return a newly created {@link SBMessage}
         */
        private SBMessage serverInfo(String body, boolean flag) {
            return new SBMessage().setType(Type.info).setBody(body).setFlag(flag);
        }

        private SBMessage serverInfo(String body, boolean flag, int sequence) {
            return new SBMessage().setType(Type.info).setBody(body).setFlag(flag).setSequence(sequence);
        }

        /**
         * A new {@link ClientConnector} is spawned for every connection
         * accepted by the server. It first validates user login, then put user
         * into server's online user list. It listens to user messages and call
         * {@link ClientConnector#response(SBMessage)} to parse user message and
         * make response.
         */
        @Override
        public void run() {
            try {
                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());

                while (true) {
                    SBMessage message = (SBMessage)in.readObject();
                    if (message == null) {
                        socket.close();
                        clientWriters.remove(name);
                        logger.info(name + " logged off");
                        return;
                    }

                    try {
                        response(message);
                    } catch (Exception e) {
                        out.writeObject(serverInfo("error on receiving message", false));
                    }
                }

            } catch (IOException e) {
                logger.info("user <" + name + "> log out");
            } catch (ClassNotFoundException e) {
                logger.warning("Class SBMessage not found");
            } finally {
                if (name != null)
                    clientWriters.remove(name);
                try {
                    socket.close();
                }  catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        SBServer server = new SBServer(23333);
        server.run();
    }
}
