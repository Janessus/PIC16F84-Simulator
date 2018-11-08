package application;

import java.util.ArrayList;
import java.util.List;

public class Simulator
{
	// Properties
	Registers registers;

	Flags flag;

	List<WrappedOperation> operations = new ArrayList<WrappedOperation>();

	int programCounter = 0;

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
		while(programCounter < operations.size()) {
			// Execute current operation
			WrappedOperation currentOperation = operations.get(programCounter);
			
			currentOperation.getOperation().getCallbackFunction().execute(currentOperation.getArguments(), this);
			System.out.println("Executed operation + programCounter" );
			System.out.println("Working register: " + registers.getWorking());
			programCounter++;
		}
		System.out.println("Simulation finished");
		System.out.println("Working register: " + registers.getWorking());
	}

	public void addOperations(ArrayList<WrappedOperation> operations)
	{
		this.operations.addAll(operations);
	}

	//
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
		registers.setWorking((byte) (registers.getWorking() + val));
	}

	public void andlw(byte val)
	{
		registers.setWorking((byte) (registers.getWorking() & val));
	}

	public void call(byte val)
	{
		this.programCounter = val;
		this.stack.add((int)val);
	}

	public void clrwdt(byte val)
	{
		// TODO
	}

	public void goTo(byte val)
	{
		this.programCounter = val;
	}

	public void iorlw(byte val)
	{
		registers.setWorking((byte) (registers.getWorking() | val));
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
	}

	public void sleep(byte val)
	{
		// TODO
	}

	public void sublw(byte val)
	{
		registers.setWorking((byte) (val - registers.getWorking()));
	}

	public void xorlw(byte val)
	{
		registers.setWorking((byte) (registers.getWorking() ^ val));
	}
}
