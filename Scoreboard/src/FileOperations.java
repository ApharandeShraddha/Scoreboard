import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FileOperations {
	
	

	//parse input file
	public void readFile(){
		
		
		try {
			
			BufferedReader br = new BufferedReader(new FileReader("Input1.txt"));
			String line = br.readLine();
			 while (line != null && !line.isEmpty()) {
				if(line.contains("ISSUEWIDTH")){
					String splitArray[] = line.split(" ");
					Driver.issueWidth=Integer.parseInt(splitArray[1]);
				
				}
				if(line.contains("DUMP")){
					String splitArray[] = line.split(" ");
					Driver.dumpFirstOutputCycle=Integer.parseInt(splitArray[1]);
					Driver.dumpSecondOutputCycle=Integer.parseInt(splitArray[2]);
					
				}
				if(line.contains("CACHEMISS")){
					String splitArray[] = line.split(" ");
					Driver.penaltyCacheMiss=Integer.parseInt(splitArray[1]);
					
					
				}
				Instruction instruction =new Instruction();
				String splitArray[] = line.split(" ");
				instruction.setInstructionName( splitArray[0]);
				if (instruction.getInstructionName().equalsIgnoreCase("ST")) {
					instruction.setInstructionName("ST");
					instruction.setSr1(splitArray[1]);
					instruction.setDestination(splitArray[2]);
					instruction.setOffSet(Integer.parseInt(splitArray[3]));
					Driver.insertEntry(instruction);
					//Driver.destinationRegisters.put(splitArray[2], false);
				} else if (instruction.getInstructionName().equalsIgnoreCase("LDH")) {
					instruction.setInstructionName("LDH");
					instruction.setDestination( splitArray[1]);
					instruction.setSr1( splitArray[2]);
					instruction.setOffSet(Integer.parseInt(splitArray[3]));
					
					Driver.insertEntry(instruction);
					//Driver.destinationRegisters.put(splitArray[1], false);
				}else if (instruction.getInstructionName().equalsIgnoreCase("LDM")) {
					instruction.setInstructionName("LDM");
					instruction.setDestination( splitArray[1]);
					instruction.setSr1( splitArray[2]);
					instruction.setOffSet(Integer.parseInt(splitArray[3]));
					Driver.insertEntry(instruction);
					//Driver.destinationRegisters.put(splitArray[1], false);
				} else if ((instruction.getInstructionName().equalsIgnoreCase("ADD")) || (instruction.getInstructionName().equalsIgnoreCase("SUB"))|| (instruction.getInstructionName().equalsIgnoreCase("MUL")) || (instruction.getInstructionName().equalsIgnoreCase("DIV")))  {
					instruction.setInstructionName(instruction.getInstructionName());
					instruction.setDestination(splitArray[1]);
					instruction.setSr1(splitArray[2]);
					instruction.setSr2(splitArray[3]);
					Driver.insertEntry(instruction);
					//Driver.destinationRegisters.put(splitArray[1], false);
				}
				
				
				//Driver.insertEntry(instruction);
				
				
				line=br.readLine();
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}

}
