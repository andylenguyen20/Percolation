import java.util.Arrays;

/**
 * Simulate percolation thresholds for a grid-base system using depth-first-search,
 * aka 'flood-fill' techniques for determining if the top of a grid is connected
 * to the bottom of a grid.
 * <P>
 * Modified from the COS 226 Princeton code for use at Duke. The modifications
 * consist of supporting the <code>IPercolate</code> interface, renaming methods
 * and fields to be more consistent with Java/Duke standards and rewriting code
 * to reflect the DFS/flood-fill techniques used in discussion at Duke.
 * <P>
 * @author Kevin Wayne, wayne@cs.princeton.edu
 * @author Owen Astrachan, ola@cs.duke.edu
 * @author Jeff Forbes, forbes@cs.duke.edu
 */


public class PercolationDFS implements IPercolate {
	// possible instance variable for storing grid state
	public int[][] myGrid;
	private int myOpenSites;
	/**
	 * Initialize a grid so that all cells are blocked.
	 * 
	 * @param n
	 *            is the size of the simulated (square) grid
	 */
	public PercolationDFS(int n) {
		// TODO complete constructor and add necessary instance variables
		if(n<=0){
			throw new IllegalArgumentException("Bad N values!");
		}
		myOpenSites = 0;
		myGrid = new int[n][n];
		for (int[] row: myGrid)
			Arrays.fill(row, BLOCKED);
		/*
		for (int i = 0; i < myGrid.length; i++)
			for (int j=0; j < myGrid[i].length; j++)
				myGrid[i][j] = BLOCKED;
		*/
	}

	public void open(int i, int j) {
		// TODO complete open
		if (myGrid[i][j] != BLOCKED)
			return;
		myOpenSites++;
		myGrid[i][j] = OPEN;
		
		for(int row = 0; row < myGrid.length; row++){
			for(int col = 0; col < myGrid[row].length; col++){
				if(myGrid[row][col] == FULL){
					myGrid[row][col] = OPEN;
				}
			}
		}
		for(int m = 0; m < myGrid[0].length; m++){
			if(myGrid[0][m] == OPEN)
				dfs(0, m);
		}
	}
	public boolean isOpen(int i, int j) {
		// TODO complete isOpen
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i +"," +j+
					" is bad!");
		return myGrid[i][j] == OPEN;
	}

	public boolean isFull(int i, int j) {
		// TODO complete isFull
		if (i < 0 || i >= myGrid.length || j < 0 || j >= myGrid[0].length)
			// out of bounds
			throw new IndexOutOfBoundsException("Index " + i +"," +j+
					" is bad!");
		return myGrid[i][j]==FULL;
	}

	public int numberOfOpenSites() {
		// TODO return the number of calls to open new sites
		return myOpenSites;
	}

	public boolean percolates() {
		// TODO: determine whether any cells on the bottom row are full
		for(int j = 0; j < myGrid.length; j++){
			if(myGrid[myGrid.length - 1][j] == FULL){
				return true;
			}
		}
		return false;
	}

	/**
	 * Private helper method to mark all cells that are open and reachable from
	 * (row,col).
	 * 
	 * @param row
	 *            is the row coordinate of the cell being checked/marked
	 * @param col
	 *            is the col coordinate of the cell being checked/marked
	 */
	private void dfs(int row, int col) {
		if (row < 0 || row >= myGrid.length || col < 0 ||
				col >= myGrid.length){
			// out of bounds
			return;
		}
		if (isFull(row, col) || !isOpen(row, col)){
			return;
		}
		myGrid[row][col] = FULL;
		dfs(row -1, col); // up
		dfs(row, col - 1); // left
		dfs(row, col + 1); // right
		dfs(row + 1, col); // down
	}
}
