import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.channels.SocketChannel;
import java.util.List;

/**
 * <p>A plug-and-play client instance. It has two sets of sending functions:
 * pre-login and post-login. In pre-login phase, you can call
 * {@link SBClient#login(String, String)} and
 * {@link SBClient#signup(String, String)}. In post-login phase, you can call
 * {@link SBClient#sendMessage(String, String)} to send a message,
 * {@link SBClient#userlist()} to obtain a list of online users and
 * {@link SBClient#logoff()} to leave. There will be a response message to each
 * of these functions.</p>
 *
 * <p>Every function call returns a {@link SBMessage} object that is just sent
 * to the server. You should put this message into a pending queue until you
 * receives server's response then make a action correspondingly. The server's
 * response has the same sequence number so you can distinguish them. Messages
 * sent from other users has sequence number (-2), so you know they are messages
 * actively sent from the server that doesn't need to be checked locally. You
 * can put incoming messages directly to the dialog box.</p>
 */
public class SBClient {

    /** SocketChannel connected to the server. */
    SocketChannel schannel;

    /** Reader of socket, used to receive messages from the server. */
    ObjectInputStream in;

    /** Writer of socket, used to write messages to the server. */
    ObjectOutputStream out;

    List<SBMessage> arrivalMessages;

    /**
     * Current sequence number that is used to send the next message. It is
     * initialized to a random number to avoid palyback attack.
     */
    int sequence;

    /**
     * Starts a client instance
     * @param host host address (e.g., csil-xx.cs.ucsb.edu)
     * @param port host port number
     * @param container container for arrival messages form the server
     * @throws IOException cannot connect to server
     */
    public SBClient(String host, int port, List<SBMessage> container) throws IOException {

        schannel = SocketChannel.open();
        schannel.configureBlocking(true);
        if (!schannel.connect(new InetSocketAddress(host, port)))
            System.exit(1);
        out = new ObjectOutputStream(schannel.socket().getOutputStream());
        in = new ObjectInputStream(schannel.socket().getInputStream());

        sequence = (int)(Math.random() * 10000);
        arrivalMessages = container;

        out.writeObject(new SBMessage().setType(Type.info));

        Thread listen = new Thread(() -> {
            while (true) {
                try {
                    SBMessage msg = (SBMessage) in.readObject();
                    if (msg == null) return;
                    System.out.println(msg);
                    if (arrivalMessages != null) arrivalMessages.add(msg);
                } catch (EOFException e) {
                    return;
                } catch (IOException e) {
                    System.out.println("Error on reading");
                } catch (ClassNotFoundException e) {
                    System.out.println("Error on parsing object");
                }
            }
        });
        listen.start();
    }

    class Listener implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    SBMessage msg = (SBMessage) in.readObject();
                    if (msg == null) return;
                    System.out.println(msg);
                    if (arrivalMessages != null) arrivalMessages.add(msg);
                } catch (EOFException e) {
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Error on reading");
                } catch (ClassNotFoundException e) {
                    System.out.println("Error on parsing object");
                }
            }
        }
    }

    /**
     * Close a running client instance.
     * @throws IOException error happened when closing socket
     */
    public void close() throws IOException {
        schannel.close();
    }

    public SBMessage login(String username, String password) throws IOException {
        SBMessage msg = new SBMessage()
                .setType(Type.control)
                .setAction(Action.login)
                .setSequence(sequence++)
                .setBody(username + " " + password);
        out.writeObject(msg);
        return msg;
    }

    public SBMessage signup(String username, String password) throws IOException {
        SBMessage msg = new SBMessage()
                .setType(Type.control)
                .setAction(Action.signup)
                .setSequence(sequence++)
                .setBody(username + " " + password);
        out.writeObject(msg);
        return msg;
    }

    public SBMessage sendMessage(String username, String body) throws IOException {
        SBMessage msg = new SBMessage()
                .setType(Type.message)
                .setReceiver(username)
                .setSequence(sequence++)
                .setBody(body);
        out.writeObject(msg);
        return msg;
    }

    public SBMessage logoff() throws IOException {
        SBMessage msg = new SBMessage()
                .setType(Type.control)
                .setAction(Action.bye)
                .setSequence(sequence++);
        out.writeObject(msg);
        return msg;
    }

    public SBMessage userlist() throws IOException {
        SBMessage msg = new SBMessage()
                .setType(Type.control)
                .setAction(Action.userlist)
                .setSequence(sequence++);
        out.writeObject(msg);
        return msg;
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage: sbclient <host> <port>");
            return;
        }

        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket sock = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        PrintWriter out = new PrintWriter(sock.getOutputStream(), true);

        Thread listen = new Thread(() -> {
            while (true) {
                try {
                    String message = in.readLine();
                    if (message == null)
                        return;
                    System.out.println(message);
                } catch (IOException e) {
                    System.out.println("Error on reading");
                }
            }
        });
        listen.start();

        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        while ((userInput = scanner.readLine()) != null)
            out.println(userInput);
    }
}
