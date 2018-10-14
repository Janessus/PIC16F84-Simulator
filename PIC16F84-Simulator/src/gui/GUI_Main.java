package gui;

import java.util.Map;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;


public class GUI_Main extends Application implements Runnable
{
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

	@Override
	public void run()
	{
		launch();
	}
	
	private void setup(Scene scene, Map<String, Object> namespace)
	{
	    MenuItem open = (MenuItem) namespace.get("menuItem_Open"); //fx:id
		
	    //TODO set up event handlers 
		if(open != null)
			open.setOnAction(event -> this.onOpenDocument());
		else
			System.out.println("Not found");
		
	}

	private Object onOpenDocument()
	{
		//TODO Open a file
		System.out.println("Open clicked!");
		return null;
	}
	
	
	
	
}