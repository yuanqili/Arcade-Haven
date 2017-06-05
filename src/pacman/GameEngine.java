package pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Stack;

/**
 * The class GameEngine is used to run the main game loop, set up the frame, and
 * draw the characters and the grid onto the frame. It also implements.
 */
public class GameEngine extends JPanel implements ActionListener {

    private JFrame frame = new JFrame();

    /**
     * Controls the speed of the game. The lower the value, the faster the game
     * goes by.
     */
    int delay = 10;

    /**
     * A variable that keeps track of whether the game is running or not.
     */
    boolean running = true;

    /**
     * Sets the width of the canvas.
     */
    int width = 450;

    /**
     * Sets the height of the canvas.
     */
    int height = 490;

    /**
     * The state that the pacman is in which is used to animate the pacman
     * character.
     */
    int state;

    /**
     * Keeps track of the numbers of loops the game has executed.
     */
    int frameCount;

    /**
     * the speed in which the pacman character changes animation. It is
     * inversely proportional to the speed.
     */
    int animationRate = 5;

    /**
     * A variable to keep track of whether the game is paused.
     */
    boolean pause = false;

    /**
     * A variable to check whether the game has began.
     */
    boolean start = false;

    /**
     * A variable to check if the pacman character is alive.
     */
    boolean alive = false;

    /**
     * Denotes the number of lives the pacman character has.
     */
    int lives = 3;

    boolean reset = false;

    /**
     * The font to be used for the in game text.
     */
    private static Font font = new Font("Times New Roman", Font.BOLD, 14);

    /**
     * The color used for the in game text.
     */
    Color fontColor = new Color(255, 54, 42);

    /**
     * An instance of GridReadCreate which represents the grid for the game.
     */
    GridReadCreate grid = new GridReadCreate();

    /**
     * An instance of Pacman, containing the character variables and methods.
     */
    Pacman pac;

    /**
     * An instance of Ghost, containing the character variables and methods for
     * the AI.
     */
    Ghost gho1;
    Ghost gho2;
    Ghost gho3;
    Ghost gho4;

    /**
     * The number of ghosts present in the game.
     */
    int ghostNum = 4;

    /**
     * An array of the x positions of the ghosts.
     */
    int[] ghostsX = new int[ghostNum];

    /**
     * An array of the y positions of the ghosts.
     */
    int[] ghostsY = new int[ghostNum];

    /**
     * An instance of ImageLoader which is in charge of loading the images for
     * all the characters.
     **/
    ImageLoader imgLdr= new ImageLoader();

    /**
     * A linked list of Items that keeps track of the currently active Items.
     */
    LinkedList<Item> activeItem = new LinkedList<>();

    /**
     * A class that only includes a long type value. It is used to construct a linked list.
     */
    class Time{
        private long value = 0;
    }

    /**
     * A linked list that keeps track of when the active item was activated
     */
    LinkedList<Time> itemStart = new LinkedList();

    /**
     * A decrementing integer that keeps track of how much time left before the player loses
     */
    int[] timeLimit = {60};

    /**
     * A constant time limit that is used to reset the timer when the player resets the game
     */
    final int constTimeLimit = 60;

    /**
     * This variable is changed to false once the bonus time score is added to the total score, to avoid infinitely incrementing the score.
     */
    boolean firstGameOver = true;

    /**
     * Overrides JPanel's paintComponent to draw the characters and the grid
     * onto the frame.
     *
     * @param g Graphics object that needs to be passed to the paintComponent class of JPanel.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    /**
     * Contains a function call to frameSetup() and characterSetup() to set up
     * the various components of the game. Also creates an instance of
     * ImageLoader to load all the character images.
     */
    public void setup() {
        frameSetup();
        characterSetup();
    }

