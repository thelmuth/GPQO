package gpqo;
import java.util.*;

public class Individual {

	public Join root;
	public double cost;
	public int numJoins;
	
	public ArrayList<Join> postorder(){
		return root.pOrder();
	}
	
	public ArrayList<Gene> leavesOf(){
		return root.leavesOf();
	}
	
	public Individual(int numJoins){
		this.numJoins = numJoins;
	}
	
	public Individual(Individual individual){
		
	}
	
	public void calcCost(){
		cost = root.cost()[0];
	}
	
	public static Individual gamma(ArrayList<Join> nodeList, ArrayList<Gene> tSet) throws Exception{
		Individual result = new Individual(nodeList.size());
		
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

	private static Gene tSetFindAndRemove(ArrayList<Gene> tSet, Relation relationRef) {
		
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

	public Individual clone(){
		return null;
	}
	
	public void randomize(ArrayList<JoinGraphNode> data){
		
		root = createRandomIndividual(data);
	}
	
	public Join createRandomIndividual(ArrayList<JoinGraphNode> data){
		Random generator = new Random();
		int randomIndex = generator.nextInt(data.size());
		
		JoinGraphNode joinGraphNode = data.get(randomIndex);
		data.remove(randomIndex);
		
		ArrayList<JoinGraphNode> joinGraphNodeListInner = getSubgraph(data, joinGraphNode.innerRelInd);
		ArrayList<JoinGraphNode> joinGraphNodeListOuter = getSubgraph(data, joinGraphNode.outerRelInd);
		
		Gene inner = null;
		if (joinGraphNodeListInner.size() == 0)
			inner = new Relation(joinGraphNode.innerRelInd);
		else
			inner = createRandomIndividual(joinGraphNodeListInner);
		
		Gene outer = null;
		if (joinGraphNodeListOuter.size() == 0)
			outer = new Relation(joinGraphNode.outerRelInd);
		else
			outer = createRandomIndividual(joinGraphNodeListOuter);

		Join join = new Join(joinGraphNode.joinInd, new Relation(joinGraphNode.innerRelInd), 
				new Relation(joinGraphNode.outerRelInd), inner, outer);
		
		inner.parent = join;
		outer.parent = join;
		
		return join;
	}
	
	public ArrayList<JoinGraphNode> getSubgraph(ArrayList<JoinGraphNode> joinGraphNodeList, int relationId){
		
		ArrayList<JoinGraphNode> returnList = new ArrayList<JoinGraphNode>();
		
		Stack<Integer> relToProcess = new Stack<Integer>();
		relToProcess.add(relationId);
		
		while(!relToProcess.empty()){
			int relInd = relToProcess.pop();
			
			ArrayList<JoinGraphNode> joinGraphNodesToRemove = new ArrayList<JoinGraphNode>();
			
			for(JoinGraphNode joinGraphNode : joinGraphNodeList){
				
				int newRelInd = joinGraphNode.returnOtherRelation(relInd);
				if (newRelInd == -1)
					continue;
				
				relToProcess.add(newRelInd);
				returnList.add(joinGraphNode);
				joinGraphNodesToRemove.add(joinGraphNode);
			}
			
			for (JoinGraphNode joinGraphNode : joinGraphNodesToRemove)
				joinGraphNodeList.remove(joinGraphNode);
		}
		
		return returnList;
	}

	//Finds a join with a particular joinID
	public Join findJoinWithId(int id) {
		return root.findJoinWithId(id);
	}
	
	public Join randomSubTree(){
		return null;
	}
	
	
	public String toString(){
		return " = The root is J" + root.joinId + "\n" + root.getTreeString();
	}
	
}
