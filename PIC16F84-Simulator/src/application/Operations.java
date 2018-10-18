package application;

public enum Operations
{
	// #################################################################Byte oriented operations#########################################################
	
	ADDWF  	(0b00011100000000, new ICallback(){

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	ANDWF  	(0b00010100000000, new ICallback(){

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	CLRF 	(0b00000110000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	CLRW 	(0b00000100000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	COMF  	(0b00100100000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	DECF 	(0b00001100000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	DECFSZ  (0b00101100000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	INCF  	(0b00101000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	INCFSZ  (0b00111100000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	IORWF  	(0b00010000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	MOVF  	(0b00100000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	MOVWF 	(0b00000010000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	NOP  	(0b00000000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	RLF 	(0b00110100000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	RRF 	(0b00110000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	SUBWF 	(0b00001000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	SWAPF  	(0b00111000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	XORWF 	(0b00011000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	
	
	//############################################################## Bit oriented operations######################################################################
	
	
	BCF 	(0b01000000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	BSF 	(0b01010000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	BTFSC 	(0b01100000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	BTFSS 	(0b01110000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	// ########################################################Literal and control operations########################################################################
	
	ADDLW  	(0b11111000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	ANDLW  	(0b11100100000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	CALL 	(0b10000000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	CLRWDT 	(0b00000001100100, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	GOTO  	(0b10100000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	IORLW  	(0b11100000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	MOVLW 	(0b11000000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	RETFIE 	(0b00000000001001, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	RETLW 	(0b11010000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	RETURN  (0b00000000001000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	SLEEP 	(0b00000001100011, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	SUBLW 	(0b11110000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}}),
	
	XORLW 	(0b11101000000000, new ICallback() {

		@Override
		public void execute()
		{
			// TODO Auto-generated method stub
			
		}});
	
	
	
	private int id;
	private ICallback callback;
	
	Operations(int id, ICallback callback)
	{
		this.id = id;
		this.callback = callback;
	}
	
	public int getId()
	{
		return id;
	}
}
