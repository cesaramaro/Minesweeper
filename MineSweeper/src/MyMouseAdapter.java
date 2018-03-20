import java.awt.Color;
import java.awt.Component;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

import javax.swing.JFrame;

public class MyMouseAdapter extends MouseAdapter {
    
    private Random generator = new Random();
    private Color lastUsedColor = null;
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
        myPanel.repaint();

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
        myPanel.mouseDownGridX = myPanel.getGridX(x, y);
        myPanel.mouseDownGridY = myPanel.getGridY(x, y);

        switch (e.getButton()) {
            case 1:     //Left mouse button
                if ((myPanel.mouseDownGridX == -1) || (myPanel.mouseDownGridY == -1)) {
                    //Had pressed outside
                    //Do nothing
                } else {
                    if ((gridX == -1) || (gridY == -1)) {
                        //Is releasing outside
                        //Do nothing
                    } else {
                        if ((myPanel.mouseDownGridX != gridX) || (myPanel.mouseDownGridY != gridY)) {
                            //Released the mouse button on a different cell where it was pressed
                            //Do nothing
                        } else {
                            //Released the mouse button on the same cell where it was pressed
                            if ((gridX == 0) || (gridY == 0)) {
                                //On the left column and on the top row... do nothing
                            } else { 
                                //On the grid other than on the left column and on the top row:
                                if(myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(FLAG_COLOR)) {
                                    // Do nothing; Can't edit a flag unless it's using right-click
                                } else {
                                Color newColor = getRandomColor();
                                
                                while (newColor.equals(lastUsedColor)) {
                                    newColor = getRandomColor();
                                } lastUsedColor = newColor;
                                
                                myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = newColor;
                                myPanel.repaint();
                                }
                            }
                        }
                    }
                }
                myPanel.repaint();
                break;
            case 3:     //Right mouse button
                if (myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(Color.WHITE)) {
                    myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = FLAG_COLOR;
                } 
                else if (myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY].equals(FLAG_COLOR)) {
                    myPanel.colorArray[myPanel.mouseDownGridX][myPanel.mouseDownGridY] = Color.WHITE;
                }
                myPanel.repaint();
                break;
            default:    //Some other button (2 = Middle mouse button, etc.)
                //Do nothing
                break;
        }
    }
    
    /*
     * Returns one of 9 random colors
     */
    private Color getRandomColor() {
        Color newColor = null;
        switch (generator.nextInt(10)) {
            case 0:
                newColor = Color.YELLOW;
                break;
            case 1:
                newColor = Color.MAGENTA;
                break;
            case 2:
                newColor = Color.BLACK;
                break;
            case 3:
                newColor = new Color(0x964B00);   //Brown (from http://simple.wikipedia.org/wiki/List_of_colors)
                break;
            case 4:
                newColor = new Color(0xB57EDC);   //Lavender (from http://simple.wikipedia.org/wiki/List_of_colors)
                break;
            case 5:
                newColor = Color.BLUE;
                break;
            case 6:
                newColor = Color.ORANGE;
                break;
            case 7:
                newColor = Color.GREEN;
                break;
            case 8:
                newColor = Color.DARK_GRAY;
                break;
            case 9:
                newColor = Color.CYAN;
        }
        return newColor;
    }
}