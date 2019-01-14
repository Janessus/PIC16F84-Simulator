package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import gui.CodePanel;
import gui.GUI_Main;
import javafx.scene.Node;

public class Simulator implements Runnable
{
	// TODO: implement PCL/PCLATH ?
	// TODO: runtime counter
	// TODO: fix opening multiple files or pressing run multiple times
	// Properties
	public Registers registers;

	OperationList operationList;

	int programCounter = 0;
	boolean skipProgramCounter = false;
	boolean skipNextInstruction = false;

	List<Integer> stack = new ArrayList<Integer>();

	public Simulator()
	{
		init();
	}

	// Methods
	public void init()
	{
		registers = new Registers();
		registers.init();
	}

	@Override
	public void run() {
		
		synchronized(this) {
			
			programCounter = 0;
			System.out.println("Operations: " + operationList.getProgramMemory().size());
			while(true) {
				// TODO: bad performance, maybe fix
				// WrappedOperation currentOperation = (WrappedOperation) operations.values().toArray()[programCounter];
				WrappedOperation currentOperation = operationList.getOperationAtAddress(programCounter);
				
				// Remove highlighting for old nodes
				Set<Node> lastNodes = CodePanel.pane.lookupAll(".current-operation");
				for(Node node:lastNodes) {
					node.getStyleClass().removeAll(Collections.singleton("current-operation"));
				}
				
				// Highlight current line in codepanel
				GUI_Main.codePanel.lineNumbers.getChildren().get(currentOperation.getLineNumber()).getStyleClass().add("current-operation");
				Node operationNode = CodePanel.codePane.getChildren().get(currentOperation.getLineNumber()-1);
				operationNode.getStyleClass().add("current-operation");
				
				// Pause thread if step mode
				if(!skipNextInstruction && (GUI_Main.checkBoxStep.isSelected() || currentOperation.hasBreakPoint)) {
					try {
						// Scroll to correct line
						CodePanel.pane.setVvalue(operationNode.getBoundsInParent().getMaxY() / CodePanel.pane.getContent().getBoundsInLocal().getHeight());
						// Pause thread
						this.wait();
						System.out.println("Pausing thread...");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				// Skip instruction for DECFSZ,INCFSZ etc
				if(!skipNextInstruction) {
					// Execute current operation
					currentOperation.getOperation().getCallbackFunction().execute(currentOperation.getArguments(), this);
					GUI_Main.getApp().gui.log("Executing " + currentOperation.getOperation().name() + " with param " + String.format("0x%02X", currentOperation.getArguments()));
					GUI_Main.getApp().gui.log("Program Counter: " + programCounter);
					GUI_Main.getApp().gui.log("W = " + String.format("0x%02X, ", registers.getWorking())
						+ "C = " + registers.getCarryFlag() + ", "
						+ "DC= " + registers.getDigitCarryFlag() + ", "
						+ "Z= " + registers.getZeroFlag());
				} else {
					skipNextInstruction = false;
				}
				
				
				// Dont increment program counter for certain operations
				if(skipProgramCounter)
				{
					skipProgramCounter = false;
				} else {
					// Wraparound
					programCounter = programCounter >= 0x3FF ? 0 : programCounter+1;
					registers.setPclDirectly(programCounter & 0b11111111);
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
		int result = this.registers.readRegister(f) + 1;
		
		// Write to correct register
		if(d == 1) {
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
		this.stack.add((int)this.programCounter);
		
		// Shift 8 more bits so there is 11 bits free for the argument
		int upperPc = (registers.readRegister(Registers.PCLATH) & 0b11000) << 8;
		this.programCounter = val | upperPc;
		registers.setPclDirectly(programCounter & 0b11111111);
		
		this.skipProgramCounter = true;
	}

	public void clrwdt(int val)
	{
		// TODO
	}

	public void goTo(int val)
	{
		// Shift 8 more bits so there is 11 bits free for the argument
		int upperPc = (registers.readRegister(Registers.PCLATH) & 0b11000) << 8;
		this.programCounter = val | upperPc;
		registers.setPclDirectly(programCounter & 0b11111111);
		this.skipProgramCounter = true;
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
		// TODO
	}

	public void retlw(int val)
	{
		registers.setWorking((byte) val);
		this.programCounter = stack.remove(stack.size() -1);
	}

	public void reTurn(int val)
	{
		// Get programCounter from stack
		this.programCounter = stack.remove(stack.size() -1);
	}

	public void sleep(int val)
	{
		// TODO
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
		this.programCounter = pc;
	}
}
