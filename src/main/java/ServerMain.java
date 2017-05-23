import networking.SBServer;

public class ServerMain {
    public static void main(String[] args) {

        int port = 23333;

        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
//            System.out.println("usage: ServerMain <port>");
//            return;
        }

        SBServer server = new SBServer(port);
        server.run();
    }
}
