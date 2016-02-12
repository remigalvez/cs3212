import edu.gwu.algtest.*;
import edu.gwu.util.*;
import edu.gwu.geometry.*;
import java.util.*;
import edu.gwu.*;
// import simplenetsim.*;

public class MyAlgorithm implements MTSPAlgorithm{

	public int algId;
	PropertyExtractor prop;

	public int[][] computeTours(int m, Pointd[] points) { return null; }
       
    public String getName(){
        return "remigalvez16 - MyAlgorithm (not yet implemented)";
    }
    
    public void setPropertyExtractor(int algId, PropertyExtractor prop){
        this.algId = algId;
        this.prop = prop;
    }   
}