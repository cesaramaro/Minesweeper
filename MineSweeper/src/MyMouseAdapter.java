import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class MyMouseAdapter extends MouseAdapter {
	
	static public Mines Mines = new Mines(10);
    private final Color FLAG_COLOR = Color.RED;
    
    public void mousePressed(MouseEvent e) {
   
        switch (e.getButton()) {
            case 1:     //Left mouse button
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
                 myPanel.repaint();

                break;
            case 3:     //Right mouse button
            	 Component c2 = e.getComponent();
                 while (!(c2 instanceof JFrame)) {
                     c2 = c2.getParent();
                     if (c2 == null) {
                         return;
                     }
                 }
                 JFrame myFrame2 = (JFrame) c2;
                 MyPanel myPanel2 = (MyPanel) myFrame2.getContentPane().getComponent(0);
                 Insets myInsets2 = myFrame2.getInsets();
                 int x3 = myInsets2.left;
                 int y3 = myInsets2.top;
                 e.translatePoint(-x3, -y3);
                 int x2 = e.getX();
                 int y2 = e.getY();
                 myPanel2.x = x2;
                 myPanel2.y = y2;
                 myPanel2.mouseDownGridX = myPanel2.getGridX(x2, y2);
                 myPanel2.mouseDownGridY = myPanel2.getGridY(x2, y2);
                 myPanel2.repaint();
                break;
            default:    //Some other button (2 = Middle mouse button, etc.)
                //Do nothing
                break;
        }
    }
    
    public void mouseReleased(MouseEvent e) {
    
        switch (e.getButton()) {
            case 1:     //Left mouse button
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
                
            	if (gridX >= 0 && gridY >= 0){
            		if(myPanel.countTotal >= 71){
            		ImageIcon picture = new ImageIcon("Won.jpg");           			
            		
            		 Object[] options = { "Play Again.", "EXIT" };
            		 JOptionPane.showOptionDialog(null, "Congratulation!!/n Play Again?", "YOU WON!!", JOptionPane.DEFAULT_OPTION, JOptionPane.CLOSED_OPTION, null, options, options[0]);
            		}
            	//Show mines
            	if(Mines.MinesNearby(gridX, gridY) && myPanel.colorArray[gridX][gridY].equals(Color.GRAY) && !myPanel.colorArray[gridX][gridY].equals(Color.BLACK)){
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
            	Component c2 = e.getComponent();
                while (!(c2 instanceof JFrame)) {
                    c2 = c2.getParent();
                    if (c2 == null) {
                        return;
                    }
                }
                JFrame myFrame2 = (JFrame)c2;
                MyPanel myPanel2 = (MyPanel) myFrame2.getContentPane().getComponent(0);  //Can also loop among components to find MyPanel
                Insets myInsets2 = myFrame2.getInsets();
                int x3 = myInsets2.left;
                int y3 = myInsets2.top;
                e.translatePoint(-x3, -y3);
                int x2 = e.getX();
                int y2 = e.getY();
                myPanel2.x = x2;
                myPanel2.y = y2;
                int gridX2 = myPanel2.getGridX(x2, y2);
                int gridY2 = myPanel2.getGridY(x2, y2);
                if (myPanel2.colorArray[gridX2][gridY2].equals(Color.WHITE)) {
                    myPanel2.colorArray[gridX2][gridY2] = FLAG_COLOR;
                    myPanel2.repaint();
                } 
                else if (myPanel2.colorArray[gridX2][gridY2].equals(Color.GRAY) || myPanel2.colorArray[gridX2][gridY2].equals(Color.BLACK)) {
                    //Do Nothing
                } else{
                	myPanel2.colorArray[gridX2][gridY2] = Color.WHITE;
                	myPanel2.repaint();
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