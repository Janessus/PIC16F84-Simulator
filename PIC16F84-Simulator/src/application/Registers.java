package application;

import gui.GUI_Main;
import javafx.application.Platform;

public class Registers
{
	//int[] Indirect_Addr, TMR0, PCL, STATUS, FSR, PORTA, PORTB, EEDATA, EEADR, PCLATH, INTCON = new int[8];	//Bank0
	//int[] Indirect_Addr, OPTION, PCL, STATUS, FSR, TRISA, TRISB, EECON1, EECON2, PCLATH, INTCON = new int[8];	//Bank1
	
	
	//Register addresses
	public static final int INDIRECT_ADDR = 0;
	public static final int TMR0 = 1;
	public static final int OPTION = 1;
	public static final int PCL = 2;
	public static final int STATUS = 3;
	public static final int FSR = 4;
	public static final int PORTA = 5;
	public static final int TRISA = 5;
	public static final int PORTB = 6;
	public static final int TRISB = 6;
	
	public static final int EEDATA = 8;
	public static final int EECON1 = 8;
	public static final int EEADR = 9;
	public static final int EECON2 = 9;
	public static final int PCLATH = 10;
	public static final int INTCON = 11;


	//indirect = 8 bit -> msb = rp0

	private int banks[][] = {new int[128], new int[128]};
	
	// Properties
	private int working = 0;
	
	// Methods
	public void init() 
	{
		//TODO
	}
	
	// Read RP0 bit for selecting bank for direct adressing
	// TODO: handle mirrored registers
	public int getBank()
	{
		return readBit(0, 3, 5);
	}

	public int getWorking() {
		return working;	
	}
	
	public void setWorking(byte val) {
		working = val;
		Platform.runLater(() -> GUI_Main.update());
	}
	
	// TODO: implement wraparound
	public int readRegister(int bank, int address)
	{
		return banks[bank][address];
	}
	
	public int readRegister(int address)
	{
		// Check for INDF access
		if(address!=0)
		{
			// Direct addressing, get bank from RP0
			return readRegister(getBank(), address);
		}
		// Indirect addressing, get bank and address from fsr
		int fsr = banks[0][4];
		return readRegister((0b10000000 & fsr) >> 7, 0b01111111 & fsr);
	}
	
	// same for setRegister
	public void setRegister(int bank, int address, int value)
	{
		banks[bank][address] = value;
		Platform.runLater(() -> GUI_Main.update(address));
	}
	
	public void setRegister(int address, int value)
	{
		// Check for INDF access
		if(address!=0)
		{
			// Direct addressing, get bank from RP0
			setRegister(getBank(), address, value);
			return;
		}
		// Indirect addressing, get bank and address from fsr
		int fsr = banks[0][4];
		setRegister((0b10000000 & fsr) >> 7, 0b01111111 & fsr, value);
	}
	
	public int readBit(int bank, int address, int pos)
	{
		// Get correct register, right shift byte and mask it
		return (banks[bank][address]>>pos) & 1;
	}
	
	public int readBit(int address, int pos)
	{
		// Check for INDF access
		if(address!=0)
		{
			// Direct addressing, get bank from RP0
			return readBit(getBank(), address, pos);
		}
		// Indirect addressing, get bank and address from fsr
		int fsr = banks[0][4];
		return readBit((0b10000000 & fsr) >> 7, 0b01111111 & fsr, pos);
	}
	
	public void setBit(int bank, int address, int pos, boolean value)
	{
		banks[bank][address] = value ? banks[bank][address] | (1 << pos) : banks[bank][address] & ~(1 << pos);
		Platform.runLater(() -> GUI_Main.update(address));
	}
	
	public void setBit(int address, int pos, boolean value)
	{
		// Check for INDF access
		if(address!=0)
		{
			// Direct addressing, get bank from RP0
			setBit(getBank(), address, pos, value);
			return;
		}
		// Indirect addressing, get bank and address from fsr
		int fsr = banks[0][4];
		setBit((0b10000000 & fsr) >> 7, 0b01111111 & fsr, value);
	}
	
	// Helpers
	public void setCarryFlag(boolean val)
	{
		setBit(0, 3, 0, val);
		setBit(1, 3, 0, val);	
		
		Platform.runLater(() -> GUI_Main.update(Registers.STATUS));
	}
	
	public boolean getCarryFlag() 
	{
		if(readBit(0, 3, 0) > 0)
			return true;
		return false;
	}
	
	public void setDigitCarryFlag(boolean val) 
	{
		setBit(0, 3, 1, val);
		setBit(1, 3, 1, val);	
		
		Platform.runLater(() -> GUI_Main.update(Registers.STATUS));
	}
	
	public boolean getDigitCarryFlag() 
	{
		if(readBit(0, 3, 1) > 0)
			return true;
		return false;
	}
	
	public void setZeroFlag(boolean val) 
	{
		setBit(0, 3, 2, val);
		setBit(1, 3, 2, val);
		
		Platform.runLater(() -> GUI_Main.update(Registers.STATUS));
	}
	
	public boolean getZeroFlag() 
	{
		if(readBit(0, 3, 2) > 0)
			return true;
		return false;
	}
}