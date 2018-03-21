import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyMouseAdapter extends MouseAdapter {
	
	public static Mines Mines = new Mines(10);
    private final Color FLAG_COLOR = Color.RED;
    
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

        switch (e.getButton()) {
            case 1:     //Left mouse button
            	if (gridX >= 0 && gridY >= 0) {
            		if(myPanel.countTotal >= 71) {
            		    ImageIcon picture = new ImageIcon("Won.jpg");           			
            		    Object[] options = { "Play Again.", "EXIT" };
            		    JOptionPane.showOptionDialog(null, "Congratulations!/n Play Again?", "YOU WON!!", JOptionPane.DEFAULT_OPTION, JOptionPane.CLOSED_OPTION, null, options, options[0]);
            		}
            	//Show mines
            	if(Mines.MinesNearby(gridX, gridY) 
            	        && myPanel.colorArray[gridX][gridY].equals(Color.GRAY) 
            	        && !myPanel.colorArray[gridX][gridY].equals(Color.BLACK)){
            			//mines around the click
            			int count = Mines.MinesNearbyCount(gridX, gridY);
            		
            		Color newColor = Color.GRAY;
            		myPanel.colorArray[gridX][gridY] = newColor;
            		myPanel.MinesCloseby[gridX][gridY] = count;
            		myPanel.repaint();
            		myPanel.countTotal++;
            		
            	}
            	
            	//Gray grid when the spot is empty
            	else if(Mines.CheckSelection(gridX, gridY)){
            		myPanel.revealAdjacent(gridX, gridY);
            	}
            	
            	//Black grid when there is a mine on the spot
            	if(Mines.CheckSelection(gridX, gridY)){
            		Color newColor = Color.BLACK;
            		myPanel.colorArray[gridX][gridY] = newColor;
            		myPanel.repaint();
            		Object[] options = { "Play Again.", "EXIT" };
           		 JOptionPane.showOptionDialog(null, "Play Again?", "GAME OVER!!", JOptionPane.DEFAULT_OPTION, JOptionPane.CLOSED_OPTION, null, options, options[0]);
            	}
            	}
                break;
            case 3:     //Right mouse button
            
                if (myPanel.colorArray[gridX][gridY].equals(Color.WHITE)) {
                    myPanel.colorArray[gridX][gridY] = FLAG_COLOR;
                    myPanel.repaint();
                } 
                else if (myPanel.colorArray[gridX][gridY].equals(Color.GRAY) 
                        || myPanel.colorArray[gridX][gridY].equals(Color.BLACK)) {
                    //Do Nothing
                } else{
                	myPanel.colorArray[gridX][gridY] = Color.WHITE;
                	myPanel.repaint();
                }
                break;
            default:    //Some other button (2 = Middle mouse button, etc.)
                //Do nothing
                break;
        }
    }

	public Mines getMines() {
		return Mines;
	}
	public void setMines(Mines mines) {
		Mines = mines;
	}
    
}