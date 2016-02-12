import java.util.*;

import edu.gwu.algtest.*;

public class EdgeSorter {

	public static GraphEdge[] getEdges(double[][] adjMatrix) {
		final String TAG = "getEdges(adjMatrix)";
		Log.m(TAG);

		List<GraphEdge> edges = new LinkedList<>();
		// Iterate through matrix entries
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix.length; j++) {
				// If edge exists, add edge to list
				if (adjMatrix[i][j] > 0) 
					edges.add(new GraphEdge(i, j, adjMatrix[i][j]));
			}
		}
		// Fill array 
		GraphEdge[] sortedEdges = new GraphEdge[edges.size()];
		for (int i = 0; i < edges.size(); i++) {
			sortedEdges[i] = edges.get(i);
		}
		// Return array
		return sortedEdges;
	}

	public static GraphEdge[] sort(double[][] adjMatrix) {
		final String TAG = "sort(adjMatrix)";
		Log.m(TAG);

		GraphEdge[] e = getEdges(adjMatrix);
		sort(e);

		return e;
	}

	// Regular quicksort
	public static void sort(GraphEdge[] edges) {
		final String TAG = "sort(edges)";
		Log.m(TAG);

		sortRecursive(edges, 0, edges.length-1);
	}
	// Recursive part of quicksort
	private static void sortRecursive(GraphEdge[] edges, int l, int r) {
		final String TAG = "sortRecursive(edges, " + l + ", " + r + ")";
		Log.m(TAG);

		if (l >= r)
			return;

		int mid = partition(edges, l, r);

		sortRecursive(edges, l, mid-1);
		sortRecursive(edges, mid, r);

		return;
	}
	// Partition
	private static int partition(GraphEdge[] edges, int l, int r) {
		final String TAG = "partition(edges, " + l + ", " + r + ")";
		Log.m(TAG);

		double midWeight = edges[l].weight;

		int ll = l+1; int rr = r;
		while (ll < rr) {
			while (edges[ll].weight <= midWeight && ll < rr) 
				ll++;
			while (edges[rr].weight > midWeight && rr > ll) 
				rr--;
			if (ll < rr) 
				swap(edges, ll, rr);
		}
		swap(edges, l, rr-1);

		return rr;
	}

	private static void swap(GraphEdge[] edges, int e, int f) {
		final String TAG = "swap(edges, " + e + ", " + f + ")";
		Log.m(TAG);

		GraphEdge temp = edges[e];
		edges[e] = edges[f];
		edges[f] = temp;
	}

	private static class Log {
		static boolean DEBUG = false;
		static final String CLASS_NAME = "EdgeSorter";

		public static void m(String TAG) {
			if (DEBUG) System.out.println(getDescription(TAG) + "Method Called.");
		}

		public static void d(String TAG, String message) {
			if (DEBUG) System.out.println(getDescription(TAG) + message);
		}

		public static void i(String TAG, int i, String name) {
			if (DEBUG) System.out.println(getDescription(TAG) + i);
		}

		public static void a(String TAG, int[] array, String name) {
			if (DEBUG) {
				if (array.length < 1) {
					System.out.println(getDescription(TAG) + name + ": " + "Empty");
				}
				System.out.print(getDescription(TAG) + name + ": ");
				System.out.print("[ " + array[0]);
				for (int i = 1; i < array.length; i++) {
					System.out.print(", " + array[i]);
				}
				System.out.println(" ]");
			}
		}

		public static String getDescription(String TAG) {
			return "DEBUGGER." + CLASS_NAME + "." + TAG + ": ";
		}
	}

}