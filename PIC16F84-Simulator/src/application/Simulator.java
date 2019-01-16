package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import gui.CodePanel;
import gui.GUI_Main;
import javafx.application.Platform;
import javafx.scene.Node;

public class Simulator implements Runnable
{
	// Properties
	// TODO: fix MCLR reset
	public Registers registers;

	OperationList operationList;
	public WrappedOperation currentOperation; 

	public static int programCounter = 0;
	int instructionCycles = 0;
	int frequency = 4; // frequency in mhz
	int wdtCounter = 0; // cycle when the wdt was cleared 
	int skipTmr0Increments = 0;
	
	public boolean isSleep = false;
	boolean skipProgramCounter = false;
	boolean skipNextInstruction = false;
	boolean isStopThread = false;
	boolean interruptWakeup = false;

	//States for pins(int state)
	int[] pinStates = new int[17];
	public static final int PIN_DEFAULT = 0;
	public static final int PIN_RISING = 1;
	public static final int PIN_FALLING = 2;
	
	
	List<Integer> stack = new ArrayList<Integer>();

	public Simulator()
	{
		init();
	}

	// Methods
	public void init()
	{
		registers = new Registers();
		registers.powerOn();
	}

	@Override
	public void run() {
		
		synchronized(this) {
			
			programCounter = 0;
			System.out.println("Operations: " + operationList.getProgramMemory().size());
			while(!isStopThread) {
				if(!isSleep) {
					// Check for interrupt wakeup from sleep last cycle
					if(interruptWakeup) {
						// Add return point to stack
						this.stack.add((int)Simulator.programCounter);
						
						// Jump to ISR
						Simulator.programCounter = 0x4;
						
					}
					
					// Interrupt handling
					if(!skipNextInstruction) {
						// 1. Set Interrupt flags in INTCON
						// INTF
						if(pinStates[5]!=PIN_DEFAULT) {
							if(pinStates[5]==PIN_RISING) {
								if(registers.readBit(1, Registers.OPTION, 6)==1);
								{
									registers.setBit(0, Registers.INTCON, 1, true);
								}
							} else if(registers.readBit(1, Registers.OPTION, 5)==1);
							{
								registers.setBit(0, Registers.INTCON, 1, true);
							}
							
							// Reset
							pinStates[5]=PIN_DEFAULT;
						}
						// RBIF
						// TODO: check if port is set to read
						for(int i=9; i<13; i++) {
							if(pinStates[i]!=PIN_DEFAULT) {
								registers.setBit(0, Registers.INTCON, 0, true);
								pinStates[i]=PIN_DEFAULT;
							}
						}
						
						// 2. Check INTCON interrupt flags + enable
						if((registers.readBit(0, Registers.INTCON, 1)==1 && registers.readBit(0, Registers.INTCON, 4)==1) // INT interrupt
								|| (registers.readBit(0, Registers.INTCON, 2)==1 && registers.readBit(0, Registers.INTCON, 5)==1) // TMR0 interrupt
								|| (registers.readBit(0, Registers.INTCON, 0)==1 && registers.readBit(0, Registers.INTCON, 3)==1)) { // RB interrupt
							
							// Check GIE
							if(registers.readBit(0, Registers.INTCON, 7)==1) {
								if(isSleep) {
									interruptWakeup = true; // Set this flag to execute one more operation, then execute ISR
									
									// Status bits
									registers.setBit(0, registers.STATUS, 3, false);
									
									registers.setBit(0, registers.STATUS, 4, true);
								} else {
									// Add return point to stack
									this.stack.add((int)Simulator.programCounter);
									
									// Jump to ISR
									Simulator.programCounter = 0x4;
								}
								// Clear GIE bit so program doesnt get stuck
								registers.setBit(0, Registers.INTCON, 7, false);
							}
						} else if(isSleep) {
							// Just wake up without jumping to ISR
							isSleep = false;
							// Status bits
							registers.setBit(0, registers.STATUS, 3, false);
							
							registers.setBit(0, registers.STATUS, 4, true);
						}
					}
					
					currentOperation = operationList.getOperationAtAddress(programCounter);
					
					int lineNumber = currentOperation.getLineNumber();
					GUI_Main.highlightLine(lineNumber);
					
					// Pause thread if step mode or breakpoint
					if(!skipNextInstruction && (GUI_Main.checkBoxStep.isSelected() || currentOperation.hasBreakPoint)) {
						try {
							// Scroll to correct line
							Node operationNode = CodePanel.codePane.getChildren().get(lineNumber-1);
							Platform.runLater(() -> CodePanel.pane.setVvalue(operationNode.getBoundsInParent().getMaxY() / CodePanel.pane.getContent().getBoundsInLocal().getHeight()));
							// Pause thread
							System.out.println("Pausing thread...");
							this.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
					// Skip instruction for DECFSZ,INCFSZ etc
					if(!skipNextInstruction) {
						// Execute current operation
						currentOperation.getOperation().getCallbackFunction().execute(currentOperation.getArguments(), this);
						GUI_Main.log("Executing " + currentOperation.getOperation().name() + " with param " + String.format("0x%02X", currentOperation.getArguments()));
						GUI_Main.log("Program Counter: " + programCounter);
						GUI_Main.log("W = " + String.format("0x%02X, ", registers.getWorking())
							+ "C = " + registers.getCarryFlag() + ", "
							+ "DC= " + registers.getDigitCarryFlag() + ", "
							+ "Z= " + registers.getZeroFlag());
					} else {
						this.nop(0);
						skipNextInstruction = false;
					}
					increaseInstructionCycles();
					
					// Dont increment program counter for certain operations
					if(skipProgramCounter)
					{
						skipProgramCounter = false;
					} else {
						// Wraparound
						programCounter = programCounter >= 0x3FF ? 0 : programCounter+1;
						registers.setRegisterDirectly(0, Registers.PCL, programCounter & 0b11111111);
					}
				}
				// Check WDT
				if(GUI_Main.checkBoxWdt.isSelected()) {
					wdtCounter++;
					
					// Calculate time
					int timePassed = wdtCounter * 4/frequency; // time passed in micro sec
					int wdtTime = 18000; // 18ms = 18000micro sec
					
					// Check prescaler assignment
					if(registers.readBit(1, Registers.OPTION, 3)==1) {
						int prescaler = registers.readRegister(1, Registers.OPTION) & 0b00000111;
						wdtTime *= Math.pow(2, prescaler);
					}
					if(timePassed>=wdtTime) {
						if(isSleep) {
							// Wakeup
							isSleep = false;
							
							// Clear PD bit
							registers.setBit(1, Registers.STATUS, 3, false);
							
							// Clear TO bit
							registers.setBit(1, Registers.STATUS, 4, false);
							
							
							GUI_Main.log("WDT triggered wakeup!");
						} else {
							registers.reset(Registers.WDT_RESET);
							
							GUI_Main.log("WDT triggered reset!");
						}
					}
				}
			}
		}
	}

	public void loadOperationList(OperationList operationList)
	{
		this.operationList = operationList;
	}
	
	// Operations implementation
	public void addwf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Calculate addition
		int valRegisterF = this.registers.readRegister(f);
		int valRegisterw = this.registers.getWorking();
		
		int result = valRegisterF + valRegisterw;
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		// Set status
		registers.setDigitCarryFlag((0x00000001 & valRegisterF) + (0x00000001 & valRegisterw) > 0xF);
		registers.setZeroFlag(result==0);
		registers.setCarryFlag(result>0xFF);
		
		registers.setDigitCarryFlag((0x00000001 & valRegisterF) + (0x00000001 & valRegisterw) > 0xF);
	}

	public void andwf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Calculate logical AND
		int result = this.registers.readRegister(f) & this.registers.getWorking();
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
	}

