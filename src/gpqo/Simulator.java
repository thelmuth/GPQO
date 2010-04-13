package gpqo;

import java.util.*;

public class Simulator {

	private static Individual crossover(Individual T1, Join S2) throws Exception{
		//Create first individual
		ArrayList<Join> nodelist1 = createNodeList(T1.postorder(), S2.pOrder());
		ArrayList<Gene> tSet1 = createTSet(T1.leavesOf(), S2.leavesOf());
		tSet1.add(S2);
		Individual return1 = Individual.gamma(nodelist1, tSet1);

		return1.calcCost();
		return return1;
	}

	private static Individual mutation(Individual T){
		int numMutations = 2;
		
		//NOTE: if we have a mutation operator that swaps the children of a join, will it also have to swap that join's leftRelation and rightRelation???
		Individual return1 = new Individual(T);
		
		Random rand = new Random();
		int chooseMutation = rand.nextInt(numMutations);
		switch(chooseMutation){
		case 0:
			mutationOperator1(return1);
			break;
		case 1:
			
			break;
		default:
			break;
		}
		
		return1.calCost();
		return return1;
	}
	
	//This mutation operator 
	private static void mutationOperator1(Individual T) {
		Join j = T.randomSubTree();
		j.randomNewJoinAlgorithm();
	}

	private static ArrayList<Join> createNodeList(ArrayList<Join> postOrderT, ArrayList<Join> postOrderS){
		ArrayList<Join> nodelist = new ArrayList<Join>();
		boolean inS = false;
		
		for(Join j : postOrderT){
			inS = false;
			
			for(Join k : postOrderS){
				if(j.joinId == k.joinId){
					inS = true;
					break;
				}
			}
			if(!inS){
				nodelist.add(j);
			}
		}
		return nodelist;
	}
	
	private static ArrayList<Gene> createTSet(ArrayList<Gene> leavesOfT, ArrayList<Gene> leavesOfS) {
		ArrayList<Gene> TSet = new ArrayList<Gene>();
		boolean inS = false;
		
		for(Gene j : leavesOfT){
			inS = false;
			
			for(Gene k : leavesOfS){
				if(((Relation)j).relationId == ((Relation)k).relationId){
					inS = true;
					break;
				}
			}
			if(!inS){
				TSet.add(j);
			}
		}
		return TSet;
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
		/*Individual ind = new Individual();
		
		ArrayList<JoinGraphNode> joinGraph = new ArrayList<JoinGraphNode>();
		
		JoinGraphNode n1 = new JoinGraphNode(15, 13, 14); 
		JoinGraphNode n2 = new JoinGraphNode(17, 14, 16);
		JoinGraphNode n3 = new JoinGraphNode(16, 14, 15);
		
		joinGraph.add(n1);
		joinGraph.add(n2);
		joinGraph.add(n3);
		
		ind.randomize(joinGraph);
		*/
		
		
		Individual indiv1 = sampleIndividual1();
		Individual indiv2 = sampleIndividual2();
		
		//Try crossover here.
		Join subtree1 = indiv1.findJoinWithId(16);
		Join subtree2 = indiv2.findJoinWithId(18);
		
		Individual newIndiv1 = new Individual(indiv1.numJoins);
		Individual newIndiv2 = new Individual(indiv1.numJoins);
		
		
		

		System.out.println("Printing indiv1");
		System.out.println(indiv1);
		
		//System.out.println("Printing indiv2");
		//System.out.println(indiv2);
		
		try {
			newIndiv1 = crossover(indiv1, subtree2);
			newIndiv2 = crossover(indiv2, subtree1);
		} catch (Exception e) {
			System.err.println("Error during crossover: " + e);
		}
		

		System.out.println("\nPrinting newIndiv1");
		System.out.println(newIndiv1);

		//System.out.println("\nPrinting newIndiv2");
		//System.out.println(newIndiv2);
		
	}
	

