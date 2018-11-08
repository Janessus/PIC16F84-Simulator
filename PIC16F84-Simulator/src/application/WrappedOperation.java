package application;

public class WrappedOperation
{
	private Operations operation;
	private int arguments;
	
	public WrappedOperation(Operations operation, int arguments) {
		this.operation = operation;
		this.arguments = arguments;
	}
	
	public Operations getOperation() {
		return this.operation;
	}
	public int getParams() {
		return this.arguments;
	}
}
