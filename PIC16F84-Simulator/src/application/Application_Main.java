package application;

import java.io.File;
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
		for(Integer instruction : opcodeList)
		{
			decoder.decode(instruction.intValue());
		}
		
		System.out.println("Starting simulation");
		simulator.run();	
	}
	
	
	public void setGui(GUI_Main gui)
	{
		this.gui = gui;
	}
	
	public void openFile(File file) {
		opcodeList = parser.parseFile(file);
	}
}
