package gpqo;

import java.util.ArrayList;
import java.util.Random;

public class Join extends Gene {
	public int joinId;
	public char joinType;
	
	public Gene inner; //Left child
	public Gene outer; //Right child
	
	public Relation leftRelation;
	public Relation rightRelation;
	
	public static String joinTypes = "SBH";
	
	public Join() {
		
	}
	
	//Copy constructor
	public Join(Join join) {
		this.joinId = join.joinId;
		this.joinType = join.joinType;
		
		this.leftRelation = join.leftRelation;
		this.rightRelation = join.rightRelation;
		
		this.inner = null;
		this.outer = null;
	}
	
	//Constructor for random join
	public Join(int joinId, Relation leftRelation, Relation rightRelation, Gene inner, Gene outer){
		this.joinId = joinId;
		
		Random generator = new Random();
		int randomIndex = generator.nextInt(joinTypes.length());
		
		this.joinType = joinTypes.charAt(randomIndex);
		
		this.leftRelation = leftRelation;
		this.rightRelation = rightRelation;
		
		this.inner = inner;
		this.outer = outer;
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
