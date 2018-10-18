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
import javafx.scene.control.MenuItem;


public class GUI_Main extends Application
{
	private static Application_Main app;
	
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
	    MenuItem open = (MenuItem) namespace.get("menuItem_Open"); //fx:id
		
	    //TODO set up event handlers 
		if(open != null)
			open.setOnAction(event -> this.onOpenDocument());
		else
			System.err.println("Not found");
	}

	private Object onOpenDocument()
	{
		//TODO Open a file
		System.out.println(this);
		System.out.println("Open clicked!");
		System.out.println(app);
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("List Files", "*.LST"),
				new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(null);
		System.out.println(app);
		
		app.openFile(selectedFile);
		
		return null;
	}
	
	public static void setApp(Application_Main app)
	{
		GUI_Main.app = app;
	}
	
	
}