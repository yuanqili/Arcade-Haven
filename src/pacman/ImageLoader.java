package pacman;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * The class of ImageLoader deals with reading images that represent the
 * graphics of pacman and ghosts
 */
public class ImageLoader {

    /**
     * Name of the root path for image data.
     */
    private final static String PATH = "res/images/";

    /**
     * Left facing pacman image.
     */
    public BufferedImage[] pacmanL = new BufferedImage[4];

    /**
     * Right facing pacman image.
     */
    public BufferedImage[] pacmanR = new BufferedImage[4];

    /**
     * Down facing pacman image.
     */
    public BufferedImage[] pacmanD = new BufferedImage[4];

    /**
     * Up facing pacman image.
     */
    public BufferedImage[] pacmanU = new BufferedImage[4];

    /**
     * Images of different-color ghosts
     */
    public BufferedImage[] ghostImages = new BufferedImage[4];

    /**
     * Loads the images of pacman and ghosts.
     */
    public ImageLoader() {
        loadPacmanImages();
        loadGhostImages();
    }

    /**
     * Loads the image of the pacman.
     */
    private void loadPacmanImages() {
        try {
            pacmanL[0] = ImageIO.read(new File(PATH + "left1.png"));
            pacmanL[1] = ImageIO.read(new File(PATH + "left2.png"));
            pacmanL[2] = ImageIO.read(new File(PATH + "left3.png"));
            pacmanR[0] = ImageIO.read(new File(PATH + "right1.png"));
            pacmanR[1] = ImageIO.read(new File(PATH + "right2.png"));
            pacmanR[2] = ImageIO.read(new File(PATH + "right3.png"));
            pacmanD[0] = ImageIO.read(new File(PATH + "down1.png"));
            pacmanD[1] = ImageIO.read(new File(PATH + "down2.png"));
            pacmanD[2] = ImageIO.read(new File(PATH + "down3.png"));
            pacmanU[0] = ImageIO.read(new File(PATH + "up1.png"));
            pacmanU[1] = ImageIO.read(new File(PATH + "up2.png"));
            pacmanU[2] = ImageIO.read(new File(PATH + "up3.png"));
            pacmanL[3] = ImageIO.read(new File(PATH + "pacman.png"));
            pacmanR[3] = pacmanL[3];
            pacmanD[3] = pacmanL[3];
            pacmanU[3] = pacmanL[3];
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the images of the ghosts.
     */
    private void loadGhostImages() {
        try {
            ghostImages[0] = ImageIO.read(new File(PATH + "ghost_r.png"));
            ghostImages[1] = ImageIO.read(new File(PATH + "ghost_o.png"));
            ghostImages[2] = ImageIO.read(new File(PATH + "ghost_p.png"));
            ghostImages[3] = ImageIO.read(new File(PATH + "ghost_b.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
