import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;

public class NaiveRectIntersection implements RectangleSetIntersectionAlgorithm {

	public IntPair[] findIntersections(IntRectangle[] rectSet1, IntRectangle[] rectSet2) {
		
		int top1, bottom1, left1, right1;
		int top2, bottom2, left2, right2;
		boolean xOverlap, yOverlap;

		List<IntPair> intersects = new ArrayList<>();

		// Iterate through first rectangle set
		for (IntRectangle r : rectSet1) {
			// Define horizontal and vertical delimiters
			top1 = r.topLeft.y;
			bottom1 = r.bottomRight.y;
			left1 = r.topLeft.x;
			right1 = r.bottomRight.x;
			// Iterate through second rectangle set
			for (IntRectangle s : rectSet2) {
				// Define horizontal and vertical delimiters
				top2 = s.topLeft.y;
				bottom2 = s.bottomRight.y;
				left2 = s.topLeft.x;
				right2 = s.bottomRight.x;

				// Set overlaps to false
				xOverlap = false;
				yOverlap = false;

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
				// Add to results
				if (xOverlap && yOverlap)
					intersects.add(new IntPair(r.ID, s.ID));
			}
		}

		if (intersects.size() > 0)
			return intersects.toArray(new IntPair[intersects.size()]);
		return null;
	}

	public String getName() {
		return "remigalvez16 - NaiveRectIntersection";
	}

	public void setPropertyExtractor(int algID, PropertyExtractor prop) {}

	public static void main(String[] args) {

	}

}