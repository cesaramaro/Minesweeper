import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Mines {

    private ArrayList<Point> mineList = new ArrayList<Point>();
    private final int MAX_MINES = 10;
    
	private Mines[] MinesCoordinates;
	private Mines[] MinesCoordinates2;
	
	// Constructor
    public Mines() {
        generateMines();
    }
	
    /**
     * Generate mines and add them to a list
     */
    // TODO Prevent mines from overlapping
    private void generateMines() {
        Random generator = new Random();
        int minesAmount = 0;
        
        while (minesAmount < MAX_MINES) {
            Point coords = new Point();
            int x = generator.nextInt(9);
            int y = generator.nextInt(9);
            
            coords.setLocation(x, y);
            mineList.add(coords);
            minesAmount++;
        }
    }
	
    /*
     * Verifies if the received coordinates are a mine
     * @param x coordinate
     * @param y coordinate 
     * @return boolean
     */
    public boolean isMine(int x, int y) {
        for (Point coords : mineList) {
            if ((coords.getX() == x) && (coords.getY() == y)) {
                return true;
            } else {
                continue;
            }
        }
        return false;
    }

    /*
     * Returns the list of mines
     */
    public ArrayList<Point> getMines() {
        return mineList;
    }
    
	//Array to place the mines coordinates
	public Mines[] getMinesCoordinates() {
		return MinesCoordinates;
	}
	
	public Mines(int length) {
		MinesCoordinates = new Mines[length];
		MinesCoordinates2 = new Mines[length];
	}

	private int x;
	private int y;
		public Mines(int a, int b){
			this.setX(a);
			this.setY(b);	
		}
	
//	//With the CreateCoordinates method fill the Mines arrays
//	public void FillsMinesCoordinates(){
//		for()
//	}
    
	public int MinesNearbyCount(int x, int y) {
		int count = 0;
		return 0;
	}

	public boolean CheckSelection(int gridX, int gridY) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean MinesNearby(int gridX, int gridY) {
		// TODO Auto-generated method stub
		return false;
	}

	//Getters
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	//Setters
	public void setX(int x) {
			this.x = x;
		}
	public void setY(int y) {
		this.y = y;
	}

	

}


	