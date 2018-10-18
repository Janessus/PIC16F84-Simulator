package application;

import java.io.File;

import gui.GUI_Main;

public class Application_Main implements Runnable
{
	GUI_Main gui;
	Parser parser;
	Simulator simulator;
	

	@Override
	public void run() 
	{
		parser = new Parser();
		simulator = new Simulator();
		
		System.out.println("app running");
		// TODO Auto-generated method stub
		while(true) {
			
		}
		
	}

	public void setGui(GUI_Main gui)
	{
		this.gui = gui;
	}
	
	public void openFile(File file) {
		parser.parseFile(file);
	}
}
