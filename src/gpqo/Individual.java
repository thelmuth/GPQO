package gpqo;
import java.util.*;

public class Individual {

	Join root;
	double cost;

	public ArrayList<Join> postorder(){
		return root.pOrder();
	}
	
	public ArrayList<Gene> leavesOf(){
		return root.leavesOf();
	}
	
	public Individual gamma(){
		return null;
	}

	public Gene randomSubTree(){
		return null;
	}
	
	public Individual clone(){
		return null;
	}
}
