package application;

import java.util.ArrayList;
import java.util.List;

public class Simulator
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

	public void run() {
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
			
			if(skipProgramCounter)
			{
				skipProgramCounter = false;
			} else {
				programCounter++;
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
		
	}

	public void andwf(byte val)
	{
		// TODO
	}

	public void clrf(byte val)
	{
		// TODO
	}

	public void clrw(byte val)
	{
		// TODO
	}

	public void comf(byte val)
	{
		// TODO
	}

	public void decf(byte val)
	{
		// TODO
	}

	public void decfsz(byte val)
	{
		// TODO
	}

	public void incf(byte val)
	{
		// TODO
	}

	public void incfsz(byte val)
	{
		// TODO
	}

	public void iorwf(byte val)
	{
		// TODO
	}

	public void movf(byte val)
	{
		// TODO
	}

	public void movwf(byte val)
	{
		// TODO
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
		// TODO
	}

	public void swapf(byte val)
	{
		// TODO
	}

	public void xorwf(byte val)
	{
		// TODO
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
		this.programCounter = val;
		this.stack.add((int)val);
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
		// TODO
		this.skipProgramCounter = true;
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
