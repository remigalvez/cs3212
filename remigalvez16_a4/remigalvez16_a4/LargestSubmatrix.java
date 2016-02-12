import java.util.*;

public class LargestSubmatrix {

    private int leftX;
    private int topY;
    private int rightX;
    private int bottomY;
    private int maxLeftX;
    private int maxTopY;
    private int maxRightX;
    private int maxBottomY;
    private int maxArea;
    private int recursionArea;
    private int maxWidth;

    public LargestSubmatrix() {}

    public Square findBiggestSubmatrix(int[][] map) {
    	maxLeftX = 0;
    	maxTopY = 0;
    	maxRightX = 0;
    	maxBottomY = 0;
    	rightX = Integer.MAX_VALUE;
    	bottomY = Integer.MAX_VALUE;
    	maxArea = 0;

    	for (int y = 0; y < map.length; y++) {
    		for (int x = 0; x < map[0].length; x++) {
    			if (map[y][x] != 0) {
    				continue;
    			}
    			maxWidth = Integer.MAX_VALUE;
    			recursionArea = 0;
    			leftX = x;
    			topY = y;
    			findBiggestSubmatrixRecursive(map, x, y);
    		}
    	}
    	return new Square(maxLeftX, maxTopY, maxRightX, maxBottomY);
	}

	private void findBiggestSubmatrixRecursive(int[][] map, int x, int y) {
		// Base case for recursion
		if (map[y][x] != 0 || y >= map.length || x >= map[0].length)
			return;
		// Iterate through row until end of submatrix
		int xIter = x;
		while (map[y][xIter] == 0 && xIter < maxWidth) { xIter++; }
		if (xIter < maxWidth)
			maxWidth = xIter;
		// Compute square area and update class variables if necessary
		int area = computeArea(leftX, topY, xIter, y+1);
		if (area > maxArea) {
			maxArea = area;
			maxLeftX = leftX;
			maxTopY = topY;
			maxRightX = xIter;
			maxBottomY = (y+1);
		}
		// Recursive call to next row
		findBiggestSubmatrixRecursive(map, leftX, y+1);
		return;
	}

	private int computeArea(int lX, int tY, int rX, int bY) {
		return Math.abs(rX - lX) * Math.abs(bY - tY);
	}

	public static void main(String[] args) {
		// Write test
		int[][] map = new int[][] {
			{-1, -1, -1, -1, -1, -1},
			{-1,  0,  0,  0,  0, -1},
			{-1, -1, -1, -1,  0, -1},
			{-1,  0,  0,  0,  0, -1},
			{-1,  0,  0,  0,  0, -1},
			{-1,  0,  0, -1,  0, -1},
			{-1,  0,  0, -1,  0, -1},
			{-1,  0,  0, -1,  0, -1},
			{-1,  0,  0, -1,  0, -1},
			{-1, -1, -1, -1, -1, -1}
		};

		System.out.println();
		System.out.println();

		LargestSubmatrix sm = new LargestSubmatrix();
		Square s = sm.findBiggestSubmatrix(map);
		System.out.println(s.toString());

		for (int y = s.getMinY(); y < s.getMaxY(); y++) {
			for (int x = s.getMinX(); x < s.getMaxX(); x++) {
				map[y][x] = 1;
			}
		}

		s = sm.findBiggestSubmatrix(map);
		System.out.println(s.toString());

		for (int y = s.getMinY(); y < s.getMaxY(); y++) {
			for (int x = s.getMinX()+1; x < s.getMaxX(); x++) {
				map[y][x] = 1;
			}
		}


		s = sm.findBiggestSubmatrix(map);
		System.out.println(s.toString());

		for (int y = s.getMinY(); y < s.getMaxY(); y++) {
			for (int x = s.getMinX(); x < s.getMaxX(); x++) {
				map[y][x] = 1;
			}
		}

		System.out.println("Array:");
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				System.out.print(map[i][j] + "\t");
			}
			System.out.println();
		}
		
	}
}
    