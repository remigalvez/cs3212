import java.util.*;

// Rename
public class Square {
	private Pointd topLeft;
	private Pointd topRight;
	private Pointd bottomLeft;
	private Pointd bottomRight;

	private int maxX, minX, maxY, minY;

	private int area;

	public Square(int leftX, int topY, int rightX, int bottomY) {
		// topLeft = new Pointd(topY, leftX);
		// bottomRight = new Pointd(bottomY, rightX);

		minX = leftX;
		maxX = rightX;
		minY = topY;
		maxY = bottomY;

		bottomLeft = new Pointd(minY, minX);
		bottomRight = new Pointd(maxY, minX);
		topLeft = new Pointd(minY, maxX);
		topRight = new Pointd(maxY, maxX);

		area = Math.abs(leftX - rightX) * Math.abs(topY - bottomY);
	}

	public String toString() {
		return "Square: " + topLeft.toString() + " | " + bottomRight.toString() + " | area: " + area;
	}

	public boolean equals(Square s) {
		if (s == null) 
			return false;
		return (this.maxX == s.maxX) && (this.minX == s.minX) && (this.maxY == s.maxY) && (this.minY == s.minY);
	}

	public int getWidth() {
		return (int) Math.abs(topLeft.gety() - bottomRight.gety());
	}

	public int getHeight() {
		return (int) Math.abs(topLeft.getx() - bottomRight.getx());
	}

	public Pointd getTopLeft() {
		return topLeft;
	}

	public Pointd getTopRight() {
		return topRight;
	}

	public Pointd getBottomLeft() {
		return bottomLeft;
	}

	public Pointd getBottomRight() {
		return bottomRight;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}
}