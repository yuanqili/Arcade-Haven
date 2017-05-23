package pacman;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * The class of ghost, which creates the object of ghost, updates the object of
 * ghost and draws the object of ghost
 */
public class Ghost {

    /**
     * Direction of ghost object
     */
    int direction = 3;

    /**
     * X coordinate of the ghost
     */
    int x = 60;

    /**
     * Y coordinate of the ghost
     */
    int y = 30;

    /**
     * Step of the ghost movement
     */
    int step = 1;

    /**
     * Height limitation of movement
     */
    int height = 400;

    /**
     * Array that stores the map data
     */
    int[][] array = {};

    /**
     * Ghost image
     */
    BufferedImage ghostImage;

    /**
     * Constructor of Ghost class, initiate ghost object
     *
     * @param grc   GridReadCreate object
     * @param x_cor initial x coordinate of the ghost
     * @param y_cor initial y coordinate of the ghost
     * @param image image of the ghost object
     */
    public Ghost(GridReadCreate grc, int x_cor, int y_cor, BufferedImage image) {
        ghostImage = image;
        int xsize = grc.arr.length;
        int ysize = grc.arr[0].length;
        x = x_cor;
        y = y_cor;
        array = new int[xsize][ysize];

        for (int i = 0; i < xsize; i++)
            for (int j = 0; j < ysize; j++)
                array[j][i] = grc.arr[i][j];
    }

    /**
     * Gets random direction for the ghost.
     *
     * @return Number that corresponds the direction
     */
    private int getDirection() {
        return new Random().nextInt(4);
    }

    /**
     * Updates the position of the ghost based on viable paths.
     */
    public void updateCharacter() {
        int[] checkArr = {0, 0, 0, 0};
        int xx = x / 30, yy = y / 30;
        boolean leftedge = xx == 0, rightedge = xx == 14;
        switch (x) {
            case -29:
                x = 449;
                leftedge = false;
                rightedge = true;
                break;
            case 449:
                x = -29;
                leftedge = true;
                rightedge = false;
                break;
        }

        //handles right/left side wrapping
        if (rightedge || array[(x + 30) / 30][yy] <= 1 && y % 30 == 0)
            checkArr[0]++;
        if (array[xx][(y + 30) / 30] <= 1 && x % 30 == 0)
            checkArr[1]++;
        if (leftedge || array[(x - 1) / 30][yy] <= 1 && y % 30 == 0)
            checkArr[2]++;
        if (array[xx][(y - 1) / 30] <= 1 && x % 30 == 0)
            checkArr[3]++;

        // check is the index of one of the 1's in checkArr
        int check = 10;
        while (check == 10) {
            if (checkArr[direction] == 1)
                check = direction;
            else
                direction = getDirection();
        }
        switch (check) {
            case 0:
                x += step;
                break;
            case 1:
                y += step;
                if (y > height) y = height;
                break;
            case 2:
                x -= step;
                break;
            case 3:
                y -= step;
                if (y < 0) y = 0;
                break;
        }
    }

    /**
     * Draws the ghost onto the graphic board.
     *
     * @param g Graphics2D object g
     */
    public void drawGhost(Graphics2D g) {
        g.drawImage(ghostImage, x, y, null);
    }

}
