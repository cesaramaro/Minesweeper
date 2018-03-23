import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyMouseAdapter extends MouseAdapter {
	
    private final Color FLAG_COLOR = Color.RED;
    private final Color MINE_COLOR = Color.BLACK;
    private final Color NOT_REVEALED = Color.WHITE;
    public static final Color REVEALED = Color.GRAY;
    public enum GameStatus { GAME_OVER, WON, PLAYING }
    GameStatus status = GameStatus.PLAYING;
    Mines Mines = new Mines();
    ArrayList<Point> minesList = Main.getMines();
    Object[] loseOptions = { "Play Again", "Exit" };
    
    public void mousePressed(MouseEvent e) {
        Component c = e.getComponent();
        while (!(c instanceof JFrame)) {
            c = c.getParent();
            if (c == null) {
                return;
            }
        }
        JFrame myFrame = (JFrame) c;
        MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);
        Insets myInsets = myFrame.getInsets();
        int x1 = myInsets.left;
        int y1 = myInsets.top;
        e.translatePoint(-x1, -y1);
        int x = e.getX();
        int y = e.getY();
        myPanel.x = x;
        myPanel.y = y;
        myPanel.mouseDownGridX = myPanel.getGridX(x, y);
        myPanel.mouseDownGridY = myPanel.getGridY(x, y);
        
        switch (e.getButton()) {
            case 1:     //Left mouse button
                myPanel.repaint();
                break;
            case 3:     //Right mouse button
                 myPanel.repaint();
                break;
            default:    //Some other button (2 = Middle mouse button, etc.)
                //Do nothing
                break;
        }
    }
    
    public void mouseReleased(MouseEvent e) {
        Component c = e.getComponent();
        while (!(c instanceof JFrame)) {
            c = c.getParent();
            if (c == null) {
                return;
            }
        }
        JFrame myFrame = (JFrame)c;
        MyPanel myPanel = (MyPanel) myFrame.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
        Insets myInsets = myFrame.getInsets();
        int x1 = myInsets.left;
        int y1 = myInsets.top;
        e.translatePoint(-x1, -y1);
        int x = e.getX();
        int y = e.getY();
        myPanel.x = x;
        myPanel.y = y;
        int gridX = myPanel.getGridX(x, y);
        int gridY = myPanel.getGridY(x, y);
        Point clickedCell = new Point(gridX, gridY);

        switch (e.getButton()) {
            case 1:     //Left mouse button
            	if ((gridX >= 0) && (gridY >= 0) && (status == GameStatus.PLAYING)) {
            	    /*
            	     * Checks if the cell clicked is a mine
            	     */
            	    if (Main.getMines().contains(clickedCell)) {
            	        // Reveal mines
            	        for (Point mine : minesList) {
            	            myPanel.colorArray[(int) mine.getX()][(int) mine.getY()] = MINE_COLOR; 
            	        }
                    myPanel.colorArray[gridX][gridY] = Color.BLACK;
                    myPanel.repaint();
                    status = GameStatus.GAME_OVER;
                    // TODO => Make buttons work (Play again, exit) 
                    // TODO Fix => Takes longer for mines to appear because of the JOptionPane
                    //JOptionPane.showOptionDialog(null, "Would you like to play again?", "GAME OVER!!", JOptionPane.DEFAULT_OPTION, JOptionPane.CLOSED_OPTION, null, loseOptions, loseOptions[0]);
                    break;
            	    }

            	    /*
            	     * If it's not a mine or already revealed, reveal nearby cells
            	     */
            	    if (myPanel.colorArray[gridX][gridY].equals(NOT_REVEALED) && !Main.getMines().contains(clickedCell)) {
            		    myPanel.revealAdjacent(gridX, gridY);
            		    myPanel.colorArray[gridX][gridY] = REVEALED;
            		    myPanel.repaint();    
            		}
            	    
            	    if (Mines.getMinesNearbyCount(gridX, gridY) > 0) {
            	        // TODO => Print image or number
            	        System.out.println("MINES NEARBY: " + Mines.getMinesNearbyCount(gridX, gridY));
            	    }
            	}
            	
            	 /*
            	  * TODO Win screen shows up depending on the number of times the revealAdjacent method is called
            	  * not when the amount of gray cells is >= 71
            	  * 
              if(myPanel.countTotal >= 71) {
                  ImageIcon picture = new ImageIcon("Won.jpg");                       
                  Object[] options = { "Play Again.", "EXIT" };
                  JOptionPane.showOptionDialog(null, "Congratulations!\nPlay Again?", "YOU WON!!", JOptionPane.DEFAULT_OPTION, JOptionPane.CLOSED_OPTION, null, options, options[0]);
              }
              if(Mines.hasMinesNearby(gridX, gridY) 
                  && myPanel.colorArray[gridX][gridY].equals(Color.GRAY) 
                  && !myPanel.colorArray[gridX][gridY].equals(MINE_COLOR)
                  && !myPanel.colorArray[gridX][gridY].equals(FLAG_COLOR)) {
              //mines around the click
              int count = Mines.MinesNearbyCount(gridX, gridY);
          
              Color newColor = Color.GRAY;
              myPanel.colorArray[gridX][gridY] = newColor;
              myPanel.MinesCloseby[gridX][gridY] = count;
              myPanel.repaint();
              myPanel.countTotal++;
          
              }
            	  */
            	    myPanel.repaint();
                break;
            case 3:     // Right mouse button
                if ((gridX >= 0) && (gridY >= 0) && (status == GameStatus.PLAYING)) {
                    if (myPanel.colorArray[gridX][gridY].equals(NOT_REVEALED)) {
                        myPanel.colorArray[gridX][gridY] = FLAG_COLOR;
                        myPanel.repaint();
                    } 
                    else if (myPanel.colorArray[gridX][gridY].equals(REVEALED) || myPanel.colorArray[gridX][gridY].equals(MINE_COLOR)) {
                        // Do Nothing
                    } else {
                        myPanel.colorArray[gridX][gridY] = NOT_REVEALED;
                        myPanel.repaint();
                    }
                }
                myPanel.repaint();
                break;
            default:    //Some other button (2 = Middle mouse button, etc.)
                //Do nothing
                break;
        }
    }
}