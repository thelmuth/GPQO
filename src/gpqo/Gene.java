package gpqo;

import java.util.*;

public abstract class Gene {

	public Join parent;

	
	public ArrayList<Gene> leavesOf(){
		ArrayList<Gene> leaves = new ArrayList<Gene>();
		
		if(this instanceof Relation){
			leaves.add((Relation)this);
			return leaves;
		}
		if(this instanceof Join){
			Join thisJoin = (Join)this;
		
			leaves.addAll(thisJoin.inner.leavesOf());
			leaves.addAll(thisJoin.outer.leavesOf());
		}
		
		return leaves;
	}


	public boolean containsLeaf(Relation relationRef) {
		
		if(this instanceof Relation){
			if(((Relation)this).relationId == relationRef.relationId)
				return true;
			else
				return false;
		}
		else {
			Join j = (Join)this;
			if(j.inner.containsLeaf(relationRef))
				return true;
			if(j.outer.containsLeaf(relationRef))
				return true;
		}
		
		return false;
	}
	
	public String getTreeString() {
		if(this instanceof Relation)
			return "";
		
		String str = " - The children of " + this + " are " + ((Join)this).inner + " and " + ((Join)this).outer + ".\n";
		
		return str + ((Join)this).inner.getTreeString() + ((Join)this).outer.getTreeString();
	}
	
	
	abstract public String toString();
	
}
