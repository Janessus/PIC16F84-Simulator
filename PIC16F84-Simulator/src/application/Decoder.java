package application;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Decoder
{
	Operations checkFirst[] = 				{Operations.NOP};
	Operations fullScaleOperation[] =		{Operations.CLRWDT, Operations.RETFIE, Operations.RETURN, Operations.SLEEP };
	Operations sevenBitOperation[] = 		{Operations.CLRF, Operations.CLRW, Operations.MOVWF, };
	Operations sixBitOperation[] = 			{Operations.ADDWF, Operations.ANDWF, Operations.COMF, Operations.DECF, Operations.DECFSZ, Operations.INCF, Operations.INCFSZ, Operations.IORWF, Operations.MOVF, Operations.NOP, Operations.RLF, Operations.RRF, Operations.SUBWF, Operations.SWAPF, Operations.XORWF, Operations.IORLW};
	Operations fiveBitOperation[] = 		{Operations.ADDLW, Operations.SUBLW};
	Operations fourBitOperation[] = 		{Operations.BCF, Operations.BSF, Operations.BTFSC, Operations.BTFSS, Operations.MOVLW, Operations.RETLW};
	Operations threeBitOperation[] = 		{Operations.CALL, Operations.GOTO, };
	
	ICallback callbackFunction;
	Application_Main main;
	Operations tmpOperation = null;
	
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
	 * 
	 * @param instructions
	 * @return
	 */
	public WrappedOperation[] decodeList(List<Integer> instructions)
	{
		Operations tmpOperation = null;
		ArrayList<WrappedOperation> operations = new ArrayList<WrappedOperation>();
		Iterator<Integer> it = instructions.iterator();
		int instruction = 0;
		
		while(it.hasNext())
		{
			instruction = it.next();
			
			if(!findInstruction(checkFirst, 0b11111110011111, instruction))
				if(!findInstruction(fullScaleOperation, 0b11111111111111, instruction))
					if(!findInstruction(sevenBitOperation, 0b11111110000000, instruction))
						if(!findInstruction(sixBitOperation, 0b11111100000000, instruction))
							if(!findInstruction(fiveBitOperation, 0b11111000000000, instruction))
								if(!findInstruction(fourBitOperation, 0b11110000000000, instruction))
									if(!findInstruction(threeBitOperation, 0b11100000000000, instruction))
										return null;
			
			operations.add(new WrappedOperation(tmpOperation, instruction & mask));
		}

		return (WrappedOperation[])operations.toArray();
	}
	
	/**
	 * Find the instruction code from an array of operations with same instructon (!= adress) length (bits)
	 * 
	 * @param list :  Operations sixBitOperation[]
	 * @param mask :  0b11111100000000
	 * @param instruction
	 * @return true if callback was set, false if not
	 */
	private boolean findInstruction(Operations list[], int mask, int instruction)
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
