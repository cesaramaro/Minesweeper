import java.awt.Point;
import java.util.ArrayList;

public class Mines {
	
	// Constructor
    public Mines() {
        // Do nothing
    }
	
    /*
     * Verifies if the received coordinates are a mine
     * @param x coordinate
     * @param y coordinate 
     * @return boolean
     */
    public static boolean isMine(int x, int y) {
        for (Point coords : Main.getMines()) {
            if ((coords.getX() == x) && (coords.getY() == y)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }
    
    /*
     * Get an ArrayList of Points of mines nearby
     * @param x coordinate to verify
     * @param y coordinate to verify
     * @return Point
     */
    
    // TODO Shorten
    public ArrayList<Point> getMinesNearby(int x, int y) {
        
        Point leftCell = new Point(x-1, y);
        Point rightCell = new Point(x+1, y);
        Point topCell = new Point(x, y-1);
        Point bottomCell = new Point(x, y+1);
        ArrayList<Point> minesNearby = new ArrayList<Point>();
        
        if (hasMinesNearby(x, y)) {
            for (Point mine : Main.getMines()) {
                if (mine.equals(leftCell)) {
                    minesNearby.add(mine);
                } else if (mine.equals(rightCell)) {
                    minesNearby.add(mine);
                } else if (mine.equals(topCell)) {
                    minesNearby.add(mine);
                } else if (mine.equals(bottomCell)) {
                    minesNearby.add(mine);
                } else {
                    continue;
                }
            }
        }
        return minesNearby;
    }
    
    /*
     * Checks if there are any mines nearby
     * @return boolean
     */
    // TODO Shorten
    public static boolean hasMinesNearby(int x, int y) {
        ArrayList<Point> mines = Main.getMines();
        Point leftCell = new Point(x-1, y);
        Point rightCell = new Point(x+1, y);
        Point topCell = new Point(x, y-1);
        Point bottomCell = new Point(x, y+1);
        Point topLeft = new Point(x-1, y-1);
        Point topRight = new Point(x+1, y-1);
        Point bottomLeft = new Point(x-1, y+1);
        Point bottomRight = new Point(x+1, y+1);
        
        if (mines.contains(leftCell) 
                || mines.contains(rightCell)
                || mines.contains(topCell) 
                || mines.contains(bottomCell)
                || mines.contains(topLeft)
                || mines.contains(topRight)
                || mines.contains(bottomLeft)
                || mines.contains(bottomRight)) {
            return true;
        } else {
        return false;
        }
    }

    /*
     * Checks how many mines there are nearby
     * @return int amount of mines nearby
     */
    // TODO Shorten
	public static int getMinesNearbyCount(int x, int y) {
        Point leftCell = new Point(x-1, y);
        Point rightCell = new Point(x+1, y);
        Point topCell = new Point(x, y-1);
        Point bottomCell = new Point(x, y+1);
        ArrayList<Point> mines = Main.getMines();
        int minesCount = 0;
        
        for (Point mine : Main.getMines()) {
            if (mine.equals(leftCell) 
                    || mine.equals(rightCell)
                    || mine.equals(topCell)
                    || mine.equals(bottomCell)) {
                minesCount++;
            }
        }
		return minesCount;
	}

	public boolean checkSelection(int gridX, int gridY) {
		// TODO Auto-generated method stub
		return false;
	}	
}


	