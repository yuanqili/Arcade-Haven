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
    int dx, dy;
    int step =1;
    int width= 800;
    int height = 400;
    int[][] array = {};
    BufferedImage ghostImage;

    void prt(String s){
        System.out.print(s);
    }

    public ghost(GridReadCreate grc, int x_cor, int y_cor)
    {
        loadImages();
        int xsize = grc.arr.length; int ysize = grc.arr[0].length;
        x = x_cor; y = y_cor;
        array = new int [xsize][ysize];

        for (int i=0; i<xsize; i++){
            for (int j=0; j<ysize; j++){
                array[j][i] = grc.arr[i][j];
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
        //prt("x is : "+x+" y is: "+y+'\n');
        //prt("xx is : "+xx+" yy is: "+yy+'\n');
        boolean leftedge = (xx == 0);
        boolean rightedge = (xx == 14);
        switch(x) {
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

        if (rightedge || array[(x+30)/30][yy]<=1 && y%30==0) {checkArr[0]++;}//Right
        //prt("Right: \n");
        //prt("looking at num: "+array[(x+15)/30][yy]+'\n');

        if (array[xx][(y+30)/30]<=1 && x%30==0) {checkArr[1]++;}//Down
        //prt("Down: \n");
        //prt("looking at num: "+array[xx][yy+1]+'\n');

        if (leftedge || array[(x-1)/30][yy]<=1 && y%30==0) {checkArr[2]++;}//Left
        //prt("Left: \n");
        //prt("looking at num: "+array[xx][yy]+'\n');

        if (array[xx][(y-1)/30]<=1 && x%30==0) {checkArr[3]++;}//Up
        //prt("Up: \n");
        //prt("looking at num: "+array[xx][yy-1]+'\n');


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
                //prt("Right: x of "+(x-step)+" changed to "+x+"\n");
                break;
            case 1:        //Down
                y += step;
                if(y>height)
                    y=height;
                //prt("Down: y of "+(y-step)+" changed to "+y+"\n");
                break;
            case 2:        //Left
                x -= step;
                //prt("Left: x of "+(x+step)+" changed to "+x+"\n");
                break;
            case 3:        //Up
                y -= step;
                if(y < 0)
                    y = 0;
                //prt("Up: y of "+(y+step)+" changed to "+y+"\n");
                break;
        }
    }
    public void drawGhost(Graphics2D g)
    {
        g.drawImage(ghostImage, x, y, null);
    }

}
