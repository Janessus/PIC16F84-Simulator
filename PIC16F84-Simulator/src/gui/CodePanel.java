package gui;

import java.util.Iterator;

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
	static ScrollPane pane;
	public Pane lineNumbers;
	public static Pane codePane;
	int lineHeight = 20;
	static int lineNumberWidth = 40;
	int i = 0;
	
	public void init()
	{
		i = 0;
		
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
		Label lbl = (Label) e.getSource();
		int id = Integer.parseInt(lbl.getId());
		
		lbl = (Label) codePane.getChildren().get(id); // TODO: isnt this the same label as above?
		Label lbl2 = (Label) lineNumbers.getChildren().get(id + 1);
		
		Iterator<Node> it = ((Pane)lbl.getParent()).getChildren().iterator();
		//reset all labels to standard color
		for(int k = 0; k < 2; k++)
		{
			while(it.hasNext())
			{
				Node next = it.next();
//				System.out.println("it.next = " + next.toString());
				next.setStyle("-fx-border-color: #444444; -fx-background-color: #404040;");
				//((Label)next).getStylesheets().add("default.css");
			}
			it = ((Pane)lbl2.getParent()).getChildren().iterator();
		}
		//lbl.getStylesheets().add("breakpoint.css");
		lbl.setStyle("-fx-border-color: #000000; -fx-background-color: #dd9999;");
		lbl2.setStyle("-fx-border-color: #000000; -fx-background-color: #dd9999;");
	}
	
	public void appendText(String s)
	{
		//Line numbers
		Label lbl = new Label(i + 1 + " ");
		lbl.setLayoutY((i) * lineHeight);	
		lbl.setMaxWidth(lineNumberWidth);
		lbl.setMinWidth(lineNumberWidth);
		lbl.setTextFill(Color.web("#fafafa"));
		lbl.setStyle("-fx-border-color: #444444; -fx-background-color: #404040;");
		lbl.setId("" + i);
		lbl.setOnMouseClicked(event -> this.onLblClicked(event));
		lineNumbers.getChildren().add(lbl);
		
		//Code
		Label lbl2 = new Label("CODE");
		lbl2.setLayoutY((i) * lineHeight);
		lbl2.setMaxHeight(20);
		lbl2.setPrefWidth(((AnchorPane)(pane.getParent())).getWidth() - lineNumberWidth-20);
		lbl2.setTextFill(Color.web("#fafafa"));
		lbl2.setStyle("-fx-border-color: #444444; -fx-background-color: #404040;");
		lbl2.setId("" + i);
		lbl2.setOnMouseClicked(event -> this.onLblClicked(event));
		codePane.getChildren().add(lbl2);
		
		((Label)codePane.getChildren().get(i)).setText(s);
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
