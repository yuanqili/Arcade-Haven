package chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Jose on 5/11/2017.
 */
public class user_list extends JPanel {

    JPanel main_panel;
    JPanel top_label;
    JButton refresh;
    JLabel label;
    DefaultListModel<String> users;
    JScrollPane userList;
    JList actual_list;

    public user_list() {
        //Initialize main panel to hold components of the user list
        main_panel = new JPanel(new BorderLayout());
        main_panel.setBackground(Color.BLACK);
        main_panel.setForeground(Color.blue);

        //Holds the Label and the refresh button for the user list
        top_label = new JPanel(new FlowLayout());
        top_label.setBackground(Color.BLACK);
        top_label.setForeground(Color.BLUE);

        refresh = new JButton("R");
        refresh.setBackground(Color.BLACK);
        refresh.setForeground(Color.BLUE);
        refresh.addActionListener(new refreshButtonListener());

        label = new JLabel("Online Users");
        label.setBackground(Color.BLACK);
        label.setForeground(Color.BLUE);

        //user list is a held in a DefaultList and JList
        users = new DefaultListModel<String>();
        actual_list = new JList(users);
        actual_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        actual_list.setBackground(Color.BLACK);
        actual_list.setForeground(Color.BLUE);
        userList = new JScrollPane(new JList(users));
        userList.setBackground(Color.BLACK);
        userList.setForeground(Color.BLUE);

        //Add both components of the label to the user list
        top_label.add(label);
        top_label.add(refresh);

        main_panel.add(BorderLayout.PAGE_START,top_label);
        main_panel.add(BorderLayout.CENTER, userList);
    }
    public static void main(String args[]){
        JFrame chat = new JFrame("Chat Box");
        chat.setContentPane(new user_list().main_panel);
        chat.setSize(200, 600);
        chat.setVisible(true);
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private class refreshButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            //The should connect to the server and get an updated list of who is online.
            users.addElement("Chris");
            users.addElement("Steve");
        }
    }
}
