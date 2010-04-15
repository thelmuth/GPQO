package gpqo;

public class JoinGraphNode {
	public int joinInd;
	public int innerRelInd;
	public int outerRelInd;
	public int innerSize;
	public int outerSize;
	
	public JoinGraphNode(int joinInd, int innerRelInd, int outerRelInd,
			int innerSize, int outerSize){
		this.joinInd = joinInd;
		this.innerRelInd = innerRelInd;
		this.outerRelInd = outerRelInd;
		this.innerSize = innerSize;
		this.outerSize = outerSize;
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
