package gpqo;

public class JoinGraphNode {
	public int joinInd;
	
	public int innerRelInd;
	public int outerRelInd;
	
	public int innerSize;
	public int outerSize;
	
	public int clustIndAttribInner;
	public int clustIndAttribOuter;
	public int joinAttrib;
	
	public int clustIndCardInner;
	public int clustIndCardOuter;
	
	public JoinGraphNode(int joinInd, int innerRelInd, int outerRelInd,
			int innerSize, int outerSize, int clustIndAttribInner, int clustIndAttribOuter,
			int joinAttrib, int clustIndCardInner, int clustIndCardOuter){
		
		this.joinInd = joinInd;
		
		this.innerRelInd = innerRelInd;
		this.outerRelInd = outerRelInd;
		
		this.innerSize = innerSize;
		this.outerSize = outerSize;
		
		this.clustIndAttribInner = clustIndAttribInner;
		this.clustIndAttribOuter = clustIndAttribOuter;
		this.joinAttrib = joinAttrib;
		
		this.clustIndCardInner = clustIndCardInner;
		this.clustIndCardOuter = clustIndCardOuter;
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
