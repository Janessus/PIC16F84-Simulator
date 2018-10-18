import application.Application_Main;
import gui.GUI_Main;

public class Main
{
	static GUI_Main gui;
	static Application_Main app;
	
	public static void main(String args[])
	{
		app = new Application_Main();
		gui = new GUI_Main();
		
		app.setGui(gui);
		gui.setApp(app);
		
		Thread appThread = new Thread(app);
		appThread.start();
		
		Thread guiThread = new Thread(gui);
		guiThread.start();
	}
	
}