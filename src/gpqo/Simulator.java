package gpqo;

import java.util.*;

public class Simulator {

	static void crossover(Individual T1, Gene S1, Individual T2, Gene S2,
			Individual return1, Individual return2){
		
	}
	
	static void mutation(Individual T){
		
	}

	public static void main(String[] args) {

		/*
		Join g1 = new Join();
		Join g2 = new Join();
		Join g3 = new Join();
		Join g4 = new Join();
		
		g1.joinId = 1;
		g2.joinId = 2;
		g3.joinId = 3;
		g4.joinId = 4;
		
		g1.inner = g2;
		g1.outer = g4;
		g2.outer = g3;
		
		Relation r1 = new Relation();
		Relation r2 = new Relation();
		Relation r3 = new Relation();
		Relation r4 = new Relation();
		Relation r5 = new Relation();
		
		r1.relationId = 1;
		r2.relationId = 2;
		r3.relationId = 3;
		r4.relationId = 4;
		r5.relationId = 5;
		
		g2.inner = r1;
		g3.inner = r2;
		g3.outer = r3;
		g4.inner = r4;
		g4.outer = r5;
		
		g1.leftRelation = r2;
		g1.rightRelation = r4;
		g2.leftRelation = r1;
		g2.rightRelation = r3;
		g3.leftRelation = r2;
		g3.rightRelation = r3;
		g4.leftRelation = r4;
		g4.rightRelation = r5;
		
		
		Individual ind = new Individual();
		
		ind.root = g1;
		
		ArrayList<Gene> leavesOfind = ind.leavesOf();
		for(int n = 0; n < leavesOfind.size(); n++){
			System.out.println("relation" + ((Relation)leavesOfind.get(n)).relationId);
		}
		
		ArrayList<Join> nodes = ind.postorder();
		for (Join node : nodes)
			System.out.println(node.joinId);
		
		Individual newInd = null;
		
		try {
			newInd = ind.gamma(nodes, leavesOfind);
		} catch (Exception e) {
			System.err.println(e.toString());
		}
		
		System.out.println("here we are");
		
		
		ArrayList<Gene> leaves2 = newInd.leavesOf();
		for(int n = 0; n < leaves2.size(); n++){
			System.out.println("relation" + ((Relation)leaves2.get(n)).relationId);
		}
		
		ArrayList<Join> nodes2 = newInd.postorder();
		for (Join node : nodes2)
			System.out.println("new nodes" + node.joinId);
			*/
		
		
		
		//Testing joinGraph
		Individual ind = new Individual();
		
		ArrayList<JoinGraphNode> joinGraph = new ArrayList<JoinGraphNode>();
		
		JoinGraphNode n1 = new JoinGraphNode(15, 13, 14); 
		JoinGraphNode n2 = new JoinGraphNode(17, 14, 16);
		JoinGraphNode n3 = new JoinGraphNode(16, 14, 15);
		
		joinGraph.add(n1);
		joinGraph.add(n2);
		joinGraph.add(n3);
		
		ind.randomize(joinGraph);
		
		
	}

}
