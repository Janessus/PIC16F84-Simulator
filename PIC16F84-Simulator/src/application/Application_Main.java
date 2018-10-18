package application;

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
		
		// TODO Auto-generated method stub
		
	}

	public void setGui(GUI_Main gui)
	{
		this.gui = gui;
	}
	
	public void openFile() {
		parser.parseFile("");
	}
}
