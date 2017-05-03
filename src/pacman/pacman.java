package pacman;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class pacman {
    int direction;
    int x=30;
    int y=30;
    int step =30;
    int width= 800;
    int height = 400;
    BufferedImage pacmanL;
    BufferedImage pacmanR;
    BufferedImage pacmanD;
    BufferedImage pacmanU;
    int[][] array;

    void prt(String s){
        System.out.print(s);
    }

    public pacman(GridReadCreate grc)
    {
        direction = KeyEvent.VK_RIGHT;
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
            pacmanL = ImageIO.read(new File("src/pacman/images/left1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pacmanR = ImageIO.read(new File("src/pacman/images/right1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }try {
            pacmanD = ImageIO.read(new File("src/pacman/images/down1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }try {
            pacmanU = ImageIO.read(new File("src/pacman/images/up1.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCharacter()
    {
        int [] checkArr = {0,0,0,0}; // should add range checker here
        int xx = x/30, yy = y/30;
        //prt("xx is : "+xx+" yy is: "+yy+'\n');
        if (array[xx+1][yy]<=1) {checkArr[0]++;}//Right
        //prt("Right: \n");
        //prt("looking at num: "+array[xx+1][yy]+'\n');
        //prt("looking at alter num: "+array[yy][xx+1]+'\n');
        if (array[xx][yy+1]<=1) {checkArr[1]++;}//Down
        //prt("Down: \n");
        //prt("looking at num: "+array[xx][yy+1]+'\n');
        //prt("looking at alter num: "+array[yy+1][xx]+'\n');
        if (array[xx-1][yy]<=1) {checkArr[2]++;}//Left
        //prt("Left: \n");
        //prt("looking at num: "+array[xx-1][yy]+'\n');
        //prt("looking at alter num: "+array[yy][xx-1]+'\n');
        if (array[xx][yy-1]<=1) {checkArr[3]++;}//Up
        //prt("Up: \n");
        //prt("looking at num: "+array[xx][yy-1]+'\n');
        //prt("looking at alter num: "+array[yy-1][xx]+'\n');
        //prt("checkarray is: {"+checkArr[0]+", "+checkArr[1]+", "+checkArr[2]+", "+checkArr[3]+"\n ");
        switch(direction)
        {
            case KeyEvent.VK_RIGHT:
                if (checkArr[0]==1){
                x += step;}
                if(x > width)
                    x=width;
                break;
            case KeyEvent.VK_DOWN:
                if (checkArr[1]==1){
                y += step;}
                if(y>height)
                    y=height;
                break;
            case KeyEvent.VK_LEFT:
                if (checkArr[2]==1){
                x -= step;}
                if(x<0)
                    x=0;
                break;
            case KeyEvent.VK_UP:
                if (checkArr[3]==1){
                y -= step;}
                if(y < 0)
                    y = 0;
                break;
        }
    }
    public void drawPac(Graphics2D g)
    {
        switch(direction)
        {
            case KeyEvent.VK_RIGHT:
                g.drawImage(pacmanR, x, y, null);
                break;
            case KeyEvent.VK_DOWN:
                g.drawImage(pacmanD, x, y, null);
                break;
            case KeyEvent.VK_LEFT:
                g.drawImage(pacmanL, x, y, null);
                break;
            case KeyEvent.VK_UP:
                g.drawImage(pacmanU, x, y, null);
                break;
        }
    }

    public void getDirection(KeyEvent k)
    {
        direction = k.getKeyCode();
    }
}
