import gui.Login;

public class GameMain {
    public static void main(String[] args) {

        String host = "localhost";
        int port = 23333;

        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (Exception e) {
//            System.out.println("usage: GameMain <host> <port>");
//            return;
        }

        new Login(host, port);
    }
}
