package gpqo;
import java.util.*;

public class Simulator {
 
	private static final int POPULATION_SIZE = 1000;
	private static final int GENERATIONS = 100;
	
	private static final int TOURNAMENT_SIZE = 7;
	private static final int GEOGRAPHY_RADIUS = 10;
	
	private static final int CROSSOVER_PERCENT = 70;
	private static final int MUTATION_PERCENT = 25;
	//CLONE_PERCENT is effectively 100 - CROSSOVER_PERCENT - MUTATION_PERCENT.
	
	private static final boolean GENERATION_REPORT = true;

	
	public static void main(String[] args) {		
		ArrayList<Individual> population = createPopulation(POPULATION_SIZE);
		
		int genNum = 0;
		
		for(; genNum < GENERATIONS; genNum++){
			population = createNextGeneration(population);
			
			if(GENERATION_REPORT){
				generationReport(genNum, population);
			}
		}
		
		finalReport(genNum, population);

	}


	private static ArrayList<Individual> createNextGeneration(ArrayList<Individual> population) {
		ArrayList<Individual> nextPopulation = new ArrayList<Individual>();
		
		for(int i = 0; i < POPULATION_SIZE; i++){
			try {
				nextPopulation.add(createOffspring(population, i));
			} catch (Exception e) {
				System.err.println("Error creating an offspring");
				e.printStackTrace();
			}
		}
		
		return nextPopulation;
	}
	
	private static Individual createOffspring(ArrayList<Individual> population, int curInd) throws Exception {
		Individual newIndividual;
		
		Random rand = new Random();
		int chooseGeneticOperator = rand.nextInt(100);

		if(chooseGeneticOperator < CROSSOVER_PERCENT){
			//Crossover
			Individual parent1 = tournamentSelection(population, curInd);
			Individual parent2 = tournamentSelection(population, curInd);
			newIndividual = crossover(parent1, parent2.randomSubTree());
		}
		else if(chooseGeneticOperator < CROSSOVER_PERCENT + MUTATION_PERCENT){
			//Mutation
			Individual parent = tournamentSelection(population, curInd);
			newIndividual = mutation(parent);
		}
		else {
			//Clone
			Individual parent = tournamentSelection(population, curInd);
			newIndividual = new Individual(parent);
		}
		
		return newIndividual;
	}


	private static Individual tournamentSelection(ArrayList<Individual> population, int curInd) {
		
		Individual selection = population.get(getIntWithinRadius(curInd));
		
		for(int i = 0; i < TOURNAMENT_SIZE - 1; i++){
			Individual competitor = population.get(getIntWithinRadius(curInd));
			if(competitor.cost < selection.cost){
				selection = competitor;
			}
		}
		
		return selection;
	}


	private static int getIntWithinRadius(int curInd) {
		Random rand = new Random();
		
		int selected = curInd + rand.nextInt(2 * GEOGRAPHY_RADIUS) - GEOGRAPHY_RADIUS;
		if(selected < 0)
			selected += POPULATION_SIZE;
		if(selected >= POPULATION_SIZE)
			selected -= POPULATION_SIZE;
		
		return selected;
	}


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
		final int MUT_PROB_1 = 35;
		final int MUT_PROB_2 = 20;
		final int MUT_PROB_3 = 20;
		final int MUT_PROB_4 = 25;
		
		Individual mutatedT = new Individual(T);
		
		Random rand = new Random();
		int chooseMutation = rand.nextInt(100);
		if(chooseMutation < MUT_PROB_1){
			mutatedT.mutationOperator1();
		}
		else if(chooseMutation < MUT_PROB_1 + MUT_PROB_2){
			mutatedT.mutationOperator2();
		}
		else if(chooseMutation < MUT_PROB_1 + MUT_PROB_2 + MUT_PROB_3){
			mutatedT.mutationOperator3();
		}
		else if(chooseMutation < MUT_PROB_1 + MUT_PROB_2 + MUT_PROB_3 + MUT_PROB_4){
			mutatedT.mutationOperator4();
		}
		
		mutatedT.calcCost();
		return mutatedT;
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
	

	private static void generationReport(int genNum, ArrayList<Individual> population) {
		Individual bestIndividual = population.get(0);
		
		for(Individual ind : population){
			if(ind.cost < bestIndividual.cost)
				bestIndividual = ind;
		}
		
		System.out.println("########## Report For Generation " + (genNum + 1) + " ##########");
		System.out.println("The best individual is:");
		System.out.println(bestIndividual);
		System.out.println();
	}
	
	private static Individual finalReport(int genNum, ArrayList<Individual> population) {
		Individual bestIndividual = population.get(0);
		
		for(Individual ind : population){
			if(ind.cost < bestIndividual.cost)
				bestIndividual = ind;
		}
		
		System.out.println("++++++++++ Final Report After " + genNum + " Generations ++++++++++");
		System.out.println("The best individual is:");
		System.out.println(bestIndividual);
		System.out.println();
		
		return bestIndividual;
	}

