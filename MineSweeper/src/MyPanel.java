import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Random;

import javax.swing.JPanel;

public class MyPanel extends JPanel {
	private static final long serialVersionUID = 3426940946811133635L;
	private static final int GRID_X = 25;
	private static final int GRID_Y = 25;
	private static final int INNER_CELL_SIZE = 29;
	private static final int TOTAL_COLUMNS = 10;
	private static final int TOTAL_ROWS = 11;   //Last row has only one cell
	public static int getInnerCellSize() {
		return INNER_CELL_SIZE;
	}
	public static int getTotalColumns() {
		return TOTAL_COLUMNS;
	}
	public static int getTotalRows() {
		return TOTAL_ROWS;
	}
	public int x = -1;
	public int y = -1;
	public int mouseDownGridX = 0;
	public int mouseDownGridY = 0;
	public Color[][] colorArray = new Color[TOTAL_COLUMNS][TOTAL_ROWS];
	public int[][] location = new int[TOTAL_COLUMNS][TOTAL_ROWS]; 
	public int[][] adjacent = new int[TOTAL_COLUMNS][TOTAL_ROWS]; 
	private int counter = 0;
	private int[] xsquare =  new int [81];
	private int[] ysquare = new int[81];
	private int[] s = new int[81];
	public MyPanel() {   //This is the constructor... this code runs first to initialize
		int mine = 0;
		int mineTotal = 0;
		Random rand = new Random();
		if (INNER_CELL_SIZE + (new Random()).nextInt(1) < 1) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("INNER_CELL_SIZE must be positive!");
		}
		if (TOTAL_COLUMNS + (new Random()).nextInt(1) < 2) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_COLUMNS must be at least 2!");
		}
		if (TOTAL_ROWS + (new Random()).nextInt(1) < 3) {	//Use of "random" to prevent unwanted Eclipse warning
			throw new RuntimeException("TOTAL_ROWS must be at least 3!");
		}
		for (int x = 0; x < TOTAL_COLUMNS; x++) {   //Top row
			colorArray[x][0] = Color.LIGHT_GRAY;
		}
		for (int y = 0; y < TOTAL_ROWS; y++) {   //Left column
			colorArray[0][y] = Color.LIGHT_GRAY;
		}
		while (mineTotal < 10)
		{
			for (int x = 1; x < 10; x++) {   //The rest of the grid
				for (int y = 1; y < 10; y++) {
					mine = rand.nextInt(10);
					if (mine == 1 && mineTotal < 10 && location[x][y] != 1)
					{
						mineTotal++;
						location[x][y] = 1;
					}
					colorArray[x][y] = Color.WHITE;
				}
			}
		}
	
		//We check adjacent mines in all squares and store there counters with there position
		for (int i = 1; i < 10; i++)
		{
			for (int j = 1; j < 10; j++)
			{
				adjacent[i][j] = check(i, j, 1);
			}
		}
		
	}
	public void paintComponent(Graphics g) {
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
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(x1, y1, width + 1, height + 1);

		
		//Paint cell colors
		for (int x = 0; x < TOTAL_COLUMNS; x++) {
			for (int y = 0; y < TOTAL_ROWS; y++) {
				if ((x == 0) || (y != TOTAL_ROWS - 1)) {
					Color c = colorArray[x][y];
					g.setColor(c);
					g.fillRect(x1 + GRID_X + (x * (INNER_CELL_SIZE + 1)) + 1, y1 + GRID_Y + (y * (INNER_CELL_SIZE + 1)) + 1, INNER_CELL_SIZE, INNER_CELL_SIZE);
				}
			}
		}
		
		//This shows the all of the squares with adjacent mines that have been uncovered.
		for (int i = 0; i < this.counter; i++)
		{
			mineTotal(xsquare[i], ysquare[i], s[i], g);
		}

	}
	
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
	
	public void drawText(int x, int y, int s)
	{
		this.xsquare[this.counter] = x;
		this.ysquare[this.counter] = y;
		this.s[this.counter] = s;
		this.counter++;
	}
	
	public void mineTotal(int x, int y, int s, Graphics g)
	{
		if (s == 1)
		{
			g.setColor(Color.BLUE);
		}
		else if (s == 2)
		{
			g.setColor(Color.GREEN);
		}
		else if (s == 3)
		{
			g.setColor(Color.YELLOW);
		}
		else
		{
			g.setColor(Color.RED);
		}
		g.drawString(String.valueOf(s),(INNER_CELL_SIZE*x)+(int)(1.4 * INNER_CELL_SIZE), (INNER_CELL_SIZE*y)+(int)(1.65 * INNER_CELL_SIZE));
	}
	
	public int check(int x, int y, int area)
	{
		int c = 0;
		if (x == 1)
		{
			if (y == 1)
			{
				for (int i = x; i <=  x + area; i++)
				{
					for (int j = y; j <=  y + area; j++)
					{
						
						if (location[i][j] == 1)
						{
							c++;
						}
					}
				}
			}
			else if (y == 9)
			{
				for (int i = x; i <=  x + area; i++)
				{
					for (int j = y - area; j <=  y; j++)
					{
						
						if (location[i][j] == 1)
						{
							c++;
						}
					}
				}
			}
			else
			{
				for (int i = x; i <=  x + area; i++)
				{
					for (int j = y - area; j <=  y + area; j++)
					{
						
						if (location[i][j] == 1)
						{
							c++;
						}
					}
				}
			}
		}
		else if (x == 9)
		{
			if (y == 1)
			{
				for (int i = x - area; i <=  x; i++)
				{
					for (int j = y; j <=  y + area; j++)
					{
						
						if (location[i][j] == 1)
						{
							c++;
						}
					}
				}
			}
			else if (y == 9)
			{
				for (int i = x - area; i <=  x; i++)
				{
					for (int j = y - area; j <=  y; j++)
					{
						
						if (location[i][j] == 1)
						{
							c++;
						}
					}
				}
			}
			else
			{
				for (int i = x - area; i <=  x; i++)
				{
					for (int j = y - area; j <=  y + area; j++)
					{
						
						if (location[i][j] == 1)
						{
							c++;
						}
					}
				}
			}
		}
		else if (y == 1)
		{
			for (int i = x - area; i <=  x + area; i++)
			{
				for (int j = y; j <=  y + area; j++)
				{
					
					if (location[i][j] == 1)
					{
						c++;
					}
				}
			}
		}
		else if (y == 9)
		{
			for (int i = x - area; i <=  x + area; i++)
			{
				for (int j = y-area; j <=  y; j++)
				{
					
					if (location[i][j] == 1)
					{
						c++;
					}
				}
			}
		}
		else
		{
			for (int i = x - area; i <=  x + area; i++)
			{
				for (int j = y - area; j <=  y + area; j++)
				{
					
					if (location[i][j] == 1)
					{
						c++;
					}
				}
			}
		}
		return c;
	}
	
	public void floodFill(int x, int y) {
	    if (x > 0 && x <= 9 && y > 0 && y <= 9) {
	        if (location[x][y] == 0 && colorArray[x][y] != Color.GRAY && adjacent[x][y] ==  0) {
	            colorArray[x][y] = Color.GRAY;
	            floodFill(x - 1, y);
	            floodFill(x + 1, y);
	            floodFill(x, y - 1);
	            floodFill(x, y + 1);
	        } else if (location[x][y] == 0 && colorArray[x][y] != Color.GRAY && adjacent[x][y] > 0){
	        	colorArray[x][y] = Color.GRAY;
	        	drawText(x, y, adjacent[x][y]);
	            return;
	        }
	        else
	        {
	        	return;
	        }
	    }
	}
	
}