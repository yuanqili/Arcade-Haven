package gui;

import networking.SBClient;
import networking.SBMessage;
import pacman.GameEngine;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class Login implements ActionListener {

    public JFrame frame = new JFrame();
    private JPanel main;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signupButton;
    private JLabel infoLabel;

    public String username;
    public String password;

    public SBClient client;

    public String host;
    public int port;

    public Login(String host, int port) {
        this.host = host;
        this.port = port;

        try {
            client = new SBClient(this.host, this.port);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        frame.add(main);
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        loginButton.addActionListener(this);

        signupButton.addActionListener(e -> {
            username = usernameField.getText();
            password = new String(passwordField.getPassword());

            SBMessage signupInfo, signupResponse;
            boolean signupStatus = false;

            try {
                signupInfo = client.signup(username, password);
                do {
                    signupResponse = (SBMessage) client.getIn().readObject();
                    signupStatus = signupResponse.getFlag();
                } while (signupResponse.getSequence() != signupResponse.getSequence());
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (ClassNotFoundException e1) {
                e1.printStackTrace();
            }

            if (signupStatus)
                infoLabel.setText("Signup succeeds");
            else
                infoLabel.setText("Signup fails");
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        username = usernameField.getText();
        password = new String(passwordField.getPassword());

        SBMessage loginInfo, loginResponse;
        boolean loginStatus = false;

        try {
            loginInfo = client.login(username, password);
            do {
                loginResponse = (SBMessage) client.getIn().readObject();
                loginStatus = loginResponse.getFlag();
            } while (loginResponse.getSequence() != loginInfo.getSequence());
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

        if (!loginStatus)
            return;

        frame.dispose();

        new Thread(() -> {
            JFrame chat = new JFrame("Chat Box");
            chat.setContentPane(new ChatBox(client).panel);
            chat.setSize(800, 400);
            chat.setVisible(true);
            chat.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        }).start();

        new Thread(() -> {
            GameEngine game = new GameEngine();
            game.setUp();
            game.run();
        }).start();
    }

    public static void main(String[] args) {
        Login login =  new Login("csil-08.cs.ucsb.edu", 23333);
    }
}
