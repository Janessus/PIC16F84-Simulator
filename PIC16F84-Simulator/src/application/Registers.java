package application;

import java.util.ArrayList;
import java.util.Arrays;

import gui.CodePanel;
import gui.GUI_Main;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;

public class Registers
{	
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
	
	//States for reset(int state)
	public static final int MCLR_NORMAL_RESET = 0;
	public static final int MCLR_SLEEP_RESET = 1;
	public static final int WDT_RESET = 2;

	public static ArrayList<Integer> mirroredRegisters = new ArrayList<Integer>(Arrays.asList(INDIRECT_ADDR, PCL, STATUS, FSR, PCLATH, INTCON));

	//indirect = 8 bit -> msb = rp0

	private int banks[][] = {new int[128], new int[128]};
	
	// Properties
	public static int working = 0;
	
	public Boolean[] portABuffer = new Boolean[5];
	public Boolean[] portBBuffer = new Boolean[8];
	
	// Reset P.43 in datasheet:
	public void powerOn() 
	{
		this.setRegisterDirectly(0, INDIRECT_ADDR, 0xff);
		
		this.setRegisterDirectly(0, PCL, 0);
		
		this.setRegisterDirectly(1, OPTION, 0xff);
		
		this.setBitDirectly(0, STATUS, 3, true);
		this.setBitDirectly(0, STATUS, 4, true);
		this.setBitDirectly(0, STATUS, 5, false);
		this.setBitDirectly(0, STATUS, 6, false);
		this.setBitDirectly(0, STATUS, 7, false);
	
		this.setRegisterDirectly(0, PCLATH, 0);
		
		this.setRegisterDirectly(0, INTCON, 0);
		
		this.setRegisterDirectly(1, TRISA, 0xff);
		this.setRegisterDirectly(1, TRISB, 0xff);
		
		this.setRegisterDirectly(1, EECON1, 0);
	}
	
	public void reset(int state)
	{
		System.out.println("Reset called");
		
		this.setRegisterDirectly(0, PCL, 0);
		
		this.setBitDirectly(0, STATUS, 5, false);
		this.setBitDirectly(0, STATUS, 6, false);
		this.setBitDirectly(0, STATUS, 7, false);
		
		this.setRegisterDirectly(0, PCLATH, 0);
	
		this.setRegisterDirectly(1, OPTION, 0xff);
		
		this.setRegisterDirectly(1, TRISA, 0xff);
		this.setRegisterDirectly(1, TRISB, 0xff);
		
		this.setRegisterDirectly(1, EECON1, 0);
		
		for(int i = 1; i < 8; i++)
		{
			this.setBitDirectly(0, INTCON, i, false);
			this.setBitDirectly(0, INTCON, i, false);
		}
		
		//Reset Conditions
		//EECON1 Condition?????
		switch (state) {
		case MCLR_NORMAL_RESET:
			Simulator.programCounter = 0;
			break;
			
		case MCLR_SLEEP_RESET:
			Simulator.programCounter = 0;
			this.setBitDirectly(0, STATUS, 3, false);
			
			this.setBitDirectly(0, STATUS, 4, true);
			break;
			
		case WDT_RESET:
			Simulator.programCounter = 0;
			this.setBitDirectly(0, STATUS, 3, true);
			
			this.setBitDirectly(0, STATUS, 4, false);
			break;
		}
		
		// Line highlighting
		int firstLine = GUI_Main.getApp().simulator.operationList.getLines().entrySet().iterator().next().getKey();
		Platform.runLater(() -> GUI_Main.highlightLine(firstLine));
		Node operationNode = CodePanel.codePane.getChildren().get(firstLine);
		Platform.runLater(() -> CodePanel.pane.setVvalue(operationNode.getBoundsInParent().getMaxY() / CodePanel.pane.getContent().getBoundsInLocal().getHeight()));
	}
	
	// Read RP0 bit for selecting bank for direct adressing
	public int getBank()
	{
		return readBit(0, 3, 5);
	}

	public int getWorking() {
		return working;	
	}
	
	public void setWorking(int val) {
		working = val&0b11111111;
		
		System.out.println("Working set to " + working);
		Platform.runLater(() -> GUI_Main.updateWorking());
	}
	
	public int readRegister(int bank, int address)
	{
		// Check for mirrored register
		if(bank==1 && isMirroredRegister(address)) {
			return readRegister(0, address);
		}
		
		return banks[bank][address];
	}
	
	public int readRegister(int address)
	{
		// Check for INDF access
		if(address!=INDIRECT_ADDR)
		{
			// Direct addressing, get bank from RP0
			return readRegister(getBank(), address);
		}
		// Indirect addressing, get bank and address from fsr
		int fsr = banks[0][FSR];
		return readRegister((0b10000000 & fsr) >> 7, 0b01111111 & fsr);
	}
	
	public void setRegister(int bank, int address, int value)
	{
		// Check for direct INDF access via FSR
		if(address==INDIRECT_ADDR)
		{
			return;
		}
		// Check for mirrored register
		if(bank==1 && isMirroredRegister(address)) {
			setRegister(0, address, value);
			return;
		}
		// Check for PORTA/PORTB access
		if(bank==0 && (address==PORTA || address==PORTB)) {
			// Set bits seperately, so each bit can be checked independently
			for(int i=0; i<8; i++) {
				boolean val = (value & (1 << i)) !=0;
				setBit(bank, address, i, val);
			}
		}
		
		if(GUI_Main.getApp().simulator != null)
		{
			// Check for PCL manipulation
			if(address==PCL) {
				int upperPc = (this.readRegister(PCLATH) & 0b11111) << 8;
				GUI_Main.getApp().simulator.setProgramCounter(upperPc | value);
			} else if (address==TMR0 && bank==0) {
				// TMR0 manipulation inhibits TMR0 increment for two cycles
				GUI_Main.getApp().simulator.inhibitTmr0Increment(2);
			}
			banks[bank][address] = value & 0b11111111;
			Platform.runLater(() -> GUI_Main.update());
		}
			
	}
	
