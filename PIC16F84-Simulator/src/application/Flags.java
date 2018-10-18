package application;

public class Flags
{
	private boolean carry;
	private boolean digitCarry;
	private boolean zero;
	
	public void setCarry(boolean val) {
		carry = val;
	}
	public boolean getCarry() {
		return carry;
	}
	public void setDigitCarry(boolean val) {
		digitCarry = val;
	}
	public boolean getDigitCarry() {
		return digitCarry;
	}
	public void setZero(boolean val) {
		zero = val;
	}
	public boolean getZero() {
		return zero;
	}
	
	
}
