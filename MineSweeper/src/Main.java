import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

public class Main {
    
    private static ArrayList<Point> mineList = new ArrayList<Point>();
    
	public static void main(String[] args) {
	    MyMouseAdapter.status = MyMouseAdapter.GameStatus.NEW_GAME;
	    generateMines();
		JFrame myFrame = new JFrame("MineSweeper");
		myFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		myFrame.setLocation(400, 150);
		myFrame.setSize(322, 380);
		
		MyPanel myPanel = new MyPanel();
		myFrame.add(myPanel);
		
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
		myFrame.addMouseListener(myMouseAdapter);
		myFrame.setResizable(false);
		myFrame.setVisible(true);
		}
	
    /**
     * Generate mines and add them to a list
     */
    private static void generateMines() {
        Random generator = new Random();
        int minesAmount = 0;
        mineList.clear();
       
        while (minesAmount < MyPanel.MAX_MINES) {
            Point newMine = new Point(generator.nextInt(9), generator.nextInt(9));
            while (mineList.contains(newMine)) {
                newMine.setLocation(generator.nextInt(9), generator.nextInt(9));
            }
            mineList.add(newMine);
            minesAmount++;
        }
    }
    
    /*
     * Returns the list of mines
     * @return ArrayList<Point>
     */
    public ArrayList<Point> getMines() {
        return mineList;
    }
}