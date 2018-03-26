import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class MyMouseAdapter extends MouseAdapter {
	
    public static final Color MINE_COLOR = Color.BLACK;
    public static final Color MINE_CELL_COLOR = Color.GRAY;
    public static final Color REVEALED = Color.LIGHT_GRAY;
    public static final Color CLICKED_MINE_COLOR = new Color(0xDC143C);
    public static final Color NOT_REVEALED = Color.WHITE;
    public static final Color FLAG_COLOR = new Color(0x708090); //Slate gray
    public static final Color CORRECT_FLAG_COLOR = new Color(0x50C878); //Emerald
    
    private enum GameStatus { GAME_OVER, WON, PLAYING, NEW_GAME }
    GameStatus status = GameStatus.NEW_GAME;
    
    Mines mines = new Mines();
    Main main = new Main();
    ArrayList<Point> minesList = Main.getMines();
    Object[] options = { "Play Again", "Exit" };
    
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
            	if ((gridX >= 0) && (gridY >= 0) && ((status == GameStatus.PLAYING) || (status == GameStatus.NEW_GAME))) {
            	    status = GameStatus.PLAYING;
            	    /*
            	     * If it's not a mine or already revealed, reveal nearby cells
            	     * If the revealed cells amount is greater than 70, show the win pop-up
            	     */
            	    if (myPanel.colorArray[gridX][gridY].equals(NOT_REVEALED) && !Main.getMines().contains(clickedCell)) {
            	        myPanel.revealAdjacent(gridX, gridY);
            	        if (myPanel.countTotal > 70) {
            	        	status = GameStatus.WON;
            	            showWindow(myFrame, "Congratulations!!\nYou won :)");
            	            myPanel.repaint();
            	            break;
            	        }
            	        myPanel.repaint();
            	    }

            	    /*
            	     * Checks if the cell clicked is a mine
            	     */
            	    if (minesList.contains(clickedCell) && !myPanel.colorArray[gridX][gridY].equals(FLAG_COLOR)) {
            	        // Reveal mines
            	        for (Point mine : minesList) {
            	            if(myPanel.colorArray[(int) mine.getX()][(int) mine.getY()] == FLAG_COLOR) {
            	                myPanel.colorArray[(int) mine.getX()][(int) mine.getY()] = CORRECT_FLAG_COLOR;
            	            } else {
            	                myPanel.colorArray[(int) mine.getX()][(int) mine.getY()] = MINE_CELL_COLOR; 
            	            }
            	        }
                    myPanel.colorArray[gridX][gridY] = CLICKED_MINE_COLOR;
                    myPanel.repaint();
                    status = GameStatus.GAME_OVER;
                    showWindow(myFrame, "You lost :(\nBetter luck next time!");
                    break;
            	    }
            	}
            	    myPanel.repaint();
                break;
            case 3:     // Right mouse button
                if ((gridX >= 0) && (gridY >= 0) && (status == GameStatus.PLAYING)) {
                    if (myPanel.colorArray[gridX][gridY].equals(NOT_REVEALED)) {
                        if (myPanel.flagCount == 0) {
                            // Do nothing
                        } else {
                            myPanel.flagCount--;
                            myPanel.colorArray[gridX][gridY] = FLAG_COLOR;
                            myPanel.repaint();
                        }
                    } 
                    else if (myPanel.colorArray[gridX][gridY].equals(REVEALED) || myPanel.colorArray[gridX][gridY].equals(MINE_CELL_COLOR)) {
                        // Do Nothing
                    } else {
                        myPanel.colorArray[gridX][gridY] = NOT_REVEALED;
                        myPanel.repaint();
                        myPanel.flagCount++;
                    }
                }
                myPanel.repaint();
                break;
            default:    //Some other button (2 = Middle mouse button, etc.)
                //Do nothing
                break;
        }
    }
    /*
     * Shows a window with button options "Play Again" and "Exit"
     * @param String message to be printed out in the window
     */
    public void showWindow(JFrame myFrame, String message) {
        JOptionPane pane = new JOptionPane();
        pane.setMessage(message);
        pane.setOptions(options);
        
        JDialog dialog = pane.createDialog(null, "Game over");
        dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        dialog.setVisible(true);

        Object selectedValue = pane.getValue();
        
        if (selectedValue.equals(options[0])) {
            myFrame.dispose();
            Main.main(null);
        }
        else if (selectedValue.equals(options[1])) {
            System.exit(0);
        }
    }    
}