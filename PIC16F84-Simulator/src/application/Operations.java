package application;

public enum Operations
{
	// Byte oriented operations
	ADDWF,
	ANDWF,
	CLRF,
	CLRW,
	COMF,
	DECF,
	DECFSZ,
	INCF,
	INCFSZ,
	IORWF,
	MOVF,
	MOVWF,
	NOP,
	RLF,
	RRF,
	SUBWF,
	SWAPF,
	XORWF,
	
	// Bit oriented operations
	BCF,
	BSF,
	BTFSC,
	BTFSS,
	
	// Literal and control operations
	ADDLW,
	ANDLW,
	CALL,
	CLRWDT,
	GOTO,
	IORLW,
	MOVLW,
	RETFIE,
	RETLW,
	RETURN,
	SLEEP,
	SUBLW,
	XORLW
}
