package application;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import gui.GUI_Main;

public class Application_Main implements Runnable
{
	GUI_Main gui;
	Parser parser;
	public Simulator simulator;
	Decoder decoder;
	// Map lineNumber -> [address, opcode]
	LinkedHashMap<Integer, ArrayList<Integer>> opcodeList = null;

	@Override
	public void run() 
	{
		com.sun.javafx.application.PlatformImpl.startup(()->{});
		parser = new Parser();
		simulator = new Simulator();
		decoder = new Decoder(this);
		
		System.out.println("app running");
	}

	public void runProgram()
	{
		if(opcodeList==null || opcodeList.isEmpty())
		{
			return;
		}
		
		System.out.println("Starting simulation");
		Thread simulatorThread = new Thread(simulator);
		simulatorThread.start();
	}
	
	
	public void setGui(GUI_Main gui)
	{
		this.gui = gui;
	}
	
	public void openFile(File file) {
		opcodeList = parser.parseFile(file);
		
		System.out.println("decoding " + opcodeList.size() + " operations...");
		simulator.loadOperationList(decoder.decodeList(opcodeList));
	}
}
