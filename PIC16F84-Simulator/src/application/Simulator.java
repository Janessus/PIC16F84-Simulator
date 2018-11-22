package application;

import java.util.ArrayList;
import java.util.List;

import gui.GUI_Main;

public class Simulator implements Runnable
{
	// Properties
	Registers registers;

	List<WrappedOperation> operations = new ArrayList<WrappedOperation>();

	int programCounter = 0;
	boolean skipProgramCounter = false;

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
			while(programCounter < operations.size()) { //TODO condition 
				// Execute current operation
				WrappedOperation currentOperation = operations.get(programCounter);
				
				currentOperation.getOperation().getCallbackFunction().execute(currentOperation.getArguments(), this);
				System.out.println("Executing " + currentOperation.getOperation().name() + " with param " + String.format("0x%02X", currentOperation.getArguments()));
				System.out.println("Program Counter: " + programCounter);
				System.out.println("W = " + String.format("0x%02X, ", registers.getWorking())
					+ "C = " + registers.getCarryFlag() + ", "
					+ "DC= " + registers.getDigitCarryFlag() + ", "
					+ "Z= " + registers.getZeroFlag());
				
				// Dont increment program counter for certain operations
				if(skipProgramCounter)
				{
					skipProgramCounter = false;
				} else {
					programCounter++;
				}
				
				// Pause thread if step mode
				if(GUI_Main.checkBoxStep.isSelected()) {
					try {
						this.wait();
						System.out.println("Pausing thread...");
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
		System.out.println("Simulation finished");
		System.out.println("W= " + String.format("0x%02X", registers.getWorking()));
	}

	public void addOperations(ArrayList<WrappedOperation> operations)
	{
		this.operations.addAll(operations);
	}
	
	// Operations implementation
	public void addwf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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

	public void andwf(byte val)
	{
		// TODO
	}

	public void clrf(byte val)
	{
		this.registers.setRegister(val, 0);
		this.registers.setZeroFlag(true);
	}

	public void clrw(byte val)
	{
		this.registers.setWorking((byte)0);
		this.registers.setZeroFlag(true);
	}

	public void comf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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

	public void decf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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

	public void decfsz(byte val)
	{
		// TODO
	}

	public void incf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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

	public void incfsz(byte val)
	{
		// TODO
	}

	public void iorwf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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

	public void movf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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

	public void movwf(byte val)
	{
		this.registers.setRegister(val, this.registers.getWorking());
	}

	public void nop(byte val)
	{
		// TODO
	}

	public void rlf(byte val)
	{
		// TODO
	}

	public void rrf(byte val)
	{
		// TODO
	}

	public void subwf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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
		registers.setDigitCarryFlag((0x00000001 & valRegisterF) - (0x00000001 & valRegisterw) > 0);
		registers.setZeroFlag(result==0);
		registers.setCarryFlag(result>0);
	}

	public void swapf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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

	public void xorwf(byte val)
	{
		byte d = (byte)(0b10000000 & val);
		byte f = (byte)(0b00000001 & val);
		
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

	public void bcf(byte val)
	{
		// TODO
	}

	public void bsf(byte val)
	{
		// TODO
	}

	public void btfsc(byte val)
	{
		// TODO
	}

	public void btfss(byte val)
	{
		// TODO
	}

	public void addlw(byte val)
	{
		int result = registers.getWorking() + val;
		registers.setDigitCarryFlag((0x00000001 & registers.getWorking()) + (0x00000001 & val) > 0xF);
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
		registers.setCarryFlag(result>0xFF);
	}

	public void andlw(byte val)
	{
		int result = registers.getWorking() & val;
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
	}

	public void call(byte val)
	{
		// Add return point to stack
		this.stack.add((int)this.programCounter);
		this.programCounter = val;
		this.skipProgramCounter = true;
	}

	public void clrwdt(byte val)
	{
		// TODO
	}

	public void goTo(byte val)
	{
		this.programCounter = val;
		this.skipProgramCounter = true;
	}

	public void iorlw(byte val)
	{
		int result = registers.getWorking() | val;
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
	}

	public void movlw(byte val)
	{
		registers.setWorking(val);
	}

	public void retfie(byte val)
	{
		// TODO
	}

	public void retlw(byte val)
	{
		// TODO
	}

	public void reTurn(byte val)
	{
		// Get programCounter from stack
		this.programCounter = stack.remove(stack.size() -1);
	}

	public void sleep(byte val)
	{
		// TODO
	}

	public void sublw(byte val)
	{
		int result = val - registers.getWorking();
		registers.setDigitCarryFlag((0x00000001 & val) - (0x00000001 & registers.getWorking()) > 0);
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
		registers.setCarryFlag(result>0);
	}

	public void xorlw(byte val)
	{
		int result = registers.getWorking() ^ val;
		registers.setWorking((byte) result);
		registers.setZeroFlag(result==0);
	}
}
