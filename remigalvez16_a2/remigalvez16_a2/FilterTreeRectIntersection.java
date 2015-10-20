import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;

public class FilterTreeRectIntersection implements RectangleSetIntersectionAlgorithm {
	// LOGGING
	public static final boolean TRACE = false;
	public static final boolean DEBUG = false;
	public static final boolean TESTING = false;

	// FINAL TREE VALUES
	public final int MIN_X, MAX_X, MIN_Y, MAX_Y;
	public final int MAX_DEPTH;

	// DEFAULT TREE VALUES ( Used with default constructors )
	public final int DEFAULT_MIN_X = 0; 
	public final int DEFAULT_MAX_X = 10000; 
	public final int DEFAULT_MIN_Y = 0; 
	public final int DEFAULT_MAX_Y = 10000;
	public final int DEFAULT_MAX_DEPTH = 15;

	// Tree root
	public FilterTreeNode root;

	/**
	 * Constructor: Instantiates with default values
	 */
	public FilterTreeRectIntersection() {
		if ( TRACE ) System.out.println("CONSTRUCTOR (Default): " + getClassName() + "FilterTreeRectIntersection()");
		MIN_X = DEFAULT_MIN_X;
		MAX_X = DEFAULT_MAX_X;
		MIN_Y = DEFAULT_MIN_Y;
		MAX_Y = DEFAULT_MAX_Y;
		MAX_DEPTH = DEFAULT_MAX_DEPTH;
	}

	/**
	 * Constructor: Initializes root as square from (0,0) to (SIZE, SIZE)
	 */
	public FilterTreeRectIntersection( final int SIZE ) {
		MIN_X = 0; MAX_X = SIZE;
		MIN_Y = 0; MAX_Y = SIZE;
		MAX_DEPTH = DEFAULT_MAX_DEPTH;
	}

	/**
	 * Constructor: Initializes root as square from (0,0) to (SIZE, SIZE) and 
	 * sets the maximum tree depth to MAX_DEPTH
	 */
	public FilterTreeRectIntersection( final int SIZE, final int MAX_DEPTH ) {
		MIN_X = 0; MAX_X = SIZE;
		MIN_Y = 0; MAX_Y = SIZE;
		this.MAX_DEPTH = MAX_DEPTH;
	}

	/**
	 * Constructor: Initializes root as square from (0,0) to (SIZE, SIZE) and 
	 * builds filter tree from rectSet
	 */ 
	public FilterTreeRectIntersection( IntRectangle[] rectSet, final int SIZE ) {
		MIN_X = 0;
		MIN_Y = 0;
		MAX_X = SIZE;
		MAX_Y = SIZE;
		MAX_DEPTH = DEFAULT_MAX_DEPTH;

		makeFilterTree(rectSet, MIN_X, MAX_X, MIN_Y, MAX_Y);
	}

	/**
	 * Constructor: No default fields, all fields specified in constructor
	 */
	public FilterTreeRectIntersection( final int MIN_X, final int MAX_X, final int MIN_Y, final int MAX_Y, final int MAX_DEPTH ) {
		this.MIN_X = MIN_X;
		this.MAX_X = MAX_X;
		this.MIN_Y = MIN_Y;
		this.MAX_Y = MAX_Y;
		this.MAX_DEPTH = MAX_DEPTH;
	}

	private FilterTreeRectIntersection makeFilterTree(IntRectangle[] rectSet, final int MIN_X, final int MAX_X, final int MIN_Y, final int MAX_Y) { 			// Add dimensions (max(x, y))
		if ( TRACE ) System.out.println("METHOD: " + getClassName() + "makeFilterTree( data, " + MIN_X + ", " + MAX_X + ", " + MIN_Y + ", " + MAX_Y + " )");
		

		// Create and define root using minimum and maximum values
		root = new FilterTreeNode( MIN_X, MAX_X, MIN_Y, MAX_Y, 0 );
		// Iterate through dataset 
		for ( IntRectangle r : rectSet ) {
			if (r == null) continue;
			// Add node to tree
			addNode( r, root );
		}
		if ( DEBUG ) System.out.println("...\treturn 'this' object\t( makeFilterTree )");
		return this;
	}