	public void clrf(int val)
	{
		this.registers.setRegister(val, 0);
		this.registers.setZeroFlag(true);
	}

	public void clrw(int val)
	{
		this.registers.setWorking((byte)0);
		this.registers.setZeroFlag(true);
	}

	public void comf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Build complement
		int result = ~ this.registers.readRegister(f);
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
	}

	public void decf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Build decrement
		int result = this.registers.readRegister(f) - 1;
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
	}

	public void decfsz(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Build decrement
		int result = this.registers.readRegister(f) - 1;
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
		this.skipNextInstruction = result == 0;
	}

	public void incf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Build increment
		int result = this.registers.readRegister(f) + 1;
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
	}

	public void incfsz(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Build increment
		int result = (this.registers.readRegister(f) + 1)%256;
		
		// Write to correct register
		if(d == 1) {
			System.out.println("saving " + result);
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
		this.skipNextInstruction = result == 0;
	}

	public void iorwf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Calculate inclusive OR
		int result = this.registers.readRegister(f) | this.registers.getWorking();
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
	}

	public void movf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Get register
		int result = this.registers.readRegister(f);
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
	}

	public void movwf(int val)
	{
		this.registers.setRegister(val, this.registers.getWorking());
	}

	public void nop(int val)
	{
		// no Command
	}

	public void rlf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Calculate left rotation
		int valRegisterF = this.registers.readRegister(f);
		
		int result = (valRegisterF << 1);
		if(registers.getCarryFlag()) {
			result++;
		}
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		// Set carry flag
		registers.setCarryFlag((0b10000000 & valRegisterF)>>7 == 1);
	}

	public void rrf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Calculate right rotation
		int valRegisterF = this.registers.readRegister(f);
		boolean valCarry = registers.getCarryFlag();
		
		// Set carry flag
		registers.setCarryFlag((0b00000001 & valRegisterF) == 1);
		
		int result = (valRegisterF >> 1);
		if(valCarry) {
			result += 0b10000000;
		}
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
	}

	public void subwf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Calculate substraction
		int valRegisterF = this.registers.readRegister(f);
		int valRegisterw = this.registers.getWorking();
		
		int result = valRegisterF - valRegisterw;
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		// Set status
		registers.setDigitCarryFlag((0x00000001 & valRegisterF) >= (0x00000001 & valRegisterw));
		registers.setZeroFlag(result==0);
		registers.setCarryFlag(result>0);
	}

	public void swapf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Get register value and swap nibbles
		int result = this.registers.readRegister(f);
		result = ((result & 0x0F)<<4 | (result & 0xF0)>>4);
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
	}

	public void xorwf(int val)
	{
		byte d = (byte)((0b10000000 & val) >> 7);
		byte f = (byte)(0b01111111 & val);
		
		// Calculate inclusive OR
		int result = this.registers.readRegister(f) ^ this.registers.getWorking();
		
		// Write to correct register
		if(d == 1) {
			this.registers.setRegister(f, result);
		} else {
			this.registers.setWorking((byte)result);
		}
		
		registers.setZeroFlag(result==0);
	}

	public void bcf(int val)
	{
		byte b = (byte)((0b1110000000 & val) >> 7);
		byte f = (byte)(0b00001111111 & val);
		
		// Write to correct register
		this.registers.setBit(f, b, false);
	}

	public void bsf(int val)
	{
		byte b = (byte)((0b1110000000 & val) >> 7);
		byte f = (byte)(0b00001111111 & val);
		
		// Write to correct register
		this.registers.setBit(f, b, true);
	}

	public void btfsc(int val)
	{
		byte b = (byte)((0b1110000000 & val) >> 7);
		byte f = (byte)(0b00001111111 & val);
		
		// Get register
		int result = this.registers.readBit(f, b);
		
		if(result==0) {
			this.skipNextInstruction = true;
		}
	}

	public void btfss(int val)
	{
		byte b = (byte)((0b1110000000 & val) >> 7);
		byte f = (byte)(0b00001111111 & val);
		
		// Get register
		int result = this.registers.readBit(f, b);
		
		if(result==1) {
			this.skipNextInstruction = true;
		}
	}

	public void addlw(int val)
	{
		int result = registers.getWorking() + val;
		registers.setDigitCarryFlag((0x00000001 & registers.getWorking()) + (0x00000001 & val) > 0xF);
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
		registers.setCarryFlag(result>0xFF);
	}

	public void andlw(int val)
	{
		int result = registers.getWorking() & val;
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
	}

	public void call(int val)
	{
		// Add return point to stack
		this.stack.add((int)Simulator.programCounter);
		
		// Shift 8 more bits so there is 11 bits free for the argument
		int upperPc = (registers.readRegister(Registers.PCLATH) & 0b11000) << 8;
		Simulator.programCounter = val | upperPc;
		registers.setRegisterDirectly(0, Registers.PCL, programCounter & 0b11111111);
		
		this.skipProgramCounter = true;
		
		// Additional instruction cycle
		increaseInstructionCycles();
	}

	public void clrwdt(int val)
	{
		wdtCounter = 0;
		
		// Clear prescaler if its assigned to wdt
		if(registers.readBit(1, Registers.OPTION, 3)==1) {
			for(int i=0; i<3; i++) {
				registers.setBit(1, Registers.OPTION, i, false);
			}
		}
		
		// Set PD bit
		registers.setBit(1, Registers.STATUS, 3, true);
		
		// Set TO bit
		registers.setBit(1, Registers.STATUS, 4, true);
	}

	public void goTo(int val)
	{
		// Shift 8 more bits so there is 11 bits free for the argument
		int upperPc = (registers.readRegister(Registers.PCLATH) & 0b11000) << 8;
		Simulator.programCounter = val | upperPc;
		registers.setRegisterDirectly(0, Registers.PCL, programCounter & 0b11111111);
		this.skipProgramCounter = true;
		
		// Additional instruction cycle
		increaseInstructionCycles();
	}

	public void iorlw(int val)
	{
		int result = registers.getWorking() | val;
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
	}

	public void movlw(int val)
	{
		registers.setWorking((byte)val);
	}

	public void retfie(int val)
	{
		// Set GIE bit again
		registers.setBit(0, Registers.INTCON, 7, true);
		
		// Get programCounter from stack
		Simulator.programCounter = stack.remove(stack.size() -1);
			
		// Additional instruction cycle
		increaseInstructionCycles();
	}

	public void retlw(int val)
	{
		registers.setWorking((byte) val);
		Simulator.programCounter = stack.remove(stack.size() -1);
		
		// Additional instruction cycle
		increaseInstructionCycles();
	}

	public void reTurn(int val)
	{
		// Get programCounter from stack
		Simulator.programCounter = stack.remove(stack.size() -1);
		
		// Additional instruction cycle
		increaseInstructionCycles();
	}

	public void sleep(int val)
	{
		wdtCounter = 0;
		
		// Clear prescaler if its assigned to wdt
		if(registers.readBit(1, Registers.OPTION, 3)==1) {
			for(int i=0; i<3; i++) {
				registers.setBit(1, Registers.OPTION, i, false);
			}
		}
		
		// Clear PD bit
		registers.setBit(1, Registers.STATUS, 3, false);
		
		// Set TO bit
		registers.setBit(1, Registers.STATUS, 4, true);

		GUI_Main.getApp().gui.log("Sleeping...");
		
		isSleep = true;
	}

	public void sublw(int val)
	{
		int result = val - registers.getWorking();
		registers.setDigitCarryFlag((0x00000001 & val) >= (0x00000001 & registers.getWorking()));
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
		registers.setCarryFlag(result>0);
	}

	public void xorlw(int val)
	{
		int result = registers.getWorking() ^ val;
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
	}
	
	public void setBreakpoint(int lineNumber, boolean val) {
		WrappedOperation operation = operationList.getOperationAtLine(lineNumber);
		if(operation!=null) {
			operation.hasBreakPoint = val;
		}
	}

	public Boolean hasBreakpoint(int lineNumber) {
		WrappedOperation operation = operationList.getOperationAtLine(lineNumber);
		if(operation==null) {
			return null;
		}
		return operation.hasBreakPoint;
	}
	
	public void setProgramCounter(int pc) {
		Simulator.programCounter = pc;
	}
	public void increaseInstructionCycles() {
		this.instructionCycles++;
		Platform.runLater(() -> GUI_Main.updateInstructionCycles());

		if(skipTmr0Increments>0) {
			skipTmr0Increments--;
		} else {
			// Check if T0CS is set
			if(registers.readBit(1, Registers.OPTION, 5)==0) {
				tmr0Tick();
			} else  { // Check for RA4 impulse
				// Check T0SE for source edge
				int t0se = registers.readBit(1, Registers.OPTION, 5);
				if(t0se==0 && pinStates[2]==PIN_RISING) {
					tmr0Tick();
					pinStates[2]=PIN_DEFAULT;
				} else if(t0se==0 && pinStates[2]==PIN_RISING) {
					tmr0Tick();
					pinStates[2]=PIN_DEFAULT;
				}
			}
		}
	}
	// Increment tmr0, respecting the prescaler
	public void tmr0Tick() {
		int val = registers.readRegister(0, Registers.TMR0);
		int increment = 1;
		
		// Check prescaler assignment
		if(registers.readBit(1, Registers.OPTION, 3)==0) {
			int prescaler = registers.readRegister(1, Registers.OPTION) & 0b00000111;
			increment *= Math.pow(2, prescaler+1);
		}
		int result = val + increment;
		
		//  Overflow
		if(result>0xFF) {
			result = result % 256;
			registers.setBit(0, Registers.INTCON, 2, true);
		}
		
		registers.setRegisterDirectly(0, Registers.TMR0, result);
	}
	// Inhibits TMR0 incrementing in timer mode; should be used in registers when TMR0 gets written
	public void inhibitTmr0Increment(int cycles) {
		skipTmr0Increments = cycles;
	}
	public void stopThread() {
 		isStopThread = true; 
	}
	public OperationList getOperationList() {
		return operationList;
	}
	public int getInstrouctionCycleCount() {
		return instructionCycles;
	}
	public void setPinState(int pin, int state) {
		pinStates[pin] = state;
	}
}
