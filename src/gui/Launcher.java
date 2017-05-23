package gui;

import pacman.GameEngine;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Launcher implements ActionListener {

    public JFrame frame = new JFrame();
    private JPanel main;
    private JButton PACMANButton;

    public Launcher() {
        frame.add(main);
        frame.setSize(640, 480);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);

        PACMANButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setVisible(false);
        GameEngine game = new GameEngine();
        game.setUp();
        game.run();
    }

    public static void main(String[] args) {
        new Launcher();
    }
}