	// Pseudocode: renamed method from "makeFilterTreeRecursive" to "addNode" for sensical purposes 
	public void addNode( IntRectangle r, FilterTreeNode quadrantNode ) {
		if ( quadrantNode == null ) {
			if ( TRACE ) System.out.println("ERROR: " + getClassName() + "addNode: Invalid argument FilterTreeNode (l.50)\t\tRect: " + r.ID);
		} if ( r == null ) {
			if ( TRACE ) System.out.println("ERROR: " + getClassName() + "addNode: Invalid argument IntRectangle (l.50)\t\tLevel: " + quadrantNode.level);
		}
		if ( TRACE ) System.out.println("METHOD: " + getClassName() + "addNode( Rect:" + r.ID + ", Level: " + quadrantNode.level + " )");


		// Compute appropriate subquadrant as an int to place node
		int quadrant = computeQuadrant( r, quadrantNode );
		// If rectangle intersects with several quadrants
		if ( quadrant == -1 || quadrantNode.level >= MAX_DEPTH ) {
			// Add rectangle to parent node `quadrantNode'
			quadrantNode.rectList.add( r );
			if ( DEBUG ) System.out.println("...\tRect: " + r.ID + " added.");
			return;
		}

		// If smaller subquadrant doesn't exist
		if ( quadrantNode.quadrants[quadrant] == null ) {
			// Compute subquadrant coordinates [ minX, maxX, minY, maxY ]
			int[] xyCoor = computeCoordinates( quadrant, quadrantNode );
			// Create subquadrant
			quadrantNode.quadrants[quadrant] = new FilterTreeNode( xyCoor[0], xyCoor[1],
																	xyCoor[2], xyCoor[3], 
																	quadrantNode.level + 1 );

			if ( DEBUG ) System.out.println("... Adding new subquadrant\t\t( addNode )");
		}
		// Recursive call
		addNode( r, quadrantNode.quadrants[quadrant] );

		if ( DEBUG ) System.out.println("...\treturn\t( addNode )");
		return;
	}

	private int computeQuadrant( IntRectangle r, FilterTreeNode node ) {
		if ( node == null ) {
			if ( TRACE ) System.out.println("ERROR: " + getClassName() + "addNode: Invalid argument FilterTreeNode (l.82)\t\tRect: " + r.ID);
		} if ( r == null ) {
			if ( TRACE ) System.out.println("ERROR: " + getClassName() + "addNode: Invalid argument IntRectangle (l.82)\t\tLevel: " + node.level);
		}
		if ( TRACE ) System.out.print("METHOD: " + getClassName() + "computeQuadrant( Rect: " + r.ID + ", Level: " + node.level + " )");


		int x, y;
		int topLeftQuad, bottomRightQuad;
		// Determine which half (horizontally) holds the top left corner's x coordinate
		if ( r.topLeft.x < node.midX ) 
			x = 0;
		else 
			x = 1;
		// Determine which half (vertically) holds the top left corner's y coordinate
		if ( r.topLeft.y < node.midY )
			y = 0;
		else 
			y = 1;
		// Compute subquadrant for bottom right corner
		if ( x == 0 && y == 0 ) topLeftQuad = 2;
		else if ( x == 0 && y == 1 ) topLeftQuad = 1;
		else if ( x == 1 && y == 0 ) topLeftQuad = 3;
		else topLeftQuad = 0;
		// Determine which half (horizontally) holds the bottom right corner's x coordinate
		if ( r.bottomRight.x < node.midX ) 
			x = 0;
		else 
			x = 1;
		// Determine which half (vertically) holds the bottom right corner's y coordinate
		if ( r.bottomRight.y < node.midY )
			y = 0;
		else 
			y = 1;
		// Compute subquadrant for bottom right corner
		if ( x == 0 && y == 0 ) bottomRightQuad = 2;
		else if ( x == 0 && y == 1 ) bottomRightQuad = 1;
		else if ( x == 1 && y == 0 ) bottomRightQuad = 3;
		else bottomRightQuad = 0;
		// If both corners are in the same subquadrant, return that subquadrant's index
		if ( topLeftQuad == bottomRightQuad ) {
			if ( DEBUG ) System.out.println("...\treturn " + topLeftQuad + "\t( computeQuadrant )");
			return topLeftQuad;
		}
		// Otherwise return -1
		if ( DEBUG ) System.out.println("...\treturn -1\t( computeQuadrant )");
		return -1;

	};

