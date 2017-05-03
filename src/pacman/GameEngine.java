package pacman;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class GameEngine extends JPanel implements ActionListener
{
    private JFrame frame;
    int delay=100;
    boolean running = true;
    int width =450; //sets the width of the canvas
    int height = 470; // sets height of the canvas

    GridReadCreate grid = new GridReadCreate();
    pacman pac = new pacman(grid);
    ghost gho = new ghost (grid, 60,30);

    public static void main(String[] args)
    {
        GameEngine game = new GameEngine();
        game.setUp();
        game.run();
        System.exit(0);
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    /*public GameEngine()
    {
        setMinimumSize(new Dimension(width, height));
        setMaximumSize(new Dimension(width, height));
        setPreferredSize(new Dimension(width, height));
        frame = new JFrame("Pac-Man");
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.pack();
        frame.setVisible(true);
    }*/
    void setUp()
    {
        frame = new JFrame();
        frame.setTitle("Pac-Man");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setVisible(true);
        frame.addKeyListener(new TAdapter());
        frame.setFocusable(true);
    }

    void run() //starts the game
    {
        //setUp();
        while(running) //checks if the game is stopped yet
        {
            update();
            revalidate();
            repaint();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        setVisible(false); //will only reach here once the game stops running
    }

    void update() //updates the frames
    {
        pac.updateCharacter();
        gho.updateCharacter();
    }

    public void draw(Graphics g) //draws the frame
    {
        //Graphics g = getGraphics();
        Graphics2D g2d = (Graphics2D) g;

        //g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        grid.printToScreen(g);
        pac.drawPac(g2d);
        gho.drawGhost(g2d);
        //repaint();
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {

            int key = e.getKeyCode();

            if (running) {
                if (key == KeyEvent.VK_LEFT) {
                    pac.direction = key;
                } else if (key == KeyEvent.VK_RIGHT) {
                    pac.direction = key;
                } else if (key == KeyEvent.VK_UP) {
                    pac.direction = key;
                } else if (key == KeyEvent.VK_DOWN) {
                    pac.direction = key;
                } else if (key == KeyEvent.VK_ESCAPE) {
                    running = false;
                }
            } else {
                if (key == 's' || key == 'S') {
                    running = true;
                    run();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}