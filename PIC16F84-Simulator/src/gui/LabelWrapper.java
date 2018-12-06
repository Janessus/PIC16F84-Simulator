package gui;

import javafx.scene.control.Label;

public class LabelWrapper
{
	Label label = null;
	int adress = -1;
	
	public LabelWrapper(Label label, int adress)
	{
		this.label = label;
		this.adress = adress;
	}
}
