package application;

public class Registers
{
	//int[] Indirect_Addr, TMR0, PCL, STATUS, FSR, PORTA, PORTB, EEDATA, EEADR, PCLATH, INTCON = new int[8];	//Bank0
	//int[] Indirect_Addr, OPTION, PCL, STATUS, FSR, TRISA, TRISB, EECON1, EECON2, PCLATH, INTCON = new int[8];	//Bank1
	
	// Properties
	private byte working;
	
	// Methods
	public void init() {
		
	}
	
	public byte getWorking() {
		return working;	
	}
	public void setWorking(byte val) {
		working = val;
	}
}
