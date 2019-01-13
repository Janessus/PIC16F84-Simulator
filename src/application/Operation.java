package application;

public enum Operation
{
	// #################################################################Byte oriented operations#########################################################
	
	ADDWF  	(0b00011100000000, new ICallback(){

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.addwf(arguments);
			
		}}),
	
	ANDWF  	(0b00010100000000, new ICallback(){

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.andwf(arguments);
		}}),
	
	CLRF 	(0b00000110000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.clrf(arguments);
			
		}}),
	
	CLRW 	(0b00000100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.clrw(arguments);
			
		}}),
	
	COMF  	(0b00100100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.comf(arguments);
			
		}}),
	
	DECF 	(0b00001100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.decf(arguments);
			
		}}),
	
	DECFSZ  (0b00101100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.decfsz(arguments);
			
		}}),
	
	INCF  	(0b00101000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.incf(arguments);
			
		}}),
	
	INCFSZ  (0b00111100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.incfsz(arguments);
			
		}}),
	
	IORWF  	(0b00010000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.iorwf(arguments);
			
		}}),
	
	MOVF  	(0b00100000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.movf(arguments);
			
		}}),
	
	MOVWF 	(0b00000010000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.movwf(arguments);
			
		}}),
	
	NOP  	(0b00000000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.nop(arguments);
			
		}}),
	
	RLF 	(0b00110100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.rlf(arguments);
			
		}}),
	
	RRF 	(0b00110000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.rrf(arguments);
			
		}}),
	
	SUBWF 	(0b00001000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.subwf(arguments);
			
		}}),
	
	SWAPF  	(0b00111000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.swapf(arguments);
			
		}}),
	
	XORWF 	(0b00011000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.xorwf(arguments);
			
		}}),
	
	
	
	//############################################################## Bit oriented operations######################################################################
	
	
	BCF 	(0b01000000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.bcf(arguments);
			
		}}),
	
	BSF 	(0b01010000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.bsf(arguments);
			
		}}),
	
	BTFSC 	(0b01100000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.btfsc(arguments);
			
		}}),
	
	BTFSS 	(0b01110000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.btfss(arguments);
			
		}}),
	
	// ########################################################Literal and control operations########################################################################
	
	ADDLW  	(0b11111000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.addlw(arguments);
			
		}}),
	
	ANDLW  	(0b11100100000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.andlw(arguments);
			
		}}),
	
	CALL 	(0b10000000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.call(arguments);
			
		}}),
	
	CLRWDT 	(0b00000001100100, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.clrwdt(arguments);
			
		}}),
	
	GOTO  	(0b10100000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.goTo(arguments);
			
		}}),
	
	IORLW  	(0b11100000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.iorlw(arguments);
			
		}}),
	
	MOVLW 	(0b11000000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.movlw(arguments);
			
		}}),
	
	RETFIE 	(0b00000000001001, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.retfie(arguments);
			
		}}),
	
	RETLW 	(0b11010000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.retlw(arguments);
			
		}}),
	
	RETURN  (0b00000000001000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.reTurn(arguments);
			
		}}),
	
	SLEEP 	(0b00000001100011, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.sleep(arguments);
			
		}}),
	
	SUBLW 	(0b11110000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.sublw(arguments);
			
		}}),
	
	XORLW 	(0b11101000000000, new ICallback() {

		@Override
		public void execute(int arguments, Simulator sim)
		{
			sim.xorlw(arguments);
			
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
