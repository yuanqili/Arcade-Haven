package gui;

import networking.SBClient;
import networking.SBMessage;

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

    public String username;
    public String password;

    SBClient client;

    public Login() {
        frame.add(main);
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        loginButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        username = usernameField.getText();
        password = new String(passwordField.getPassword());

        try {
            client = new SBClient("csil-08.cs.ucsb.edu", 23333);
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }

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

        frame.setVisible(false);

        ChatBox chatbox = new ChatBox(client);
        JFrame chat = new JFrame("Chat Box");
        chat.setContentPane(chatbox.panel);
        chat.setSize(800, 400);
        chat.setVisible(true);
        chat.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        new Login();
    }
}
