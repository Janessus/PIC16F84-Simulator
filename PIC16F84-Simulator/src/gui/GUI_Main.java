package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;


public class GUI_Main extends Application 
{
	 @Override
	    public void start(Stage stage) throws Exception {
	        Parent root = FXMLLoader.load(getClass().getResource("layout.fxml"));
	         
	        Scene scene = new Scene(root);

	        stage.setScene(scene);
	        stage.show();
	        
	        /*
	        Button btn = (Button)scene.lookup("#btnTest");
	        
	        if(btn != null)
	        	btn.setText("TEEEEEST!");
	        else 
	        	System.out.println("NULL");
	        */
	    }
	
	public static void main(String[] args) {
		launch(args);
		
		
	}
}