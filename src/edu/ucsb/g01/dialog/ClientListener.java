package edu.ucsb.g01.dialog;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * A ClientListener is used to receives messages sent from other players via the
 * server. It implements Runnable interface so you can (and you should) run it
 * in another thread. You can (again, you should) inherit and override
 * messageArrival() method so you can do whatever you want upon message arrival.
 * It listens to the socket created by the ClientSender.
 */
public class ClientListener implements Runnable {

    /** TCP socket connected to the server. */
    private Socket dialogSocket = null;

    /** Used to send Message object to the server. */
    private ObjectInputStream in = null;

    public ClientListener(Socket dialogSocket) {
        try {
            this.dialogSocket = dialogSocket;
            this.in = new ObjectInputStream(dialogSocket.getInputStream());
        } catch (IOException e) {
            System.err.println("Error on listening to the socket");
            System.exit(1);
        }
    }

    /**
     * Begins listening for incoming messages.  Upon message arrival, the
     * messageArrival handler is called.
     */
    @Override
    public void run() {
        while (!dialogSocket.isClosed()) {
            try {
                Message msg = (Message)in.readObject();
            } catch (IOException e) {
                System.err.println("Unable to read incoming message");
            } catch (ClassNotFoundException e) {
                System.err.println("Message class not found");
            }
        }
    }

    /**
     * Closes all open connections and streams.  It should be called manually
     * when exit.
     */
    public void close() {
        try {
            if (dialogSocket != null && !dialogSocket.isClosed())
                dialogSocket.close();
            if (in != null)
                in.close();
        } catch (IOException e) {
            System.err.println("Error on closing sender");
        }
    }

    /**
     * A handler that is called upon message arrival.
     *
     * @param msg the incoming message
     */
    public void messageArrial(Message msg) {
    }
}
