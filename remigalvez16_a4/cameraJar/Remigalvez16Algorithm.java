import java.util.*;

public class Remigalvez16Algorithm implements CameraPlacementAlgorithm {

    // Algorithm should solve and return list of Camera instances
    // as camera locations and orientations.

    public ArrayList<Camera> solve (CameraPlacementProblem problem) 
    {
    	ArrayList<Camera> cameraList = new ArrayList<>();
    	ArrayList<Wall> walls = problem.getInteriorWalls();

        Camera camera;
        CameraPlacementResult result = null;
        result = CameraPlacement.evaluatePlacement (problem, cameraList);
        LargestSubmatrix m = new LargestSubmatrix();
        Square s, ss = null;
        while (result.numNotCovered > 0) {
            // Get biggest submatrix of 0s
            s = m.findBiggestSubmatrix(result.cover);
            if (s.equals(ss)) {
                System.err.println("ERROR: Camera misplaced or misoriented.");
                break;
            }
            ss = s;
            int topAngle;
            int bottomAngle;
            // Initialize default angles -- aims along longer side of the rectangle
            if (s.getWidth() > s.getHeight()) {
                topAngle = 300;
                bottomAngle = 120;
            } else {
                topAngle = 330;
                bottomAngle = 150;
            }
            Pointd topLeft = s.getTopLeft();
            Pointd bottomRight = s.getBottomRight();
            // Check if corners are valid camera positions
            switch (isValidPosition(topLeft, result.cover) ? 1 : 0) {
                case 1:
                    camera = new Camera(topLeft, topAngle, true);
                    cameraList.add(camera);
                    break;
                case 0:
                    camera = relocateCameraToCorner(topLeft, result.cover, false);
                    cameraList.add(camera);
                    break;
            }

            switch (isValidPosition(bottomRight, result.cover) ? 1 : 0) {
                case 1:
                    camera = new Camera(bottomRight, bottomAngle, true);
                    cameraList.add(camera);
                    break;
                case 0:
                    camera = relocateCameraToCorner(bottomRight, result.cover, false);
                    cameraList.add(camera);
                    break;
            }
            // Evaluate camera placement
            result = CameraPlacement.evaluatePlacement(problem, cameraList);
        }

    	return cameraList;
    }

    private boolean isValidPosition(Pointd p, int[][] map) {
        int x = (int) p.getx();
        int y = (int) p.gety();

        if (map[x][y] == -1 || map[x-1][y] == -1 || map[x][y-1] == -1 || map[x-1][y-1] == -1) {
            return true;
        } else {
            return false;
        }
    }

    private Camera relocateCameraToCorner(Pointd p, int[][] map, boolean secondAttempt) {
        boolean top, left;
        int x = (int) p.x;
        int y = (int) p.y;
        int dx = 1;
        // Find nearest corner & replace camera
        while (true) {
            if (x-dx >= 0 && map[x-dx][y] == -1) { left = true; p.x = x-dx+1; dx--; break; } 
            else if (x+dx < map.length && map[x+dx][y] == -1) { left = false; p.x = x+dx; break; }
            dx++;
        }
        int dy = 1;
        while (true) {
            if (y-dy >= 0 && map[x][y-dy] == -1) { top = false; p.y = y-dy+1; dy++; break; } 
            else if (y+dy < map[0].length && map[x][y+dy] == -1) { top = true; p.y = y+dy; break; }
            dy++;
        }

        double angle = Math.atan((double) dy/(double) dx);
        angle = Math.toDegrees(angle);
        int intAngle = (int) angle;

        if (!top && !left) intAngle = 180 - intAngle;
        else if (top && !left) intAngle = 180 + intAngle;
        else if (top && left) intAngle = 360 - intAngle;
        return new Camera(p, intAngle, true);
    }

    public class Square {
    private Pointd topLeft;
    private Pointd topRight;
    private Pointd bottomLeft;
    private Pointd bottomRight;

    private int maxX, minX, maxY, minY;

    private int area;

    public Square(int leftX, int topY, int rightX, int bottomY) {
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
    }  

}

