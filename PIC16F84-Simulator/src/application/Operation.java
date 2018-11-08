package application;

public enum Operation
{
	// #################################################################Byte oriented operations#########################################################
	
	ADDWF  	(0b00011100000000, new ICallback(){

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.addlw((byte)arguments);
			
		}}),
	
	ANDWF  	(0b00010100000000, new ICallback(){

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.andwf((byte)arguments);
		}}),
	
	CLRF 	(0b00000110000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.clrf((byte)arguments);
			
		}}),
	
	CLRW 	(0b00000100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.clrw((byte)arguments);
			
		}}),
	
	COMF  	(0b00100100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.comf((byte)arguments);
			
		}}),
	
	DECF 	(0b00001100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.decf((byte)arguments);
			
		}}),
	
	DECFSZ  (0b00101100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.decfsz((byte)arguments);
			
		}}),
	
	INCF  	(0b00101000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.incf((byte)arguments);
			
		}}),
	
	INCFSZ  (0b00111100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.incfsz((byte)arguments);
			
		}}),
	
	IORWF  	(0b00010000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.iorwf((byte)arguments);
			
		}}),
	
	MOVF  	(0b00100000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.movf((byte)arguments);
			
		}}),
	
	MOVWF 	(0b00000010000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.movwf((byte)arguments);
			
		}}),
	
	NOP  	(0b00000000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.nop((byte)arguments);
			
		}}),
	
	RLF 	(0b00110100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.rlf((byte)arguments);
			
		}}),
	
	RRF 	(0b00110000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.rrf((byte)arguments);
			
		}}),
	
	SUBWF 	(0b00001000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.subwf((byte)arguments);
			
		}}),
	
	SWAPF  	(0b00111000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.swapf((byte)arguments);
			
		}}),
	
	XORWF 	(0b00011000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.xorwf((byte)arguments);
			
		}}),
	
	
	
	//############################################################## Bit oriented operations######################################################################
	
	
	BCF 	(0b01000000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.bcf((byte)arguments);
			
		}}),
	
	BSF 	(0b01010000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.bsf((byte)arguments);
			
		}}),
	
	BTFSC 	(0b01100000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.btfsc((byte)arguments);
			
		}}),
	
	BTFSS 	(0b01110000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.btfss((byte)arguments);
			
		}}),
	
	// ########################################################Literal and control operations########################################################################
	
	ADDLW  	(0b11111000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.addlw((byte)arguments);
			
		}}),
	
	ANDLW  	(0b11100100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.andlw((byte)arguments);
			
		}}),
	
	CALL 	(0b10000000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.call((byte)arguments);
			
		}}),
	
	CLRWDT 	(0b00000001100100, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.clrwdt((byte)arguments);
			
		}}),
	
	GOTO  	(0b10100000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.goTo((byte)arguments);
			
		}}),
	
	IORLW  	(0b11100000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.iorlw((byte)arguments);
			
		}}),
	
	MOVLW 	(0b11000000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.movlw((byte)arguments);
			
		}}),
	
	RETFIE 	(0b00000000001001, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.retfie((byte)arguments);
			
		}}),
	
	RETLW 	(0b11010000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.retlw((byte)arguments);
			
		}}),
	
	RETURN  (0b00000000001000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.reTurn((byte)arguments);
			
		}}),
	
	SLEEP 	(0b00000001100011, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.sleep((byte)arguments);
			
		}}),
	
	SUBLW 	(0b11110000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.sublw((byte)arguments);
			
		}}),
	
	XORLW 	(0b11101000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.xorlw((byte)arguments);
			
		}});
	
	
	
	private int id;
	private ICallback callback;
	public Simulator sim;
	
	Operation(int id, ICallback callback)
	{
		this.id = id;
		this.callback = callback;
	}
	
	public int getId()
	{
		return id;
	}
	
	public ICallback getCallbackFunction()
	{
		return callback;
	}
	
	public void setSimulator(Simulator simulator)
	{
		sim = simulator;
	}
}
