package gpqo;

public class Relation extends Gene {
	//These attributes are used if this gene is a relation
	public int relationId;
	public int size;
	public static int defaultSize = 10000;
	
	public Relation(){
		this(-1, defaultSize);
	}

	public Relation(int relationId){
		this(relationId, defaultSize);
	}
	
	public Relation(int relationId, int size){
		this.relationId = relationId;
		this.size = size;
	}
	
	public String toString(){
		return "R" + relationId;
	}
	
}