	public void setRegister(int address, int value)
	{
		// Check for INDF access
		if(address!=INDIRECT_ADDR)
		{
			// Direct addressing, get bank from RP0
			setRegister(getBank(), address, value);
			return;
		}
		// Indirect addressing, get bank and address from fsr
		int fsr = banks[0][FSR];
		setRegister((0b10000000 & fsr) >> 7, 0b01111111 & fsr, value);
	}
	
	public int readBit(int bank, int address, int pos)
	{
		// Check for mirrored register
		if(bank==1 && isMirroredRegister(address)) {
			return readBit(0, address, pos);
		}
		
		// Get correct register, right shift byte and mask it
		return (banks[bank][address]>>pos) & 1;
	}
	
	public int readBit(int address, int pos)
	{
		// Check for INDF access
		if(address!=INDIRECT_ADDR)
		{
			// Direct addressing, get bank from RP0
			return readBit(getBank(), address, pos);
		}
		// Indirect addressing, get bank and address from fsr
		int fsr = banks[0][FSR];
		return readBit((0b10000000 & fsr) >> 7, 0b01111111 & fsr, pos);
	}
	
	public void setBit(int bank, int address, int pos, boolean value)
	{
		// Check for direct INDF access via FSR
		if(address==INDIRECT_ADDR)
		{
			return;
		}
		
		// Check for mirrored register
		if(bank==1 && isMirroredRegister(address)) {
			setBit(0, address, pos, value);
			return;
		}

		// Check for PORTA access
		if(bank==0 && address==PORTA) {
			// Check if port is set as input
			if(readBit(1, TRISA, pos)==1) {
				// Buffer value and return, dont write to PORTA
				portABuffer[pos] = value;
				return;
			}
		}
		
		// Check for PORTB access
		if(bank==0 && address==PORTB) {
			// Check if port is set as input
			if(readBit(1, TRISB, pos)==1) {
				// Buffer value and return, dont write to PORTA
				portBBuffer[pos] = value;
				return;
			}
		}
		
		// Check for PCL manipulation
		if(address==PCL) {
			int upperPc = (this.readRegister(PCLATH) & 0b11111) << 8;
			int lowerPc = value ? banks[bank][address] | (1 << pos) : banks[bank][address] & ~(1 << pos);
			GUI_Main.getApp().simulator.setProgramCounter(upperPc | lowerPc);
		} else if (address==TMR0 && bank==0) {
			// TMR0 manipulation inhibits TMR0 increment for two cycles
			GUI_Main.getApp().simulator.inhibitTmr0Increment(2);
		}
		int result = value ? banks[bank][address] | (1 << pos) : banks[bank][address] & ~(1 << pos);
		banks[bank][address] = result%256;
		Platform.runLater(() -> GUI_Main.update());
	}
	
	public void setBit(int address, int pos, boolean value)
	{
		// Check for INDF access
		if(address!=INDIRECT_ADDR)
		{
			// Direct addressing, get bank from RP0
			setBit(getBank(), address, pos, value);
			return;
		}
		// Indirect addressing, get bank and address from fsr
		int fsr = banks[0][FSR];
		setBit((0b10000000 & fsr) >> 7, 0b01111111 & fsr, pos, value);
	}
	
	// Helpers
	public void setCarryFlag(boolean val)
	{
		setBitDirectly(0, 3, 0, val);
		
		Platform.runLater(() -> GUI_Main.update());
	}
	
	public boolean getCarryFlag() 
	{
		if(readBit(0, 3, 0) > 0)
			return true;
		return false;
	}
	
	public void setDigitCarryFlag(boolean val) 
	{
		setBitDirectly(0, 3, 1, val);
		
		Platform.runLater(() -> GUI_Main.update());
	}
	
	public boolean getDigitCarryFlag() 
	{
		if(readBit(0, 3, 1) > 0)
			return true;
		return false;
	}
	
	public void setZeroFlag(boolean val) 
	{
		setBitDirectly(0, 3, 2, val);
		
		Platform.runLater(() -> GUI_Main.update());
	}
	
	public boolean getZeroFlag() 
	{
		if(readBit(0, 3, 2) > 0)
			return true;
		return false;
	}
	
	public boolean isMirroredRegister(int address) {
		if(address>11 || mirroredRegisters.contains(address)) {
			return true;
		}
		return false;
	}
	
	// Set a register without triggering checks; e.g.: set PCL without manipulating the PC
	public void setRegisterDirectly(int bank, int address, int value) {
		banks[bank][address] = value&0b11111111;
		Platform.runLater(() -> GUI_Main.update());
	}
	public void setBitDirectly(int bank, int address, int pos, boolean value)
	{
		banks[bank][address] = value ? banks[bank][address] | (1 << pos) : banks[bank][address] & ~(1 << pos);
		Platform.runLater(() -> GUI_Main.update());
	}
}