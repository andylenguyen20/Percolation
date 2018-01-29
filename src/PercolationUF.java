import java.util.Arrays;

/**
 * Simulate a system to see its Percolation Threshold, but use a UnionFind
 * implementation to determine whether simulation occurs. The main idea is that
 * initially all cells of a simulated grid are each part of their own set so
 * that there will be n^2 sets in an nxn simulated grid. Finding an open cell
 * will connect the cell being marked to its neighbors --- this means that the
 * set in which the open cell is 'found' will be unioned with the sets of each
 * neighboring cell. The union/find implementation supports the 'find' and
 * 'union' typical of UF algorithms.
 * <P>
 * 
 * @author Owen Astrachan
 * @author Jeff Forbes
 *
 */

public class PercolationUF implements IPercolate {
	private final int OUT_BOUNDS = -1;
	private final int INDEX_SOURCE = 0;
	private final int INDEX_SINK;
	public int[][] myGrid;
	public QuickUWPC QF;
	private int myOpenSites;
	/**
	 * Constructs a Percolation object for a nxn grid that creates
	 * a IUnionFind object to determine whether cells are full
	 */
	public PercolationUF(int n) {
		// TODO complete PercolationUF constructor
		if(n<=0){
			throw new IllegalArgumentException("Bad N values!");
		}
		QF = new QuickUWPC(n * n + 2);
		INDEX_SINK = n*n + 1;
		myOpenSites = 0;
		myGrid = new int[n][n];
		for (int[] row: myGrid)
			Arrays.fill(row, BLOCKED);
	}

	/**
	 * Return an index that uniquely identifies (row,col), typically an index
	 * based on row-major ordering of cells in a two-dimensional grid. However,
	 * if (row,col) is out-of-bounds, return OUT_BOUNDS.
	 */
	public int getIndex(int row, int col) {
		// TODO complete getIndex
		if (row < 0 || row >= myGrid.length || col < 0 ||
				col >= myGrid.length){
			// out of bounds
			return OUT_BOUNDS;
		}
		return myGrid.length*row+col+1;
		
	}

	public void open(int i, int j) {
		// TODO complete open
		if (myGrid[i][j] != BLOCKED)
			return;
		myOpenSites++;
		myGrid[i][j] = OPEN;
		connect(i,j);
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
		return QF.connected(getIndex(i,j), INDEX_SOURCE);
	}

	public int numberOfOpenSites() {
		// TODO return the number of calls to open new sites
		return myOpenSites;
	}

	public boolean percolates() {
		// TODO complete percolates
		return QF.connected(INDEX_SINK, INDEX_SOURCE);
	}

	/**
	 * Connect new site (row, col) to all adjacent open sites
	 */
	private void connect(int row, int col) {
		// TODO complete connect
		int index = getIndex(row,col);
		//check if we can link to source or sink
		if(row == 0){
			QF.union(index, INDEX_SOURCE);
		}
		if(row == myGrid.length - 1){
			QF.union(INDEX_SINK, index);
		}
		
		//link to cells around it
		if(row > 0 && isOpen(row - 1, col)){
			QF.union(index, getIndex(row - 1, col));
		}
		if(row < myGrid.length - 1 && isOpen(row + 1, col)){
			QF.union(index, getIndex(row + 1, col));
		}
		if(col > 0 && isOpen(row, col - 1)){
			QF.union(index, getIndex(row, col - 1));		
		}
		if(col < myGrid[0].length - 1 && isOpen(row, col + 1)){
			QF.union(index, getIndex(row, col + 1));
		}
	}

}
