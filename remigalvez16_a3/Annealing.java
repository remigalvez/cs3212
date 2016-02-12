import java.util.*;

import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;

public class Annealing implements MTSPAlgorithm {

    int algId;
    PropertyExtractor prop;

    double T = 100.0;
    int M;

    public int[][] computeTours(int m, Pointd[] points) {
        M = m;
        Naive naive = new Naive();
        int[][] assignedPoints = naive.computeTours(m, points);
        double tourCost = computeTotalCost(assignedPoints, points);
        while (T > 1.0) {
            int[][] newAssignedPoints = swapTour(assignedPoints, points);
           
            double tempCost = computeTotalCost(newAssignedPoints, points);
            
            if(tempCost < tourCost) {
                tourCost = tempCost;
                assignedPoints = newAssignedPoints;
            } else {
                boolean toss = coinToss(assignedPoints, newAssignedPoints, points);
                if (toss) {
                    tourCost = tempCost;
                    assignedPoints = newAssignedPoints;
                }
            }
            
            T = T - 1.0;
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
    public int getIndex(Pointd p, Pointd[] points) {
        int count = 0;
        for (int i = 0; i < points.length; i++) {
            if (p.equals(points[i])) {
                return count;
            }
            count++;
        }
        return -1;    
    }

    // Performs even coin toss
    public boolean coinToss(int[][] a, int[][] b, Pointd[] points) {
        double p = Math.exp((-computeTotalCost(b, points) - computeTotalCost(a, points))/T);
        double u = Math.random();
        
        if (u < p)
            return true;
        return false;
    }

    public int[][] swapTour(int[][] assignedPoints, Pointd[] points){
        int[][] duplicatePtsMatrix = assignedPoints;
        
        double rA = Math.random() * points.length;
        double rB = Math.random() * points.length;
        int aa = (int) rA;
        int bb = (int) rB;
        Pointd a = points[aa];
        Pointd b = points[bb];
        
        for (int i = 0; i < duplicatePtsMatrix.length; i++) {
            for (int j = 0; j < duplicatePtsMatrix[i].length; j++){
                if (assignedPoints[i][j] == getIndex(a,points)) {
                    duplicatePtsMatrix[i][j] = getIndex(b,points);
                }
                else if (assignedPoints[i][j] == getIndex(b,points)) {
                    duplicatePtsMatrix[i][j] = getIndex(a,points);
                }
            }
        }
        return duplicatePtsMatrix;
    }

    // Computes average distance between points
    public double computeAvgDistance(Pointd p, Pointd[] points) {
        double avg = 0.0;
        for (int i = 0; i < points.length; i++) {
            avg = avg + computeDistance(p, points[i]);
        }
        avg = avg / points.length;
        return avg;
    }
    
    // Sums total cost of tours
    public double computeTotalCost(int[][] pointMatrix, Pointd[] points) {
        int[] costs = new int[pointMatrix.length];
        for (int i = 0; i < costs.length; i++) {
            costs[i] = 0;
        }
        // Compute distances for each edge
        for (int i = 0; i < pointMatrix.length; i++) {
            for (int j = 1; j < pointMatrix[i].length; j++) {
                costs[i] += computeDistance(points[pointMatrix[i][j-1]],points[pointMatrix[i][j]]);
            }
        }
        // Sum results
        double total = 0.0;
        for (int i = 0; i < costs.length; i++) {
            total += costs[i];
        }
        return total;
    }
    
    public String getName(){
        return "remigalvez16 - Annealing";
    }
    
    public void setPropertyExtractor(int algId, PropertyExtractor prop){
        this.algId=algId;
        this.prop=prop;
    }
}