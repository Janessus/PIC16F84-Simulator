package application;

public enum Operations
{
	// Byte oriented operations
	ADDWF  	(0b00011100000000),
	ANDWF  	(0b00010100000000),
	CLRF 	(0b00000110000000),
	CLRW 	(0b00000100000000),
	COMF  	(0b00100100000000),
	DECF 	(0b00001100000000),
	DECFSZ  (0b00101100000000),
	INCF  	(0b00101000000000),
	INCFSZ  (0b00111100000000),
	IORWF  	(0b00010000000000),
	MOVF  	(0b00100000000000),
	MOVWF 	(0b00000010000000),
	NOP  	(0b00000000000000),
	RLF 	(0b00110100000000),
	RRF 	(0b00110000000000),
	SUBWF 	(0b00001000000000),
	SWAPF  	(0b00111000000000),
	XORWF 	(0b00011000000000),
	
	// Bit oriented operations
	BCF 	(0b01000000000000),
	BSF 	(0b01010000000000),
	BTFSC 	(0b01100000000000),
	BTFSS 	(0b01110000000000),
	
	// Literal and control operations
	ADDLW  	(0b11111000000000),
	ANDLW  	(0b11100100000000),
	CALL 	(0b10000000000000),
	CLRWDT 	(0b00000001100100),
	GOTO  	(0b10100000000000),
	IORLW  	(0b11100000000000),
	MOVLW 	(0b11000000000000),
	RETFIE 	(0b00000000001001),
	RETLW 	(0b11010000000000),
	RETURN  (0b00000000001000),
	SLEEP 	(0b00000001100011),
	SUBLW 	(0b11110000000000),
	XORLW 	(0b11101000000000);
	
	private int id;
	Operations(int id)
	{
		this.id = id;
	}
	
	public int getId()
	{
		return id;
	}
}
