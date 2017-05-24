import networking.SBServer2;

public class ServerMain {
    public static void main(String[] args) {

        int port = 23333;

        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
//            System.out.println("usage: ServerMain <port>");
//            return;
        }

        SBServer2 server = new SBServer2(port);
        server.run();
    }
}
