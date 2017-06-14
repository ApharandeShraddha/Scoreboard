# Scoreboard ( Programming Language used - JAVA)

This is an implementation of scoreboard for a CPU. A scoreboard is a centralized data
structure that keeps track of where every executing instruction is in any functional unit pipeline.

Project Description:
The scoreboard will track the cycle-by-cycle progress of the following three types of functional
units. All subclasses of a base class called Unit . When instantiating one of these units, you
should provide a latency and a name for debugging purposes. 
1. Pipelined: Fully pipelined with N pipeline stages, which can have up to N instructions in
different phases of execution. Example: 4-stage pipelined multiplier unit.
2. NonPipelined: Non-pipelined, where only one instruction can execute at a time and
requires N cycles to complete. Example: Divider unit.
3. PipelinedVariable: Pipelined with uncertain delay. A minimum delay of N cycles is
specified, and N instructions can be in flight, but the pipeline may experience
unexpected stalls. A unit of this type will report an unknown completion time until M
cycles before completion. Example: Load unit.

Sample Input:(text file)
DUMP 15 20
ISSUEWIDTH 4
CACHEMISS 10
LDH F6 R2 34
LDM F2 R3 45
MUL F2 F4 F0
SUB F8 F6 F2
DIV F10 F0 F6
ADD F6 F8 F2
DIV F0 F2 F4
ADD F6 F0 F8
ST F6 R1 0
SUB F8 F10 F4
MUL F6 F10 F8

Sample Ouput:
Cycle 0 dispatch: LDH F6 R2 34
Cycle 1 issue: LDH F6 R2 34
Cycle 1 dispatch: LDM F2 R3 45
Cycle 2 issue: LDM F2 R3 45
Cycle 2 dispatch: MUL F2 F4 F0
Cycle 3 dispatch: SUB F8 F6 F2
Cycle 4 dispatch: DIV F10 F0 F6
Cycle 5 complete: LDH F6 R2 34
Cycle 5 issue: DIV F10 F0 F6
Cycle 5 dispatch: ADD F6 F8 F2
Cycle 6 dispatch: DIV F0 F2 F4
Cycle 7 dispatch: ADD F6 F0 F8
Cycle 8 issue: ADD F6 F0 F8
Cycle 8 dispatch: ST F6 R1 0
Cycle 9 complete: ADD F6 F0 F8
Cycle 9 issue: ST F6 R1 0
Cycle 9 dispatch: SUB F8 F10 F4
Cycle 10 dispatch: MUL F6 F10 F8
Cycle 12 complete: LDM F2 R3 45
Cycle 12 issue: MUL F2 F4 F0
Cycle 13 complete: DIV F10 F0 F6
Cycle 13 complete: ST F6 R1 0


Implementation Details:
addUnit -> It will add all execution units to scoreboard
issueInstruction -> It will issue instructions in every cycle equaly to issueBandwidth
CompletionTime -> It will give how many more cycles you need to free perticular registers
advanceClock -> It will increase cycles.
Dump -> It will print Output of cycles 

Please follow below steps to run code.
1. Execute make file to create executable files
2.Add input file in bin folder.
3. Execute following commands
   1.For INORDER Execution
   	cd bin
   	java Driver 
   2. For OUT of ORDER Execution
    	cd bin
   	java DriverIQ  
