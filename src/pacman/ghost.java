package pacman;

import java.util.Random;

import javax.imageio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ghost {
    int direction = 3;
    int x=60;
    int y=30;
    int step =1;
    int width= 800;
    int height = 400;
    int[][] array = {};
    BufferedImage ghostImage;

    void prt(String s){
        System.out.print(s);
    }

    public ghost(GridReadCreate grc)
    {
        loadImages();
        int xsize = grc.arr.length; int ysize = grc.arr[0].length;
        array = new int [xsize][ysize];

        for (int i=0; i<xsize; i++){
            for (int j=0; j<ysize; j++){
                array[i][j] = grc.arr[i][j];
            }
        }
    }
    public void loadImages() {
        try {
            ghostImage = ImageIO.read(new File("src/pacman/images/ghost.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private int getDirection(){
        Random rand = new Random();
        int n = rand.nextInt(4); //int m = rand.nextInt(2) + 1;
        return n;
    }

    // updateCharacter needs to be change, currently it moves step by step
    // which means can change direction one step and another step
    // needs to proceed until a wall, then call update character()
    public void updateCharacter()
    {
        int [] checkArr = {0,0,0,0}; // should add range checker here
        int xx = x/30, yy = y/30;
        prt("xx is : "+xx+" yy is: "+yy+'\n');
        if (array[xx+1][yy]<=1) {checkArr[0]++;}//Right
        if (array[xx][yy-1]<=1) {checkArr[1]++;}//Down
        if (array[xx-1][yy]<=1) {checkArr[2]++;}//Left
        if (array[xx][yy+1]<=1) {checkArr[3]++;}//Up

        int check = 10;
        // check is the index of one of the 1's in checkArr
        while (check == 10){

            if (checkArr[direction] == 1){
                check = direction;
            }
            //prt("while loop\n");
            else {
                direction = getDirection();
            }
        }
        switch(check)
        {
            case 0:        //Right
                x += step;
                if(x > width)
                    x=width;
                break;
            case 1:        //Down
                y += step;
                if(y < 0)
                    y = 0;
                break;
            case 2:        //Left
                x -= step;
                if(x<0)
                    x=0;
                break;
            case 3:        //Up
                y -= step;
                if(y>height)
                    y=height;
                prt("changed y to "+y+"\n");
                break;
        }
    }
    public void drawGhost(Graphics2D g)
    {
        g.drawImage(ghostImage, x, y, null);
    }

}
