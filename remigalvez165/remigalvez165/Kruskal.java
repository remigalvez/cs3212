import edu.gwu.util.*;
import edu.gwu.algtest.*;
import edu.gwu.debug.*;

import java.util.*;

public class Kruskal implements SpanningTreeAlgorithm {

	int algID;
	PropertyExtractor props;

	int numVertices;
	int[] component;
	GraphEdge[] sortedEdge;
	double[] sortedEdgeWeight;
	double[][] mst;

	boolean directed;

	///////////////////////////////////////
	//// SpanningTreeAlgorithm methods ////
	///////////////////////////////////////

	public void initialize(int numVertices) {
		final String TAG = "initialize(" + numVertices + ")";
		Log.m(TAG);
		
		// Initialize variables
		this.numVertices = numVertices;
		this.mst = new double[numVertices][numVertices];
		this.component = new int[numVertices];

		// Set each vertex as individual component
		for (int i = 0; i < numVertices; i++) {
			component[i] = i;
		}
	}

	public double[][] minimumSpanningTree(double[][] adjMatrix) {
		final String TAG = "minimumSpanningTree(adjMatrix)";
		Log.m(TAG);

		// Check if graph is directed
		directed = isDirected(adjMatrix);
		
		// Get array of edges sorted in order of increasing weight
		sortedEdge = EdgeSorter.sort(adjMatrix);

		// Iterate through edges
		for (int i = 0; i < sortedEdge.length; i++) {
			int s = sortedEdge[i].startVertex;
			int e = sortedEdge[i].endVertex;
			int comp1 = component[s];
			int comp2 = component[e];

			// If new edge has both endpoints in same component, skip edge
			if (comp1 == comp2) continue;
			// Otherwise
			else {
				// Join both components
				int comp = min(component[s], component[e]);
				for (int k = 0; k < numVertices; k++) {
					if (component[k] == comp1 || component[k] == comp2) 
						component[k] = comp;
				}
				// Add edge to MST
				addToSpanningTree(sortedEdge[i]);
			}
		}

		Log.r(TAG, "mst");
		return mst;
	}

	public GraphVertex[] minimumSpanningTree(GraphVertex[] adjList) { return null; }

	public double getTreeWeight() {
		final String TAG = "getTreeWeight";
		Log.m(TAG);
		
		double sum = 0.0;

		// If graph is not directed, sum entries of upper triangular matrix
		if (!directed) {
			for (int i = 0; i < numVertices; i++) {
				for (int j = i+1; j < numVertices; j++) {
					sum += adjMatrix[i][j];
				}
			}
		} 
		// Otherwise, sum entries of entire matrix
		else {
			for (int i = 0; i < numVertices; i++) {
				for (int j = 0; j < numVertices; j++) {
					sum += adjMatrix[i][j];
				}
			}
		}

		return sum;
	}

	////////////////////
	//// My methods ////
	////////////////////

	public int min(int a, int b) {
		return a < b ? a : b;
	}

	public void addToSpanningTree(GraphEdge edge) {
		int s = edge.startVertex;
		int e = edge.endVertex;
		double w = edge.weight;

		// Add weighted edge to MST
		mst[s][e] = w;
		// Add reverse edge if graph is not directed
		if (!directed) 
			mst[e][s] = w;
	}

	public boolean isDirected(double[][] adjMatrix) {
		// Check if upper triangular matrix is equal to lower triangular matrix
		for (int i = 0; i < numVertices; i++) {
			for (int j = i+1; j < numVertices; j++) {
				if (adjMatrix[i][j] != adjMatrix[j][i])
					return true;
			}
		}
		return false;
	}

	/////////////////////////
	//// Algorithm methods //
	/////////////////////////


	public String getName() {
		final String TAG = "getName";
		return "remigalvez16 - Kruskal";
	}

	public void setPropertyExtractor(int algId, PropertyExtractor props) { 
		this.algID = algID;
		this.props = props;
	}

	public static void main(String[] args) {






	}



	////////////////////////////////////
	//// Logger class for debugging ////
	////////////////////////////////////

	private static class Log {
		static boolean DEBUG = false;
		static final String CLASS_NAME = "Kruskal";

		public static void m(String TAG) {
			if (DEBUG) System.out.println(getDescription(TAG) + "Method Called.");
		}

		public static void r(String TAG, String val) {
			if (DEBUG) System.out.println(getDescription(TAG) + "Returning: " + val);	
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
