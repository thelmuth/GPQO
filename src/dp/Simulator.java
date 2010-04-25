package dp;

import java.util.*;

public class Simulator {

	private static final int NUMBER_RELATIONS = 13;

	public static void main(String[] args) {

		HashMap<Integer, HashSet<Integer>> relation2Neighbors =
			new HashMap<Integer, HashSet<Integer>>();
		ArrayList<Gene> partialSolutions = new ArrayList<Gene>();
		ArrayList<Relation> relations = new ArrayList<Relation>();
		ArrayList<JoinGraphNode> joinGraph = new ArrayList<JoinGraphNode>();

		int numJoins = getRepresentation2(relation2Neighbors, partialSolutions,
				relations, joinGraph);

		while (numJoins-- > 0) {
			ArrayList<Gene> newSetOfSolutions = new ArrayList<Gene>();

			for (Gene g : partialSolutions) {
				for (Relation rightRel : relations) {

					Relation leftRel = g
							.joinsWith(relation2Neighbors, rightRel);

					if (!g.containsLeaf(rightRel) && leftRel != null) {

						int joinIdAndAttrib[] = initJoin(leftRel, rightRel,
								joinGraph);

						int numJoinTypes = Join.joinTypes.length();
						for (int typeInd = 0; typeInd < numJoinTypes; ++typeInd) {

							Join newJoin = new Join(joinIdAndAttrib[0],
									leftRel, rightRel, g, rightRel,
									joinIdAndAttrib[1], Join.joinTypes
											.charAt(typeInd));

							newSetOfSolutions.add(newJoin);
						}
					}
				}
			}

			// Keep the ones with the best cost
			partialSolutions = keepBestPartialSolutions(newSetOfSolutions);
		}

		System.out.println("Number of solutions = " + partialSolutions.size()
				+ "\n");

		for (int i = 0; i < partialSolutions.size(); i++) {
			System.out.println(partialSolutions.get(i).stringify());

		}

	}

	public static int[] initJoin(Relation leftRel, Relation rightRel,
			ArrayList<JoinGraphNode> joinGraph) {

		int joinIdAndAttrib[] = new int[2];

		for (JoinGraphNode joinGraphNode : joinGraph) {

			if ((joinGraphNode.innerRelInd == leftRel.relationId &&
					joinGraphNode.outerRelInd == rightRel.relationId)
					|| (joinGraphNode.outerRelInd == leftRel.relationId &&
							joinGraphNode.innerRelInd == rightRel.relationId)) {

				joinIdAndAttrib[0] = joinGraphNode.joinInd;
				joinIdAndAttrib[1] = joinGraphNode.joinAttrib;
				break;
			}
		}

		return joinIdAndAttrib;

	}

	private static ArrayList<Gene> keepBestPartialSolutions(
			ArrayList<Gene> partialSolutions) {

		ArrayList<Gene> bestPartialSolutions = new ArrayList<Gene>();
		HashMap<Integer, ArrayList<Gene>> eqClasses = new HashMap<Integer, ArrayList<Gene>>();

		for (Gene g : partialSolutions) {
			int hashValue = g.hashRelationIds(NUMBER_RELATIONS);
			if (!eqClasses.containsKey(hashValue))
				eqClasses.put(hashValue, new ArrayList<Gene>());

			eqClasses.get(hashValue).add(g);
		}

		// Hash table is done, remove suboptimal solutions
		for (ArrayList<Gene> eqClass : eqClasses.values()) {
			Gene bestPartial = eqClass.get(0);
			double bestCost = ((Join) bestPartial).justCost();

			// System.out.println("====================================== Printing EQ Class with hash = "
			// + eqClass.get(0).hashCode());

			for (Gene partial : eqClass) {
				double partialCost = ((Join) partial).justCost();

				// System.out.println(partial.stringify());

				if (partialCost < bestCost) {
					bestCost = partialCost;
					bestPartial = partial;
				}
			}

			bestPartialSolutions.add(bestPartial);
		}

		return bestPartialSolutions;
	}
	
