package gui;

import java.io.File;
import java.util.Map;

import application.Application_Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;


public class GUI_Main extends Application
{
	private static Application_Main app;
	
	public static TextArea mainWindow;
	public static CheckBox checkBoxStep;
	public static CheckBox pins[] = new CheckBox[18];
	
	
	@Override
    public void start(Stage stage) throws Exception 
	{
	 	FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
	    Parent root = loader.load();
	    root.autosize();
	    Map<String, Object> namespace = loader.getNamespace();

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
        
        setup(scene, namespace);
    }

	private void setup(Scene scene, Map<String, Object> namespace)
	{
		mainWindow = (TextArea) namespace.get("mainWindow");
		
	    MenuItem open = (MenuItem) namespace.get("menuItem_Open"); //fx:id
		if(open != null)
			open.setOnAction(event -> this.onOpenDocument());
		else
			System.err.println("Not found");
		
		
		
		Button btnRun = (Button) namespace.get("btnRun");
		Button btnStep = (Button) namespace.get("btnStep");
		checkBoxStep = (CheckBox) namespace.get("checkBoxStep");
		
		btnRun.setOnAction(event -> this.onRunClicked());
		btnStep.setOnAction(event -> this.onStepClicked());
		
		
		//Checkbox grafik 
		for(int i = 0; i < 18; i++)
		{
			String name = "pin" + (i+1);
			pins[i] = (CheckBox) namespace.get(name);
		}
		
		pins[0].setOnAction(event -> this.pinChanged(1));
		pins[1].setOnAction(event -> this.pinChanged(2));
		pins[2].setOnAction(event -> this.pinChanged(3));
		pins[3].setOnAction(event -> this.pinChanged(4));
		pins[4].setOnAction(event -> this.pinChanged(5));
		pins[5].setOnAction(event -> this.pinChanged(6));
		pins[6].setOnAction(event -> this.pinChanged(7));
		pins[7].setOnAction(event -> this.pinChanged(8));
		pins[8].setOnAction(event -> this.pinChanged(9));
		pins[9].setOnAction(event -> this.pinChanged(10));
		pins[10].setOnAction(event -> this.pinChanged(11));
		pins[11].setOnAction(event -> this.pinChanged(12));
		pins[12].setOnAction(event -> this.pinChanged(13));
		pins[13].setOnAction(event -> this.pinChanged(14));
		pins[14].setOnAction(event -> this.pinChanged(15));
		pins[15].setOnAction(event -> this.pinChanged(16));
		pins[16].setOnAction(event -> this.pinChanged(17));
		pins[17].setOnAction(event -> this.pinChanged(18));

		
		//TODO Gui updater 
	}
	
	private void pinChanged(int i)
	{
		//TODO
		System.out.println("\nPIN_CHANGED " + i);
		System.out.println("Selected = " + pins[i-1].isSelected());
		
	}
	
	private void onRunClicked()
	{
		app.runProgram();
	}
	
	private void onStepClicked()
	{
		synchronized(app.simulator) {
			app.simulator.notify();
		}
	}

	private Object onOpenDocument()
	{
		//TODO Open a file
		System.out.println(this);
		System.out.println("Open clicked!");
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("List Files", "*.LST"),
				new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(null);
		
		app.openFile(selectedFile);
		
		return null;
	}
	
	public static void setApp(Application_Main app)
	{
		GUI_Main.app = app;
	}
}