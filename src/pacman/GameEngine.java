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

    /** controls the speed. */
    int delay=10;

    boolean running = true;
    int width =450; //sets the width of the canvas
    int height = 490; // sets height of the canvas
    int state;
    int frameCount;
    int animationRate = 5; //the speed in which the pacman character changes animation
    boolean pause = false; //a variable used to check if the game is paused
    boolean start = false; //a variable denoting whether or not the game began
    boolean alive = false; //checks if pacman is alive
    int lives = 3;
    String [] filenames = {"res/images/ghost_r.png", "res/images/ghost_o.png", "res/images/ghost_p.png", "res/images/ghost_b.png"};

    //The font to be used
    private static Font font = new Font("Times New Roman", Font.BOLD, 14);

    //color used for the text
    Color fontColor = new Color(255, 54, 42);

    GridReadCreate grid = new GridReadCreate();
    Pacman pac;
    Ghost gho;
    //Ghost gho2;
    //Ghost gho3;
    //Ghost gho4;

    int ghostNum = 4; //number of ghosts
    int[] ghostsX = new int[4]; //x position of ghosts
    int[] ghostsY = new int[4]; //y position of ghosts

    ImageLoader imgLdr;

    public static void main(String[] args) {
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

    public void setUp()
    {
        frameSetUp();
        imgLdr = new ImageLoader(filenames);
        characterSetUp();
    }

    void frameSetUp()
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

    void characterSetUp() {
        alive = true;
        start = false;
        pac = new Pacman(grid, lives, imgLdr.pacmanL, imgLdr.pacmanU, imgLdr.pacmanR, imgLdr.pacmanD);
        gho = new Ghost(grid,  300,  30, imgLdr.ghostImages[0]);
        //gho2 = new Ghost(grid,  30, 210, imgLdr.ghostImages[1]);
        //gho3 = new Ghost(grid, 120, 390, imgLdr.ghostImages[2]);
        //gho4 = new Ghost(grid, 360, 240, imgLdr.ghostImages[3]);
    }

    public void run() {
        //setUp();
        while(running) //checks if the game is stopped yet
        {
            while(!(grid.winCondition())  && !(pac.lossCondition()) && !pause && start && running && alive)
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
//                ghostsX[1] = gho2.x;
//                ghostsY[1] = gho2.y;
//                ghostsX[2] = gho3.x;
//                ghostsY[2] = gho3.y;
//                ghostsX[3] = gho4.x;
//                ghostsY[3] = gho4.y;
            }
            if(!alive && lives > 1)
            {
                lives--;
                characterSetUp();
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
    private void pauseScreen(Graphics g) { g.drawString("PAUSE", 190, 220); }
    private void startScreen(Graphics g) { g.drawString("PRESS SPACE TO START", 150, 220); }

    void update() {
        alive = pac.updateCharacter(ghostsX, ghostsY, ghostNum);
        gho.updateCharacter();
//        gho2.updateCharacter();
//        gho3.updateCharacter();
//        gho4.updateCharacter();
        grid.update(pac.x, pac.y);
        frameCount++;
        state = (frameCount/animationRate) % 4;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.fillRect(0, 0, width, height);
        grid.printToScreen(g, font, fontColor);
        pac.drawPac(g2d, state);
        gho.drawGhost(g2d);
//        gho2.drawGhost(g2d);
//        gho3.drawGhost(g2d);
//        gho4.drawGhost(g2d);
        String l = "Lives: " + lives;
        g.drawString(l, 360, 20);
        if(grid.winCondition())
            winScreen(g);
        if(pac.lossCondition())
            lossScreen(g);
        if(pause)
            pauseScreen(g);
        if(!start)
            startScreen(g);
    }

    class TAdapter extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (running) {
                switch (key) {
                    case KeyEvent.VK_LEFT:
                        pause = false;
                        pac.direction = key;
                        break;
                    case KeyEvent.VK_RIGHT:
                        pause = false;
                        pac.direction = key;
                        break;
                    case KeyEvent.VK_UP:
                        pause = false;
                        pac.direction = key;
                        break;
                    case KeyEvent.VK_DOWN:
                        pause = false;
                        pac.direction = key;
                        break;
                    case KeyEvent.VK_ESCAPE:
                        running = false;
                        break;
                    case KeyEvent.VK_P:
                        pause = true;
                        break;
                    case KeyEvent.VK_SPACE:
                        start = true;
                        break;
                }
            } else if (key == 's' || key == 'S') {
                running = true;
                run();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}