	private static Individual sampleIndividual1() {
		Relation rel1Indiv1 = new Relation(1);
		Relation rel2Indiv1 = new Relation(2);
		Relation rel3Indiv1 = new Relation(3);
		Relation rel4Indiv1 = new Relation(4);
		Relation rel5Indiv1 = new Relation(5);
		Relation rel6Indiv1 = new Relation(6);
		Relation rel7Indiv1 = new Relation(7);
		Relation rel8Indiv1 = new Relation(8);
		Relation rel9Indiv1 = new Relation(9);
		Relation rel10Indiv1 = new Relation(10);
		Relation rel11Indiv1 = new Relation(11);
		Relation rel12Indiv1 = new Relation(12);
		Relation rel13Indiv1 = new Relation(13);
		Relation rel14Indiv1 = new Relation(14);
		Relation rel15Indiv1 = new Relation(15);
		Relation rel16Indiv1 = new Relation(16);
		Relation rel17Indiv1 = new Relation(17);
		Relation rel18Indiv1 = new Relation(18);
		Relation rel19Indiv1 = new Relation(19);
		
		Join join15Indiv1 = new Join(15, rel13Indiv1, rel14Indiv1, rel13Indiv1, rel14Indiv1);
		Join join16Indiv1 = new Join(16, rel14Indiv1, rel15Indiv1, join15Indiv1, rel15Indiv1);
		Join join17Indiv1 = new Join(17, rel14Indiv1, rel16Indiv1, join16Indiv1, rel16Indiv1);
		Join join4Indiv1 = new Join(4, rel4Indiv1, rel5Indiv1, rel4Indiv1, rel5Indiv1);
		Join join3Indiv1 = new Join(3, rel3Indiv1, rel4Indiv1, rel3Indiv1, join4Indiv1);
		Join join5Indiv1 = new Join(5, rel5Indiv1, rel6Indiv1, join3Indiv1, rel6Indiv1);
		Join join2Indiv1 = new Join(2, rel2Indiv1, rel3Indiv1, rel2Indiv1, join5Indiv1);
		Join join6Indiv1 = new Join(6, rel6Indiv1, rel7Indiv1, join2Indiv1, rel7Indiv1);
		Join join1Indiv1 = new Join(1, rel1Indiv1, rel5Indiv1, rel1Indiv1, join6Indiv1);
		Join join8Indiv1 = new Join(8, rel8Indiv1, rel9Indiv1, rel8Indiv1, rel9Indiv1);
		Join join11Indiv1 = new Join(11, rel10Indiv1, rel19Indiv1, rel10Indiv1, rel19Indiv1);
		Join join9Indiv1 = new Join(9, rel9Indiv1, rel10Indiv1, join8Indiv1, join11Indiv1);
		Join join12Indiv1 = new Join(12, rel8Indiv1, rel11Indiv1, join9Indiv1, rel11Indiv1);
		Join join7Indiv1 = new Join(7, rel7Indiv1, rel8Indiv1, join1Indiv1, join12Indiv1);
		Join join10Indiv1 = new Join(10, rel10Indiv1, rel18Indiv1, join7Indiv1, rel18Indiv1);
		Join join18Indiv1 = new Join(18, rel12Indiv1, rel17Indiv1, rel12Indiv1, rel17Indiv1);
		Join join13Indiv1 = new Join(13, rel11Indiv1, rel12Indiv1, join10Indiv1, join18Indiv1);
		Join join14Indiv1 = new Join(14, rel13Indiv1, rel12Indiv1, join17Indiv1, join13Indiv1);
		
		int numJoins = 18;
		Individual indiv1 = new Individual(numJoins);
		indiv1.root = join14Indiv1;
		indiv1.calcCost();
		
		return indiv1;
	}
	
