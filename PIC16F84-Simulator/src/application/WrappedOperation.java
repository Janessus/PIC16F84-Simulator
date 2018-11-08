package application;

public class WrappedOperation
{
	private Operation operation;
	private int arguments;
	
	public WrappedOperation(Operation operation, int arguments) {
		this.operation = operation;
		this.arguments = arguments;
	}
	
	public Operation getOperation() {
		return this.operation;
	}
	public int getArguments() {
		return this.arguments;
	}
}
