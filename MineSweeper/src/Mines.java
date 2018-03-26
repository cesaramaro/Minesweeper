import java.awt.Point;
import java.util.ArrayList;

public class Mines {
    Main main = new Main();
    
	//Constructor
    public Mines() {
        //Do nothing
    }
    
    /*
     * Verifies if the received coordinates are a mine
     * @param x coordinate
     * @param y coordinate 
     * @return boolean
     */
    public boolean isMine(int x, int y) {
        for (Point coords : main.getMines()) {
            if ((coords.getX() == x) && (coords.getY() == y)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
    
    /*
     * Checks if there are any mines nearby
     * @return boolean
     */
    public boolean hasMinesNearby(int x, int y) {
        ArrayList<Point> mines = main.getMines();
        ArrayList<Point> cells = new ArrayList<Point>();
        Point leftCell = new Point(x-1, y);
        Point rightCell = new Point(x+1, y);
        Point topCell = new Point(x, y-1);
        Point bottomCell = new Point(x, y+1);
        Point topLeft = new Point(x-1, y-1);
        Point topRight = new Point(x+1, y-1);
        Point bottomLeft = new Point(x-1, y+1);
        Point bottomRight = new Point(x+1, y+1);
        cells.add(leftCell);
        cells.add(rightCell);
        cells.add(topCell);
        cells.add(bottomCell);
        cells.add(topLeft);
        cells.add(topRight);
        cells.add(bottomLeft);
        cells.add(bottomRight);
        
        for (Point cell : cells) {
            if (mines.contains(cell)) {
                return true;
            } else { continue; }
        }
        return false;
        }

    /*
     * Checks how many mines there are nearby
     * @return int amount of mines nearby
     */
	public int getMinesNearbyCount(int x, int y) {
        int minesCount = 0;
        
        for(int i = x-1; i <= x+1; i++){
			for(int j = y-1; j <= y+1; j++){
				// La conjuncion de dos negaciones es la negacion de una disjuncion.
				if(!(i == x && j == y)){
					if(isMine(i, j))
						minesCount++;
				}
			}
		}
		return minesCount;
	}
}


	