	private int[] computeCoordinates( int quadrant, FilterTreeNode n ) {
		if ( n == null ) {
			if ( TRACE ) System.out.println("ERROR: " + getClassName() + "computeCoordinates: Invalid argument FilterTreeNode (l.133)\t\tQuadrant: " + quadrant);
		}
		if ( TRACE ) System.out.print("METHOD: " + getClassName() + "computeCoordinates( " + quadrant + ", Level: " + n.level + " )");


		int[] xyCoor = new int[4];
		// Based on quadrant (case number), compute appropriate coordinates for subquadrant of n
		switch (quadrant) {
			case 0:
				xyCoor[0] = n.midX;
				xyCoor[1] = n.rightX;
				xyCoor[2] = n.midY;
				xyCoor[3] = n.topY;
				break;
			case 1:
				xyCoor[0] = n.leftX;
				xyCoor[1] = n.midX-1;
				xyCoor[2] = n.midY;
				xyCoor[3] = n.topY;
				break;
			case 2:
				xyCoor[0] = n.leftX;
				xyCoor[1] = n.midX-1;
				xyCoor[2] = n.botY;
				xyCoor[3] = n.midY-1;
				break;
			case 3:
				xyCoor[0] = n.midX;
				xyCoor[1] = n.rightX;
				xyCoor[2] = n.botY;
				xyCoor[3] = n.midY-1;
				break;
			default: 
				xyCoor[0] = -1;
				xyCoor[1] = -1;
				xyCoor[2] = -1;
				xyCoor[3] = -1;
				break;
		}


		if ( DEBUG ) System.out.print("...\treturn [ ");
		if ( DEBUG ) { for (int i = 0; i < 4; i++) System.out.print(xyCoor[i] + " "); System.out.println("]\t\t( computeCoordinates )"); }
		

		return xyCoor;
	}

	public List<IntRectangle> filterTreeSearch( IntRectangle rect ) {
		if ( rect == null )
			if ( DEBUG ) System.out.println("ERROR: " + getClassName() + "filterTreeSearch: Invalid argument IntRectangle (l.147)");
		if ( TRACE ) System.out.print("METHOD: " + getClassName() + "filterTreeSearch( Rect: " + rect.ID + " )");

		// Call recursive function starting with root node
		if ( DEBUG ) System.out.println("...\treturn recursive  ( filterTreeSearch )");
		return filterTreeSearchRecursive( rect, root );
	}

	public List<IntRectangle> filterTreeSearchRecursive( IntRectangle rect, FilterTreeNode node) {
		if ( node == null ) 
			if ( TRACE ) System.out.println("ERROR: Invalid argument FilterTreeNode (l.187)\t\tRect: " + rect.ID);
		if ( rect == null ) 
			if ( TRACE ) System.out.println("ERROR: Invalid argument IntRectangle (l.187)\t\tLevel: " + node.level);

		if ( TRACE ) System.out.println("METHOD: " + getClassName() + "filterTreeSearchRecursive( Rect: " + rect.ID + ", Level: " + node.level + " )");


		List<IntRectangle> intersections = new ArrayList<>();

		// Iterate through the node's list of rectangles
		Iterator<IntRectangle> n = node.rectList.iterator();
		while ( n.hasNext() ) {
			IntRectangle r = n.next();
			// Add rectangle if it intersects
			if ( intersect( r, rect ) ) {
				if ( DEBUG ) System.out.println("(l.204) Rect. " + r.ID + " and rect. " + rect.ID + " intersect.");
				intersections.add( r );
			}
		}

		// Compute subquadrant
		int quadrant = computeQuadrant( rect, node );
		List<IntRectangle> l;
		// If rectangle is in at least 2 quadrants
		if (quadrant == -1) {
			// Get quadrants rectangle intersects with
			List<Integer> intersectingQuads = getIntersectingQuadrants( rect, node );
			// Iterate through intersecting quadrants
			for (int i = 0; i < intersectingQuads.size(); i++) {
				int quad = intersectingQuads.get( i );
				if ( node.quadrants[quad] != null ) {
					// Call recursive function on quadrants the rectangle intersects with
					l = filterTreeSearchRecursive( rect, node.quadrants[quad]);
					// Add intersections from recursive function to list of current intersections
					if (l.size() > 0) 
						intersections.addAll( l );
				}
			}
		} else {
			if ( node.quadrants[quadrant] != null ) {
				// Call recursive function 
				l = filterTreeSearchRecursive( rect, node.quadrants[quadrant]);
				// Add intersections from recursive function to list of current intersections
				if (l.size() > 0)
					intersections.addAll( l );
			}
		}
		if ( DEBUG ) System.out.println("...\treturn " + intersections + "\t( filterTreeSearchRecursive )");
		// Return list of current intersections
		return intersections;
	}

