package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jose on 5/11/2017.
 */
public class chat_pane implements ActionListener{
    JPanel main_panel;
    JButton     sendMessage;
    JTextField  messageBox;
    JTextArea   chatBox;

    public chat_pane(){

        //main_panel will the be the main container for the chat window
        main_panel = new JPanel();
        main_panel.setLayout(new BorderLayout());

        //SouthPanel will be the Panel that will hold the text box for users to type their message in and it
        //will hold the "SEND" button so the user will be able to send their message to the other user.
        JPanel southPanel = new JPanel();
        southPanel.setBackground(Color.BLACK);
        southPanel.setLayout(new GridBagLayout());

        //This is where the user will type the message
        messageBox = new JTextField(30);
        messageBox.requestFocusInWindow();
        messageBox.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) sendReceive();
            }
        });
        messageBox.setBackground(Color.BLACK);
        messageBox.setForeground(Color.BLUE);

        //The actual send button for the user to send the message to intended user
        sendMessage = new JButton("Send");
        sendMessage.addActionListener(this);
        sendMessage.setBackground(Color.black);
        sendMessage.setForeground(Color.blue);

        //The actual chat window that will contain all the messages for the user and for the entire public.
        chatBox = new JTextArea();
        chatBox.setEditable(false);
        chatBox.setFont(new Font("Serif", Font.PLAIN, 15));
        chatBox.setLineWrap(true);
        chatBox.setBackground(Color.BLACK);
        chatBox.setForeground(Color.BLUE);

        //Add the chat box to the main_panel
        main_panel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        //These constraints help properly set the messageBox and the Send button at the bottom of the main panel
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

        //adding the messageBox and sendMessage button to the panel which will then be added to the main panel
        southPanel.add(messageBox, left);
        southPanel.add(sendMessage, right);

        main_panel.add(BorderLayout.SOUTH, southPanel);
    }

    @Override
    public void actionPerformed(ActionEvent e) { sendReceive();}

    private void sendReceive() {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        Date dateobj = new Date();
        System.out.println(df.format(dateobj));
        if(!messageBox.getText().equals("")) {
            chatBox.setText(chatBox.getText() + "<" + df.format(dateobj) + "> " + "Username: " + messageBox.getText() + "\n");
        }
        messageBox.setText("");
    }
    public static void main(String args[]) {
        JFrame chat = new JFrame("Chat Box");
        chat.setContentPane(new chat_pane().main_panel);
        chat.setSize(800, 400);
        chat.setVisible(true);
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
