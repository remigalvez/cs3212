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
}
