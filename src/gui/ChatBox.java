package gui;

import networking.SBClient;
import networking.SBMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

public class ChatBox {

    public JPanel panel;

    public ChatBox(SBClient client) {
        panel = new JPanel(new BorderLayout());
        panel.add(BorderLayout.LINE_START, new UserListPanel(client).panel);
        panel.add(BorderLayout.CENTER, new ChatPanel(client).panel);
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        SBClient client = new SBClient("csil-08.cs.ucsb.edu", 23333);
        client.login("timcook", "123456");
        JFrame chat = new JFrame("Chat Box");
        chat.setContentPane(new ChatBox(client).panel);
        chat.setSize(800, 400);
        chat.setVisible(true);
        chat.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}

class ChatPanel extends JPanel implements ActionListener {

    public JPanel panel = new JPanel(new BorderLayout());
    private JPanel southPanel = new JPanel(new GridBagLayout());
    private JTextField msgInput = new JTextField(30);
    private JButton msgSendButton = new JButton("Send");
    private JTextArea msgHist = new JTextArea();
    private JScrollPane msgHistScroller = new JScrollPane(msgHist);

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private SBClient client;

    public ChatPanel(SBClient client) {

        this.client = client;
        this.in = client.getIn();
        this.out = client.getOut();

        msgSendButton.addActionListener(this);
        msgHist.setEditable(false);
        msgHist.setLineWrap(true);

        GridBagConstraints left = new GridBagConstraints();
        left.anchor = GridBagConstraints.CENTER;
        left.fill = GridBagConstraints.HORIZONTAL;
        left.weightx = 512.0D;
        left.weighty = 1.0D;

        GridBagConstraints right = new GridBagConstraints();
        right.insets = new Insets(0, 10, 0, 0);
        right.anchor = GridBagConstraints.LINE_END;
        right.fill = GridBagConstraints.NONE;
        right.weightx = 1.0D;
        right.weighty = 1.0D;

        southPanel.add(msgInput, left);
        southPanel.add(msgSendButton, right);
        panel.add(BorderLayout.SOUTH, southPanel);
        panel.add(BorderLayout.CENTER, msgHistScroller);
    }

    private enum Direction {SENT, RECV}

    private void display(SBMessage msg, Direction dir) {
        String s = String.format(
                dir == Direction.SENT ? "(%s) to [%s]: %s%n" : "(%s) from [%s]: %s%n",
                new SimpleDateFormat("HH:mm:ss").format(msg.timestamp), msg.sender, msg.body);
        msgHist.append(s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String msg = msgInput.getText();
        if (msg.equals("")) return;
        msgInput.setText("");
        try {
            SBMessage sent = client.sendInfo(msg);
            out.writeObject(sent);
            display(sent, Direction.SENT);
            SBMessage recv = (SBMessage) client.getIn().readObject();
            display(recv, Direction.RECV);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class UserListPanel extends JPanel implements ActionListener {

    public JPanel panel = new JPanel(new BorderLayout());
    private JPanel topLabel = new JPanel(new FlowLayout());
    private JButton refresh = new JButton("Refresh");
    private JLabel label = new JLabel("Online Users");
    private DefaultListModel<String> users = new DefaultListModel<>();
    private JList actualList = new JList(users);
    private JScrollPane userList = new JScrollPane(actualList);

    private ObjectOutputStream out;
    private ObjectInputStream in;
    private SBClient client;

    public UserListPanel(SBClient client) {

        this.client = client;
        this.in = client.getIn();
        this.out = client.getOut();

        refresh.addActionListener(this);
        actualList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        topLabel.add(label);
        topLabel.add(refresh);
        panel.add(BorderLayout.PAGE_START, topLabel);
        panel.add(BorderLayout.CENTER, userList);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            SBMessage query = client.userlist();
            out.writeObject(query);
            SBMessage list = (SBMessage) in.readObject();
            users.removeAllElements();
            Arrays.stream(list.getBody().split(" ")).forEach(users::addElement);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
