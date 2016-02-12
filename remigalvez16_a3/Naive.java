import java.util.*;

import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;


public class Naive implements MTSPAlgorithm {

    int algId;
    PropertyExtractor prop;
    
    public int[][] computeTours(int m, Pointd[] points) {
        // Instantiate array to store results
        int[][] assignedPoints = new int[m][];
        // Compute average number of points per saleman
        int averageNum = points.length / m;
        // Instantiate counter
        int count = 0;
        // Instantiate size for each salesman
        for (int i = 0; i < m-1; i++) {
            assignedPoints[i] = new int[averageNum];
            count += averageNum;
        }
        // Last salesman might have remainder elements
        // Instantiate differently
        assignedPoints[m-1] = new int[points.length - count];
        // Reset counter
        count = 0;
        for(int i = 0; i < m; i++){
            for(int j = 0; j < assignedPoints[i].length; j++){
                assignedPoints[i][j] = count;
                count++;
            }
        }
        // Return results
        return assignedPoints;
    
    }
    
    public String getName(){
        return "remigalvez16 - Naive";
    }
    
    public void setPropertyExtractor(int algId, PropertyExtractor prop){
        this.algId=algId;
        this.prop=prop;
    }
    
}