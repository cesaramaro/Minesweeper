import java.util.Random;

public class Mines {
	
	private Mines[] MinesCoordinates;
	private Mines[] MinesCoordinates2;
	
	//Array to place the mines coordinates
	public Mines[] getMinesCoordinates(){
		return MinesCoordinates;
	}
	
	public Mines(int lenght) {
		MinesCoordinates = new Mines[lenght];
		MinesCoordinates2 = new Mines[lenght];
	}

	private int x;
	private int y;
		public Mines(int a, int b){
			this.setX(a);
			this.setY(b);	
		}
	//Create random numbers
	private Mines MinesCoordinates(){
		Random generator = new Random();
		return new Mines(generator.nextInt(9), generator.nextInt(9));
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


	