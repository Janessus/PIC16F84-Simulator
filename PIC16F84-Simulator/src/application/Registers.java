package application;

public class Registers
{
	//int[] Indirect_Addr, TMR0, PCL, STATUS, FSR, PORTA, PORTB, EEDATA, EEADR, PCLATH, INTCON = new int[8];	//Bank0
	//int[] Indirect_Addr, OPTION, PCL, STATUS, FSR, TRISA, TRISB, EECON1, EECON2, PCLATH, INTCON = new int[8];	//Bank1

	private int bank0[] = new int[128];
	private int bank1[] = new int[128];

	
	
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
	
	public int readRegister(int bank, int address)
	{
		return banks[bank][address];
	}
	
	public void setRegister(int bank, int address, int value)
	{
		banks[bank][address] = value;
	}
	
	public int readBit(int bank, int address, int pos)
	{
		// Get correct register, right shift byte and mask it
		return (banks[bank][address]>>pos) & 1;
	}
	
	public void setBit(int bank, int address, int pos, boolean value)
	{
		banks[bank][address] = value ? banks[bank][address] | (1 << pos) : banks[bank][address] & ~(1 << pos);
	}
	
}