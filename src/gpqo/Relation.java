package gpqo;

public class Relation extends Gene {
	//These attributes are used if this gene is a relation
	public int relationId;
	public int size;
	
	public static int defaultSize = 20;
	public static int defaultNumTuples = 20;
	
	public int clustIndAttrib = -1;
	public int clustIndCard = -1;
	
	public Relation(Relation relation){
		this.relationId = relation.relationId;
		this.size = relation.size;
		this.clustIndAttrib = relation.clustIndAttrib;
		this.clustIndCard = relation.clustIndCard;
	}

	public Relation(int relationId){
		this(relationId, defaultSize, -1, -1);
	}
	
	public Relation(int relationId, int size, int clustIndAttrib, int clustIndCard){
		this.relationId = relationId;
		this.size = size;
		this.clustIndAttrib = clustIndAttrib;
		this.clustIndCard = clustIndCard;
	}
	
	public String toString(){
		return "R" + relationId;
	}
	
}
