package chat;


import javax.swing.*;
import java.awt.*;

public class chat_box {
    JPanel main_panel;

    public chat_box() {
        main_panel = new JPanel(new BorderLayout());
        main_panel.add(BorderLayout.LINE_START, new user_list().main_panel);
        main_panel.add(BorderLayout.CENTER, new chat_pane().main_panel);
        main_panel.setBackground(Color.BLACK);
        main_panel.setForeground(Color.BLUE);
    }

    public static void main(String args[]) {
        JFrame chat = new JFrame("Chat Box");
        chat.setContentPane(new chat_box().main_panel);
        chat.setSize(800, 400);
        chat.setVisible(true);
        chat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
