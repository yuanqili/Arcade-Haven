import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class smallClient {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Receiver start");
        SocketChannel schannel = SocketChannel.open();
        schannel.configureBlocking(true);
        if (schannel.connect(new InetSocketAddress("localhost", 23333))) {
            ObjectOutputStream oos = new ObjectOutputStream(schannel.socket().getOutputStream());
            ObjectInputStream ois = new ObjectInputStream(schannel.socket().getInputStream());

            SBMessage msg = new SBMessage()
                    .setType(Type.control)
                    .setAction(Action.login)
                    .setBody("apple 123456");
            oos.writeObject(msg);

            SBMessage recvmsg = (SBMessage)ois.readObject();
            System.out.format("Receive: %s%n", recvmsg);
        }
        System.out.println("Receiver end");
    }

}
