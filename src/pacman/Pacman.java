package pacman;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * The class of pacman, which creates the object of pacman, updates the object
 * of pacman and draws the object of pacman.
 */
public class Pacman {
    /**
     * Direction is key input of moving direction.
     */
    int direction = KeyEvent.VK_RIGHT;
    int po_direction = KeyEvent.VK_RIGHT;
    int ctn_direction = KeyEvent.VK_RIGHT;

    /**
     * Checks if the pacman is stuck.
     */
    boolean stuck = false;

    /**
     * X coordinate of pacman.
     */
    int x = 30;

    /**
     * Y coordinate of pacman.
     */
    int y = 30;

    /**
     * Step of pacman movement.
     */
    int step = 1;

    /**
     * Height limitation of movement.
     */
    int height = 400;

    /**
     * Array that stores the map data.
     */
    int[][] array;

    /**
     * Remaining lives of pacman.
     */
    int lives = 3; //number of lives you have

    /**
     * Left facing pacman image.
     */
    private BufferedImage[] pacmanL = new BufferedImage[4];

    /**
     * Right facing pacman image.
     */
    private BufferedImage[] pacmanR = new BufferedImage[4];

    /**
     * Down facing pacman image.
     */
    private BufferedImage[] pacmanD = new BufferedImage[4];

    /**
     * Up facing pacman image.
     */
    private BufferedImage[] pacmanU = new BufferedImage[4];

    /**
     * Constructor for pacman class, initiate pacman object
     *
     * @param grc      GridReadCreate Object
     * @param numLives Number of lives remaining
     * @param L        Left facing pacman image
     * @param U        Up facing pacman image
     * @param R        Right facing pacman image
     * @param D        Down facing pacman image
     */
    public Pacman(GridReadCreate grc, int numLives,
                  BufferedImage[] L, BufferedImage[] U,
                  BufferedImage[] R, BufferedImage[] D) {
        pacmanL = L;
        pacmanD = D;
        pacmanR = R;
        pacmanU = U;
        int xsize = grc.arr.length;
        int ysize = grc.arr[0].length;
        array = new int[xsize][ysize];

        for (int i = 0; i < xsize; i++)
            for (int j = 0; j < ysize; j++)
                array[j][i] = grc.arr[i][j];
        lives = numLives;
    }

