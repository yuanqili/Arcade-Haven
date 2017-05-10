package pacman;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author jbinx
 */

public class GridReadCreate extends JPanel{


	// File name. Will implement method to change this because making it static is dumb.
	// Edit this for your own purposes for now.
	final static String FILE_NAME = "src/Grid/TestData.txt";

	// Not really sure what this is lol... Copied it from online.
	final static Charset ENCODE = StandardCharsets.UTF_8;

	// Array that contains the numbers for each portion of the 15x15 grid. Probably will
	// adapt this to be larger/variable size in a later version.
	static int[][] arr = new int[15][15];

	//current score of the game
	static int score = 0;

	//how much each dot is worth
	int pointWorth = 10;



	//Number of dots in the grid (TODO: this should be in the data file, but will be set as a constant for now)
	int totalDots = 111;


	/*
	 * Main method. Currently is being used to check if the object was reading in the
	 * data correctly, but will can edited to test printing.
	 */
	public static void drawGrid(Graphics g) throws IOException {
		GridReadCreate reader = new GridReadCreate();
		List<String> strList = reader.readFile(FILE_NAME);
		reader.translateTo2DArray(strList);
		for(int j = 0; j < arr.length; j++) {
			for(int i = 0; i < arr[0].length; i++) {
				System.out.print(arr[j][i] + " ");
			}
			System.out.println();
		}
	}





	//  Reads the file. Variable name (a string) is the name of the input file.
	List<String> readFile(String name) throws IOException {
		Path path = Paths.get(name);
		return Files.readAllLines(path, ENCODE);
	}

	/*
	 * Changes the string List that is read in from the text file and puts the info
	 * in the 2D int array. Used in conjunction with readFile()
	 */
	void translateTo2DArray(List<String> list) {
		for(int i = 0; i < 15; i++) {
			String str = list.get(i);
			String[] split = str.split(" ");
			for(int j = 0; j < split.length; j++) {
				int fromStr = Integer.parseInt(split[j]);
				arr[i][j] = fromStr;
			}
		}
	}

	/*
	 * Returns the correct image given an int (typically from the 2D int array).
	 */
	static Image getImage(int input) {
		String str = "src/Grid/" + "image" + input + ".png";
		//String str = "image" + input + ".png";
		ImageIcon ii = new ImageIcon(str);
		Image im = ii.getImage();
		return im;
	}


	/*
	 * Iterates through the array and prints out the corresponding images.
	 *
	 * Images are 30x30 pixels, so using the array locations (i,j) multiplied by
	 * 30 should work fine for the location of the images.
	 */
	void printToScreen(Graphics g, Font font, Color fontColor) {
		int iMax = arr.length;
		int jMax = arr[0].length;
		Graphics2D g2d = (Graphics2D) g;
		for(int i = 0; i < iMax; i++) {
			for(int j = 0; j < jMax; j++) {
				// REPLACE below with call to other function that will print an image.
				// see getImage(int) above, which returns the appropriate image given an int
				g2d.drawImage(getImage(arr[j][i]), i*30, j*30, this);
			}
		}
		g.setFont(font);
		g.setColor(fontColor);
		String s = "Score: " + score;
		g.drawString(s, 20, 20);
	}

	// not sure if the initialization is necessary... but it's here.
	GridReadCreate() {
		try {
			translateTo2DArray(readFile(FILE_NAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
        /*int[][] arr = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}};*/
	}

	void update(int pacPosX, int pacPosY) //the input is the pacman position
	{
		int x = pacPosX/30; //switch from pixel position to array location
		int y = pacPosY/30;
		if(arr[y][x] == 1) {
			arr[y][x] = 0;
			score += pointWorth;
			totalDots--;
		}
	}

	//check if the player won
	boolean winCondition()
	{
		if(totalDots==0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


}
