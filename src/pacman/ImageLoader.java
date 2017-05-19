package pacman;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhanchengqian on 2017/5/18.
 */
public class ImageLoader {

    public BufferedImage[] pacmanL = new BufferedImage[4];
    public BufferedImage[] pacmanR = new BufferedImage[4];
    public BufferedImage[] pacmanD = new BufferedImage[4];
    public BufferedImage[] pacmanU = new BufferedImage[4];
    public BufferedImage[] ghostImages = new BufferedImage[4];

    public ImageLoader(String [] fileNames){
        pacmanLoadImages();
        ghostLoadImages(fileNames);
    }

    public void pacmanLoadImages() {
        /*File here = new File(".");
        try {
            System.out.println(here.getCanonicalPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        try {
            pacmanL[0] = ImageIO.read(new File("res/images/left1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanL[1] = ImageIO.read(new File("res/images/left2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }try {
            pacmanL[2] = ImageIO.read(new File("res/images/left3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanR[0] = ImageIO.read(new File("res/images/right1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanR[1] = ImageIO.read(new File("res/images/right2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanR[2] = ImageIO.read(new File("res/images/right3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanD[0] = ImageIO.read(new File("res/images/down1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanD[1] = ImageIO.read(new File("res/images/down2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanD[2] = ImageIO.read(new File("res/images/down3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanU[0] = ImageIO.read(new File("res/images/up1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanU[1] = ImageIO.read(new File("res/images/up2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanU[2] = ImageIO.read(new File("res/images/up3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanL[3] = ImageIO.read(new File("res/images/pacman.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pacmanR[3] = pacmanL[3];
        pacmanD[3] = pacmanL[3];
        pacmanU[3] = pacmanL[3];
    }

    public void ghostLoadImages(String [] fileNames) {
        try {
            for (int i=0; i<fileNames.length; i++) {
                ghostImages[i] = ImageIO.read(new File(fileNames[i]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
