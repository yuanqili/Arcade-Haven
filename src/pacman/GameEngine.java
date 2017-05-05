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
    int delay=10; //controls the speed
    boolean running = true;
    int width =450; //sets the width of the canvas
    int height = 490; // sets height of the canvas
    int state;
    int frameCount;
    int animationRate = 5; //the speed in which the pacman character changes animation

    //The font to be used
    private static Font font = new Font("Times New Roman", Font.BOLD, 14);

    //color used for the text
    Color fontColor = new Color(255, 54, 42);

    GridReadCreate grid = new GridReadCreate();
    pacman pac = new pacman(grid);
    ghost gho = new ghost (grid, 300,30);

    int ghostNum = 1; //number of ghosts
    int[] ghostsX = new int[1]; //x position of ghosts
    int[] ghostsY = new int[1]; //y position of ghosts

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
            while(!(grid.winCondition()) && !(pac.lossCondition()) && running)
            {
                update();
                revalidate();
                repaint();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ghostsX[0] = gho.x;
                ghostsY[0] = gho.y;
            }
            revalidate();
            repaint();
        }
        setVisible(false); //will only reach here once the game stops running
    }

    //displays the winning screen
    private void winScreen(Graphics g)
    {
        g.drawString("YOU WON!", 190, 220);
    }
    private void lossScreen(Graphics g)
    {
        g.drawString("YOU LOST!", 190, 220);
    }


    void update() //updates the frames
    {
        pac.updateCharacter(ghostsX, ghostsY, ghostNum);
        gho.updateCharacter();
        grid.update(pac.x, pac.y);
        frameCount++;
        state = (frameCount/animationRate) % 4;
    }

    public void draw(Graphics g) //draws the frame
    {
        //Graphics g = getGraphics();
        Graphics2D g2d = (Graphics2D) g;

        //g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
        grid.printToScreen(g, font, fontColor);
        pac.drawPac(g2d, state);
        gho.drawGhost(g2d);
        if(grid.winCondition())
        {
            winScreen(g);
        }
        if(pac.lossCondition())
        {
            lossScreen(g);
        }
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