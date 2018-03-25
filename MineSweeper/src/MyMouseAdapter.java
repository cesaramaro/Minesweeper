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

public class MyMouseAdapter extends MouseAdapter {
	
    public static final Color MINE_COLOR = Color.BLACK;
    public static final Color MINE_CELL_COLOR = Color.GRAY;
    public static final Color REVEALED = Color.LIGHT_GRAY;
    public static final Color CLICKED_MINE_COLOR = new Color(0xDC143C);
    public static final Color NOT_REVEALED = Color.WHITE;
    public static final Color FLAG_COLOR = new Color(0xE0B0FF); //Lavender
    
    private enum GameStatus { GAME_OVER, WON, PLAYING, NEW_GAME }
    GameStatus status = GameStatus.NEW_GAME;
    
    Mines mines = new Mines();
    Main main = new Main();
    ArrayList<Point> minesList = Main.getMines();
    Object[] loseOptions = { "Exit", "Play Again" };
    Object[] winOptions = { "Exit", "Play Again" };
    
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
                    //TODO fix so when it wins it ends
                  if(!minesList.contains(clickedCell)) {
                      if(myPanel.countTotal == 71){
                    	  status = GameStatus.WON;
                          showWinWindow(myFrame);
                      }
                  }
            	    /*
            	     * Checks if the cell clicked is a mine
            	     */
            	    if (Main.getMines().contains(clickedCell) && !myPanel.colorArray[gridX][gridY].equals(FLAG_COLOR)) {
            	        // Reveal mines
            	        for (Point mine : minesList) {
            	            myPanel.colorArray[(int) mine.getX()][(int) mine.getY()] = MINE_CELL_COLOR; 
            	        }
//                    myPanel.colorArray[gridX][gridY] = CLICKED_MINE_COLOR;
//                    myPanel.repaint();
//                    showLoseWindow(myFrame);
//                    status = GameStatus.GAME_OVER;
                    break;
            	    }

            	    /*
            	     * If it's not a mine or already revealed, reveal nearby cells
            	     */
            	    if (myPanel.colorArray[gridX][gridY].equals(NOT_REVEALED) && !Main.getMines().contains(clickedCell)) {
            		    myPanel.revealAdjacent(gridX, gridY);
            		    myPanel.repaint();
            		}
            	}
            	    myPanel.repaint();
                break;
            case 3:     // Right mouse button
                if ((gridX >= 0) && (gridY >= 0) && (status == GameStatus.PLAYING)) {
                    if (myPanel.colorArray[gridX][gridY].equals(NOT_REVEALED)) {
                        myPanel.colorArray[gridX][gridY] = FLAG_COLOR;
                        myPanel.repaint();
                    } 
                    else if (myPanel.colorArray[gridX][gridY].equals(REVEALED) || myPanel.colorArray[gridX][gridY].equals(MINE_CELL_COLOR)) {
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
    
    public void showLoseWindow(JFrame myFrame) {
        JOptionPane pane = new JOptionPane();
        pane.setMessage("You lost :(\nBetter luck next time!");
        pane.setOptions(loseOptions);
        
        JDialog dialog = pane.createDialog(null, "Game over");
        dialog.setVisible(true);
        
        Object selectedValue = pane.getValue();
        
        if (selectedValue.equals(loseOptions[1])) {
            myFrame.dispose();
            Main.main(null);
        }
        else if (selectedValue.equals(loseOptions[0])) {
            System.exit(0);
        }
    }
    
    public void showWinWindow(JFrame myFrame){
    	 JOptionPane pane = new JOptionPane();
         pane.setMessage("Congratulations!!\n You Won :)");
         pane.setOptions(winOptions);
         
         JDialog dialog = pane.createDialog(null, "Game over");
         dialog.setVisible(true);
         
         Object selectedValue = pane.getValue();
         
         if (selectedValue.equals(winOptions[1])) {
             myFrame.dispose();
             Main.main(null);
         }
         else if (selectedValue.equals(winOptions[0])) {
             System.exit(0);
         }
    }
    
}