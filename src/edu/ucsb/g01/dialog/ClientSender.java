package edu.ucsb.g01.dialog;

import edu.ucsb.g01.core.User;

import java.io.*;
import java.net.Socket;
import java.util.Date;

/**
 * A ClientSender is used to send message to the destination player via the
 * server. It runs as follows:
 * - call constructor() to connect to the server,
 * - call send() to send several messages, and
 * - call close() to clean up sockets and streams it opens.
 */
public class ClientSender {

    /** Server host name. */
    public final String host;

    /** Server port number. */
    public final int port;

    /** User information (who sends the message). */
    public final User user;

    /** TCP socket connected to the server. */
    private Socket dialogSocket = null;

    /** Used to send Message object to the server. */
    private ObjectOutputStream out = null;

    /** A timer used to generate timestamp. */
    private Date timer;

    /**
     * Initializes the ClientSender.  It connects to the server and prepares
     * output stream to send messages.
     *
     * @param user User object returned by the server upon login
     * @param host server host name
     * @param port server port number
     */
    public ClientSender(User user, String host, int port) {
        this.host = host;
        this.port = port;
        this.timer = new Date();
        this.user = user;

        try {
            this.dialogSocket = new Socket(host, port);
            this.out = new ObjectOutputStream(dialogSocket.getOutputStream());
        } catch (IOException e) {
            System.err.format("Unable to connect to %s:%d.%n", host, port);
            this.close();
            System.exit(1);
        }
    }

    /**
     * Sends message to destUid.
     *
     * @param destUid destination player
     * @param message message body
     * @return true if the message is sent successfully, false otherwise
     */
    public boolean send(int destUid, String message) {
        Message msg = new Message(user.uid, destUid, timer.getTime(), message);
        try {
            out.writeObject(msg);
        } catch (IOException e) {
            System.err.println("Unable to send message");
            return false;
        }

        return true;
    }

    /**
     * Closes all open connections and streams.  It should be called manually
     * when exit.
     */
    public void close() {
        try {
            if (dialogSocket != null && !dialogSocket.isClosed())
                dialogSocket.close();
            if (out != null)
                out.close();
        } catch (IOException e) {
            System.err.println("Error on closing sender");
        }
    }

    /**
     * Returns socket connected to the server.
     */
    public Socket getDialogSocket() {
        return dialogSocket;
    }
}
