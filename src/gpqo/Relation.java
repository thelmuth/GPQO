package gpqo;

public class Relation extends Gene {
	//These attributes are used if this gene is a relation
	public int relationId;
	
	public Relation(int relationId){
		this.relationId = relationId;
	}
	
	public Relation(){}

}
