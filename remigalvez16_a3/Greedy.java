import java.util.*;

import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;

public class Greedy implements MTSPAlgorithm {
    
    int algId;
    PropertyExtractor prop;

    public int[][] computeTours(int m, Pointd[] points) {
        // Instantiate array to store results
        int[][] assignedPoints = new int[m][];
        // Compute distance between two first elements
        double minDistance = computeDistance(points[0], points[1]);
        int totalTourLength = 0;
        // Compute average number of points per saleman
        int averageNum = points.length / m;
        
        // Instantiate array sizes
        int localCounter = 0;
        for (int i = 0; i < m-1; i++){
            assignedPoints[i] = new int[averageNum];
            localCounter += averageNum;
        }
        assignedPoints[m-1] = new int[points.length-localCounter];
        
        // Create linked list of points for convenience
        LinkedList<Pointd> pointList = new LinkedList<Pointd>();
        for(int i=0;i<points.length;i++){
            pointList.add(points[i]);
        }
       
        // Set current point to first point
        Pointd currentPoint = points[0];
        
        for (int i = 0; i < m-1; i++){
            int tourLength = 0;
            while (tourLength < assignedPoints[i].length) {
                if (currentPoint == points[0]) {
                    assignedPoints[i][tourLength] = 0;
                    tourLength++;
                }
                // Remove point from list
                int index = getListIndex(currentPoint, pointList);
                pointList.remove(index);
                // Compute nearest neighbor
                Pointd nearestNeighbor = nearestNeighbor(currentPoint, pointList);

                assignedPoints[i][tourLength] = getIndex(nearestNeighbor,points);
                tourLength++;
                totalTourLength++;
                currentPoint = nearestNeighbor;
            }
            
        }
       
        // Last salesman might have remainder elements
        // Repeat for last row
        int tourLengthLast = 0;
        int tourLength = 0;
       
        while (tourLengthLast < assignedPoints[m-1].length && pointList.size() > 1) {
           // Remove point from list
            int index = getListIndex(currentPoint, pointList);
            pointList.remove(index);
            // Compute nearest neighbor
            Pointd nearestNeighbor = nearestNeighbor(currentPoint, pointList);

            assignedPoints[m-1][tourLength] = getIndex(nearestNeighbor, points);
            tourLength++;
            totalTourLength++;
            currentPoint = nearestNeighbor;
        }
        return assignedPoints;
    }

    // Compute euclidean distance between points a and b
    public double computeDistance(Pointd a, Pointd b){
        double dX = a.x - b.x;
        double dY = a.y - b.y;
        double dist = Math.sqrt(dX*dX + dY*dY);
        return dist;
    }

    // Get index of point p in the array
    public int getIndex(Pointd p, Pointd[] points){
        int count=0;
        for(int i=0; i < points.length; i++){
            if (p.equals(points[i]))
                return count;
            count++;
        }
        return -1;
    }
    
    // Gets index of point p in the list
    public int getListIndex(Pointd pt, LinkedList<Pointd> list) {
        LinkedList<Pointd> duplicateList = new LinkedList<>();
        for(Pointd p : list) {
            duplicateList.add(p);
        }
        int count = 0;
        Pointd inquiry = duplicateList.peek();

        while (duplicateList.size() > 0) {
            inquiry = duplicateList.peek();
            if (pt.y == inquiry.y && pt.x == inquiry.x) {
                return count;
            }
            inquiry = duplicateList.poll();
            count++;
        }
        return -1;
    }
    
    // Finds nearest neighbor to point argument
    public Pointd nearestNeighbor(Pointd pt, LinkedList<Pointd> list) {
        Pointd nearestNeighbor = null;
        double minCost = Double.MAX_VALUE;
        LinkedList<Pointd> duplicateList = new LinkedList<>();
        for (Pointd y : list) {
            duplicateList.add(y);
        }
        int count = 0;
        while (duplicateList.size() != 0) {
            Pointd query = duplicateList.poll();
            if (computeDistance(pt, query) < minCost) {
                nearestNeighbor = query;
                minCost = computeDistance(pt, query);
            }
        }
        return nearestNeighbor;
    }

    public String getName(){
        return "remigalvez16 - Greedy";
    }
    
    public void setPropertyExtractor(int algId, PropertyExtractor prop){
        this.algId=algId;
        this.prop=prop;
    }
    


}