	public List<Integer> getIntersectingQuadrants( IntRectangle rect, FilterTreeNode node ) {
		if ( node == null ) 
			if ( TRACE ) System.out.println("ERROR: " + getClassName() + "getIntersectingQuadrants: Invalid argument FilterTreeNode (l.240)\t\tRect: " + rect.ID);
		if ( rect == null ) 
			if ( TRACE ) System.out.println("ERROR: " + getClassName() + "getIntersectingQuadrants: Invalid argument IntRectangle (l.240)\t\tLevel: " + node.level);

		if ( TRACE ) System.out.println("METHOD: " + getClassName() + "getIntersectingQuadrants( Rect: " + rect.ID + ", Level: " + node.level + " )");


		List<Integer> intersectingQuads = new ArrayList<>();

		// Instantiate quadrants as IntRectangle
		IntRectangle[] quads = new IntRectangle[4];
		quads[0] = new IntRectangle(new IntPoint(node.midX, node.topY), new IntPoint(node.rightX, node.midY));
		quads[1] = new IntRectangle(new IntPoint(node.leftX, node.topY), new IntPoint(node.midX-1, node.midY));
		quads[2] = new IntRectangle(new IntPoint(node.leftX, node.midY-1), new IntPoint(node.midX-1, node.botY));
		quads[3] = new IntRectangle(new IntPoint(node.midX, node.midY-1), new IntPoint(node.rightX, node.botY));

		// Check which quadrants intersect with the rectangle
		for (int i = 0; i < 4; i++) {
			if ( intersect( rect, quads[i] ))
				// Add quadrant index to a list
				intersectingQuads.add( i );
		}
		if ( DEBUG ) System.out.println("...\treturn "  + intersectingQuads + "\t( getIntersectingQuadrants )");
		// Return list of indeces of quadrants that intersect with rect
		return intersectingQuads;
	}

	public boolean intersect( IntRectangle r, IntRectangle s ) {
		if ( r == null || s == null ) {
			if ( TRACE ) System.out.println("ERROR: " + getClassName() + "intersect: Invalid argument IntRectangle (l.266)");
		}
		// Uncomment for full trace. Very time-consuming when uncommented.
		// if ( TRACE ) System.out.print("METHOD: " + getClassName() + "intersect( Rect: " + r.ID + ", Rect: " + s.ID + " )");
		

		// Define overlaps, and set to false
		boolean xOverlap = false;
		boolean yOverlap = false;
		// Define horizontal and vertical delimiters
		int top1 = r.topLeft.y;
		int bottom1 = r.bottomRight.y;
		int left1 = r.topLeft.x;
		int right1 = r.bottomRight.x;
		// Define horizontal and vertical delimiters
		int top2 = s.topLeft.y;
		int bottom2 = s.bottomRight.y;
		int left2 = s.topLeft.x;
		int right2 = s.bottomRight.x;
		// Check for intersecting coordinates
		if ((left1 >= left2 && left1 <= right2) || (left2 >= left1 && left2 <= right1)) 
			xOverlap = true; 
		if ((top1 >= bottom2 && top1 <= top2) || (top2 >= bottom1 && top2 <= top1)) 
			yOverlap = true;
		// If rectangles overlap both horizontally and vertically
		if (xOverlap && yOverlap)
			return true;
		return false;
	}

