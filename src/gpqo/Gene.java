package gpqo;

import java.util.*;

public class Gene {

	public Join parent;

	
	public ArrayList<Gene> leavesOf(){
		ArrayList<Gene> leaves = new ArrayList<Gene>();
		
		if(this instanceof Relation){
			leaves.add(this);
			return leaves;
		}
		if(this instanceof Join){
			Join thisJoin = (Join)this;
		
			leaves.addAll(thisJoin.inner.leavesOf());
			leaves.addAll(thisJoin.outer.leavesOf());
		}
		
		return leaves;
	}
	
}
