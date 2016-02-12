import edu.gwu.util.*;
import edu.gwu.algtest.*;
import edu.gwu.debug.*;

import java.util.*;

public class UnionFindInt {

	int[] component;

	public UnionFindInt(int numVertices) {
		component = new int[numVertices];
	}

	public void union(int a, int b) {
		int comp = min(component[a], component[b]);
	} 

	public int min(int a, int b) {
		return a < b ? a : b;
	}

}