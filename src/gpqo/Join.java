package gpqo;

import java.util.ArrayList;

public class Join extends Gene {
	public int joinId;
	public char joinType;
	
	public Gene inner; //Left child
	public Gene outer; //Right child
	
	public Relation leftRelation;
	public Relation rightRelation;
	
	public Join() {
		
	}
	
	public Join(Join join) {
		this.joinId = join.joinId;
		this.joinType = join.joinType;
		
		this.leftRelation = join.leftRelation;
		this.rightRelation = join.rightRelation;
		
		this.inner = null;
		this.outer = null;
	}



	public ArrayList<Join> pOrder(){
		
		ArrayList<Join> returnList = new ArrayList<Join>();
		
		if(!(inner == null) && (inner instanceof Join))
			returnList.addAll(((Join)inner).pOrder());
		
		if(!(outer == null) && (outer instanceof Join))
			returnList.addAll(((Join)outer).pOrder());
		
		returnList.add(this);
		return returnList;
	}
	
	public String toString(){
		return "join " + joinId;
	}
	
}
