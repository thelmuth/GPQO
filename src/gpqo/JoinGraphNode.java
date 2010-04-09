package gpqo;

public class JoinGraphNode {
	public int joinInd;
	public int innerRelInd;
	public int outerRelInd;
	
	public JoinGraphNode(int joinInd, int innerRelInd, int outerRelInd){
		this.joinInd = joinInd;
		this.innerRelInd = innerRelInd;
		this.outerRelInd = outerRelInd;
	}
	
	public boolean containsRel(int relIndex){
		return (innerRelInd == relIndex) || (outerRelInd == relIndex);
	}
	
	public int returnOtherRelation(int relIndex){
		if (relIndex == innerRelInd)
			return outerRelInd;
		
		if (relIndex == outerRelInd)
			return innerRelInd;
		
		return -1;
	}
}
