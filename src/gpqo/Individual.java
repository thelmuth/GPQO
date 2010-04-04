package gpqo;
import java.util.*;

public class Individual {

	Join root;
	double cost;

	public ArrayList<Join> postorder(){
		return root.pOrder();
	}
	
	public ArrayList<Gene> leavesOf(){
		return root.leavesOf();
	}
	
	public Individual gamma(ArrayList<Join> nodeList, ArrayList<Gene> tSet) throws Exception{
		Individual result = new Individual();
		
		for(Join join : nodeList){
			Join newJoin = new Join(join);
			
			Relation leftRelation = newJoin.leftRelation;
			Relation rightRelation = newJoin.rightRelation;
			
			Gene A = tSetFindAndRemove(tSet, leftRelation);
			Gene B = tSetFindAndRemove(tSet, rightRelation);
			
			newJoin.inner = A;
			newJoin.outer = B;
			
			tSet.add(newJoin);
		}
		
		if(tSet.size() != 1)
			throw new Exception("tSet size is not a single tree after running gamma.");

		Join rootTree = (Join)tSet.get(0);
		result.root = rootTree;
		return result;
	}

	private Gene tSetFindAndRemove(ArrayList<Gene> tSet, Relation relationRef) {
		
		for(int i = 0; i < tSet.size(); i++){
			Gene g = tSet.get(i);
			
			if(g instanceof Relation){
				Relation bugrel = (Relation)g;
				int relid = bugrel.relationId;
				
				if(relid == relationRef.relationId){
					tSet.remove(i);
					return g;
				}
			}
			if(g instanceof Join){
				if(g.containsLeaf(relationRef)){
					tSet.remove(i);
					return g;
				}
			}
		}
		
		return null;
	}

	public Gene randomSubTree(){
		return null;
	}
	
	public Individual clone(){
		return null;
	}
}
