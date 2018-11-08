package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Decoder
{
	Operation checkFirst[] = 				{Operation.NOP};
	Operation fullScaleOperation[] =		{Operation.CLRWDT, Operation.RETFIE, Operation.RETURN, Operation.SLEEP };
	Operation sevenBitOperation[] = 		{Operation.CLRF, Operation.CLRW, Operation.MOVWF, };
	Operation sixBitOperation[] = 			{Operation.ADDWF, Operation.ANDWF, Operation.COMF, Operation.DECF, Operation.DECFSZ, Operation.INCF, Operation.INCFSZ, Operation.IORWF, Operation.MOVF, Operation.NOP, Operation.RLF, Operation.RRF, Operation.SUBWF, Operation.SWAPF, Operation.XORWF, Operation.IORLW, Operation.XORLW};
	Operation fiveBitOperation[] = 			{Operation.ADDLW, Operation.SUBLW, Operation.ANDLW};
	Operation fourBitOperation[] = 			{Operation.BCF, Operation.BSF, Operation.BTFSC, Operation.BTFSS, Operation.MOVLW, Operation.RETLW};
	Operation threeBitOperation[] = 		{Operation.CALL, Operation.GOTO, };
	
	ICallback callbackFunction;
	Application_Main main;
	Operation tmpOperation = null;
	
	int mask = 0;
	
	
	public Decoder(Application_Main main)
	{
		this.main = main;
	}
	
	
	/**
	 * Calls the matching function from opcode
	 * @param instruction : opcode
	 * @return true, if instruction was valid
	 */
	public boolean decode(int instruction)
	{
		if(!findInstruction(checkFirst, 0b11111110011111, instruction))
			if(!findInstruction(fullScaleOperation, 0b11111111111111, instruction))
				if(!findInstruction(sevenBitOperation, 0b11111110000000, instruction))
					if(!findInstruction(sixBitOperation, 0b11111100000000, instruction))
						if(!findInstruction(fiveBitOperation, 0b11111000000000, instruction))
							if(!findInstruction(fourBitOperation, 0b11110000000000, instruction))
								if(!findInstruction(threeBitOperation, 0b11100000000000, instruction))
									return false;
		
		callbackFunction.execute(instruction & mask, main.simulator);

		return true;
	}
	
	/**
	 * TODO: duplicate code, unify with decode() or delete decode()
	 * @param instructions
	 * @return
	 */
	public ArrayList<WrappedOperation> decodeList(List<Integer> instructions)
	{
		ArrayList<WrappedOperation> operations = new ArrayList<WrappedOperation>();
		Iterator<Integer> it = instructions.iterator();
		
		for(int instruction:instructions) {
			if(!findInstruction(checkFirst, 0b11111110011111, instruction))
				if(!findInstruction(fullScaleOperation, 0b11111111111111, instruction))
					if(!findInstruction(sevenBitOperation, 0b11111110000000, instruction))
						if(!findInstruction(sixBitOperation, 0b11111100000000, instruction))
							if(!findInstruction(fiveBitOperation, 0b11111000000000, instruction))
								if(!findInstruction(fourBitOperation, 0b11110000000000, instruction))
									if(!findInstruction(threeBitOperation, 0b11100000000000, instruction))
										return null;
			
			operations.add(new WrappedOperation(tmpOperation, instruction & mask));
			System.out.println("Decoded Operation!");
		}
		
		return operations;
	}
	
	/**
	 * Find the instruction code from an array of Operation with same instructon (!= adress) length (bits)
	 * 
	 * @param list :  Operation sixBitOperation[]
	 * @param mask :  0b11111100000000
	 * @param instruction
	 * @return true if callback was set, false if not
	 */
	private boolean findInstruction(Operation list[], int mask, int instruction)
	{
		int tmp = (instruction & mask);
		for(int i = 0; i < list.length; i++)
		{
			if(list[i].getId() == tmp)
			{
				callbackFunction = list[i].getCallbackFunction();
				tmpOperation = list[i];
				
				//invert mask
				this.mask = (~mask) & 0b11111111111111;
				return true;
			}
		}
		return false;
	}
}
