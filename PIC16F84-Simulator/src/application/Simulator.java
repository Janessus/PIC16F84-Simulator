package application;

public class Simulator
{
	// Properties
	Registers registers;
	Flags flag;
	
	// Methods
	public void init() {
		registers = new Registers();
		registers.init();
	}
	
	public void movlw(byte val) {
		registers.setWorking(val);
	}
	public void andlw(byte val) {
		registers.setWorking((byte) (registers.getWorking() & val));
	}
	public void iorlw(byte val) {
		registers.setWorking((byte) (registers.getWorking() | val));
	}
	public void sublw(byte val) {
		registers.setWorking((byte) (val - registers.getWorking()));
	}
	public void xorlw(byte val) {
		registers.setWorking((byte) (registers.getWorking() ^ val));
	}
	public void addlw(byte val) {
		registers.setWorking((byte) (registers.getWorking() + val));
	}
	
}