	private static ArrayList<Individual> createPopulation(int count){
		
		int maxSize = 1000, minSize = 10;
		int numRelations = 19, numJoins = 18;
		
		ArrayList<Individual> population = new ArrayList<Individual>();
		Random generator = new Random();
		
		int[] relSizes = new int[numRelations + 1];
		for (int relInd = 1; relInd < numRelations + 1; ++relInd){
			// The line below will generate sizes between minSize and maxSize
			int size = generator.nextInt(maxSize - minSize) + minSize;
			relSizes[relInd] = size;
		}
		int[] clustIndAttrib = new int[numRelations + 1];
		int[] clustIndCard = new int[numRelations + 1];
		int[] joinAttrib = new int[numJoins + 1];
		
		relSizes[1] = 200;		clustIndAttrib[1] = 2;		clustIndCard[1] = 10; 	joinAttrib[1] = 2;
		relSizes[2] = 50;		clustIndAttrib[2] = 2;		clustIndCard[2] = 20; 	joinAttrib[2] = 2;
		relSizes[3] = 1000;		clustIndAttrib[3] = 1;		clustIndCard[3] = 30; 	joinAttrib[3] = 1;
		relSizes[4] = 1300;		clustIndAttrib[4] = 1;		clustIndCard[4] = 50; 	joinAttrib[4] = 1;
		relSizes[5] = 8;		clustIndAttrib[5] = 2;		clustIndCard[5] = 40; 	joinAttrib[5] = 5;
		relSizes[6] = 1270;		clustIndAttrib[6] = 5;		clustIndCard[6] = 20; 	joinAttrib[6] = 5;
		relSizes[7] = 520;		clustIndAttrib[7] = 5;		clustIndCard[7] = 10; 	joinAttrib[7] = 4;
		relSizes[8] = 31;		clustIndAttrib[8] = 4;		clustIndCard[8] = 60; 	joinAttrib[8] = 2;
		relSizes[9] = 9;		clustIndAttrib[9] = 1;		clustIndCard[9] = 30; 	joinAttrib[9] = 1;
		relSizes[10] = 500;		clustIndAttrib[10] = 2;		clustIndCard[10] = 40;	joinAttrib[10] = 3;
		relSizes[11] = 6;		clustIndAttrib[11] = 4;		clustIndCard[11] = 20; 	joinAttrib[11] = 2;
		relSizes[12] = 10;		clustIndAttrib[12] = 4;		clustIndCard[12] = 10; 	joinAttrib[12] = 4;
		relSizes[13] = 1100;	clustIndAttrib[13] = 1;		clustIndCard[13] = 50; 	joinAttrib[13] = 4;
		relSizes[14] = 5;		clustIndAttrib[14] = 2;		clustIndCard[14] = 30; 	joinAttrib[14] = 1;
		relSizes[15] = 100;		clustIndAttrib[15] = 2;		clustIndCard[15] = 10; 	joinAttrib[15] = 1;
		relSizes[16] = 1200;	clustIndAttrib[16] = 5;		clustIndCard[16] = 60; 	joinAttrib[16] = 3;
		relSizes[17] = 290;		clustIndAttrib[17] = 5;		clustIndCard[17] = 70; 	joinAttrib[17] = 2;
		relSizes[18] = 65;		clustIndAttrib[18] = 3;		clustIndCard[18] = 90; 	joinAttrib[18] = 5;
		relSizes[19] = 90;		clustIndAttrib[19] = 2; 	clustIndCard[19] = 10; 
		
		
		while(count-- > 0){
			ArrayList<JoinGraphNode> joinGraph = new ArrayList<JoinGraphNode>();
			joinGraph.add(new JoinGraphNode(1, 1, 5, relSizes[1], relSizes[5],
					clustIndAttrib[1], clustIndAttrib[5], joinAttrib[1],
					clustIndCard[1], clustIndCard[5]));
			
			joinGraph.add(new JoinGraphNode(2, 3, 2, relSizes[3], relSizes[2],
					clustIndAttrib[3], clustIndAttrib[2], joinAttrib[2],
					clustIndCard[3], clustIndCard[2]));
			
			joinGraph.add(new JoinGraphNode(3, 4, 3, relSizes[4], relSizes[3],
					clustIndAttrib[4], clustIndAttrib[3], joinAttrib[3],
					clustIndCard[4], clustIndCard[3]));
			
			joinGraph.add(new JoinGraphNode(4, 4, 5, relSizes[4], relSizes[5],
					clustIndAttrib[4], clustIndAttrib[5], joinAttrib[4],
					clustIndCard[4], clustIndCard[5]));
			
			joinGraph.add(new JoinGraphNode(5, 5, 6, relSizes[5], relSizes[6],
					clustIndAttrib[5], clustIndAttrib[6], joinAttrib[5],
					clustIndCard[5], clustIndCard[6]));
			
			joinGraph.add(new JoinGraphNode(6, 6, 7, relSizes[6], relSizes[7],
					clustIndAttrib[6], clustIndAttrib[7], joinAttrib[6],
					clustIndCard[6], clustIndCard[7]));
			
			joinGraph.add(new JoinGraphNode(7, 7, 8, relSizes[7], relSizes[8],
					clustIndAttrib[7], clustIndAttrib[8], joinAttrib[7],
					clustIndCard[7], clustIndCard[8]));
			
			joinGraph.add(new JoinGraphNode(8, 8, 9, relSizes[8], relSizes[9],
					clustIndAttrib[8], clustIndAttrib[9], joinAttrib[8],
					clustIndCard[8], clustIndCard[9]));
			
			joinGraph.add(new JoinGraphNode(9, 9, 10, relSizes[9], relSizes[10],
					clustIndAttrib[9], clustIndAttrib[10], joinAttrib[9],
					clustIndCard[9], clustIndCard[10]));
			
			joinGraph.add(new JoinGraphNode(10, 10, 18, relSizes[10], relSizes[18],
					clustIndAttrib[10], clustIndAttrib[18], joinAttrib[10],
					clustIndCard[10], clustIndCard[18]));
			
			joinGraph.add(new JoinGraphNode(11, 10, 19, relSizes[10], relSizes[19],
					clustIndAttrib[10], clustIndAttrib[19], joinAttrib[11],
					clustIndCard[10], clustIndCard[19]));
			
			joinGraph.add(new JoinGraphNode(12, 8,  11, relSizes[8], relSizes[11],
					clustIndAttrib[8], clustIndAttrib[11], joinAttrib[12],
					clustIndCard[8], clustIndCard[11]));
			
			joinGraph.add(new JoinGraphNode(13, 11, 12, relSizes[11], relSizes[12],
					clustIndAttrib[11], clustIndAttrib[12], joinAttrib[13],
					clustIndCard[11], clustIndCard[12]));
			
			joinGraph.add(new JoinGraphNode(14, 12, 13, relSizes[12], relSizes[13],
					clustIndAttrib[12], clustIndAttrib[13], joinAttrib[14],
					clustIndCard[12], clustIndCard[13]));
			
			joinGraph.add(new JoinGraphNode(15, 13, 14, relSizes[13], relSizes[14],
					clustIndAttrib[13], clustIndAttrib[14], joinAttrib[15],
					clustIndCard[13], clustIndCard[14]));
			
			joinGraph.add(new JoinGraphNode(16, 14, 15, relSizes[14], relSizes[15],
					clustIndAttrib[14], clustIndAttrib[15], joinAttrib[16],
					clustIndCard[14], clustIndCard[15]));
			
			joinGraph.add(new JoinGraphNode(17, 14, 16, relSizes[14], relSizes[16],
					clustIndAttrib[14], clustIndAttrib[16], joinAttrib[17],
					clustIndCard[14], clustIndCard[16]));
			
			joinGraph.add(new JoinGraphNode(18, 12, 17, relSizes[12], relSizes[17],
					clustIndAttrib[12], clustIndAttrib[17], joinAttrib[18],
					clustIndCard[12], clustIndCard[17]));
			
			Individual individual = new Individual();
			individual.randomize(joinGraph);
			individual.calcCost();
			population.add(individual);
		}
		
		return population;
		
	}
	
