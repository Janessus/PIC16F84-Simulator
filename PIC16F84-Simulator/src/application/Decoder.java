package application;

public class Decoder
{
	Operations sevenOrMoreBitOperation[] = 	{Operations.CLRF, Operations.CLRW, Operations.MOVWF, Operations.NOP, Operations.CLRWDT, Operations.RETFIE, Operations.RETURN, Operations.SLEEP };
	Operations sixBitOperation[] = 			{Operations.ADDWF, Operations.ANDWF, Operations.COMF, Operations.DECF, Operations.DECFSZ, Operations.INCF, Operations.INCFSZ, Operations.IORWF, Operations.MOVF, Operations.NOP, Operations.RLF, Operations.RRF, Operations.SUBWF, Operations.SWAPF, Operations.XORWF, Operations.IORLW};
	Operations fiveBitOperation[] = 		{Operations.ADDLW, Operations.SUBLW};
	Operations fourBitOperation[] = 		{Operations.BCF, Operations.BSF, Operations.BTFSC, Operations.BTFSS, Operations.MOVLW, Operations.RETLW};
	Operations threeBitOperation[] = 		{Operations.CALL, Operations.GOTO, };
	
	public boolean decode(int instruction)
	{
		int tmp = (instruction & 0b11111110000000);
		
		for(int i = 0; i < sevenOrMoreBitOperation.length; i++)
		{
			if(sevenOrMoreBitOperation[i].getId() == tmp)
			{
				
			}
		}
		
		
		
		return true;
	}
}
