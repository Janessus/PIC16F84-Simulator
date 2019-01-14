package application;

import java.util.LinkedHashMap;

public class OperationList
{
	// Map address -> operation
	LinkedHashMap<Integer, WrappedOperation> programMemory = new LinkedHashMap<Integer, WrappedOperation>();
	
	// Map lineNumber -> operation
	LinkedHashMap<Integer, WrappedOperation> lines = new LinkedHashMap<Integer, WrappedOperation>();
	
	public OperationList(LinkedHashMap<Integer, WrappedOperation> programMemory, LinkedHashMap<Integer, WrappedOperation> lines) {
		this.programMemory = programMemory;
		this.lines = lines;
	}
	public LinkedHashMap<Integer, WrappedOperation> getLines() {
		return this.lines;
	}
	public LinkedHashMap<Integer, WrappedOperation> getProgramMemory() {
		return this.programMemory;
	}
	public WrappedOperation getOperationAtLine(int lineNumber) {
		return this.lines.get(lineNumber);
	}
	public WrappedOperation getOperationAtAddress(int address) {
		return this.programMemory.get(address);
	}
}
