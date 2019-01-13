package application;

public class WrappedOperation
{
	private Operation operation;
	private int arguments;
	private int lineNumber;
	
	public WrappedOperation(Operation operation, int arguments, int lineNumber) {
		this.operation = operation;
		this.arguments = arguments;
		this.lineNumber = lineNumber;
	}
	
	public Operation getOperation() {
		return this.operation;
	}
	public int getArguments() {
		return this.arguments;
	}
	public int getLineNumber() {
		return this.lineNumber;
	}
}