	private static int getRepresentation2(
			HashMap<Integer, HashSet<Integer>> relation2Neighbors,
			ArrayList<Gene> partialSolutions, ArrayList<Relation> relations,
			ArrayList<JoinGraphNode> joinGraph) {

		int numRelations = 13, numJoins = 12;

		for (int i = 1; i < numRelations + 1; ++i)
			relation2Neighbors.put(i, new HashSet<Integer>());

		int[] relSizes = new int[numRelations + 1];
		int[] clustIndAttrib = new int[numRelations + 1];
		int[] clustIndCard = new int[numRelations + 1];
		int[] joinAttrib = new int[numJoins + 1];
		
		relSizes[1] = 4500;		clustIndAttrib[1] = 2;	clustIndCard[1] = 15;         joinAttrib[1] = 2;
        relSizes[2] = 8;		clustIndAttrib[2] = 2;  clustIndCard[2] = 30;         joinAttrib[2] = 4;
        relSizes[3] = 700;      clustIndAttrib[3] = 4;  clustIndCard[3] = 45;         joinAttrib[3] = 1;
        relSizes[4] = 3;        clustIndAttrib[4] = 1;  clustIndCard[4] = 10;         joinAttrib[4] = 1;
        relSizes[5] = 90;       clustIndAttrib[5] = 3;  clustIndCard[5] = 75;         joinAttrib[5] = 3;
        relSizes[6] = 2300;     clustIndAttrib[6] = 3;  clustIndCard[6] = 40;         joinAttrib[6] = 1;
        relSizes[7] = 900;      clustIndAttrib[7] = 1;  clustIndCard[7] = 30;         joinAttrib[7] = 1;
        relSizes[8] = 300;      clustIndAttrib[8] = 1;  clustIndCard[8] = 15;         joinAttrib[8] = 1;
        relSizes[9] = 40;       clustIndAttrib[9] = 2;  clustIndCard[9] = 40;         joinAttrib[9] = 2;
        relSizes[10] = 70;      clustIndAttrib[10] = 4; clustIndCard[10] = 75;        joinAttrib[10] = 2;
        relSizes[11] = 8;       clustIndAttrib[11] = 2; clustIndCard[11] = 60;        joinAttrib[11] = 4;
        relSizes[12] = 3000;    clustIndAttrib[12] = 1; clustIndCard[12] = 35;        joinAttrib[12] = 4;
        relSizes[13] = 30;      clustIndAttrib[13] = 4; clustIndCard[13] = 60;         
        


		for (int i = 1; i < numRelations + 1; ++i) {
			Relation r = new Relation(i, relSizes[i], clustIndAttrib[i],
					clustIndCard[i]);
			relations.add(r);
			partialSolutions.add(r);
		}

		joinGraph.add(new JoinGraphNode(1, 1, 2, relSizes[1], relSizes[2],
				clustIndAttrib[1], clustIndAttrib[2], joinAttrib[1],
				clustIndCard[1], clustIndCard[2]));

		relation2Neighbors.get(1).add(2);
		relation2Neighbors.get(2).add(1);

		joinGraph.add(new JoinGraphNode(2, 1, 3, relSizes[1], relSizes[3],
				clustIndAttrib[1], clustIndAttrib[3], joinAttrib[2],
				clustIndCard[1], clustIndCard[3]));

		relation2Neighbors.get(1).add(3);
		relation2Neighbors.get(3).add(1);

		joinGraph.add(new JoinGraphNode(3, 3, 4, relSizes[3], relSizes[4],
				clustIndAttrib[3], clustIndAttrib[4], joinAttrib[3],
				clustIndCard[3], clustIndCard[4]));

		relation2Neighbors.get(4).add(3);
		relation2Neighbors.get(3).add(4);

		joinGraph.add(new JoinGraphNode(4, 3, 5, relSizes[3], relSizes[5],
				clustIndAttrib[3], clustIndAttrib[5], joinAttrib[4],
				clustIndCard[3], clustIndCard[5]));

		relation2Neighbors.get(3).add(5);
		relation2Neighbors.get(5).add(3);

		joinGraph.add(new JoinGraphNode(5, 3, 6, relSizes[3], relSizes[6],
				clustIndAttrib[3], clustIndAttrib[6], joinAttrib[5],
				clustIndCard[3], clustIndCard[6]));

		relation2Neighbors.get(3).add(6);
		relation2Neighbors.get(6).add(3);

		joinGraph.add(new JoinGraphNode(6, 6, 7, relSizes[6], relSizes[7],
				clustIndAttrib[6], clustIndAttrib[7], joinAttrib[6],
				clustIndCard[6], clustIndCard[7]));

		relation2Neighbors.get(6).add(7);
		relation2Neighbors.get(7).add(6);

		joinGraph.add(new JoinGraphNode(7, 7, 8, relSizes[7], relSizes[8],
				clustIndAttrib[7], clustIndAttrib[8], joinAttrib[7],
				clustIndCard[7], clustIndCard[8]));

		relation2Neighbors.get(7).add(8);
		relation2Neighbors.get(8).add(7);

		joinGraph.add(new JoinGraphNode(8, 8, 9, relSizes[8], relSizes[9],
				clustIndAttrib[8], clustIndAttrib[9], joinAttrib[8],
				clustIndCard[8], clustIndCard[9]));

		relation2Neighbors.get(8).add(9);
		relation2Neighbors.get(9).add(8);

		joinGraph.add(new JoinGraphNode(9, 5, 10, relSizes[5], relSizes[10],
				clustIndAttrib[5], clustIndAttrib[10], joinAttrib[9],
				clustIndCard[5], clustIndCard[10]));

		relation2Neighbors.get(5).add(10);
		relation2Neighbors.get(10).add(5);

		joinGraph.add(new JoinGraphNode(10, 4, 11, relSizes[4], relSizes[11],
				clustIndAttrib[4], clustIndAttrib[11], joinAttrib[10],
				clustIndCard[4], clustIndCard[11]));

		relation2Neighbors.get(4).add(11);
		relation2Neighbors.get(11).add(4);

		joinGraph.add(new JoinGraphNode(11, 4, 12, relSizes[4], relSizes[12],
				clustIndAttrib[4], clustIndAttrib[12], joinAttrib[11],
				clustIndCard[4], clustIndCard[12]));

		relation2Neighbors.get(4).add(12);
		relation2Neighbors.get(12).add(4);

		joinGraph.add(new JoinGraphNode(12, 8, 13, relSizes[8], relSizes[13],
				clustIndAttrib[8], clustIndAttrib[13], joinAttrib[12],
				clustIndCard[8], clustIndCard[13]));

		relation2Neighbors.get(8).add(13);
		relation2Neighbors.get(13).add(8);


		return numJoins;

	}

	
	private static int getRepresentation(
			HashMap<Integer, HashSet<Integer>> relation2Neighbors,
			ArrayList<Gene> partialSolutions, ArrayList<Relation> relations,
			ArrayList<JoinGraphNode> joinGraph) {

		int numRelations = 19, numJoins = 18;

		for (int i = 1; i < numRelations + 1; ++i)
			relation2Neighbors.put(i, new HashSet<Integer>());

		int[] relSizes = new int[numRelations + 1];
		int[] clustIndAttrib = new int[numRelations + 1];
		int[] clustIndCard = new int[numRelations + 1];
		int[] joinAttrib = new int[numJoins + 1];

		relSizes[1] = 200;
		clustIndAttrib[1] = 2;
		clustIndCard[1] = 10;
		joinAttrib[1] = 2;
		relSizes[2] = 50;
		clustIndAttrib[2] = 2;
		clustIndCard[2] = 20;
		joinAttrib[2] = 2;
		relSizes[3] = 1000;
		clustIndAttrib[3] = 1;
		clustIndCard[3] = 30;
		joinAttrib[3] = 1;
		relSizes[4] = 1300;
		clustIndAttrib[4] = 1;
		clustIndCard[4] = 50;
		joinAttrib[4] = 1;
		relSizes[5] = 8;
		clustIndAttrib[5] = 2;
		clustIndCard[5] = 40;
		joinAttrib[5] = 5;
		relSizes[6] = 1270;
		clustIndAttrib[6] = 5;
		clustIndCard[6] = 20;
		joinAttrib[6] = 5;
		relSizes[7] = 520;
		clustIndAttrib[7] = 5;
		clustIndCard[7] = 10;
		joinAttrib[7] = 4;
		relSizes[8] = 31;
		clustIndAttrib[8] = 4;
		clustIndCard[8] = 60;
		joinAttrib[8] = 2;
		relSizes[9] = 9;
		clustIndAttrib[9] = 1;
		clustIndCard[9] = 30;
		joinAttrib[9] = 1;
		relSizes[10] = 500;
		clustIndAttrib[10] = 2;
		clustIndCard[10] = 40;
		joinAttrib[10] = 3;
		relSizes[11] = 6;
		clustIndAttrib[11] = 4;
		clustIndCard[11] = 20;
		joinAttrib[11] = 2;
		relSizes[12] = 10;
		clustIndAttrib[12] = 4;
		clustIndCard[12] = 10;
		joinAttrib[12] = 4;
		relSizes[13] = 1100;
		clustIndAttrib[13] = 1;
		clustIndCard[13] = 50;
		joinAttrib[13] = 4;
		relSizes[14] = 5;
		clustIndAttrib[14] = 2;
		clustIndCard[14] = 30;
		joinAttrib[14] = 1;
		relSizes[15] = 100;
		clustIndAttrib[15] = 2;
		clustIndCard[15] = 10;
		joinAttrib[15] = 1;
		relSizes[16] = 1200;
		clustIndAttrib[16] = 5;
		clustIndCard[16] = 60;
		joinAttrib[16] = 3;
		relSizes[17] = 290;
		clustIndAttrib[17] = 5;
		clustIndCard[17] = 70;
		joinAttrib[17] = 2;
		relSizes[18] = 65;
		clustIndAttrib[18] = 3;
		clustIndCard[18] = 90;
		joinAttrib[18] = 5;
		relSizes[19] = 90;
		clustIndAttrib[19] = 2;
		clustIndCard[19] = 10;

		for (int i = 1; i < numRelations + 1; ++i) {
			Relation r = new Relation(i, relSizes[i], clustIndAttrib[i],
					clustIndCard[i]);
			relations.add(r);
			partialSolutions.add(r);
		}

		joinGraph.add(new JoinGraphNode(1, 1, 5, relSizes[1], relSizes[5],
				clustIndAttrib[1], clustIndAttrib[5], joinAttrib[1],
				clustIndCard[1], clustIndCard[5]));

		relation2Neighbors.get(1).add(5);
		relation2Neighbors.get(5).add(1);

		joinGraph.add(new JoinGraphNode(2, 3, 2, relSizes[3], relSizes[2],
				clustIndAttrib[3], clustIndAttrib[2], joinAttrib[2],
				clustIndCard[3], clustIndCard[2]));

		relation2Neighbors.get(3).add(2);
		relation2Neighbors.get(2).add(3);

		joinGraph.add(new JoinGraphNode(3, 4, 3, relSizes[4], relSizes[3],
				clustIndAttrib[4], clustIndAttrib[3], joinAttrib[3],
				clustIndCard[4], clustIndCard[3]));

		relation2Neighbors.get(4).add(3);
		relation2Neighbors.get(3).add(4);

		joinGraph.add(new JoinGraphNode(4, 4, 5, relSizes[4], relSizes[5],
				clustIndAttrib[4], clustIndAttrib[5], joinAttrib[4],
				clustIndCard[4], clustIndCard[5]));

		relation2Neighbors.get(4).add(5);
		relation2Neighbors.get(5).add(4);

		joinGraph.add(new JoinGraphNode(5, 5, 6, relSizes[5], relSizes[6],
				clustIndAttrib[5], clustIndAttrib[6], joinAttrib[5],
				clustIndCard[5], clustIndCard[6]));

		relation2Neighbors.get(5).add(6);
		relation2Neighbors.get(6).add(5);

		joinGraph.add(new JoinGraphNode(6, 6, 7, relSizes[6], relSizes[7],
				clustIndAttrib[6], clustIndAttrib[7], joinAttrib[6],
				clustIndCard[6], clustIndCard[7]));

		relation2Neighbors.get(6).add(7);
		relation2Neighbors.get(7).add(6);

		joinGraph.add(new JoinGraphNode(7, 7, 8, relSizes[7], relSizes[8],
				clustIndAttrib[7], clustIndAttrib[8], joinAttrib[7],
				clustIndCard[7], clustIndCard[8]));

		relation2Neighbors.get(7).add(8);
		relation2Neighbors.get(8).add(7);

		joinGraph.add(new JoinGraphNode(8, 8, 9, relSizes[8], relSizes[9],
				clustIndAttrib[8], clustIndAttrib[9], joinAttrib[8],
				clustIndCard[8], clustIndCard[9]));

		relation2Neighbors.get(8).add(9);
		relation2Neighbors.get(9).add(8);

		joinGraph.add(new JoinGraphNode(9, 9, 10, relSizes[9], relSizes[10],
				clustIndAttrib[9], clustIndAttrib[10], joinAttrib[9],
				clustIndCard[9], clustIndCard[10]));

		relation2Neighbors.get(9).add(10);
		relation2Neighbors.get(10).add(9);

		joinGraph.add(new JoinGraphNode(10, 10, 18, relSizes[10], relSizes[18],
				clustIndAttrib[10], clustIndAttrib[18], joinAttrib[10],
				clustIndCard[10], clustIndCard[18]));

		relation2Neighbors.get(10).add(18);
		relation2Neighbors.get(18).add(10);

		joinGraph.add(new JoinGraphNode(11, 10, 19, relSizes[10], relSizes[19],
				clustIndAttrib[10], clustIndAttrib[19], joinAttrib[11],
				clustIndCard[10], clustIndCard[19]));

		relation2Neighbors.get(10).add(19);
		relation2Neighbors.get(19).add(10);

		joinGraph.add(new JoinGraphNode(12, 8, 11, relSizes[8], relSizes[11],
				clustIndAttrib[8], clustIndAttrib[11], joinAttrib[12],
				clustIndCard[8], clustIndCard[11]));

		relation2Neighbors.get(8).add(11);
		relation2Neighbors.get(11).add(8);

		joinGraph.add(new JoinGraphNode(13, 11, 12, relSizes[11], relSizes[12],
				clustIndAttrib[11], clustIndAttrib[12], joinAttrib[13],
				clustIndCard[11], clustIndCard[12]));

		relation2Neighbors.get(11).add(12);
		relation2Neighbors.get(12).add(11);

		joinGraph.add(new JoinGraphNode(14, 12, 13, relSizes[12], relSizes[13],
				clustIndAttrib[12], clustIndAttrib[13], joinAttrib[14],
				clustIndCard[12], clustIndCard[13]));

		relation2Neighbors.get(12).add(13);
		relation2Neighbors.get(13).add(12);

		joinGraph.add(new JoinGraphNode(15, 13, 14, relSizes[13], relSizes[14],
				clustIndAttrib[13], clustIndAttrib[14], joinAttrib[15],
				clustIndCard[13], clustIndCard[14]));

		relation2Neighbors.get(13).add(14);
		relation2Neighbors.get(14).add(13);

		joinGraph.add(new JoinGraphNode(16, 14, 15, relSizes[14], relSizes[15],
				clustIndAttrib[14], clustIndAttrib[15], joinAttrib[16],
				clustIndCard[14], clustIndCard[15]));

		relation2Neighbors.get(14).add(15);
		relation2Neighbors.get(15).add(14);

		joinGraph.add(new JoinGraphNode(17, 14, 16, relSizes[14], relSizes[16],
				clustIndAttrib[14], clustIndAttrib[16], joinAttrib[17],
				clustIndCard[14], clustIndCard[16]));

		relation2Neighbors.get(14).add(16);
		relation2Neighbors.get(16).add(14);

		joinGraph.add(new JoinGraphNode(18, 12, 17, relSizes[12], relSizes[17],
				clustIndAttrib[12], clustIndAttrib[17], joinAttrib[18],
				clustIndCard[12], clustIndCard[17]));

		relation2Neighbors.get(12).add(17);
		relation2Neighbors.get(17).add(12);

		return numJoins;

	}

}