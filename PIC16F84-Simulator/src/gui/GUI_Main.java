package gui;

import java.io.File;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

import application.Application_Main;
import application.Registers;
import application.Simulator;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;



public class GUI_Main extends Application
{
	private static Application_Main app;
	
	public static TextArea mainWindow;
	public static CheckBox checkBoxStep;
	public static CheckBox checkBoxWdt;
	public static CheckBox pins[] = new CheckBox[18];
	public static LabelWrapper labels[] = new LabelWrapper[20];
	public static CodePanel codePanel;
	public static TextArea sramView = null;
	public static Stage sramViewStage = null;
	public static Spinner<Double> spinnerFreq;
	public static TextArea console = null;
	public static Pane trisA;
	public static Pane trisB;
	

	private Parent root;
	
	static Label lblWorking;
	static Label lblCycles;
	static Label lblTime;
	
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
		spinnerFreq = (Spinner<Double>) namespace.get("spinnerFreq");
		spinnerFreq.valueProperty().addListener((obs, oldValue, newValue) -> {
			updateInstructionCycles();
		});
		// Update while typing
		spinnerFreq.getEditor().textProperty().addListener((obs, oldValue, newValue) -> {
			updateInstructionCycles();
		});
		
		trisA = (Pane) namespace.get("trisa");
		trisB = (Pane) namespace.get("trisb");
		
		lblWorking = (Label) namespace.get("lblWorking");
		lblCycles = (Label) namespace.get("lblCycles");
		lblTime = (Label) namespace.get("lblTime");
		
		((CheckBox)(trisA.getChildren().get(1))).setOnAction((event) -> trisChanged(7));
		((CheckBox)(trisA.getChildren().get(2))).setOnAction((event) -> trisChanged(6));
		((CheckBox)(trisA.getChildren().get(3))).setOnAction((event) -> trisChanged(5));
		((CheckBox)(trisA.getChildren().get(4))).setOnAction((event) -> trisChanged(4));
		((CheckBox)(trisA.getChildren().get(5))).setOnAction((event) -> trisChanged(3));
		((CheckBox)(trisA.getChildren().get(6))).setOnAction((event) -> trisChanged(2));
		((CheckBox)(trisA.getChildren().get(7))).setOnAction((event) -> trisChanged(1));
		((CheckBox)(trisA.getChildren().get(8))).setOnAction((event) -> trisChanged(0));
		
