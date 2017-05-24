package experiment;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by yuanqili on 5/23/17.
 */
public class Server2ChatTest {

    public static void main(String[] args) throws IOException {

        Socket socket = new Socket("localhost", 23333);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        Scanner in = new Scanner(socket.getInputStream());


//        out.println("login timcook 123456");

        out.println("login ucsb 12345678");
        out.println("info hahaha");
        out.println("userlist");
        out.println("message timcook hahahaha");

//        System.out.println(in.nextLine());
//        System.out.println(in.nextLine());

        while (in.hasNextLine())
            System.out.println(in.nextLine());
    }
}
