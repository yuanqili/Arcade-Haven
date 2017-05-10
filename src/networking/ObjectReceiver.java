import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

public class ObjectReceiver {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        System.out.println("Receiver start");
        SocketChannel schannel = SocketChannel.open();
        schannel.configureBlocking(true);
        if (schannel.connect(new InetSocketAddress("localhost", 23333))) {
            ObjectInputStream ois = new ObjectInputStream(schannel.socket().getInputStream());
            SBMessage msg = (SBMessage)ois.readObject();
            System.out.format("Receive string: %s%n", msg);
        }
        System.out.println("Receiver end");
    }
}
