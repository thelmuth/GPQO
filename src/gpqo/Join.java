package gpqo;

import java.util.ArrayList;
import java.util.Random;

public class Join extends Gene {
	public int joinId;
	public char joinType;
	
	public Gene inner; //Left child
	public Gene outer; //Right child
	
	public Relation leftRelation;
	public Relation rightRelation;
	
	public static String joinTypes = "SBH";
	
	public Join() {}
	
	public Join(int joinId){
		this.joinId = joinId;
	}
	
	//Copy constructor
	public Join(Join join) {
		this.joinId = join.joinId;
		this.joinType = join.joinType;
		
		this.leftRelation = join.leftRelation;
		this.rightRelation = join.rightRelation;
		
		this.inner = null;
		this.outer = null;
	}
	
	//Constructor for random join
	public Join(int joinId, Relation leftRelation, Relation rightRelation, Gene inner, Gene outer){
		this.joinId = joinId;
		
		Random generator = new Random();
		int randomIndex = generator.nextInt(joinTypes.length());
		
		this.joinType = joinTypes.charAt(randomIndex);
		
		this.leftRelation = leftRelation;
		this.rightRelation = rightRelation;
		
		this.inner = inner;
		this.outer = outer;
	}

	public double[] cost(){
		
		double innerCost = 0, innerSize = 0, outerCost = 0, outerSize = 0;
		double[] returnCostAndSize = new double[2];
		
		if (inner instanceof Relation){
			innerSize = ((Relation)inner).size;
			innerCost = 0;
		}
		else{
			double[] costAndSize = ((Join)inner).cost();
			innerCost = costAndSize[0];
			innerSize = costAndSize[1];
		}
			
		if (outer instanceof Relation){
			outerSize = ((Relation)outer).size;
			outerCost = 0;
		}
		else{
			double[] costAndSize = ((Join)outer).cost();
			outerCost = costAndSize[0];
			outerSize = costAndSize[1];
		}
		
		// Assuming a reduction factor of 10. 
		double reductionFactor = 10;
		returnCostAndSize[0] = calcCost(innerSize, outerSize) + innerCost + outerCost;
		returnCostAndSize[1] = Math.ceil(innerSize * outerSize / reductionFactor); 
		
		return returnCostAndSize;
	}
	
	protected double calcCost(double innerSize, double outerSize){
		// Assuming buffer size of 1002
		double bufferSize = 1002, cost = -1;
		
		switch (joinType){
			case 'S': 
				double ratioNtoBInner = Math.ceil(innerSize / bufferSize);
				double ratioNtoBOuter = Math.ceil(outerSize / bufferSize);
				
				cost = 2 * innerSize * (Math.ceil(Math.log(ratioNtoBInner) / Math.log(bufferSize - 1)) + 1) + 
					2 * outerSize * (Math.ceil(Math.log(ratioNtoBOuter) / Math.log(bufferSize - 1)) + 1) + 
					(innerSize + outerSize);
				break;
			
			case 'B': 
				double smallerRel = Math.min(innerSize, outerSize);
				double biggerRel = Math.max(innerSize, outerSize);
				
				cost = smallerRel + biggerRel * Math.ceil(smallerRel / (bufferSize - 2)); 
				break;
			
			case 'H': 
				cost = 3 * (innerSize + outerSize);
				break;
			
			default: 
				System.out.println("Invalid join type!"); 
				break;
		}
		
		return cost;
	}

	public ArrayList<Join> pOrder(){
		
		ArrayList<Join> returnList = new ArrayList<Join>();
		
		if(!(inner == null) && (inner instanceof Join))
			returnList.addAll(((Join)inner).pOrder());
		
		if(!(outer == null) && (outer instanceof Join))
			returnList.addAll(((Join)outer).pOrder());
		
		returnList.add(this);
		return returnList;
	}
	
	public String toString(){
		return "join " + joinId;
	}
	
}
