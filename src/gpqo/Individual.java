package gpqo;
import java.util.*;

public class Individual {

	private static final int NUM_JOINS = 18;
	
	public Join root;
	public double cost;
	//public int numJoins;
	

	public Individual(){ }
	
	public Individual(Individual individual){
		root = new Join(individual.root);
		cost = individual.cost;
	}
	
	public ArrayList<Join> postorder(){
		return root.pOrder();
	}
	
	public ArrayList<Gene> leavesOf(){
		return root.leavesOf();
	}
	

	public void setParents(){
		root.setParents(null);
	}
	

	
	public void calcCost(){
		cost = root.cost()[0];
	}
	
	public static Individual gamma(ArrayList<Join> nodeList, ArrayList<Gene> tSet) throws Exception{
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
	
	public void randomize(ArrayList<JoinGraphNode> data){
		root = createRandomIndividual(data);
	}
	
	public Join createRandomIndividual(ArrayList<JoinGraphNode> data){
		Random generator = new Random();
		int size = data.size();
		if (size == 0)
			System.out.println("Oh noes!");
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
		Random rand = new Random();
		int jId = rand.nextInt(NUM_JOINS) + 1;
		return findJoinWithId(jId);
	}
	
	//This mutation operator gives a random join a new join algorithm.
	public void mutationOperator1() {
		Join j = randomSubTree();
		j.randomNewJoinAlgorithm();
	}

	//This mutation operator swaps a join's inner and outer children.
	public void mutationOperator2() {
		Join j = randomSubTree();
		
		Gene temp = j.inner;
		j.inner = j.outer;
		j.outer = temp;
		
		Relation tempR = j.leftRelation;
		j.leftRelation = j.rightRelation;
		j.rightRelation = tempR;
	}
	
	//This join operator randomizes an entire subtree. The subtree must contain at most 2/3 of the join nodes in the entire tree, since otherwise
	//the operator would randomize too much. Also, the chosen subtree must not be a join leaf.
	public void mutationOperator3() {
		setParents();
		Join j = randomSubTree();
		
		//Find subtree that is at most 2/3 the size of the full tree that is not a join leaf node.
		while((3 * j.subtreeSize() > 2 * NUM_JOINS & root.subtreeSize() > 3) || j.isJoinLeaf()){
			j = randomSubTree();
		}
		
		ArrayList<JoinGraphNode> graphOfSubtree = j.getJoinGraph();
		
		if(j.parent.inner == j)
			j.parent.inner = createRandomIndividual(graphOfSubtree);
		else
			j.parent.outer = createRandomIndividual(graphOfSubtree);
	}

	//This mutation operator swaps a random join and its parent
	public void mutationOperator4() {
		setParents();
		
		while(true){
			//Choose a join to swap with its parent.
			//Can't choose root, since it doesn't have a parent to swap with.
			Join b = randomSubTree();
			if(b.parent != null){
				Join a = b.parent;
				
				//Find under which of b's children is one of a's relations
				//Then swap a and b
				if(b.inner.containsLeaf(a.leftRelation) || b.inner.containsLeaf(a.rightRelation)){
					//b.inner contains one of a's relations
					if(a.inner == b){
						a.inner = b.inner;
					}
					else{
						a.outer = b.inner;
					}
					b.inner = a;
				}
				else {
					//b.outer contains one of a's relations
					if(a.inner == b){
						a.inner = b.outer;
					}
					else{
						a.outer = b.outer;
					}
					b.outer = a;
				}
				
				//Set new parent of b to have b as a child
				Join p = a.parent;
				if(a.parent == null){
					//If here, a was the root, so now b is the root.
					root = b;
					b.parent = null;
				}
				else if(p.inner == a){
					p.inner = b;
				}
				else {
					p.outer = b;
				}
				
				a.parent = b;
				break;
			}
		}
	}
	

	public String toString(){
		String str = " >> root is J" + root.joinId + "\n";
		str += " >> cost = " + cost + "\n";
		str += root.getTreeString(1);
		return str;
	}

	
}
