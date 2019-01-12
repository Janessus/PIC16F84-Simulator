package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import gui.GUI_Main;

import java.util.ArrayList;;

public class Parser
{
	/**
	 * @param file the file to be parsed
	 * @return List of operations as long value
	 */
	public List<Integer> parseFile(File file) {
		System.out.println("parsing file");
		
		// Create FileInputStream and BufferedReader
		FileInputStream stream = null;
      try {
          stream = new FileInputStream(file);
      } catch (FileNotFoundException e) {
          e.printStackTrace();
      }
      BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
      
      // Read lines
      List<Integer> operations = new ArrayList<Integer>();
      String line;
      try {
      	while ((line = reader.readLine()) != null) 
      	{
      		//Display Code in GUI
      		if(line.charAt(5) != ' ') {  // Check if 5th character isn't empty
         		operations.add(Integer.parseInt(line.substring(5, 9), 16)); // Parse characters 5 to 8
         		GUI_Main.codePanel.appendText(line + "\n");//Display Code in GUI
         	}
      		else
      			GUI_Main.codePanel.appendText("\t\t\t    " + line.trim() + "\n");//Display Code in GUI
         }
   	} catch (IOException e) {
   		e.printStackTrace();
		}
      
     // Close reader
     try {
         reader.close();
     } catch (IOException e) {
         e.printStackTrace();
     }
     return operations;
	}
}
