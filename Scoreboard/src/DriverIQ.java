import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DriverIQ {
	
	static int cycles=3;
	static int issueWidth;
	// Cachemiss
	static int penaltyCacheMiss;
	static int choice;
	
	// DUMP
	static int dumpFirstOutputCycle;
	static int dumpSecondOutputCycle;
	
	//static Instruction[] iq=new Instruction[50];
	static List<Instruction> iq=new ArrayList<>();


	
	
	// Destination Registers map
	static Map<String, Boolean> destinationRegisters = new HashMap<String, Boolean>();

	// use input map(key as destination register for instructions)
	static ArrayList<Instruction> inputInstructions = new ArrayList<>();

	// map to keep record for dump
	static HashMap<Integer, Map<Instruction, String>> instructionDump = new HashMap<>();

	// map for issued instructions
	static ArrayList<Instruction> inputInstructionsIssued = new ArrayList<Instruction>();
	
	//this cycle issued instructions
	static ArrayList<Instruction> inputInstructionsIssuedLastCycle = new ArrayList<Instruction>();

	
	static Instruction[] arrayALU=new Instruction[1];
	static Instruction[] arrayMUL=new Instruction[4];
	static Instruction[] arrayDIV=new Instruction[8];
	static Instruction[] arrayLSU=new Instruction[2];
	
	static Unit ALU= new Unit("pipelined",1,"ALU");
	static Unit MUL= new Unit("pipelined",4,"MUL");
	static Unit DIV= new Unit("pipelined",8,"DIV");
	static Unit LSU= new Unit("pipelined",2,"LSU");
	
	
	
	


	private static void displayOptions() {
		System.out.println("1:addUnit");
		System.out.println("2:issueInstruction");
		System.out.println("3:completionTime");
		System.out.println("4:advanceClock");
		System.out.println("5:Dump");
		System.out.println("Enter your choice:");
	}

	static private void readFile() {
		// TODO Auto-generated method stub
		FileOperationsIQ file = new FileOperationsIQ();
		file.readFile();
	}
	
	public static void insertEntry(Instruction instruction) {
		inputInstructions.add(instruction);

	}
	
	
	public static void main(String[] args) {
		try {
		do {
			displayOptions();
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
				choice = Integer.parseInt(reader.readLine());
			
			switch (choice) {
			case 1:
				System.out.println(
						"Please enter Fucntional unit type(pipelined or non-pipelined , latency (e.g 4 for MUL) and name of functional unit (e.g MUL,DIV,ALU,LSU)");
				
				System.out.println("Funtional Unit added... Press ENTER to continue...");
				break;
			case 2:
				
				readFile();
				issueInstruction();
				break;
			case 3:
				System.out.println("Enter destination register for which you want to know completion time: ");
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(System.in));
				String register = (reader2.readLine()).toString();
				 completionTime(register);
				
				break;
			case 4:
				advanceClock();
				break;
			case 5:
				dump();
				break;
			}
		} while (choice < 6 || choice > 0);
		
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}
	
	static private void removeCompletedInstructions(boolean fromAdvClock) {
		// TODO Auto-generated method stub
		Instruction removedInstruction1=null;
		if (ALU.getTotalInstructions() == ALU.getLatency() || (fromAdvClock && (arrayALU[arrayALU.length-1]) !=null)) {
			removedInstruction1= arrayALU[ALU.getLatency()-1];
			ALU.decrementTotalInstructions();
		}else if (MUL.getTotalInstructions() == MUL.getLatency() || (fromAdvClock && (arrayMUL[arrayMUL.length-1]) !=null)) {
			removedInstruction1= arrayMUL[MUL.getLatency()-1];
			MUL.decrementTotalInstructions();
		}else if (DIV.getTotalInstructions() == DIV.getLatency() || (fromAdvClock && (arrayDIV[arrayDIV.length-1]) !=null)) {
			removedInstruction1= arrayDIV[DIV.getLatency()-1];
			DIV.decrementTotalInstructions();
		}else if (LSU.getTotalInstructions() == LSU.getLatency() || (fromAdvClock && (arrayLSU[arrayLSU.length-1]) !=null)) {
			removedInstruction1= arrayLSU[LSU.getLatency()-1];
			LSU.decrementTotalInstructions();
		}
		
		if(removedInstruction1!=null){
			destinationRegisters.put(removedInstruction1.getDestination(), true);
			if (instructionDump.containsKey(cycles)) {
				instructionDump.get(cycles).put(removedInstruction1, "Complete");

			} else {
				Map<Instruction, String> instructionStatus = new HashMap<>();
				instructionStatus.put(removedInstruction1, "Complete");
				instructionDump.put(cycles, instructionStatus);
			}

		}
		
	}
	

	// Add status of changed instruction to instructionDump
	static void trackInstructionDump(Instruction[] ad) {
		if(ad==null)
			return;
		for (int i = 0; i < ad.length; i++) {
			Instruction ins=ad[i];
			if(ins==null)
				continue;
		//	if (ad.length < (getUnit(ad[i].getInstructionName()).getLatency())) {
			int displayI=i+1;
				if (instructionDump.containsKey(cycles)) {
					instructionDump.get(cycles).put(ad[i],
							getUnit(ad[i].getInstructionName()).getName() + "(" + displayI + ")");

				} else {
					Map<Instruction, String> instructionStatus = new HashMap<>();
					instructionStatus.put(ad[i], getUnit(ad[i].getInstructionName()).getName() + "(" + displayI + ")");
					instructionDump.put(cycles, instructionStatus);

				}
			}
		//}
	}

	static private void dump() {
		for (int cycles : instructionDump.keySet()) {
			Map<Instruction, String> id = instructionDump.get(cycles);
			for (Instruction i : id.keySet()) {
				String instructionStatus = id.get(i);

				System.out.println("Cycle " + cycles + ":" + instructionStatus + " " + i.toString());

			}

		}

	}
	
	private static void advanceClock() {
		// TODO Auto-generated method stub
		// remove Completed Instructions

		removeCompletedInstructions(true);

		// move forward each instruction in each unit

		Instruction[] arrayALU1 = new Instruction[arrayALU.length];
		Instruction[] arrayMUL1 = new Instruction[arrayMUL.length];
		Instruction[] arrayDIV1 = new Instruction[arrayDIV.length];
		Instruction[] arrayLSU1 = new Instruction[arrayLSU.length];

		System.arraycopy(arrayALU, 0, arrayALU1, 1, arrayALU.length - 1);
		System.arraycopy(arrayMUL, 0, arrayMUL1, 1, arrayMUL.length - 1);
		System.arraycopy(arrayDIV, 0, arrayDIV1, 1, arrayDIV.length - 1);
		System.arraycopy(arrayLSU, 0, arrayLSU1, 1, arrayLSU.length - 1);

		DriverIQ.arrayALU = arrayALU1;
		DriverIQ.arrayMUL = arrayMUL1;
		DriverIQ.arrayDIV = arrayDIV1;
		DriverIQ.arrayLSU = arrayLSU1;

		for (Instruction inst : inputInstructionsIssuedLastCycle) {
			Unit unit = getUnit(inst.getInstructionName());
			Instruction[] array = getInstructionsInThisUnit(unit);

			int lastInstructionIndex = 0;
			for (int i = 0; i < array.length; i++) {
				Instruction ins = array[i];
				if (ins == null) {
					lastInstructionIndex = i;
					break;
				}
			}

			array[lastInstructionIndex] = inst;

		}
		inputInstructionsIssuedLastCycle.removeAll(inputInstructionsIssuedLastCycle);

		List<Instruction> listRemoved = new ArrayList<>();
		for (Instruction instruction : iq) {
			Unit functionalUnit = getUnit(instruction.getInstructionName());

			if (isDependencyResolved(instruction) && isFunctionalUnitFree(functionalUnit)) {
				inputInstructionsIssued.add(instruction);
				inputInstructionsIssuedLastCycle.add(instruction);
				functionalUnit.setFunctionalUnitFree(false);

				addInstructionsToFunctionalUnit(instruction, functionalUnit);

				destinationRegisters.put(instruction.getDestination(), false);
				//addInstructionStatusInDumpAgain(instruction, "issue");

		

			listRemoved.add(instruction);
			Unit unit = getUnit(instruction.getInstructionName());
			Instruction[] array = getInstructionsInThisUnit(unit);

			int lastInstructionIndex = 0;
			for (int i = 0; i < array.length; i++) {
				Instruction ins = array[i];
				if (ins == null) {
					lastInstructionIndex = i;
					break;
				}
			}

			array[lastInstructionIndex] = instruction;
			}

		}
		inputInstructions.removeAll(inputInstructionsIssued);
		inputInstructionsIssuedLastCycle.removeAll(inputInstructionsIssuedLastCycle);

		for (Instruction instruction : iq) {
			Unit functionalUnit = getUnit(instruction.getInstructionName());
			functionalUnit.setFunctionalUnitFree(true);

		}
		iq.removeAll(listRemoved);

		trackInstructionDump(arrayALU);
		trackInstructionDump(arrayMUL);
		trackInstructionDump(arrayDIV);
		trackInstructionDump(arrayLSU);

		issueInstruction();

	}

	static private void completionTime(String destinationRegister) {
		if (destinationRegisters.isEmpty()) {
			System.out.println("No instructions issued so register is free");

		}
		for (String destinationRegisterFromMap : destinationRegisters.keySet()) {
			if (destinationRegister.equalsIgnoreCase(destinationRegisterFromMap)) {
				Boolean validity = destinationRegisters.get(destinationRegisterFromMap);
				if (validity) {
					System.out.println("Its Free Register");
					
				} else {
					for (Instruction instruction : inputInstructionsIssued) {
						if (instruction.getDestination().endsWith(destinationRegister)) {
							String instructionName = instruction.getInstructionName();
							Unit unit = getUnit(instructionName);

							Instruction[] ins = getInstructionsInThisUnit(unit);
							int counter = 0;
							for (int i = 0; i < ins.length; i++) {
								counter++;
								Instruction insArray = ins[i];
								if (insArray!=null && insArray.equals(ins[i])) {
									int remainingycles = unit.getLatency() - counter;
									System.out.println("Remaining cycles for the completion of instruction holding this register :- "+remainingycles);
								}
							}

						}
					}
				}
					
			}
		}
				

			

	
	}
	

	private static Unit getUnit(String instructionName) {
		Unit functionalUnitFinal = null;
	
			if (((instructionName.equalsIgnoreCase("ADD")) || (instructionName.equalsIgnoreCase("SUB")))) {
				functionalUnitFinal = ALU;
			} else if (instructionName.equalsIgnoreCase("MUL")) {
				functionalUnitFinal = MUL;
			} else if (instructionName.equalsIgnoreCase("DIV")) {
				functionalUnitFinal = DIV;
			} else if ((instructionName.equalsIgnoreCase("LDM")) || (instructionName.equalsIgnoreCase("LDH")) || (instructionName.equalsIgnoreCase("ST"))) {
				functionalUnitFinal = LSU;
			}
		
		return functionalUnitFinal;
	}
	
	// Check registers dependency
	static private Boolean isDependencyResolved(Instruction instruction) {

			String receivedDestinationRegister = instruction.getDestination();
			String src1 = instruction.getSr1();
			
			if ((instruction.getInstructionName().equalsIgnoreCase("ADD"))
					|| (instruction.getInstructionName().equalsIgnoreCase("SUB"))
					|| (instruction.getInstructionName().equalsIgnoreCase("MUL"))
					|| (instruction.getInstructionName().equalsIgnoreCase("DIV"))) {
				String src2 = instruction.getSr2();
				for (Map.Entry<String, Boolean> entry : destinationRegisters.entrySet()) {
					if ((src1.equalsIgnoreCase(entry.getKey())) || (src2.equalsIgnoreCase(entry.getKey()))
							|| (receivedDestinationRegister.equalsIgnoreCase(entry.getKey()))) {
						if (entry.getValue())
							return true;
						return false;
					}

				}
			} else {
				for (Map.Entry<String, Boolean> entry : destinationRegisters.entrySet()) {
					if ((src1.equalsIgnoreCase(entry.getKey()))
							|| (receivedDestinationRegister.equalsIgnoreCase(entry.getKey()))) {
						if (entry.getValue())
							return true;
						return false;
					}

				}

			}

			return true;
		}
		
		// Check functional unit availability
		static private Boolean isFunctionalUnitFree(Unit CurrentFunctionalUnit) {
			if (CurrentFunctionalUnit.getUnitType().equalsIgnoreCase("pipelined")) {
				if ( CurrentFunctionalUnit.getTotalInstructions() <= CurrentFunctionalUnit.getLatency() && CurrentFunctionalUnit.isFunctionalUnitFree()) {
					return true;
				}
			} else if (CurrentFunctionalUnit.getUnitType().equalsIgnoreCase("unpipelined")) {
				if (  CurrentFunctionalUnit.getTotalInstructions() < 1) {
					return true;
				}
			}
			return false;

		}
		
	static private void addInstructionStatusInDumpAgain(Instruction instruction,String statusIn) {
		// TODO Auto-generated method stub
		//flag to say that is this instruction was alread in decode
		boolean flagd=false;
		
		for (int i1 : instructionDump.keySet()) {
			Map<Instruction, String> dumpInstruction = instructionDump.get(i1);
			for (Instruction i2 : dumpInstruction.keySet()) {
				
					if (i2.equals(instruction)) {
						String status = dumpInstruction.get(instruction);
						if (status.equalsIgnoreCase(statusIn)) {
							flagd=true;

						}
				}
			}
		}
		
		if(!flagd){
			if (instructionDump.containsKey(cycles)) {
				instructionDump.get(cycles).put(instruction, statusIn);
			}else{
				Map<Instruction, String> instructionStatus = new HashMap<>();
				instructionStatus.put(instruction, statusIn);
				instructionDump.put(cycles, instructionStatus);
			}
			
			}

	}
	
	static private Instruction[] getInstructionsInThisUnit(Unit functionalUnit) {
		
		// TODO Auto-generated method stub
		
		Instruction[] array=null;
		if (functionalUnit.getName().equalsIgnoreCase("ALU")) {
			array= arrayALU;
			
		} else if (functionalUnit.getName().equalsIgnoreCase("MUL")) {
			array= arrayMUL;
			
		}else if (functionalUnit.getName().equalsIgnoreCase("DIV")) {
			array= arrayDIV;
			
		} else if (functionalUnit.getName().equalsIgnoreCase("LSU")) {
			array= arrayLSU;
		}
		return 	array;


	}
	
	
	
	// add issued instruction in to functional unit
	//before calling this method make sure to check complete last instruction in array
	static private void addInstructionsToFunctionalUnit(Instruction instruction, Unit functionalUnit) {
		
		/*//check whether we can add new instruction or fu is full
		if(lastInstructionIndex==functionalUnit.getLatency()){
			
		}else{
			
		}
			*/
		
		if(functionalUnit.getLatency()!=1)
		functionalUnit.incrementTotalInstructions();
	}

	private static void issueInstruction() {
		// TODO Auto-generated method stub
		
		removeCompletedInstructions(false);
		for (int i = 0; i < issueWidth; i++) {
			
			if (!inputInstructions.isEmpty() && inputInstructions.size()>i) {
				Instruction instruction = inputInstructions.get(i);
				String instructionName = instruction.getInstructionName();
				Unit functionalUnit = getUnit(instructionName);
				if (instructionName.equalsIgnoreCase("LDM")) {
					int latencyOld = functionalUnit.getLatency();
					int latencyNew = latencyOld + DriverIQ.penaltyCacheMiss;
					functionalUnit.setLatency(latencyNew);
					arrayLSU=new Instruction[latencyNew];
				}
				
			if( isDependencyResolved(instruction) && isFunctionalUnitFree(functionalUnit)){
				inputInstructionsIssued.add(instruction);
				inputInstructionsIssuedLastCycle.add(instruction);
				functionalUnit.setFunctionalUnitFree(false);
				
				addInstructionsToFunctionalUnit(instruction, functionalUnit);
				
				destinationRegisters.put(instruction.getDestination(), false);
				addInstructionStatusInDumpAgain(instruction,"issue");


			}else{
				//addInstructionStatusInDumpAgain(instruction,"decode");
				addInstructionToIQ(instruction);
				addInstructionStatusInDumpAgain(instruction,"IssueQ");

				
			}
				
				
			} else {
				System.out.println("No more instructions to issue");
				
			}
			
		}
		inputInstructions.removeAll(inputInstructionsIssued);

		ALU.setFunctionalUnitFree(true);
		MUL.setFunctionalUnitFree(true);
		DIV.setFunctionalUnitFree(true);
		LSU.setFunctionalUnitFree(true);
		System.out.println("Cycle:- " + cycles);

		cycles++;

	}

	private static void addInstructionToIQ(Instruction instruction) {
		// TODO Auto-generated method stub
		if(!iq.contains(instruction))
		iq.add(instruction);
		
	}
	

}
