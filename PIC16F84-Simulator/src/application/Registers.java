package application;

public class Registers
{
	//int[] Indirect_Addr, TMR0, PCL, STATUS, FSR, PORTA, PORTB, EEDATA, EEADR, PCLATH, INTCON = new int[8];	//Bank0
	//int[] Indirect_Addr, OPTION, PCL, STATUS, FSR, TRISA, TRISB, EECON1, EECON2, PCLATH, INTCON = new int[8];	//Bank1

	private int banks[][] = {new int[128], new int[128]};
	
	// Properties
	private int working = 0;
	
	// Methods
	public void init() {
		
	}
	
	public int getWorking() {
		return working;	
	}
	
	public void setWorking(byte val) {
		working = val;
	}
	
	// TODO: fix this, bank parameter is useless, also implement wraparound
	public int readRegister(int bank, int address)
	{
		return banks[bank][address];
	}
	
	// same for setRegister
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
	
	// Helpers
	// TODO: implement
	public void setCarryFlag(boolean val)
	{
		banks[0][3] &= 0b11111110;
		banks[1][3] &= 0b11111110;
		
		if(val) 
		{
			banks[1][3] |= 0b00000001;
			banks[0][3] |= 0b00000001;
		}	
	}
	
	public boolean getCarryFlag() 
	{
		if((banks[0][3] & 0b00000001) > 0)
			return true;
		return false;
	}
	
	public void setDigitCarryFlag(boolean val) 
	{
		banks[0][3] &= 0b11111101;
		banks[1][3] &= 0b11111101;
		
		if(val) 
		{
			banks[1][3] |= 0b00000010;
			banks[0][3] |= 0b00000010;
		}	
	}
	public boolean getDigitCarryFlag() 
	{
		if((banks[0][3] & 0b00000010) > 0)
			return true;
		return false;
	}
	public void setZeroFlag(boolean val) 
	{
		banks[0][3] &= 0b11111011;
		banks[1][3] &= 0b11111011;
		
		if(val) 
		{
			banks[1][3] |= 0b00000100;
			banks[0][3] |= 0b00000100;
		}	
	}
	public boolean getZeroFlag() 
	{
		if((banks[0][3] & 0b00000100) > 0)
			return true;
		return false;
	}
}