package gui;

import java.io.File;
import java.util.Map;

import application.Application_Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;


public class GUI_Main extends Application
{
	private static Application_Main app;
	
	public static TextArea mainWindow;
	public static CheckBox checkBoxStep;
	
	@Override
    public void start(Stage stage) throws Exception 
	{
	 	FXMLLoader loader = new FXMLLoader(getClass().getResource("layout.fxml"));
	    Parent root = loader.load();
	    
	    Map<String, Object> namespace = loader.getNamespace();

        Scene scene = new Scene(root);

        stage.setScene(scene);
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
		
	}
	
	private void onRunClicked()
	{
		app.runProgram();
	}
	
	private void onStepClicked()
	{
		app.runProgram();
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