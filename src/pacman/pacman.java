package pacman;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class pacman {
    int direction, po_direction, ctn_direction;
    boolean stuck = false;
    int x=30;
    int y=30;
    int step =1;
    int width= 800;
    int height = 400;
    private BufferedImage[] pacmanL = new BufferedImage[4];
    private BufferedImage[] pacmanR = new BufferedImage[4];
    private BufferedImage[] pacmanD = new BufferedImage[4];
    private BufferedImage[] pacmanU = new BufferedImage[4];
    int[][] array;
    int lives = 1; //number of lives you have

    void prt(String s){
        System.out.print(s);
    }

    public pacman(GridReadCreate grc)
    {
        direction = KeyEvent.VK_RIGHT;
        po_direction = KeyEvent.VK_RIGHT;
        ctn_direction = KeyEvent.VK_RIGHT;
        loadImages();
        int xsize = grc.arr.length; int ysize = grc.arr[0].length;
        array = new int [xsize][ysize];

        for (int i=0; i<xsize; i++){
            for (int j=0; j<ysize; j++){
                array[j][i] = grc.arr[i][j];
            }
        }
    }
    public void loadImages() {
        /*File here = new File(".");
        try {
            System.out.println(here.getCanonicalPath());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        */
        try {
            pacmanL[0] = ImageIO.read(new File("src/pacman/images/left1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanL[1] = ImageIO.read(new File("src/pacman/images/left2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }try {
            pacmanL[2] = ImageIO.read(new File("src/pacman/images/left3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanR[0] = ImageIO.read(new File("src/pacman/images/right1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanR[1] = ImageIO.read(new File("src/pacman/images/right2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanR[2] = ImageIO.read(new File("src/pacman/images/right3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanD[0] = ImageIO.read(new File("src/pacman/images/down1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanD[1] = ImageIO.read(new File("src/pacman/images/down2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanD[2] = ImageIO.read(new File("src/pacman/images/down3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanU[0] = ImageIO.read(new File("src/pacman/images/up1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanU[1] = ImageIO.read(new File("src/pacman/images/up2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanU[2] = ImageIO.read(new File("src/pacman/images/up3.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanL[3] = ImageIO.read(new File("src/pacman/images/pacman.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        pacmanR[3] = pacmanL[3];
        pacmanD[3] = pacmanL[3];
        pacmanU[3] = pacmanL[3];
    }

    public void updateCharacter(int[] ghostPosX, int[] ghostPosY, int ghostNum)
    {
        updateLives(ghostPosX, ghostPosY, ghostNum);
        int[] checkArr = {0, 0, 0, 0}; // should add range checker here
        int xx = x / 30, yy = y / 30;
        if (array[(x + 30) / 30][yy] <= 1 && y % 30 == 0) {
            checkArr[0]++;
        }//Right
        if (array[xx][(y + 30) / 30] <= 1 && x % 30 == 0) {
            checkArr[1]++;
        }//Down
        if (array[(x - 1) / 30][yy] <= 1 && y % 30 == 0) {
            checkArr[2]++;
        }//Left
        if (array[xx][(y - 1) / 30] <= 1 && x % 30 == 0) {
            checkArr[3]++;
        }//Up

        int temp_direction = direction;
        if (x%30 == 0 && y%30 == 0 && !stuck){
            temp_direction = po_direction;
        }
        else if ((x%30 != 0 || y%30 != 0) && !stuck){
            po_direction = direction;
            temp_direction = ctn_direction;
        }
        else if (x%30 == 0 && y%30 == 0 && stuck){
            temp_direction = direction;
        }

        switch (temp_direction) {
            case KeyEvent.VK_RIGHT:
                if (checkArr[0] == 1) {
                    x += step;
                    ctn_direction = KeyEvent.VK_RIGHT;
                    stuck = false;
                }
                else {
                    stuck = true;
                }
                if (x > width)
                    x = width;
                break;
            case KeyEvent.VK_DOWN:
                if (checkArr[1] == 1) {
                    y += step;
                    ctn_direction = KeyEvent.VK_DOWN;
                    stuck = false;
                }
                else {
                    stuck = true;
                }
                if (y > height)
                    y = height;
                break;
            case KeyEvent.VK_LEFT:
                if (checkArr[2] == 1) {
                    x -= step;
                    ctn_direction = KeyEvent.VK_LEFT;
                    stuck = false;
                }
                else{
                    stuck = true;
                }
                if (x < 0)
                    x = 0;
                break;
            case KeyEvent.VK_UP:
                if (checkArr[3] == 1) {
                    y -= step;
                    ctn_direction = KeyEvent.VK_UP;
                    stuck = false;
                }
                else{
                    stuck = true;
                }
                if (y < 0)
                    y = 0;
                break;
        }
    }
    public void drawPac(Graphics2D g, int state)
    {
        int temp_dir;
        if (x%30 == 0 && y%30 == 0){
            temp_dir = direction;
        }
        else {
            temp_dir = ctn_direction;
        }
        switch(temp_dir)
        {
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

    void updateLives(int[] ghostPosX, int[] ghostPosY, int ghostNum)
    {
        for(int i =0; i < ghostNum; i++)
        {
            if(x == ghostPosX[i] && y == ghostPosY[i])
            {
                lives--;
            }

        }
    }

    //checks if the playerLost
    boolean lossCondition()
    {
        if(lives<=0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}