package pacman;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * A GridReadCreate object reads an input file containing lines of numbers
 * corresponding to grid pieces. It also then translates this file into an array
 * containing the correct numbers for each location, and draws those images.
 * GridReadCreate also keeps track of the player's current score and can
 * determine if the player has won at any time.
 */
public class GridReadCreate extends JPanel {

    /**
     * Name of the root path for grid data.
     */
    private final static String PATH = "res/grid/";

    /**
     * Name of the file where grid data is stored.
     */
    final static String FILE_NAME = PATH + "TestData.txt";

    /**
     * Array that contains the numbers for each portion of the 15x15 grid.
     */
    static int[][] arr = new int[15][15];

    /**
     * Current score of the game.
     */
    static int score = 0;

    /**
     * How many points each dot is worth.
     */
    int pointWorth = 10;

    /**
     * Number of dots in the grid.
     */
    int totalDots = 111;

    /**
     * Default constructor for GridReadCreate objects. Reads and translates a
     * file into the array.
     */
    GridReadCreate() {
        try {
            translateTo2DArray(readFile(FILE_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the correct image given an int (typically from the 2D integer
     * array arr[][]).
     *
     * @param input the integer reference for the desired file
     * @return the image corresponding to the supplied integer
     */
    static Image getImage(int input) {
        String path = String.format("%simage%d.png", PATH, input);
        return new ImageIcon(path).getImage();
    }

    /**
     * Reads a file into the system. Used in conjunction with
     * translateTo2DArray()
     *
     * @param name the name of the file to be read
     * @return a list of strings containing each line of the input file
     */
    List<String> readFile(String name) throws IOException {
        Path path = Paths.get(name);
        return Files.readAllLines(path);
    }

    /**
     * Changes the string List that is read in from the text file and puts the
     * info in the 2D int array. Used in conjunction with readFile().
     *
     * @param list the list of strings containing each line of file data
     */
    void translateTo2DArray(List<String> list) {
        for (int i = 0; i < 15; i++) {
            String[] split = list.get(i).split(" ");
            for (int j = 0; j < split.length; j++)
                arr[i][j] = Integer.parseInt(split[j]);
        }
    }

    /**
     * Iterates through the array and prints out the corresponding images.
     * Images are 30x30 pixels, so using the array locations (i,j) multiplied by
     * 30 generates the location of the images. Also prints player's current
     * score.
     *
     * @param g         Graphics object that can change font, font color, and
     *                  contains a method to print a string.
     * @param font      the font that the score will be printed in
     * @param fontColor the color that the font will be printed in
     */
    void printToScreen(Graphics g, Font font, Color fontColor) {
        int iMax = arr.length;
        int jMax = arr[0].length;
        Graphics2D g2d = (Graphics2D) g;
        for (int i = 0; i < iMax; i++) {
            for (int j = 0; j < jMax; j++) {
                // REPLACE below with call to other function that will print an image.
                // see getImage(int) above, which returns the appropriate image given an int
                g2d.drawImage(getImage(arr[j][i]), i * 30, j * 30, this);
            }
        }
        g.setFont(font);
        g.setColor(fontColor);
        String s = "Score: " + score;
        g.drawString(s, 20, 20);
    }

    /**
     * Uses the pacman position to redraw the board by replacing dots with blank
     * spaces when the pacman reaches that space.
     *
     * @param pacPosX the pacman character's x-position
     * @param pacPosY the pacman character's y-position
     */
    void update(int pacPosX, int pacPosY) {
        int x = pacPosX / 30, y = pacPosY / 30;
        if (arr[y][x] == 1) {
            arr[y][x] = 0;
            score += pointWorth;
            totalDots--;
        }
    }

    /**
     * Determines if the game has been won.
     *
     * @return true if the game has been won; false otherwise
     */
    boolean winCondition() {
        return totalDots == 0;
    }
}
