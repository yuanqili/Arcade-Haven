package edu.ucsb.g01.dialog;

import edu.ucsb.g01.core.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Date;

/**
 * <p>ClientTalker is used send/recv messages to/from the server. Messages are
 * encapsulated in {@link edu.ucsb.g01.dialog.Message} class, which contains
 * necessary information related to a message.</p>
 *
 * <p>To send a message, use {@link #send(int, String)} method. To receive a
 * message, use {@link #recv()} method. You can (indeed, you should) provide a
 * {@link edu.ucsb.g01.dialog.MessageReceiver} instance as a handler that is
 * called upon a message arrival. For example, the following instance prints a
 * message to the console.</p>
 *
 * <pre>{@code
 * MessageReceiver r = (msg) -> {
 *     System.out.println("From: " + msg.srcUid);
 *     System.out.println("Message: " + msg.messageBody);
 * };}</pre>
 */
public class ClientTalker {

    /** Server host name. */
    public final String host;

    /** Server port number. */
    public final int port;

    /** User information (who sends the message). */
    public final User user;

    /** TCP socket connected to the server. */
    private Socket dialogSocket = null;

    /** Output stream to send object via the socket. */
    private ObjectOutputStream out = null;

    /** Input stream to messageHandler object via the socket. */
    private ObjectInputStream in = null;

    /** Upon message arrival, the receiver is called to handle it. */
    private MessageReceiver receiver = null;

    /** A timer used to generate timestamp. */
    private Date timer;

    /**
     * Initializes the ClientTalker and connects to the server.
     *
     * @param host server host name
     * @param port server port number
     * @param user User object returned by the server upon login
     */
    public ClientTalker(String host, int port, User user) {
        this.host = host;
        this.port = port;
        this.timer = new Date();
        this.user = user;

        try {
            this.dialogSocket = new Socket(host, port);
            this.out = new ObjectOutputStream(dialogSocket.getOutputStream());
            this.in = new ObjectInputStream(dialogSocket.getInputStream());
        } catch (IOException e) {
            System.err.format("Unable to connect to %s:%d.%n", host, port);
            this.close();
            System.exit(1);
        }
    }

    /**
     * Initializes the ClientTalker and connects to the server.
     *
     * @param host     server host name
     * @param port     server port number
     * @param user     User object returned by the server upon login
     * @param receiver handler upon message arrival
     */
    public ClientTalker(String host, int port, User user, MessageReceiver receiver) {
        this(host, port, user);
        this.receiver = receiver;
    }

    /**
     * Sends a message to destUid.
     *
     * @param destUid target user
     * @param message message body
     * @return true if the message is sent successfully
     */
    public boolean send(int destUid, String message) {
        Message msg = new Message(user.uid, destUid, timer.getTime(), message);
        try {
            out.writeObject(msg);
            return true;
        } catch (IOException e) {
            System.err.println("Unable to send message");
            return false;
        }
    }

    /**
     * Receives a message from the server, MessageReceiver handler is called.
     *
     * @return true if the message is received successfully
     */
    public boolean recv() {
        try {
            Message msg = (Message) in.readObject();
            if (receiver != null)
                receiver.messageHandler(msg);
            return true;
        } catch (IOException e) {
            System.err.println("Unable to messageHandler message");
            return false;
        } catch (ClassNotFoundException e) {
            System.err.println("Unable to retrieve Message object");
            return false;
        }
    }

    /**
     * Closes ClientTalker's open connections and streams.
     */
    public void close() {
        try {
            if (dialogSocket != null && !dialogSocket.isClosed())
                dialogSocket.close();
            if (in != null)
                in.close();
            if (out != null)
                out.close();
        } catch (IOException ex) {
            System.err.println("Error on closing ClientTalker");
        }
    }
}
