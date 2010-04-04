package gpqo;

import java.util.ArrayList;

public class Join extends Gene {
	public int joinId;
	public char joinType;
	
	public Gene inner; //Left child
	public Gene outer; //Right child
	
	public Relation relation1;
	public Relation relation2;
	
	
	public ArrayList<Join> pOrder(){
		
		ArrayList<Join> returnList = new ArrayList<Join>();
		
		if(!(inner == null) && (inner instanceof Join))
			returnList.addAll(((Join)inner).pOrder());
		
		if(!(outer == null) && (outer instanceof Join))
			returnList.addAll(((Join)outer).pOrder());
		
		returnList.add(this);
		return returnList;
	}
	
}
