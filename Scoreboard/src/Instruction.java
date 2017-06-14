
public class Instruction {
	private String instructionName;
	private String destination;
	private String sr1;
	private String sr2;
	private int offSet;
	private boolean dummy=false;
	
	
	public String getInstructionName() {
		return instructionName;
	}
	public void setInstructionName(String instructionName) {
		this.instructionName = instructionName;
	}
	public String getDestination() {
		return destination;
	}
	public void setDestination(String destination) {
		this.destination = destination;
	}
	public String getSr1() {
		return sr1;
	}
	public void setSr1(String sr1) {
		this.sr1 = sr1;
	}
	public String getSr2() {
		return sr2;
	}
	public void setSr2(String sr2) {
		this.sr2 = sr2;
	}
	public int getOffSet() {
		return offSet;
	}
	public void setOffSet(int offSet) {
		this.offSet = offSet;
	}
	public boolean isDummy() {
		return dummy;
	}
	public void setDummy(boolean dummy) {
		this.dummy = dummy;
	}
	@Override
	public String toString() {
		if ((instructionName.equalsIgnoreCase("LDM")) || (instructionName.equalsIgnoreCase("LDH"))
				|| (instructionName.equalsIgnoreCase("ST"))) {
			return " " + instructionName + " " + destination + " " + sr1 + " " + offSet + " ";
		} else {
			return " " + instructionName + " " + destination + " " + sr1 + " " + sr2 + " ";
		}

	}
	
	
	
}
