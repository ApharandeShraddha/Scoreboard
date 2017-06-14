public class Unit {
	//to disallow more than one instructions to enter in FU
	private boolean isFunctionalUnitFree=true;
	
	private String name;
	private int latency;
	private String unitType;
	
	//total no of instructions
	//increment while issuing instructions
	int totalInstructions;


	public Unit(String unitTypeIn,int latencyIn,String nameIn) {
		// TODO Auto-generated constructor stub
		this.unitType=unitTypeIn;
		this.latency=latencyIn;
		this.name=nameIn;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLatency() {
		return latency;
	}
	public void setLatency(int latency) {
		this.latency = latency;
	}
	
	


	public String getUnitType() {
		return unitType;
	}
	public void setUnitType(String unitType) {
		this.unitType = unitType;
	}






	public boolean isFunctionalUnitFree() {
		return this.isFunctionalUnitFree;
	}



	public void setFunctionalUnitFree(boolean isFunctionalUnitFree) {
		this.isFunctionalUnitFree = isFunctionalUnitFree;
	}
	
	
	
	public int getTotalInstructions() {
		return totalInstructions;
	}

	public void setTotalInstructions(int totalInstructions) {
		this.totalInstructions = totalInstructions;
	}

	public void incrementTotalInstructions() {
		// TODO Auto-generated method stub
		this.totalInstructions++;
	}
	public void decrementTotalInstructions() {
		// TODO Auto-generated method stub
		this.totalInstructions--;
	}
	
	
	
}
