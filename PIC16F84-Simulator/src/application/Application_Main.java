package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gui.GUI_Main;

public class Application_Main implements Runnable
{
	GUI_Main gui;
	Parser parser;
	Simulator simulator;
	Decoder decoder;
	List<Integer> opcodeList = null;

	@Override
	public void run() 
	{
		parser = new Parser();
		simulator = new Simulator();
		decoder = new Decoder(this);
		
		
		System.out.println("app running");
		// TODO Auto-generated method stub
	}

	public void runProgram()
	{
		System.out.println("decoding " + opcodeList.size() + " operations...");
		
		for(Integer opcode:opcodeList) {
			System.out.println(String.format("0x%08X", opcode));
		}
		
		ArrayList<WrappedOperation> operations = decoder.decodeList(opcodeList);
		
		//simulator.addOperations(operations);
		
		//System.out.println("Starting simulation");
		//simulator.run();	
	}
	
	
	public void setGui(GUI_Main gui)
	{
		this.gui = gui;
	}
	
	public void openFile(File file) {
		opcodeList = parser.parseFile(file);
	}
}