	//Sample Individuals
	/*
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
	*/

}



//Try mutation
/*
Individual indA = sampleIndividual1();
System.out.println("--indA");
System.out.println(indA);

Individual nInd = mutation(indA);

System.out.println("\n--mutated indA");
System.out.println(nInd);
*/

//Try crossover here.
/*
Individual indiv1 = sampleIndividual1();
Individual indiv2 = sampleIndividual2(); 
 
Join subtree1 = indiv1.findJoinWithId(16);
Join subtree2 = indiv2.findJoinWithId(18);

Individual newIndiv1 = new Individual(indiv1.numJoins);
Individual newIndiv2 = new Individual(indiv1.numJoins);

System.out.println("Printing indiv1");
System.out.println(indiv1);

System.out.println("Printing indiv2");
System.out.println(indiv2);

try {
	newIndiv1 = crossover(indiv1, subtree2);
	newIndiv2 = crossover(indiv2, subtree1);
} catch (Exception e) {
	System.err.println("Error during crossover: " + e);
}


System.out.println("\nPrinting newIndiv1");
System.out.println(newIndiv1);

System.out.println("\nPrinting newIndiv2");
System.out.println(newIndiv2);
*/

//Try out cost function
/*
System.out.println("cost of indiv1 = " + indiv1.cost);
System.out.println("cost of indiv2 = " + indiv2.cost);
System.out.println("cost of newIndiv1 = " + newIndiv1.cost);
System.out.println("cost of newIndiv2 = " + newIndiv2.cost);

ArrayList<Individual> population = createPopulation(200);

for(Individual i : population){
	System.out.println(i.cost);
}
*/