	private static Individual sampleIndividual2() {
		Relation rel1Indiv2 = new Relation(1);
		Relation rel2Indiv2 = new Relation(2);
		Relation rel3Indiv2 = new Relation(3);
		Relation rel4Indiv2 = new Relation(4);
		Relation rel5Indiv2 = new Relation(5);
		Relation rel6Indiv2 = new Relation(6);
		Relation rel7Indiv2 = new Relation(7);
		Relation rel8Indiv2 = new Relation(8);
		Relation rel9Indiv2 = new Relation(9);
		Relation rel10Indiv2 = new Relation(10);
		Relation rel11Indiv2 = new Relation(11);
		Relation rel12Indiv2 = new Relation(12);
		Relation rel13Indiv2 = new Relation(13);
		Relation rel14Indiv2 = new Relation(14);
		Relation rel15Indiv2 = new Relation(15);
		Relation rel16Indiv2 = new Relation(16);
		Relation rel17Indiv2 = new Relation(17);
		Relation rel18Indiv2 = new Relation(18);
		Relation rel19Indiv2 = new Relation(19);
		
		Join join1Indiv2 = new Join(1, rel1Indiv2, rel5Indiv2, rel1Indiv2, rel5Indiv2);
		Join join4Indiv2 = new Join(4, rel4Indiv2, rel5Indiv2, rel4Indiv2, join1Indiv2);
		Join join3Indiv2 = new Join(3, rel4Indiv2, rel3Indiv2, join4Indiv2, rel3Indiv2);
		Join join2Indiv2 = new Join(2, rel3Indiv2, rel2Indiv2, join3Indiv2, rel2Indiv2);
		Join join7Indiv2 = new Join(7, rel7Indiv2, rel8Indiv2, rel7Indiv2, rel8Indiv2);
		Join join6Indiv2 = new Join(6, rel6Indiv2, rel7Indiv2, rel6Indiv2, join7Indiv2);
		Join join13Indiv2 = new Join(13, rel11Indiv2, rel12Indiv2, rel11Indiv2, rel12Indiv2);
		Join join12Indiv2 = new Join(12, rel8Indiv2, rel11Indiv2, join6Indiv2, join13Indiv2);
		Join join18Indiv2 = new Join(18, rel12Indiv2, rel17Indiv2, join12Indiv2, rel17Indiv2);
		Join join5Indiv2 = new Join(5, rel5Indiv2, rel6Indiv2, join2Indiv2, join18Indiv2);
		Join join10Indiv2 = new Join(10, rel10Indiv2, rel18Indiv2, rel10Indiv2, rel18Indiv2);
		Join join9Indiv2 = new Join(9, rel9Indiv2, rel10Indiv2, rel9Indiv2, join10Indiv2);
		Join join8Indiv2 = new Join(8, rel8Indiv2, rel9Indiv2, join5Indiv2, join9Indiv2);
		Join join11Indiv2 = new Join(11, rel10Indiv2, rel19Indiv2, join8Indiv2, rel19Indiv2);
		Join join16Indiv2 = new Join(16, rel14Indiv2, rel15Indiv2, rel14Indiv2, rel15Indiv2);
		Join join15Indiv2 = new Join(15, rel13Indiv2, rel14Indiv2, rel13Indiv2, join16Indiv2);
		Join join14Indiv2 = new Join(14, rel12Indiv2, rel13Indiv2, join11Indiv2, join15Indiv2);
		Join join17Indiv2 = new Join(17, rel14Indiv2, rel16Indiv2, join14Indiv2, rel16Indiv2);
		
		int numJoins = 18;
		Individual indiv2 = new Individual(numJoins);
		indiv2.root = join17Indiv2;
		indiv2.calcCost();
		
		return indiv2;
	}
	
	private static ArrayList<Individual> createPopulation(int count){
		ArrayList<JoinGraphNode> joinGraph = new ArrayList<JoinGraphNode>();
		joinGraph.add(new JoinGraphNode(1, 1, 5));
		joinGraph.add(new JoinGraphNode(2, 3, 2));
		joinGraph.add(new JoinGraphNode(3, 4, 3));
		joinGraph.add(new JoinGraphNode(4, 4, 5));
		joinGraph.add(new JoinGraphNode(5, 5, 6));
		joinGraph.add(new JoinGraphNode(6, 6, 7));
		joinGraph.add(new JoinGraphNode(7, 7, 8));
		joinGraph.add(new JoinGraphNode(8, 8, 9));
		joinGraph.add(new JoinGraphNode(9, 9, 10));
		joinGraph.add(new JoinGraphNode(10, 10, 18));
		joinGraph.add(new JoinGraphNode(11, 10, 19));
		joinGraph.add(new JoinGraphNode(12, 8,  11));
		joinGraph.add(new JoinGraphNode(13, 11, 12));
		joinGraph.add(new JoinGraphNode(14, 12, 13));
		joinGraph.add(new JoinGraphNode(15, 13, 14));
		joinGraph.add(new JoinGraphNode(16, 14, 15));
		joinGraph.add(new JoinGraphNode(17, 14, 16));
		joinGraph.add(new JoinGraphNode(18, 12, 17));
		
		ArrayList<Individual> population = new ArrayList<Individual>();
		
		int numJoins = joinGraph.size();
		while(count-- > 0){
			Individual individual = new Individual(numJoins);
			individual.randomize(joinGraph);
			individual.calcCost();
			population.add(individual);
		}
		
		return population;
		
	}

}
