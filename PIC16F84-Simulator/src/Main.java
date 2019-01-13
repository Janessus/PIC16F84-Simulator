import application.Application_Main;
import gui.GUI_Main;
import javafx.application.Application;

public class Main
{
	static GUI_Main gui;
	static Application_Main app;
	
	//Test2
	
	public static void main(String args[])
	{
		app = new Application_Main();
		
		app.setGui(gui);
		GUI_Main.setApp(app);
		
		Thread appThread = new Thread(app);
		appThread.start();
		
		Application.launch(GUI_Main.class);
		
		//Thread guiThread = new Thread(gui);
		//guiThread.start();
	}
}