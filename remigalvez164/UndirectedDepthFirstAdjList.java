import edu.gwu.util.*;
import edu.gwu.algtest.*;
import edu.gwu.debug.*;

import java.util.*;

public class UndirectedDepthFirstAdjList implements UndirectedGraphSearchAlgorithm {

	public int numVertices;                 // Number of vertices, given as input. 
	public int numEdges;                    // We keep track of the number of edges. 
	public boolean isWeighted;              // Is this a weighted graph? 
	public boolean useMatrix = false;        // Adjacency-matrix or list? 
	public double [][] adjMatrix;           // The matrix. Note.we use "double" to store 
								                          // "double" weights, if the graph is weighted. 

	public int[] visitOrder;                // visitOrder[i] = the i-th vertex to be visited in order. 
	public int visitCount;                  // We will track visits with this counter. 
	public int[] completionOrder;           // completionOrder[i] = the i-th vertex that completed. 
	public int completionCount;             // For tracking. 

	public List<LinkedList<GraphEdge>> adjList;
	public int[] componentLabel;
	public int currentComponentLabel;

	public UndirectedDepthFirstAdjList (int numVertices) {
		initialize(numVertices, false);
	}

	///////////////////////////////////////////
	///////// UDFAL Interface Methods /////////
	///////////////////////////////////////////

	public void initialize(int numVertices, boolean isWeighted) {
		String TAG = "initialize(" + numVertices + "," + isWeighted + ")";
		Log.d(TAG, "method called");

		this.numVertices = numVertices;
		this.isWeighted = true;
		visitOrder = new int[numVertices];
		completionOrder = new int[numVertices];
		componentLabel = new int[numVertices];
		adjMatrix = new double[numVertices][numVertices];
		adjList = new ArrayList<LinkedList<GraphEdge>>(numVertices);

		initSearch();

	}

	public void insertUndirectedEdge(int startVertex, int endVertex, double weight) {
		String TAG = "insertUndirectedEdge(" + startVertex + "," + endVertex + "," + weight + ")";
		Log.d(TAG, "method called");

		GraphEdge e = new GraphEdge(startVertex, endVertex, weight);

		if (adjList.get(startVertex).indexOf(e) != -1)
			return;

		// Add edge to list at the starting vertex's index.
		adjList.get(startVertex).add(new GraphEdge(startVertex, endVertex, weight));
		adjList.get(endVertex).add(new GraphEdge(endVertex, startVertex, weight));
	}

	public int[] depthFirstVisitOrder() {
		String TAG = "depthFirstVisitOrder";
		Log.d(TAG, "method called");
		
		depthFirstList();

		Log.a(TAG, visitOrder, "Vist order");
		return visitOrder;
	}

	public int[] depthFirstCompletionOrder() {
		String TAG = "depthFirstCompletionOrder";
		Log.d(TAG, "method called");

		depthFirstList();

		Log.a(TAG, completionOrder, "Completion Order");
		return completionOrder;
	}

	public int numConnectedComponents() {
		String TAG = "numConnectedComponents";
		Log.d(TAG, "method called");
		// TODO.Implement this method

		depthFirstList();

		int max = -1;
		for (int i = 0; i < numVertices; i++) {
			if (componentLabel[i] > max) {
				max = componentLabel[i];
			}
		}
		return max+1;
	}

	public int[] componentLabels() {
		String TAG = "componentLabels";
		Log.d(TAG, "method called");
		
		depthFirstList();

		return componentLabel;
	}

	//////////////////////////////
	///////// My Methods /////////
	//////////////////////////////

	public void initSearch() {
		String TAG = "initSearch";
		Log.d(TAG, "method called");
    // IMPORTANT.all initializations will use "-1". We will test 
    // equality with "-1", so it's important not to change this cavalierly. 
    visitCount = -1;
    completionCount = -1;
    currentComponentLabel = -1;
    for (int i=0; i < numVertices; i++) {
      visitOrder[i] = -1;
      completionOrder[i] = -1;
      componentLabel[i] = -1;
      adjList.add(new LinkedList<>());
      for (int j = 0; j < numVertices; j++) {
      	adjMatrix[i][j] = -1;
      }
    }
  }

  // List implementation of depth-first search.
  public void depthFirstList() {
  	String TAG = "depthFirstList";
		Log.d(TAG, "method called");
  	// 1. Initialize visit variables (same initialization for breadth-first search). 
    initSearch ();

    // 2. Find an unvisited vertex and apply depth-first search to it. 
    //    Note.if the graph is connected, the call with i=0 will result 
    //          in visiting all vertices. Nonetheless, we don't know this  
    //          in advance, so we need to march through all the vertices. 

    for (int i=0; i < numVertices; i++) {
      if (visitOrder[i] < 0) {
      	currentComponentLabel++;
      	componentLabel[i] = currentComponentLabel;
        depthFirstListRecursive (i);
      }
    }
  }

