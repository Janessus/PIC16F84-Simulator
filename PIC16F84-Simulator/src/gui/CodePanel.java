package gui;

import javafx.event.Event;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;

public class CodePanel
{
	ScrollPane pane;
	Pane tmp, tmp2;
	int lineHeight = 20;
	int lineNumberWidth = 40;
	
	public void init()
	{
		tmp = new Pane();
		tmp2 = new Pane();
		for(int i = 1; i < 200 ; i++) 
		{
			Label lbl = new Label(i + " ");
			lbl.setLayoutY((i - 1) * lineHeight);	
			lbl.setMaxWidth(lineNumberWidth);
			lbl.setMinWidth(lineNumberWidth);
			lbl.setStyle("-fx-border-color:lightgrey; -fx-background-color: #aaaaff;");
			lbl.setId("" + i);
			lbl.setOnMouseClicked(event -> this.onLblClicked(event));
			tmp.getChildren().add(lbl);
		}
		
		for(int i = 1; i < 200 ; i++) 
		{
			Label lbl2 = new Label("CODE");
			lbl2.setLayoutY((i - 1) * lineHeight);
			lbl2.setMaxHeight(20);
			lbl2.setPrefWidth(((AnchorPane)(pane.getParent())).getWidth() - lineNumberWidth-20);
			lbl2.setStyle("-fx-border-color: #aaaaff; -fx-background-color: #aaaaff;");
			lbl2.setId("" + i);
			lbl2.setOnMouseClicked(event -> this.onLblClicked(event));
			tmp2.getChildren().add(lbl2);
		}
		
		tmp2.setLayoutX(35);
		
		pane.setPrefWidth(((AnchorPane)(pane.getParent())).getWidth());
		pane.setPrefHeight(((AnchorPane)(pane.getParent())).getHeight());

		tmp.getChildren().add(tmp2);
		
		pane.setContent(tmp);
	}
	
	public void onLblClicked(Event e)
	{
		Label lbl = (Label)e.getSource();
		Label lbl2 = (Label)((Pane)lbl.getParent().getParent()).getChildren().get(Integer.parseInt(lbl.getId())-1);
		
		//print line number
		//System.out.println(lbl.getId());
		
		lbl.setStyle("-fx-border-color: #000000; -fx-background-color: #dd9999;");
		lbl2.setStyle("-fx-border-color: #000000; -fx-background-color: #dd9999;");
	}
}
