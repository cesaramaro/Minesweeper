import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Ellipse2D;
import java.util.Date;
import java.util.Random;
import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
    public static final int MAX_MINES = 10;
    public static final int MAX_FLAGS = 10;
	private static final int GRID_X = 10;
	private static final int GRID_Y = 10;
	private static final int INNER_CELL_SIZE = 32;
	private static final int INNER_CELL_PLUS = INNER_CELL_SIZE + 1;
	private static final int TOTAL_COLUMNS = 9;
	private static final int TOTAL_ROWS = 9;
	private static final int NUMBER_SIZE = 20;
	private static final int MINE_WIDTH = 18;
	private static final int MINE_HEIGHT = 18;
	private static final int CROSS_WIDTH = 4;
	private static final int CROSS_HEIGHT = 24;
	private static final int CELL_CENTER = INNER_CELL_SIZE / 2;
	
	private Date startDate;
	public int sec = 0;
	
	private int timeX = 237;
	private int timeY = 313;
	private int flagX = 10;
	private int flagY = 313;
	
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int minesNearby[][] = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public int flagsNearby[][] = new int[TOTAL_COLUMNS][TOTAL_ROWS];
	public int countTotal = 0;
	public int flagCount = 10;
	Mines mines = new Mines();
	
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
				colorArray[x][y] = MyMouseAdapter.NOT_REVEALED;
				minesNearby[x][y] = 0;
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

		//Paints the background
		g.setColor(new Color(0x89CFF0));
		g.fillRect(x1, y1, width + 1, height + 1);

		/*
		 * Draws a 9x9 grid
		 */
		g.setColor(Color.BLACK);
		for (int y = 0; y <= TOTAL_ROWS; y++) {
			g.drawLine(x1 + GRID_X, y1 + GRID_Y + (y * INNER_CELL_PLUS), x1 + GRID_X + (INNER_CELL_PLUS * TOTAL_COLUMNS), y1 + GRID_Y + (y * INNER_CELL_PLUS));
		}
		for (int x = 0; x <= TOTAL_COLUMNS; x++) {
			g.drawLine(x1 + GRID_X + (x * INNER_CELL_PLUS), y1 + GRID_Y, x1 + GRID_X + (x * INNER_CELL_PLUS), y1 + GRID_Y + (INNER_CELL_PLUS * (TOTAL_ROWS)));
		}

		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * INNER_CELL_PLUS) + 1, y1 + GRID_Y + (y * INNER_CELL_PLUS) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
			}
		}
		
		/*
		 * Draws mines and flags
		 */
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((minesNearby[x][y] != 0) && colorArray[x][y] != MyMouseAdapter.MINE_COLOR) {
					int total = minesNearby[x][y];
					g.setColor(getNumberColor(total));
					g.setFont(new Font("Arial", Font.BOLD, NUMBER_SIZE));
					g.drawString(String.valueOf(total), x1 + GRID_X + (x * INNER_CELL_PLUS) + 10, y1 + GRID_Y + (y * INNER_CELL_PLUS) + 25);
				}
                else if ((minesNearby[x][y] == 0) && ((colorArray[x][y] == MyMouseAdapter.MINE_CELL_COLOR) || colorArray[x][y] == MyMouseAdapter.CLICKED_MINE_COLOR)) {
                    Ellipse2D.Double mineCircle = new Ellipse2D.Double((x * INNER_CELL_PLUS + CELL_CENTER + 1), (y * INNER_CELL_PLUS + CELL_CENTER + 1), MINE_WIDTH, MINE_HEIGHT);
                    g2.setPaint(MyMouseAdapter.MINE_COLOR);
                    g2.fill(mineCircle);
                    g.fillRect((x * INNER_CELL_PLUS + CELL_CENTER + 8), (y * INNER_CELL_PLUS + CELL_CENTER - 2), CROSS_WIDTH, CROSS_HEIGHT);
                    g.fillRect((x * INNER_CELL_PLUS + CELL_CENTER - 2), (y * INNER_CELL_PLUS + CELL_CENTER + 8), CROSS_HEIGHT, CROSS_WIDTH);
                }
                else if (flagsNearby[x][y] == 0 && ((colorArray[x][y] == MyMouseAdapter.FLAG_COLOR) || colorArray[x][y] == MyMouseAdapter.CORRECT_FLAG_COLOR)) {
                	    g2.setColor(Color.BLACK);
                	    g.fillRect((x * INNER_CELL_PLUS + CELL_CENTER + 14), (y * INNER_CELL_PLUS + CELL_CENTER + 4), 4, 18);
                	    g.fillRect((x * INNER_CELL_PLUS + CELL_CENTER + 11), (y * INNER_CELL_PLUS + CELL_CENTER + 18), 10, 4);
                	    g.fillRect((x * INNER_CELL_PLUS + CELL_CENTER + 10), (y * INNER_CELL_PLUS + CELL_CENTER + 20), 12, 4);
            		    g.setColor(Color.RED);
            		    g.fillRect((x * INNER_CELL_PLUS + CELL_CENTER + 2), (y * INNER_CELL_PLUS + CELL_CENTER + 2), 15, 10);
            		    g.setColor(Color.BLACK);
            		    g.drawRect((x * INNER_CELL_PLUS + CELL_CENTER + 2), (y * INNER_CELL_PLUS + CELL_CENTER + 2), 15, 10);
            		    g.drawRect((x * INNER_CELL_PLUS + CELL_CENTER + 3), (y * INNER_CELL_PLUS + CELL_CENTER + 3), 13, 8);
                }
			}
		}
		
		/*
		 * Adds timer at the bottom right of the panel
		 */
		g.setColor(Color.BLACK);
		g.fillRect(timeX, timeY, 70, 33);
		if (MyMouseAdapter.status == MyMouseAdapter.GameStatus.NEW_GAME) {
		    startDate = new Date();
		}
		
	    if ((MyMouseAdapter.status != MyMouseAdapter.GameStatus.GAME_OVER) 
	            && (MyMouseAdapter.status != MyMouseAdapter.GameStatus.NEW_GAME)) {
	        sec = (int) ((new Date().getTime() - startDate.getTime()) / 1000);
	    }
	    
	    if(sec > 999) { sec = 999; }
	    g.setColor(Color.WHITE);
	    g.setFont(new Font("Arial", Font.PLAIN, 35));
	    if (sec < 10) {
	        g.drawString("00" + Integer.toString(sec), timeX + 10, timeY + 28);
	    } else if (sec < 100) {
	        g.drawString("0" + Integer.toString(sec), timeX + 10, timeY + 28);
	    } else if (sec < 1000) {
	        g.drawString(Integer.toString(sec), timeX + 10, timeY + 28);
	    }
		repaint();
		
		/*
		 * Adds flag counter at the bottom left of the panel
		 */
		g.setColor(Color.BLACK);
		g.fillRect(flagX, flagY, 70, 33);
		g.setColor(Color.WHITE);
		g.setFont(new Font("Arial", Font.PLAIN, 35));
		if(flagCount == MAX_FLAGS) {
			g.drawString("0" + Integer.toString(flagCount), flagX + 10, flagY + 28);
		} else if (flagCount < MAX_FLAGS) {
			g.drawString("00" + Integer.toString(flagCount), flagX + 10, flagY + 28);
		}
	}
	
	/*
	 * Gets a color based on the amount of mines surrounding the cell
	 * @param int mines amount
	 * @return Color
	 */
    private Color getNumberColor(int total) {
        Color color = null;
        switch (total) {
            case 1:
                color = Color.BLUE;
                break;
            case 2:
                color = Color.RED;
                break;
            case 3:
                color = Color.YELLOW;
                break;
            case 4:
            	    color = Color.GREEN;
            	    break;
            default:
                color = Color.CYAN;
                break;
        }
        return color;
    }
    
    /*
     * Reveals adjacent cells that don't have a mine
     * and paints the amount of mines on the cells nearby
     * @param int x coordinate
     * @param int y coordinate
     */
	public void revealAdjacent(int x, int y) {
		if ((x < 0) || (y < 0) || (x >= 9) || (y >= 9)) { return; }
		if (mines.isMine(x, y)) { return; }
		if (colorArray[x][y] == MyMouseAdapter.FLAG_COLOR) { return; }
		if (mines.hasMinesNearby(x, y) && colorArray[x][y] != MyMouseAdapter.REVEALED) {
			int counter = mines.getMinesNearbyCount(x, y);
			colorArray[x][y] = MyMouseAdapter.REVEALED;
			minesNearby[x][y] = counter;
			countTotal++;
			repaint();
			return;
		} else {
			if (colorArray[x][y] == MyMouseAdapter.REVEALED) { return; }
			countTotal++;
			colorArray[x][y] = MyMouseAdapter.REVEALED;
			repaint();
			revealAdjacent(x-1, y);
			revealAdjacent(x, y-1);
			revealAdjacent(x+1, y);
			revealAdjacent(x, y+1);
		}
		repaint();
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
		if ((x % INNER_CELL_PLUS == 0) || (y % INNER_CELL_PLUS == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / INNER_CELL_PLUS;
		y = y / INNER_CELL_PLUS;
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
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
		if ((x % INNER_CELL_PLUS == 0) || (y % INNER_CELL_PLUS == 0)) {   //Coordinate is at an edge; not inside a cell
			return -1;
		}
		x = x / INNER_CELL_PLUS;
		y = y / INNER_CELL_PLUS;
		if (x < 0 || x > TOTAL_COLUMNS - 1 || y < 0 || y > TOTAL_ROWS - 1) {   //Outside the rest of the grid
			return -1;
		}
		return y;
	}
}