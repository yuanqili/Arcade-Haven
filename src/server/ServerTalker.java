package server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Map;

public class ServerTalker {

    /** Online users and their sockets. */
    Map<String, Conversation> onlineUsers = new Hashtable<>();

    /**
     * Adds a new user to online user table.
     *
     * @param username the new user that is just online
     */
    public void userOnline(String username, Socket socket) {
        onlineUsers.put(username, new Conversation(socket));
    }
}

/** A channel is a connection to a user. */
class Conversation {

    Socket dialogSocket;
    ObjectInputStream in = null;
    ObjectOutputStream out = null;

    public Conversation(Socket dialogSocket) {
        this.dialogSocket = dialogSocket;
        try {
            this.in = new ObjectInputStream(dialogSocket.getInputStream());
            this.out = new ObjectOutputStream(dialogSocket.getOutputStream());
        } catch (IOException e) {
            System.err.println("Cannot communicate through this socket");
        }
    }
}