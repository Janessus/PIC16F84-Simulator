package application;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import gui.GUI_Main;

public class Application_Main implements Runnable
{
	GUI_Main gui;
	Parser parser;
	public Simulator simulator;
	Decoder decoder;
	
	Thread simulatorThread = null;
	
	// Map lineNumber -> [address, opcode]
	LinkedHashMap<Integer, ArrayList<Integer>> opcodeList = null;

	@Override
	public void run() 
	{
		com.sun.javafx.application.PlatformImpl.startup(()->{});
		parser = new Parser();
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
		if(simulatorThread!=null) {
			simulator.stopThread();
			// User old operationList to keep breakpoints
			OperationList operationList = simulator.getOperationList();
			simulator = new Simulator();
			simulator.loadOperationList(operationList);
		}
		System.out.println("creating new simulator thread");
		simulatorThread = new Thread(simulator);
		simulatorThread.start();
	}
	
	
	public void setGui(GUI_Main gui)
	{
		this.gui = gui;
	}
	
	public void openFile(File file) {
		opcodeList = parser.parseFile(file);
		
		// Stop simulator if its running
		if(simulatorThread!=null) {
			simulator.stopThread();
		}
		System.out.println("decoding " + opcodeList.size() + " operations...");
		simulator = new Simulator();
		simulator.loadOperationList(decoder.decodeList(opcodeList));
	}
}