  // Recursive visiting of vertices starting from a vertex. 
  public void depthFirstListRecursive(int v) {
  	String TAG = "depthFirstListRecursive(" + v + ")";
		Log.d(TAG, "method called");

    // 1. First, visit the given vertex. Note.visitCount is a global. 
    visitCount++;
    visitOrder[v] = visitCount;
    Log.i(TAG, visitCount, "Visit Count");

    LinkedList<GraphEdge> vtxs = adjList.get(v);

    for (GraphEdge vtx: vtxs) {    	
    	int i = vtx.endVertex;
    	if (visitOrder[i] == -1) {
    		// current.endVertex is an unvisited neighbor.
    		depthFirstListRecursive(i);
    		componentLabel[i] = currentComponentLabel;
    	}
    }

    // 3. After returning from recursion(s), set the post-order or "completion" order number. 
    completionCount++;
    completionOrder[v] = completionCount;
    Log.i(TAG, completionCount, "Completion count");
  }

  // Random coin flip
  public static boolean randomCoinFlip (double p) {
    if (UniformRandom.uniform() < p)
      return true;
    else
      return false;
  }

  public static int coinFlipGraphComponents (int numVertices, double p) {
  	// Instantiate new graph on n vertices
  	UndirectedDepthFirstAdjList g = new UndirectedDepthFirstAdjList(numVertices);
  	// Iterate through vertices
  	for (int i = 0; i < numVertices; i++) {
  		// Iterate through vertices
  		for (int j = i+1; j < numVertices; j++) {
  			if (randomCoinFlip(p))
  				g.insertUndirectedEdge(i, j, 1);
  		}
  	}
  	int a = g.numConnectedComponents();
  	return a;
  }

	///////////////////////////////////////////////
	///////// Algorithm Interface Methods /////////
	///////////////////////////////////////////////

	public String getName() {
	  return "remigalvez16 - UndirectedDepthFirstAdjList";
	}

	/////////////////////////////////////////
	///////// Unimplemented Methods /////////
	/////////////////////////////////////////

	public void setPropertyExtractor(int algID, edu.gwu.util.PropertyExtractor prop) {  }
	public int[] breadthFirstVisitOrder() { return null; }
	public boolean existsCycle() { return false; }
	public int[] articulationVertices() { return null; }
	public GraphEdge[] articulationEdges() { return null; }
	public boolean existsOddCycle() { return false; }

	///////////////////////////////
	///////// Main method /////////
	///////////////////////////////

	public static void main(String[] args) {
		Tester.testDFS();
		Tester.testCoinFlip();
	}

	/////////////////////////////////
	///////// Inner-classes /////////
	/////////////////////////////////

	private static class Log {
		static boolean DEBUG = false;
		static final String CLASS_NAME = "UndirectedDepthFirstAdjList";

		public static void d(String TAG, String message) {
			if (DEBUG) System.out.println("DEBUGGER." + CLASS_NAME + "." + TAG + ": " + message);
		}

		public static void i(String TAG, int i, String name) {
			if (DEBUG) System.out.println("DEBUGGER." + CLASS_NAME + "." + TAG + "." + name + ": " + i);
		}

		public static void a(String TAG, int[] array, String name) {
			if (DEBUG) {
				if (array.length < 1) {
					System.out.println("DEBUGGER." + CLASS_NAME + "." + TAG + "." + name + ": " + "Empty");
				}
				System.out.print("DEBUGGER." + CLASS_NAME + "." + TAG + "." + name + ": ");
				System.out.print("[ " + array[0]);
				for (int i = 1; i < array.length; i++) {
					System.out.print(", " + array[i]);
				}
				System.out.println(" ]");
			}
		}
	}

	private static class Tester {
		static String TAG = "Tester";

		public static boolean testDFS() {
			TAG += ".testDFS";

			boolean success = false;

			UndirectedDepthFirstAdjList graph = new UndirectedDepthFirstAdjList(10);
			graph.insertUndirectedEdge(0, 1, 1);
			graph.insertUndirectedEdge(0, 2, 1);
			graph.insertUndirectedEdge(1, 2, 1);
			graph.insertUndirectedEdge(3, 4, 1);
			graph.insertUndirectedEdge(3, 5, 1);
			graph.insertUndirectedEdge(4, 6, 1);
			graph.insertUndirectedEdge(5, 6, 1);
			graph.insertUndirectedEdge(7, 8, 1);
			graph.insertUndirectedEdge(7, 9, 1);
			graph.insertUndirectedEdge(8, 9, 1);
			if (graph.numConnectedComponents() == 3)
				success = true;
			else {
				System.out.println("Tests failed.");
				return false;
			}
			graph.insertUndirectedEdge(2, 3, 1);
			if (graph.numConnectedComponents() == 2)
				success = true;
			else {
				System.out.println("Tests failed.");
				return false;
			}
			graph.insertUndirectedEdge(5, 7, 1);
			if (graph.numConnectedComponents() == 1)
				success = true;
			else {
				System.out.println("Tests failed.");
				return false;
			}

			System.out.println("Tests passed.");
			return success;
		}

		public static void testCoinFlip() {
			// Declare array to store average number of components for values 0.1, 0.2, ..., 0.9
			double[] average = new double[10];
			double p;
			for (int i = 1; i < 10; i++) {
				p = (double) i / 10.0;
				int sum = 0;
				for (int j = 0; j < 1000; j++) {
					int n = (int) UniformRandom.uniform(10, 1000);
					int numComponents = UndirectedDepthFirstAdjList.coinFlipGraphComponents(n, p);
					sum += numComponents;
				}
				average[i] = (double) sum / 1000.0;
				System.out.println("Number of components: " + average[i] + "\tfor p = " + p);
			}
		}

	}

} // end-class