import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Compute statistics on Percolation afte performing T independent experiments
 * on an N-by-N grid. Compute 95% confidence interval for the percolation
 * threshold, and mean and std. deviation Compute and print timings
 * 
 * @author Kevin Wayne
 * @author Jeff Forbes
 * @author Josh Hug
 */
/*
 * A class that given N and T, performs T experiments on an NxN grid, and
 * computes the mean, standard deviation, and confidence interval of the
 * percolation threshold, and timings of percolation simulations.
 */
public class PercolationStats {
	public static int RANDOM_SEED = 1234;
	public static Random ourRandom = new Random(RANDOM_SEED);
	private int mySize;
	private IPercolate myPerc;
	private double[] myThresholds;
	// TODO Add methods as described in assignment writeup
	public PercolationStats(int N, int T){
		// perform T independent experiments on an N-by-N grid
		if(N<=0 || T <= 0){
			throw new IllegalArgumentException("Bad N and T values!");
		}
		mySize = N;
		myThresholds = new double[T];
		for(int i = 0; i < T; i++){
			myPerc = new PercolationUF(N);   //ADJUST HERE!
			ArrayList<Point> sites = new ArrayList<Point>();
			for (int k = 0; k < mySize; k++)
				for (int j = 0; j < mySize; j++)
					sites.add(new Point(k,j));
			Collections.shuffle(sites, ourRandom);
			for (Point cell: sites) {
				myPerc.open(cell.y, cell.x);
				if (myPerc.percolates()){
					break;
				}
			}
			double numOpen = myPerc.numberOfOpenSites();
			double threshold = numOpen / (N*N);
			myThresholds[i] = threshold;
		}
	}
	public double mean(){
		// sample mean of percolation threshold
		double sum = 0;
		for(int i = 0; i < myThresholds.length; i++){
			sum = myThresholds[i] + sum;
		}
		return sum / myThresholds.length;
		
	}

	public double stddev(){
		// sample standard deviation of percolation threshold
		double sum = 0;
		for(int i = 0; i < myThresholds.length; i++){
			sum += Math.pow(myThresholds[i]-mean(), 2);
		}
		return Math.sqrt(sum / (myThresholds.length - 1));
	}

	public double confidenceLow(){
		// low  endpoint of 95% confidence interval
		return mean() - 1.96 * stddev()/ Math.sqrt(myThresholds.length);
	}

	public double confidenceHigh(){
		// high endpoint of 95% confidence interval
		return mean() + 1.96 * stddev()/ Math.sqrt(myThresholds.length);
	}

	public static void main(String[] args){
		// print out values for testing &  analysis
		long start = System.currentTimeMillis();
		PercolationStats pst = new PercolationStats(500,1);
		
		System.out.println("Mean: " + pst.mean());
		System.out.println("Standard deviation: " + pst.stddev());
		System.out.println("Low endpoint of 95% Confidence Interval: " + pst.confidenceLow());
		System.out.println("High endpoint of 95% Confidence Interval: " + pst.confidenceHigh());
		long end = System.currentTimeMillis();
		System.out.print("time in milliseconds: ");
		System.out.println(end-start);
	}
}
