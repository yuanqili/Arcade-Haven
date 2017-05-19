import networking.SBServer;

public class ServerMain {
    public static void main(String[] args) {
        SBServer server = new SBServer(23333);
        server.run();
    }
}