	public IntPair[] findIntersections( IntRectangle[] rectSet1, IntRectangle[] rectSet2) { 
		if ( TRACE ) System.out.println("METHOD: " + getClassName() + "findIntersections( data1, data2 )");

		int numIntersections = 0;
		List<IntPair> intersections = new ArrayList<>();
		// First, place data rectangles in rectSet1 into tree.
		makeFilterTree(rectSet1, MIN_X, MAX_X, MIN_Y, MAX_Y);
		if ( DEBUG ) System.out.println("Filter tree built.");
		// Now scan rectangles in second set and query against tree.
		for (int i = 0; i < rectSet2.length; i++) {
			if ( rectSet2[i] == null ) continue;
			// Get list of intersections from tree.
			List<IntRectangle> intersectionSet = filterTreeSearch(rectSet2[i]);
			// Add all intersections to intersections list
			if (intersectionSet.size() > 0) {
				numIntersections += intersectionSet.size();
				for (int j = 0; j < intersectionSet.size(); j++) {
					intersections.add( new IntPair( intersectionSet.get(j).ID, rectSet2[i].ID ));
				}
			}
		}

		// If intersection list is empty, return null
		if ( intersections.size() <= 0 ) {
			if ( DEBUG ) System.out.println("...\treturn null\t( findIntersections )");
			return null;
		}

		// Fill results array with data from intersections list
		IntPair[] results = new IntPair[numIntersections];
		for (int i = 0; i < intersections.size(); i++) {
			results[i] = intersections.get( i );
		}


		// LOGGER //
		if ( DEBUG ) {
			System.out.print("...\treturn ");
			for (IntPair p : results) {
				System.out.print("[" + p.i + ", " + p.j + "], ");
			}
			System.out.println("\t( findIntersections )");
		}
		// LOGGER END //


		return results;
	}

	public String getName() {
            return "remigalvez16 - FilterTreeRectIntersection";
    }

    public String getClassName() {
    	return "FilterTreeRectIntersection.";
    }

    public void setPropertyExtractor(int algID, PropertyExtractor prop) {}

    public static void main(String[] args) {
    	FilterTreeRectIntersection tester = new FilterTreeRectIntersection( );
    	
    	System.out.println("TEST 1:");
    	boolean test1 = tester.test1();
    	
    	System.out.println("\nTEST 2:");
    	boolean test2 = tester.test2();

    	if ( test1 && test2 ) 
    		System.out.println("\nAll tests passed.");
    	else 
    		System.out.println("\nTesting failed.");
    }



    ///////////////////////////
    ///////// TESTING /////////
    ///////////////////////////


    // Test two datasets provided in all possible orders
    public boolean test1() {
    	IntRectangle[] data1 = new IntRectangle[5];
    	IntRectangle[] data2 = new IntRectangle[11];

    	data1[0] = new IntRectangle( 55, 95, 95, 55 );
    	data1[1] = new IntRectangle( 5, 95, 45, 55 );
    	data1[2] = new IntRectangle( 5, 45, 45, 5 );
    	data1[3] = new IntRectangle( 55, 45, 95, 5 );
    	data1[4] = new IntRectangle( 25, 75, 75, 25 );

    	data2[0] = new IntRectangle( 0, 4, 4, 0 );
    	data2[1] = new IntRectangle( 96, 100, 100, 96 );
    	data2[2] = new IntRectangle( 30, 90, 70, 80 );
    	data2[3] = new IntRectangle( 10, 70, 20, 30 );
    	data2[4] = new IntRectangle( 30, 20, 70, 10 );
    	data2[5] = new IntRectangle( 80, 70, 90, 30 );
    	data2[6] = new IntRectangle( 30, 70, 70, 30 );
    	
    	data2[7] = new IntRectangle( 65, 85, 85, 65 ); 
    	data2[8] = new IntRectangle( 15, 85, 35, 65 );
    	data2[9] = new IntRectangle( 15, 35, 35, 15 );
    	data2[10] = new IntRectangle( 65, 35, 85, 15 );

    	boolean status1 = false;
    	boolean status2 = false;

	    System.out.println("Running test 1...");
    	if ( TESTING ) System.out.println("  findIntersections( data1, data2 ) ...");
    	IntPair[] intersections1 = findIntersections( data1, data2 );
    	String expected1 = "[1, 8], [2, 8], [2, 9], [3, 9], [4, 10], [3, 10], [1, 11], [4, 11], [1, 12], [2, 12], [4, 12], [3, 12], [5, 12], [1, 13], [5, 13], [2, 14], [5, 14], [3, 15], [5, 15], [4, 16], [5, 16]";
    	if ( TESTING ) System.out.println("\tExpected:\t" + expected1);
    	

    	String actual1 = "";
		for (int i = 0; i < intersections1.length-1; i++) actual1 += "[" + intersections1[i].i + ", " + intersections1[i].j + "], ";
		actual1 += "[" + intersections1[intersections1.length-1].i + ", " + intersections1[intersections1.length-1].j + "]";
		if ( TESTING ) System.out.print("\tResults:\n\t" + actual1);
		if ( TESTING ) System.out.println();

		if ( expected1.equals(actual1) ) {
			System.out.println("Test passed.");
			status1 = true;
		} else {
			System.out.println("Test failed.");
			status1 = false;
		}

		System.out.println("\nRunning test 2...");
		if ( TESTING ) System.out.println("  findIntersections( data2, data1 ) ...");
		IntPair[] intersections2 = findIntersections( data2, data1 );
		String expected2 = "[13, 1], [8, 1], [12, 1], [11, 1], [14, 2], [8, 2], [9, 2], [12, 2], [9, 3], [10, 3], [12, 3], [15, 3], [16, 4], [10, 4], [12, 4], [11, 4], [13, 5], [14, 5], [16, 5], [12, 5], [15, 5]";
		if ( TESTING ) System.out.println("\tExpected:\n\t" + expected2);
		
		String actual2 = "";
		for (int i = 0; i < intersections2.length-1; i++) actual2 += "[" + intersections2[i].i + ", " + intersections2[i].j + "], ";
		actual2 += "[" + intersections2[intersections2.length-1].i + ", " + intersections2[intersections2.length-1].j + "]";
		if ( TESTING ) System.out.print("\tResults:\n\t" + actual2);
		if ( TESTING ) System.out.println();

		if ( expected2.equals(actual2) ) {
			System.out.println("Test passed.");
			status2 = true;
		} else {
			System.out.println("Test failed.");
			status2 = false;
		}

    	if (status1 && status2) 
    		return true;
    	return false;
    }


