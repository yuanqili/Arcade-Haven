import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * @author jbinx
 *
 * Sorry this is ridiculously unorganized at the moment. I'll fix it within the next
 * few days.
 */

public class GridReadCreate extends JPanel{
	
	/*
	 * Main method. Currently is being used to check if the object was reading in the
	 * data correctly, but will can edited to test printing.
	 */
	public static void main(String []args) throws IOException {
		GridReadCreate reader = new GridReadCreate();
		List<String> strList = reader.readFile(FILE_NAME);
		int[][] arr = reader.translateTo2DArray(strList);
		for(int j = 0; j < arr.length; j++) {
			for(int i = 0; i < arr[0].length; i++) {
			System.out.print(arr[j][i] + " ");
			}
			System.out.println();
		}
	}
	
	// File name. Will implement method to change this because making it static is dumb.
	// Edit this for your own purposes for now.
	final static String FILE_NAME = "/Users/jeremybinkley/CS/Pacman/TestData.txt";
	
	// Not really sure what this is lol... Copied it from online.
	final static Charset ENCODE = StandardCharsets.UTF_8;
	
	// Array that contains the numbers for each portion of the 15x15 grid. Probably will
	// adapt this to be larger/variable size in a later version.
	int[][] arr = new int[15][15];
	
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
	Image getImage(int input) {
		String str = "image" + input + ".png";
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
	void printToScreen(int[][] arr) {
		int iMax = arr.length;
		int jMax = arr[0].length;
		for(int i = 0; i < iMax; i++) {
			for(int j = 0; j < jMax; j++) {
				// REPLACE below with call to other function that will print an image.
				// see getImage(int) above, which returns the appropriate image given an int
				//Graphics2D g2d = (Graphics2D) g;
		        //g2d.drawImage(getImage(arr[i][j]), i*30, i*30, this);
			}
		}
	}
	
	// not sure if the initialization is necessary... but it's here.
		//int[][] arr = {{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
		//{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}};
}
