package application;

public class Registers
{
	//int[] Indirect_Addr, TMR0, PCL, STATUS, FSR, PORTA, PORTB, EEDATA, EEADR, PCLATH, INTCON = new int[8];	//Bank0
	//int[] Indirect_Addr, OPTION, PCL, STATUS, FSR, TRISA, TRISB, EECON1, EECON2, PCLATH, INTCON = new int[8];	//Bank1
	
	private int bank0[] = new int[80];
	private int bank1[] = new int[80];
	
	// Properties
	private int working;
	
	// Methods
	public void init() {
		
	}
	
	public int getWorking() {
		return working;	
	}
	
	public void setWorking(byte val) {
		working = val;
	}
	
	public int readRegister(int address)
	{
		//TODO
		return address;
	}
	
	public void setRegister(int address, int value)
	{
		//TODO
	}
	
	public int readBit(int address, int pos)
	{
		//TODO
		return 0;
	}
	
	public void setBit(int address, int pos, int value)
	{
		//TODO
	}
	
}