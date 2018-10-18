package application;

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
	int mask = 0;
	
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
		
		callbackFunction.execute(instruction & mask);

		return true;
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
				
				//invert mask
				this.mask = (~mask) & 0b11111111111111;
				return true;
			}
		}
		return false;
	}
}