    // Test two datasets provided in all possible orders
    public boolean test2() {
    	IntRectangle[] data1 = new IntRectangle[3];
    	IntRectangle[] data2 = new IntRectangle[3];

    	data1[0] = new IntRectangle(1, 18, 8, 12);
    	data1[1] = new IntRectangle(6, 14, 19, 1);
    	data1[2] = new IntRectangle(1, 3, 2, 2);

    	data2[0] = new IntRectangle(11, 14, 14, 12);
    	data2[1] = new IntRectangle(6, 8, 9, 6);
    	data2[2] = new IntRectangle(3, 16, 4, 12);

    	boolean status1 = false;
    	boolean status2 = false;

    	System.out.println("Running test 3...");
    	if ( TESTING ) System.out.println("  findIntersections( data1, data2 ) ...");
    	IntPair[] intersections1 = findIntersections( data1, data2 );
    	String expected1 = "[18, 20], [18, 21], [17, 22]";
    	if ( TESTING ) System.out.println("\tExpected:\t" + expected1);
    	

    	String actual1 = "";
		for (int i = 0; i < intersections1.length-1; i++) actual1 += "[" + intersections1[i].i + ", " + intersections1[i].j + "], ";
		actual1 += "[" + intersections1[intersections1.length-1].i + ", " + intersections1[intersections1.length-1].j + "]";
		if ( TESTING ) System.out.print("\tResults:\n\t" + actual1);
		if ( TESTING ) System.out.println();

		if ( expected1.equals(actual1) ) {
			System.out.println("Test passed.");
			status1 = true;
		} else {
			System.out.println("Test failed.");
			status1 = false;
		}



		System.out.println("\nRunning test 4...");
		if ( TESTING ) System.out.println("  findIntersections( data2, data1 ) ...");
		IntPair[] intersections2 = findIntersections( data2, data1 );
		String expected2 = "[22, 17], [21, 18], [20, 18]";
		if ( TESTING ) System.out.println("\tExpected:\n\t" + expected2);
		
		String actual2 = "";
		for (int i = 0; i < intersections2.length-1; i++) actual2 += "[" + intersections2[i].i + ", " + intersections2[i].j + "], ";
		actual2 += "[" + intersections2[intersections2.length-1].i + ", " + intersections2[intersections2.length-1].j + "]";
		if ( TESTING ) System.out.print("\tResults:\n\t" + actual2);
		if ( TESTING ) System.out.println();

		if ( expected2.equals(actual2) ) {
			System.out.println("Test passed.");
			status2 = true;
		} else {
			System.out.println("Test failed.");
			status2 = false;
		}

    	if (status1 && status2) 
    		return true;
    	return false;
    }
}
