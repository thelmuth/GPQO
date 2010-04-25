package dp;

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
	
	public void setParents(Join par) {
		this.parent = par;
		if(this instanceof Join){
			((Join)this).inner.setParents((Join)this);
			((Join)this).outer.setParents((Join)this);
		}
	}
	
	public Relation joinsWith(HashMap<Integer, HashSet<Integer>> h, Relation r){
		ArrayList<Gene> leaves = leavesOf();

		for (Gene g : leaves) {
			if(h.get(r.relationId).contains(((Relation) g).relationId)){
				return (Relation) g;
			}
		}
		return null;
	}
	
	public int hashRelationIds(int largestRelationId){
		int value = 0;
		
		ArrayList<Gene> leaves = leavesOf();
		for(Gene r : leaves){
			value += Math.round(Math.pow(2, ((Relation)r).relationId));
		}
		
		if(this instanceof Join){
			if(((Join)this).joinType == 'S'){
				//Join is sorted, so hash on the attribute as well.
				int attribute = ((Join)this).joinAttribute;
				value += Math.round(Math.pow(2, largestRelationId + attribute));
			}
		}
		
		return value;
	}
	
	public String getTreeString(int depth) {
		if(this instanceof Relation)
			return "";
		
		Join thisJoin = (Join)this;
		
		boolean INLJoinInner = false, INLJoinOuter = false;
		
		if(thisJoin.inner instanceof Relation){
			INLJoinInner = ((Relation)thisJoin.inner).clustIndAttrib == thisJoin.joinAttribute;
		}
		if(thisJoin.outer instanceof Relation){
			INLJoinOuter = ((Relation)thisJoin.outer).clustIndAttrib == thisJoin.joinAttribute;
		}
		
		
		String str = "";
		for(int i = 0; i < depth; i++)
			str += "  ";
		str += " - " + this + " uses join type ";
		
		if(thisJoin.joinType == 'I'){
			if(INLJoinInner || INLJoinOuter)
				str += 'I';
			else
				str += 'B';			
		}
		else {
			str += thisJoin.joinType;
		}

		str += " and has children " + thisJoin.inner + " and " + thisJoin.outer + ".\n";
		
		return str + thisJoin.inner.getTreeString(depth + 1) + thisJoin.outer.getTreeString(depth + 1);
	}
	
	public String stringify(){
		String str = "cost = " + ((Join)this).justCost() + "\n";
		if(((Join)this).joinType == 'S'){
			str += "Sorted on " + ((Join)this).joinAttribute + " attribute\n";
		}
		else{
			str += "Not sorted\n";
		}
		
		str += getTreeString(0);
		
		return str;
	}
	
	
	abstract public String toString();
	
}