		((CheckBox)(trisB.getChildren().get(1))).setOnAction((event) -> trisChanged(17));
		((CheckBox)(trisB.getChildren().get(2))).setOnAction((event) -> trisChanged(16));
		((CheckBox)(trisB.getChildren().get(3))).setOnAction((event) -> trisChanged(15));
		((CheckBox)(trisB.getChildren().get(4))).setOnAction((event) -> trisChanged(14));
		((CheckBox)(trisB.getChildren().get(5))).setOnAction((event) -> trisChanged(13));
		((CheckBox)(trisB.getChildren().get(6))).setOnAction((event) -> trisChanged(12));
		((CheckBox)(trisB.getChildren().get(7))).setOnAction((event) -> trisChanged(11));
		((CheckBox)(trisB.getChildren().get(8))).setOnAction((event) -> trisChanged(10));
		
		
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
			int j=i+1;
			pins[i].setOnAction(event -> this.pinChanged(j));
		}
		
		
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
			Platform.runLater(() -> sramView.setText(getSramString()));
		}
		for(int i = 0; i < 20; i++)
		{
			if(i == 18) { // PORTA + TRISA
				int portRegister = app.simulator.registers.readRegister(0, Registers.PORTA);
				int trisRegister = app.simulator.registers.readRegister(1, Registers.TRISA);
				for(int j=0; j<5; j++) {
					boolean tris = (trisRegister & (1 << j)) !=0;
					CheckBox trisCheck = ((CheckBox)trisA.getChildren().get(8-j));
					Platform.runLater(() -> trisCheck.setSelected(tris));
					if(!tris) { // Cleared tris bit means output mode
						boolean port = (portRegister & (1 << j)) !=0;
						
						// Set pin
						CheckBox pin = pins[5+j];
						Platform.runLater(() -> pin.setSelected(port));
					}
				}
			}
			else if(i==19) { // PORTB + TRISB
				int portRegister = app.simulator.registers.readRegister(0, Registers.PORTB);
				int trisRegister = app.simulator.registers.readRegister(1, Registers.TRISB);
				for(int j=0; j<8; j++) {
					boolean tris = (trisRegister & (1 << j)) !=0;
					CheckBox trisCheck = ((CheckBox)trisB.getChildren().get(8-j));
					Platform.runLater(() -> trisCheck.setSelected(tris));
					if(!tris) { // Cleared tris bit means output mode
						boolean port = (portRegister & (1 << j)) !=0;
						
						// Set pin
						CheckBox pin = pins[5+j];
						Platform.runLater(() -> pin.setSelected(port));
					}
				}
			}
			else if(i < 9) {
				labels[i].label.setText("0x" + String.format("%02X", app.simulator.registers.readRegister(0, labels[i].adress)));
			}
			else {
				labels[i].label.setText("0x" + String.format("%02X", app.simulator.registers.readRegister(1, labels[i].adress)));
			}
		}
		
	}
	
	public static void updateWorking() {
		if(lblWorking != null)
			Platform.runLater(() -> lblWorking.setText("0x" + String.format("%02X", Registers.working)));
	}
	
	public static void updateInstructionCycles() {
		if(app.simulator == null) {
			return;
		}
		int cycles = app.simulator.getInstrouctionCycleCount();
		Platform.runLater(() -> lblCycles.setText("" + cycles));
		Platform.runLater(() -> lblTime.setText("" + (cycles * 4/spinnerFreq.getValue())));
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
	
	private static void trisChanged(int i)
	{
		System.out.println("TrisChanged " + i);
		if(i < 8) //TRISA
		{
			app.simulator.registers.setBit(1, Registers.TRISA, i, ((CheckBox)(trisA.getChildren().get(i + 1))).isSelected());
		}
		else //TRISB
		{
			i -= 10;
			app.simulator.registers.setBit(1, Registers.TRISB, i, ((CheckBox)(trisB.getChildren().get(i + 1))).isSelected());
		}
	}
	
	private void pinChanged(int i)
	{
		if(app.simulator==null) {
			return;
		}
		boolean isSelected = pins[i-1].isSelected();
		
		// Set pin states for simulator
		if(isSelected) {
			Platform.runLater(() -> app.simulator.setPinState(i-1, Simulator.PIN_RISING));
		} else {
			Platform.runLater(() -> app.simulator.setPinState(i-1, Simulator.PIN_FALLING));
		}
		
		
		switch (i)
		{
		case 1:
			if(app.simulator.registers.readBit(1, Registers.TRISA, 2)==1) {
				
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 2, isSelected));
			}
			
			break;
			
		case 2:
			if(app.simulator.registers.readBit(1, Registers.TRISA, 3)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 3, isSelected));
			}
			
			break;
			
		case 3: 
			if(app.simulator.registers.readBit(1, Registers.TRISA, 4)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 4, isSelected));
			}
			
			break;
			
		case 4: 
			if(!isSelected) //Pin is Low-Active
			{
				if(app.simulator.isSleep)
					Platform.runLater(() -> app.simulator.registers.reset(Registers.MCLR_SLEEP_RESET));
				else
					Platform.runLater(() -> app.simulator.registers.reset(Registers.MCLR_NORMAL_RESET));
			}	
			break;
			
		case 5: 
			break;
			
		case 6: 
			if(app.simulator.registers.readBit(1, Registers.TRISB, 0)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 0, isSelected));
			}
			break;
			
		case 7: 
			if(app.simulator.registers.readBit(1, Registers.TRISB, 1)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 1, isSelected));
			}
			break;
			
		case 8: 
			if(app.simulator.registers.readBit(1, Registers.TRISB, 2)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 2, isSelected));
			} 
			break;
			
		case 9: 
			if(app.simulator.registers.readBit(1, Registers.TRISB, 3)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 3, isSelected));
			}
			break;
			
		case 10: 
			if(app.simulator.registers.readBit(1, Registers.TRISB, 4)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 4, isSelected));
			}
			break;
			
		case 11: 
			if(app.simulator.registers.readBit(1, Registers.TRISB, 5)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 5, isSelected));
			} 
			break;
			
		case 12:
			if(app.simulator.registers.readBit(1, Registers.TRISB, 6)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 6, isSelected));
			}
			break;
			
		case 13: 
			if(app.simulator.registers.readBit(1, Registers.TRISB, 7)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTB, 7, isSelected));
			}
			break;
			
		case 14: 
			break;
			
		case 15: 
			break;
			
		case 16: 
			
			break;
			
		case 17: 
			if(app.simulator.registers.readBit(1, Registers.TRISA, 0)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 0, isSelected));
			}
			break;
			
		case 18:
			if(app.simulator.registers.readBit(1, Registers.TRISA, 1)==1) {
				Platform.runLater(() -> app.simulator.registers.setBit(0, Registers.PORTA, 1, isSelected));
			}
			break;
		}
	}
	
	public static void highlightLine(int lineNumber) {
		// Remove highlighting for old nodes
		Set<Node> lastNodes = CodePanel.pane.lookupAll(".current-operation");
		for(Node node:lastNodes) {
			Platform.runLater(() -> node.getStyleClass().removeAll(Collections.singleton("current-operation")));
		}
		
		// Highlight current line in codepanel
		Platform.runLater(() -> GUI_Main.codePanel.lineNumbers.getChildren().get(lineNumber).getStyleClass().add("current-operation"));
		Node operationNode = CodePanel.codePane.getChildren().get(lineNumber-1);
		Platform.runLater(() -> operationNode.getStyleClass().add("current-operation"));
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
		Platform.runLater(() -> sramViewStage.show());
		// Force window in foreground
		Platform.runLater(() -> sramViewStage.setAlwaysOnTop(true));
		Platform.runLater(() -> sramViewStage.setAlwaysOnTop(false));
		update();
	}

	private void onOpenDocument()
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
		
	}
	
	public static void setApp(Application_Main app)
	{
		GUI_Main.app = app;
	}
	public static Application_Main getApp()
	{
		return GUI_Main.app;
	}
	public static void log(String log) {
		System.out.println(log);
		
		Platform.runLater(() -> console.appendText(log + "\n"));
	}
}