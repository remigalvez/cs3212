import edu.gwu.algtest.*;
import edu.gwu.debug.*;
import edu.gwu.util.*;

public class Min implements MinAlgorithm {
    static final boolean debug = false;

    PropertyExtractor props;
    int algID;
    
    public String getName()
    {
	return "Min";
    }

    public void setPropertyExtractor (int algID, PropertyExtractor props)
    {
	this.algID = algID;
	this.props = props;
    }

    public int getMin(int[] data){
	int min = data[0];
	if(data.length == 1){
	    return min;
	}
	else {
	   min = data[0];
	   for(int i = 0; i < data.length; i++){
	       if(data[i] < min){
		   min = data[i];
	       }
	   }
	}
	return min;
    }
}
