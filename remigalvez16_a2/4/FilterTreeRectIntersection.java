import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;

public class FilterTreeRectIntersection implements RectangleSetIntersectionAlgorithm {
	public static final boolean DEBUG = true;

	public final int MIN_X, MAX_X, MIN_Y, MAX_Y;

	public FilterTreeNode root;

	public FilterTreeRectIntersection() {
		MIN_X = 0;
		MAX_X = 100000;
		MIN_Y = 0;
		MAX_Y = 100000;
	}

	private FilterTreeRectIntersection makeFilterTree(IntRectangle[] rectSet, final int MIN_X, final int MAX_X, final int MIN_Y, final int MAX_Y) { 			// Add dimensions (max(x, y))
		root = new FilterTreeNode( MIN_X, MAX_X, MIN_Y, MAX_Y, 0 );
		for ( IntRectangle r : rectSet ) {
			addNode( r, root );
		}
		return this;
	}

	// Renamed from "makeFilterTreeRecursive" to "addNode" for sensical purposes
	public void addNode( IntRectangle r, FilterTreeNode quadrantNode ) {
		// if (DEBUG) System.out.println(MIN_X + "," + MAX_X + "\t" + MIN_Y + "," + MAX_Y + "\tAdding rectangle : " + r.topLeft + ", " + r.bottomRight);
		// Compute appropriate subquadrant as an int to place node
		int quadrant = computeQuadrant( r, quadrantNode );
		// If rectangle intersects with several quadrants
		if ( quadrant == -1 ) {
			// Add rectangle to parent node `quadrantNode'
			quadrantNode.rectList.add( r );
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
			addNode( r, quadrantNode.quadrants[quadrant] );
		}
		return;
	}

	private int computeQuadrant( IntRectangle r, FilterTreeNode node ) {
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
		if ( topLeftQuad == bottomRightQuad )
			return topLeftQuad;
		// Otherwise return -1
		return -1;

	};

	private int[] computeCoordinates( int quadrant, FilterTreeNode n ) {
		int[] xyCoor = new int[4];
		// Based on quadrant, compute appropriate coordinates for subquadrant of n
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

		return xyCoor;
	}

	public List<IntRectangle> filterTreeSearch( IntRectangle rect ) {
		// Call recursive function starting with root node
		return filterTreeSearchRecursive( rect, root );
	}

	public List<IntRectangle> filterTreeSearchRecursive( IntRectangle rect, FilterTreeNode node) {

		if ( node == null ) {
			if ( DEBUG ) System.out.println("Uh oh, seems like we've got ourselves a Null Pointer!");
			return new ArrayList<IntRectangle>();
		}

		List<IntRectangle> intersections = new ArrayList<>();

		// Iterate through list of rectangles
		Iterator<IntRectangle> n = node.rectList.iterator();
		while ( n.hasNext() ) {
			IntRectangle r = n.next();
			// Add rectangle if it intersects
			if ( intersect( r, rect ) )
				intersections.add( r );
		}

		// Compute subquadrant
		int quadrant = computeQuadrant( rect, node );
		if (quadrant == -1) {
			// Get quadrants rectangle intersects with
			List<Integer> intersectingQuads = getIntersectingQuadrants( rect, node );
			// Iterate through intersecting quadrants
			for (int i = 0; i < intersectingQuads.size(); i++) {
					List<IntRectangle> l = filterTreeSearchRecursive( rect, node.quadrants[intersectingQuads.get( i )]);
					if (l.size() > 0)
						intersections.addAll( l );
			}
		} else {
			List<IntRectangle> l = filterTreeSearchRecursive( rect, node.quadrants[quadrant]);
			if (l.size() > 0)
				intersections.addAll( l );
		}

		return intersections;
	}

	public List<Integer> getIntersectingQuadrants( IntRectangle rect, FilterTreeNode node ) {
		List<Integer> intersectingQuads = new ArrayList<>();

		IntRectangle[] quads = new IntRectangle[4];
		quads[0] = new IntRectangle(new IntPoint(node.midX, node.topY), new IntPoint(node.rightX, node.midY));
		quads[1] = new IntRectangle(new IntPoint(node.leftX, node.topY), new IntPoint(node.midX-1, node.midY));
		quads[2] = new IntRectangle(new IntPoint(node.leftX, node.midY-1), new IntPoint(node.midX-1, node.botY));
		quads[3] = new IntRectangle(new IntPoint(node.midX, node.midY-1), new IntPoint(node.rightX, node.botY));

		for (int i = 0; i < 4; i++) {
			if ( intersect( rect, quads[i] ))
				intersectingQuads.add( i );
		}
		return intersectingQuads;
	}

	public boolean intersect(IntRectangle r, IntRectangle s) {
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
		if ((right1 >= left2 && right1 <= right2) || (right2 >= left1 && right2 <= right1)) 
			xOverlap = true; 
		if ((top1 >= bottom2 && top1 <= top2) || (top2 >= bottom1 && top2 <= top1)) 
			yOverlap = true;
		if ((bottom1 >= bottom2 && bottom1 <= top2) || (bottom2 >= bottom1 && bottom2 <= top1)) 
			yOverlap = true;
		// If rectangles overlap both horizontally and vertically
		if (xOverlap && yOverlap)
			return true;
		return false;
	}

	public IntPair[] findIntersections( IntRectangle[] rectSet1, IntRectangle[] rectSet2) { 
		int numIntersections = 0;
		List<IntPair> intersections = new ArrayList<>();
		// First, place data rectangles in rectSet1 into tree.
		makeFilterTree(rectSet1, MIN_X, MAX_X, MIN_Y, MAX_Y);
		// Now scan rectangles in second set and query against tree.
		for (int i = 0; i < rectSet2.length; i++) {
			// Get list of intersections from tree.
			List<IntRectangle> intersectionSet = filterTreeSearch(rectSet2[i]);
			if (intersectionSet.size() > 0) {
				numIntersections += intersectionSet.size();
				for (int j = 0; j < intersectionSet.size(); j++) {
					intersections.add(new IntPair( rectSet2[i].ID, intersectionSet.get(i).ID ));
				}
			}
		}
		IntPair[] intersectionsArr = new IntPair[intersections.size()];
		for (int i = 0; i < intersections.size(); i++) {
			intersectionsArr[i] = intersections.get( i );
		}
		return intersectionsArr;
	}

	public String getName() {
            return "remigalvez16 - FilterTreeRectIntersection";
    }

    public void setPropertyExtractor(int algID, PropertyExtractor prop) {}

    public static void main(String[] args) {

    }

}