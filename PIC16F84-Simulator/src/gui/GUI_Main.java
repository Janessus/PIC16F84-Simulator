package gui;

import java.io.File;
import javafx.scene.control.ScrollPane;
import java.io.IOException;
import java.util.Map;

import application.Application_Main;
import application.Registers;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;



public class GUI_Main extends Application
{
	// TODO: Render INDF in GUI properly
	private static Application_Main app;
	
	public static TextArea mainWindow;
	public static CheckBox checkBoxStep;
	public static CheckBox checkBoxWdt;
	public static CheckBox pins[] = new CheckBox[18];
	public static LabelWrapper labels[] = new LabelWrapper[20];
	public static CodePanel codePanel;
	public static TextArea sramView = null;
	public static Stage sramViewStage = null;
	public TextArea console = null;

	private Parent root;
	
	@Override
   public void start(Stage stage) throws Exception 
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("layout2.fxml"));
		root = loader.load();
		root.autosize();
		Map<String, Object> namespace = loader.getNamespace();
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add("gui/style.css");
		
		stage.setScene(scene);
		stage.setMaximized(true);

		stage.widthProperty().addListener((obs, oldVal, newVal) -> 
		{
			if(codePanel != null)
				Platform.runLater(() -> CodePanel.onResizeWindow());
		});

		stage.maximizedProperty().addListener((obs, oldVal, newVal) -> 
		{
			if(codePanel != null)
		        Platform.runLater(() -> CodePanel.onResizeWindow());
		});
		
		stage.show();

		app.setGui(this);
		
		setup(scene, namespace);
    }

	private void setup(Scene scene, Map<String, Object> namespace)
	{
		mainWindow = (TextArea) namespace.get("mainWindow");
		codePanel = new CodePanel();
		
		MenuItem open = (MenuItem) namespace.get("menuItem_Open"); //fx:id
		
		open.setOnAction(event -> this.onOpenDocument());

		Button btnRun = (Button) namespace.get("btnRun");
		Button btnStep = (Button) namespace.get("btnStep");
		Button btnViewSram = (Button) namespace.get("btnViewSram");
		
		CodePanel.pane = (ScrollPane) namespace.get("CodePane");
		checkBoxStep = (CheckBox) namespace.get("checkBoxStep");
		checkBoxWdt = (CheckBox) namespace.get("wdte");
		console = (TextArea) namespace.get("console");
		
		btnRun.setOnAction(event -> this.onRunClicked());
		btnStep.setOnAction(event -> this.onStepClicked());

		if(btnViewSram != null)
			btnViewSram.setOnAction(event -> this.onViewSramClicked());
		
		codePanel.init();
		
		//CheckBox grafik 
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

		//Bank0
		labels[0] = new LabelWrapper((Label) namespace.get("indf"), Registers.INDIRECT_ADDR);
		labels[1] = new LabelWrapper((Label) namespace.get("tmr0"), Registers.TMR0);
		labels[2] = new LabelWrapper((Label) namespace.get("pcl"), Registers.PCL);
		labels[3] = new LabelWrapper((Label) namespace.get("status"), Registers.STATUS);
		labels[4] = new LabelWrapper((Label) namespace.get("fsr"), Registers.FSR);
		labels[18] = new LabelWrapper((Label) namespace.get("porta"), Registers.PORTA);
		labels[19] = new LabelWrapper((Label) namespace.get("portb"), Registers.PORTB);
		labels[5] = new LabelWrapper((Label) namespace.get("eedata"), Registers.EEDATA);
		labels[6] = new LabelWrapper((Label) namespace.get("eeadr"), Registers.EEADR);
		labels[7] = new LabelWrapper((Label) namespace.get("pclath"), Registers.PCLATH);
		labels[8] = new LabelWrapper((Label) namespace.get("intcon"), Registers.INTCON);
		
		//Bank1
		labels[9] = new LabelWrapper((Label) namespace.get("indf2"), Registers.INDIRECT_ADDR);
		labels[10] = new LabelWrapper((Label) namespace.get("option_reg"), Registers.OPTION);
		labels[11] = new LabelWrapper((Label) namespace.get("pcl2"), Registers.PCL);
		labels[12] = new LabelWrapper((Label) namespace.get("status2"), Registers.STATUS);
		labels[13] = new LabelWrapper((Label) namespace.get("fsr2"), Registers.FSR);
		labels[14] = new LabelWrapper((Label) namespace.get("eecon1"), Registers.EECON1);
		labels[15] = new LabelWrapper((Label) namespace.get("eecon2"), Registers.EECON2);
		labels[16] = new LabelWrapper((Label) namespace.get("pclath2"), Registers.PCLATH);
		labels[17] = new LabelWrapper((Label) namespace.get("intcon2"), Registers.INTCON);
		
		// Sram viewer
		FXMLLoader fxmlLoader = new FXMLLoader();
		fxmlLoader.setLocation(getClass().getResource("view_sram.fxml"));

		sramViewStage = new Stage();
		sramViewStage.setTitle("My New Stage Title");

		try {
			sramViewStage.setScene(new Scene(fxmlLoader.load(), 450, 450));
		} catch (IOException e) {
			e.printStackTrace();
		}

		sramView = (TextArea) fxmlLoader.getNamespace().get("sramView");
	}
	
	
	
	public static void update()
	{
		// Populate Sram Viewer
		if(sramViewStage.isShowing()) {
			sramView.setText(getSramString());
		}
		for(int i = 0; i < 20; i++)
		{
			if(i < 9 || i == 18 || i == 19)
				labels[i].label.setText("0x" + String.format("%02X", app.simulator.registers.readRegister(0, labels[i].adress)));
			else
				labels[i].label.setText("0x" + String.format("%02X", app.simulator.registers.readRegister(1, labels[i].adress)));
		}
	}
	
	public static void update(int address)
	{
		// Populate Sram Viewer
		if(sramViewStage!=null && sramViewStage.isShowing()) {
			sramView.setText(getSramString());
		}

		// Update Labels
		for(int i = 0; i < 20; i++)
		{
			if(address == labels[i].adress)
			{
				if(i < 9 || i == 18 || i == 19)
					labels[i].label.setText("0x" + String.format("%02X", app.simulator.registers.readRegister(0, labels[i].adress)));
				else
					labels[i].label.setText("0x" + String.format("%02X", app.simulator.registers.readRegister(1, labels[i].adress)));
			}
		}
	}
	
	public static String getSramString() {
		String ret = "";

		for(int i=0xC; i<=0x4F; i++)
		{
			ret += "0x" + String.format("%02X", i) + ": " + String.format("%02X", app.simulator.registers.readRegister(0, i));
			ret += i%8==0 ? "\n" : "\t";

		}

		return ret;
	}
	
	private void pinChanged(int i)
	{
		switch (i)
		{
		case 1:
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 2, pins[i-1].isSelected()));
			break;
			
		case 2:
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 3, pins[i-1].isSelected()));
			break;
			
		case 3: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 4, pins[i-1].isSelected()));
			break;
			
		case 4: 
			break;
			
		case 5: 
			break;
			
		case 6: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 0, pins[i-1].isSelected()));
			break;
			
		case 7: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 1, pins[i-1].isSelected()));
			break;
			
		case 8: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 2, pins[i-1].isSelected()));
			break;
			
		case 9: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 3, pins[i-1].isSelected()));
			break;
			
		case 10: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 4, pins[i-1].isSelected()));
			break;
			
		case 11: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 5, pins[i-1].isSelected()));
			break;
			
		case 12: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 6, pins[i-1].isSelected()));
			break;
			
		case 13: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 7, pins[i-1].isSelected()));
			break;
			
		case 14: 
			
			break;
			
		case 15: 
			break;
			
		case 16: 
			
			break;
			
		case 17: 
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 0, pins[i-1].isSelected()));
			break;
			
		case 18:
			Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 1, pins[i-1].isSelected()));
			break;
		}
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

	private void onViewSramClicked()
	{
		sramViewStage.show();
		// Force window in foreground
		sramViewStage.setAlwaysOnTop(true);
		sramViewStage.setAlwaysOnTop(false);
		update();
	}

	private Object onOpenDocument()
	{
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open Resource File");
		fileChooser.getExtensionFilters().addAll(
				new ExtensionFilter("List Files", "*.LST"),
				new ExtensionFilter("Text Files", "*.txt"),
				new ExtensionFilter("All Files", "*.*"));
		File selectedFile = fileChooser.showOpenDialog(null);
		
		codePanel.init();
		
		if(selectedFile != null) {
			app.openFile(selectedFile);
		}
		
		
		return null;
	}
	
	public static void setApp(Application_Main app)
	{
		GUI_Main.app = app;
	}
	public static Application_Main getApp()
	{
		return GUI_Main.app;
	}
	public void log(String log) {
		System.out.println(log);
		
		// TODO: for some reason this causes errors
		Platform.runLater(() -> console.appendText(log + "\n"));
	}
}