    /**
     * Creates the frame, sets the title, size and close operation.
     */
    void frameSetup() {
        timeLimit[0] = constTimeLimit;
        firstGameOver = true;
        frame.setTitle("Pac-Man");
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
        frame.add(this);
        frame.setFocusable(true);
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (!running) return;
                switch (key) {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_UP:
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
                    case KeyEvent.VK_R:
                        reset = true;
                        frameCount=0;
                        break;
                    case KeyEvent.VK_SPACE:
                        start = true;
                        break;
                }
            }
        });
        frame.setVisible(true);
    }

    /**
     * resets the pacman character to being alive and recalls the constructor of
     * all the characters with their initial positions.
     */
    void characterSetup() {
        alive = true;
        start = false;
        reset = false;
        pac  = new Pacman(grid, lives, imgLdr.pacmanL, imgLdr.pacmanU, imgLdr.pacmanR, imgLdr.pacmanD);
        gho1 = new Ghost(grid, 300, 30, imgLdr.ghostImages[0]);
        gho2 = new Ghost(grid, 30, 210, imgLdr.ghostImages[1]);
        gho3 = new Ghost(grid, 120, 390, imgLdr.ghostImages[2]);
        gho4 = new Ghost(grid, 360, 240, imgLdr.ghostImages[3]);
    }

    /**
     * Runs the game loop if the winning and losing conditions have not been met
     * yet, the game is not paused, the game is running, the game has started,
     * and Pacman is alive. If Pacman is dead it updates the number of lives and
     * resets the characters and the starting prompt. It repaints the frame as
     * long as the game is running, whether or not the other conditions are met.
     * Once the game stops running, the window is closed.
     */
    public void run() {
        while (running) {
            while (!grid.winCondition() && !pac.lossCondition() && (timeLimit[0] - (frameCount/100)) > 0
                    && !pause && start && running && alive) {
                update();
                revalidate();
                repaint();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ghostsX[0] = gho1.x;
                ghostsY[0] = gho1.y;
                ghostsX[1] = gho2.x;
                ghostsY[1] = gho2.y;
                ghostsX[2] = gho3.x;
                ghostsY[2] = gho3.y;
                ghostsX[3] = gho4.x;
                ghostsY[3] = gho4.y;
            }
            if (!alive && lives > 1 && (timeLimit[0] - (frameCount/100)) >0) {
                lives--;
                ghostsX = new int[]{300, 30, 120, 360};
                ghostsY = new int[]{30, 210, 390, 240};
                if(activeItem.peekFirst()!=null) {
                    deactivateAllItems();
                }
                characterSetup();
                reset = false;
            } else if (reset && (pac.lossCondition()|| (timeLimit[0] - (frameCount/100)) <=0)) {
                lives = 3;
                ghostsX = new int[]{300, 30, 120, 360};
                ghostsY = new int[]{30, 210, 390, 240};
                if(activeItem.peekFirst()!=null)
                    deactivateAllItems();
                characterSetup();
                grid = new GridReadCreate();
                grid.score = 0;
            }
            revalidate();
            repaint();
        }
        // will only reach here once the game stops running
        setVisible(false);
    }


    /**
     * Displays the winning screen.
     *
     * @param g Graphics object that contains a method for drawing strings.
     */
    private void winScreen(Graphics g) {
        g.setColor(new Color(220, 200, 25));
        g.fillRect(50 - 3, 450 / 2 - 30 - 3, 450 - 94, 56);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(50, 450 / 2 - 30, 450 - 100, 50);
        g.setColor(new Color(220, 200, 25));
        //grid.score += (timeLimit - (frameCount/100)) * 10;
        String s = "Score: " + grid.score;
        g.drawString(s, 20, 20);
        g.drawString("YOU WON!", 190, 220);
    }

    /**
     * Displays the losing screen.
     *
     * @param g Graphics object that contains a method for drawing strings.
     */
    private void lossScreen(Graphics g) {
        g.setColor(new Color(220, 200, 25));
        g.fillRect(50 - 3, 450 / 2 - 30 - 3, 450 - 94, 56 + 30);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(50, 450 / 2 - 30, 450 - 100, 50 + 30);
        g.setColor(new Color(220, 200, 25));
        g.drawString("YOU LOST!", 190, 220);
        g.drawString("PRESS SPACE TO CONTINUE", 100, 250);
    }

    /**
     * Displays the pause screen.
     *
     * @param g Graphics object that contains a method for drawing strings.
     */
    private void pauseScreen(Graphics g) {
        g.setColor(new Color(220, 200, 25));
        g.fillRect(50 - 3, 450 / 2 - 30 - 3, 450 - 94, 56 + 30);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(50, 450 / 2 - 30, 450 - 100, 50 + 30);
        g.setColor(new Color(220, 200, 25));
        g.drawString("PAUSE", 190, 220);
        g.drawString("PRESS ARROW KEYS TO UNPAUSE", 100, 250);
    }

    /**
     * Displays the start screen.
     *
     * @param g Graphics object that contains a method for drawing strings.
     */
    private void startScreen(Graphics g) {
        g.setColor(new Color(220, 200, 25));
        g.fillRect(50 - 3, 450 / 2 - 30 - 3, 450 - 94, 56);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(50, 450 / 2 - 30, 450 - 100, 50);
        g.setColor(new Color(220, 200, 25));
        g.drawString("PRESS SPACE TO START", 150, 227);
    }

    /**
     * Displays the reset screen.
     *
     * @param g Graphics object that contains a method for drawing strings.
     */
    private void resetScreen(Graphics g) {
        if(activeItem.peekFirst()!=null)
            deactivateAllItems();
        g.setColor(new Color(220, 200, 25));
        g.fillRect(50 - 3, 450 / 2 - 30 - 3, 450 - 94, 56 + 30);
        g.setColor(new Color(0, 0, 0));
        g.fillRect(50, 450 / 2 - 30, 450 - 100, 50 + 30);
        g.setColor(new Color(220, 200, 25));
        g.drawString("YOU LOST!", 190, 220);
        g.drawString("PRESS R TO RESTART GAME", 130, 250);
    }

    /**
     * Updates all the characters as well as the grid. Also updates whether or
     * not Pacman as well as the state variable, then increments the frameCount.
     */
    void update() {
        alive = pac.updateCharacter(ghostsX, ghostsY, ghostNum);
        gho1.updateCharacter();
        gho2.updateCharacter();
        gho3.updateCharacter();
        gho4.updateCharacter();
        Item itemType = grid.update(pac);
        if(itemType != null)
            activateItem(itemType);
        frameCount++;
        state = (frameCount / animationRate) % 4;
        for(int i = 0; i< itemStart.size(); i++) {
            if (((frameCount - itemStart.get(i).value) * 10) >= activeItem.get(i).duration) {
                deactivateItem(i);
                i--;
            }
        }
    }

    /**
     * Invokes the deactivation method of the active item.
     */
    private void deactivateItem(int index) {
        System.out.print(index);
        activeItem.get(index).deactivate(grid, pac);
        activeItem.remove(index);
        itemStart.remove(index);
    }

    /**
     * Deactivates all active items
     */
    private void deactivateAllItems() {
        while(activeItem.peekFirst()!=null)
        {
            deactivateItem(0);
        }
    }

    /**
     * Invokes the activation method of an item and records the time the item was activated.
     * @param itemType Denotes the type of item that should be activated.
     */
    private void activateItem(Item itemType) {
        activeItem.addLast(itemType);
        activeItem.getLast().activate(grid, pac, timeLimit);
        Time t = new Time();
        t.value = frameCount;
        itemStart.addLast(t);
    }

    /**
     * Calls the draw functions of the grid, pacman, and ghosts. Also draws the
     * number of remaining lives on the top right of the screen. It checks if
     * the winning or losing condition is met, if so it calls winScreen or
     * lossScreen respectively. If the pause variable is true or the start
     * variable is false, it calls the pauseScreen or startScreen functions
     * respectively.
     *
     * @param g Graphics instance which is passed to the printToScreen method of
     *          grid and used to create an instance of Graphics2D which is
     *          passed into the character classes.
     */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.fillRect(0, 0, width, height);
        grid.printToScreen(g, font, fontColor);
        pac.drawPac(g2d, state);
        gho1.drawGhost(g2d);
        gho2.drawGhost(g2d);
        gho3.drawGhost(g2d);
        gho4.drawGhost(g2d);
        String l = "Lives: " + lives;
        String l2 = "Time: " + (timeLimit[0] - (frameCount/100));
        String s = "Score: " + grid.score;
        g.drawString(s, 20, 20);
        g.drawString(l, 360, 20);
        g.drawString(l2, 180, 20);
        if (grid.winCondition()){
            if(firstGameOver) {
                grid.score += (timeLimit[0] - (frameCount / 100)) * 10;
                firstGameOver=false;
                System.out.print(grid.score);
            }
            winScreen(g);
        }
        if (pac.lossCondition() || (timeLimit[0] - (frameCount/100)) <=0) {
            if(firstGameOver) {
                System.out.print(grid.score);
                firstGameOver = false;
            }
            resetScreen(g);
        }
        if (pause)
            pauseScreen(g);
        if (!start)
            startScreen(g);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public static void main(String[] args) {
        GameEngine game = new GameEngine();
        game.setup();
        game.run();
    }
}
