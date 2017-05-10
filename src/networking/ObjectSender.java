import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

public class ObjectSender {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel sschannel = ServerSocketChannel.open();
        sschannel.socket().bind(new InetSocketAddress(23333));
        int sequence = 1;

        while (true) {
            SocketChannel schannel = sschannel.accept();
            System.out.println("Sender start");
            ObjectOutputStream oos = new ObjectOutputStream(
                    schannel.socket().getOutputStream());

            SBMessage msg = new SBMessage()
                    .setType(Type.message)
                    .setAction(Action.send)
                    .setSequence(sequence++)
                    .setSender("SBServer")
                    .setReceiver("Everyone")
                    .setBody("Hey how are you doing?");

            oos.writeObject(msg);

            oos.close();
            System.out.println("Connection end");
        }
    }
}