    /**
     * Update the pacman object's position
     *
     * @param ghostPosX Array of X positions of the ghosts
     * @param ghostPosY Array of Y positions of the ghosts
     * @param ghostNum  Numebr of ghosts
     * @return If the pacman is still alive, false means not dead
     */
    public boolean updateCharacter(int[] ghostPosX, int[] ghostPosY, int ghostNum) {
        int[] checkArr = {0, 0, 0, 0};
        int xx = x / 30, yy = y / 30;
        boolean leftedge = xx == 0;
        boolean rightedge = xx == 14;
        if (step == 1) {
            if (x <= -29) {
                x = 449;
                leftedge = false;
                rightedge = true;
            }
            else if (x >= 449) {
                x = -29;
                leftedge = true;
                rightedge = false;
            }
        }
        else {
            if (x <= -28) {
                x = 448;
                leftedge = false;
                rightedge = true;
            }
            else if (x >= 448) {
                x = -28;
                leftedge = true;
                rightedge = false;
            }
        }

        if (rightedge || array[(x + 30) / 30][yy] <= 1 && y % 30 == 0)
            checkArr[0]++;
        if (array[xx][(y + 30) / 30] <= 1 && x % 30 == 0)
            checkArr[1]++;
        if (leftedge || array[(x - 1) / 30][yy] <= 1 && y % 30 == 0)
            checkArr[2]++;
        if (array[xx][(y - 1) / 30] <= 1 && x % 30 == 0)
            checkArr[3]++;

        int temp_direction = direction;
        if (x % 30 == 0 && y % 30 == 0 && !stuck)
            temp_direction = po_direction;
        else if ((x % 30 != 0 || y % 30 != 0) && !stuck) {
            po_direction = direction;
            temp_direction = ctn_direction;
        } else if (x % 30 == 0 && y % 30 == 0 && stuck)
            temp_direction = direction;

        switch (temp_direction) {
            case KeyEvent.VK_RIGHT:
                if (checkArr[0] == 1) {
                    if (step == 1)
                        x += step;
                    else if (step == 2 && ((x+1)% 30 == 0 || (x-1)% 30 == 0 || (y+1)% 30 == 0 || (y-1)% 30 == 0))
                        x += (step-1);
                    else
                        x += step;
                    ctn_direction = KeyEvent.VK_RIGHT;
                    stuck = false;
                } else {
                    stuck = true;
                }
                break;
            case KeyEvent.VK_DOWN:
                if (checkArr[1] == 1) {
                    if (step == 1)
                        y += step;
                    else if (step == 2 && ((x+1)% 30 == 0 || (x-1)% 30 == 0 || (y+1)% 30 == 0 || (y-1)% 30 == 0))
                        y += (step-1);
                    else
                        y += step;
                    ctn_direction = KeyEvent.VK_DOWN;
                    stuck = false;
                } else {
                    stuck = true;
                }
                if (y > height)
                    y = height;
                break;
            case KeyEvent.VK_LEFT:
                if (checkArr[2] == 1) {
                    if (step == 1)
                        x -= step;
                    else if (step == 2 && ((x+1)% 30 == 0 || (x-1)% 30 == 0 || (y+1)% 30 == 0 || (y-1)% 30 == 0))
                        x -= (step-1);
                    else
                        x -= step;
                    ctn_direction = KeyEvent.VK_LEFT;
                    stuck = false;
                } else {
                    stuck = true;
                }
                break;
            case KeyEvent.VK_UP:
                if (checkArr[3] == 1) {
                    if (step == 1)
                        y -= step;
                    else if (step == 2 && ((x+1)% 30 == 0 || (x-1)% 30 == 0 || (y+1)% 30 == 0 || (y-1)% 30 == 0))
                        y -= (step-1);
                    else
                        y -= step;
                    ctn_direction = KeyEvent.VK_UP;
                    stuck = false;
                } else {
                    stuck = true;
                }
                if (y < 0)
                    y = 0;
                break;
        }
        return updateLives(ghostPosX, ghostPosY, ghostNum);
    }

    /**
     * Draws the pacman onto the graphic board.
     *
     * @param g     Graphic2D object g
     * @param state pacman facing information
     */
    public void drawPac(Graphics2D g, int state) {
        int temp_dir;
        temp_dir = x % 30 == 0 && y % 30 == 0 ? direction : ctn_direction;
        switch (temp_dir) {
            case KeyEvent.VK_RIGHT:
                g.drawImage(pacmanR[state], x, y, null);
                break;
            case KeyEvent.VK_DOWN:
                g.drawImage(pacmanD[state], x, y, null);
                break;
            case KeyEvent.VK_LEFT:
                g.drawImage(pacmanL[state], x, y, null);
                break;
            case KeyEvent.VK_UP:
                g.drawImage(pacmanU[state], x, y, null);
                break;
        }
    }

    /**
     * Updates the remaining lives of the pacman.
     *
     * @param ghostsX  Array of X positions of the ghosts
     * @param ghostsY  Array of Y positions of the ghosts
     * @param ghostNum Number of remaining ghosts
     * @return If the pacman is still alive, false means not dead
     */
    boolean updateLives(int[] ghostsX, int[] ghostsY, int ghostNum) {
        for (int i = 0; i < ghostNum; i++)
            if (x <= ghostsX[i] + 7 && x >= ghostsX[i] - 7
                    && y <= ghostsY[i] + 7 && y >= ghostsY[i] - 7) {
                lives--;
                return false;
            }
        return true;
    }

    /**
     * Check if the game is lost
     *
     * @return True if game is lost
     */
    boolean lossCondition() {
        return lives <= 0;
    }
}