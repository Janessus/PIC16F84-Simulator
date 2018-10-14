import gui.GUI_Main;

public class Main
{
	static GUI_Main gui;
	
	public static void main(String args[])
	{
		
		gui = new GUI_Main();
		Thread guiThread = new Thread(gui);
		guiThread.start();
		
	}
	
}