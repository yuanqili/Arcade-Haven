package gui;

import pacman.GameEngine;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Login {

    public JFrame frame = new JFrame();
    private JPanel main;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JLabel infoLabel;

    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    public String username;
    public String password;
    public String host;
    public int port;

    public Login(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            socket = new Socket(host, port);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(socket.getInputStream());
            out.println("info init");
            in.nextLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        frame.add(main);
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        loginButton.addActionListener(e -> {
            username = usernameField.getText();
            password = new String(passwordField.getPassword());
            out.println("login " + username + " " + password);

            String response = in.nextLine();
            System.out.println(response);
            boolean loginStatus = response.split(" ")[1].equals("succeed");
            infoLabel.setText(loginStatus ? "Login succeeds" : "Login fails");
            if (!loginStatus)
                return;

            frame.dispose();
            EventQueue.invokeLater(() -> new ChatTest(socket, in, out, username).start());
            new Thread(() -> {
                GameEngine game = new GameEngine(out, username);
                game.setup();
                game.run();
            }).start();
        });

        signupButton.addActionListener(e -> {
            username = usernameField.getText();
            password = new String(passwordField.getPassword());
            out.println("signup " + username + " " + password);
            String response = in.nextLine();
            System.out.println(response);
            boolean signupStatus = response.split(" ")[1].equals("succeed");
            infoLabel.setText(signupStatus ? "Signup succeeds" : "Signup fails");
        });
    }

    public static void main(String[] args) {
//        Login login =  new Login("csil-08.cs.ucsb.edu", 23333);
    }
}
