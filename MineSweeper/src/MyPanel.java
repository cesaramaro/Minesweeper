import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.Random;
import javax.swing.JPanel;

import javafx.scene.shape.Ellipse;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 50;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 10;   //Last row has only one cell
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int MinesCloseby[][] = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public int countTotal = 0;
	static public Mines Mines = new Mines();
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //The 9x9 grid
			for (int y = 0; y < TOTAL_ROWS; y++) {
				colorArray[x][y] = Color.WHITE;
				MinesCloseby[x][y] = 0;
			}
		}
	}
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		super.paintComponent(g);
		
		
		//Compute interior coordinates
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		int x2 = getWidth() - myInsets.right - 1;
		int y2 = getHeight() - myInsets.bottom - 1;
		int width = x2 - x1;
		int height = y2 - y1;

		//Paint the background
		g.setColor( new Color(0xB57EDC));
		g.fillRect(x1, y1, width + 1, height + 1);

		//Draw the grid minus the bottom row (which has only one cell)
		//The grid is 9x9
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS - 1; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)), x1 + GRID_X + ((INNER_CELL_SIZE + 1) * TOTAL_COLUMNS), y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y, x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)), y1 + GRID_Y + ((INNER_CELL_SIZE + 1) * (TOTAL_ROWS - 1)));
		}

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((y != TOTAL_ROWS - 1)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}
		//mines closeby
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((MinesCloseby[x][y] != 0) && colorArray[x][y] != Color.GRAY) {
					int total = MinesCloseby[x][y];
					if(total == 1){
						g.setColor(Color.BLUE);
					}else if(total == 2){
						g.setColor(Color.RED);
					}else if(total == 3){
						g.setColor(Color.YELLOW);
					}else if(total == 4){
						g.setColor(Color.GREEN);
					}else{
						g.setColor(Color.CYAN);
					}
					g.setFont(new Font("Arial", Font.BOLD, 40));
					g.drawString(String.valueOf(total), x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 18, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 38);
				}else if((MinesCloseby[x][y] == 0) && colorArray[x][y] == Color.GRAY){
					g.setColor(Color.BLACK	);
					Ellipse2D.Double mine = new Ellipse2D.Double((x * (INNER_CELL_SIZE + 1))+ 35, ((y * (INNER_CELL_SIZE + 1)) + 35), 30, 30);
					g2.draw(mine);	
					g2.setPaint(Color.BLACK);
					g2.fill(new Ellipse2D.Double((x * (INNER_CELL_SIZE + 1))+ 35, ((y * (INNER_CELL_SIZE + 1)) + 35), 30, 30));
					g.fillRect((x * (INNER_CELL_SIZE + 1))+ 48, ((y * (INNER_CELL_SIZE + 1)) + 30), 4, 40);
					g.fillRect((x * (INNER_CELL_SIZE + 1))+ 30, ((y * (INNER_CELL_SIZE + 1)) + 48), 40, 4);

				}
			}
		}
	}

	// This method helps to find the adjacent boxes that don't have a mine.
	// It is partially implemented since the verify hasn't been discussed in class
	// Verify that the coordinates in the parameters are valid.
	// Also verifies if there are any mines around the x,y coordinate
	public void revealAdjacent(int x, int y) {
		if((x < 0) || (y < 0) || (x >= 9) || (y >= 9)) { return; }
		if(Mines.isMine(x, y)){return;}
		if (Mines.hasMinesNearby(x, y)) {
			int counter = Mines.getMinesNearbyCount(x, y);
			colorArray[x][y] = MyMouseAdapter.REVEALED;
			MinesCloseby[x][y] = counter;
			countTotal++;
			repaint();
			return;
		}else{
			if(colorArray[x][y] == MyMouseAdapter.REVEALED){return;}
			colorArray[x][y] = MyMouseAdapter.REVEALED;
			revealAdjacent(x-1, y);
			revealAdjacent(x, y-1);
			revealAdjacent(x+1, y);
			revealAdjacent(x, y+1);
			countTotal++;
		}
		System.out.println("Test");

	}
	
	//Getters
	public int getGridX(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return x;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return x;
	}
	public int getGridY(int x, int y) {
		Insets myInsets = getInsets();
		int x1 = myInsets.left;
		int y1 = myInsets.top;
		x = x - x1 - GRID_X;
		y = y - y1 - GRID_Y;
		if (x < 0) {   //To the left of the grid
			return -1;
		}
		if (y < 0) {   //Above the grid
			return -1;
		}
		if ((x % (INNER_CELL_SIZE + 1) == 0) || (y % (INNER_CELL_SIZE + 1) == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / (INNER_CELL_SIZE + 1);
		y = y / (INNER_CELL_SIZE + 1);
		if (x == 0 && y == TOTAL_ROWS - 1) {    //The lower left extra cell
			return y;
		}
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 2) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}