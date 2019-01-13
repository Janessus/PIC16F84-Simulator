package gui;

import java.util.Collections;
import java.util.Iterator;

import application.Application_Main;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;

public class CodePanel
{
	public static ScrollPane pane;
	public Pane lineNumbers;
	public static Pane codePane;
	int lineHeight = 20;
	static int lineNumberWidth = 40;
	int i = 0;
	
	public void init()
	{
		i = 1;
		
		lineNumbers = new Pane();
		codePane = new Pane();
		codePane.setLayoutX(40);
		
		pane.setPrefWidth(((AnchorPane)(pane.getParent())).getWidth());
		pane.setPrefHeight(((AnchorPane)(pane.getParent())).getHeight());

		lineNumbers.getChildren().add(codePane);
		
		pane.setContent(lineNumbers);
	}
	
	public void onLblClicked(Event e)
	{
		int index = Integer.parseInt(((Label) e.getSource()).getId());
		
		Label lblCode = (Label) codePane.getChildren().get(index - 1);
		Label lblLineNumber = (Label) lineNumbers.getChildren().get(index);
		
		Boolean hasBreakpoint = GUI_Main.getApp().simulator.hasBreakpoint(index);
		
		if(hasBreakpoint == null) {
			return;
		}
		
		if(hasBreakpoint) {
			GUI_Main.getApp().simulator.setBreakpoint(index, false);
			lblCode.getStyleClass().removeAll(Collections.singleton("breakpoint"));
			lblLineNumber.getStyleClass().removeAll(Collections.singleton("breakpoint"));
		} else {
			GUI_Main.getApp().simulator.setBreakpoint(index, true);
			lblCode.getStyleClass().add("breakpoint");
			lblLineNumber.getStyleClass().add("breakpoint");
		}
	}
	
	public void appendText(String s)
	{
		//Line numbers
		Label lbl = new Label(i + " ");
		lbl.setLayoutY((i-1) * lineHeight);	
		lbl.setMaxWidth(lineNumberWidth);
		lbl.setMinWidth(lineNumberWidth);
		lbl.setTextFill(Color.web("#fafafa"));
		lbl.setStyle("-fx-border-color: #444444; -fx-background-color: #404040;");
		lbl.setId("" + i);
		lbl.setOnMouseClicked(event -> this.onLblClicked(event));
		lineNumbers.getChildren().add(lbl);
		
		//Code
		Label lbl2 = new Label("CODE");
		lbl2.setLayoutY((i-1) * lineHeight);
		lbl2.setMaxHeight(20);
		lbl2.setPrefWidth(((AnchorPane)(pane.getParent())).getWidth() - lineNumberWidth-20);
		lbl2.setTextFill(Color.web("#fafafa"));
		lbl2.setStyle("-fx-border-color: #444444; -fx-background-color: #404040;");
		lbl2.setId("" + i);
		lbl2.setOnMouseClicked(event -> this.onLblClicked(event));
		lbl2.setText(s);
		codePane.getChildren().add(lbl2);
		
		i++;
	}
	
	public static void onResizeWindow()
	{
		Iterator<Node> it = codePane.getChildren().iterator();
		while(it.hasNext())
		{
			Label tmp = (Label)it.next();
			tmp.setMinWidth(((AnchorPane)(pane.getParent())).getWidth() - lineNumberWidth-20);
		}
	}
}
