package controllers;



import java.util.ArrayList;


import entities.MusicStruct;

public class StructInterpreter {
	
	public ArrayList<String> interpretMeasure(ArrayList<String> unparsedMeasures) {
		
		ArrayList<String> parsedMeasures = new ArrayList<String>();
		
		for (int i = 0; i < unparsedMeasures.size(); i++) {
			String[] singleMeasures = unparsedMeasures.get(i).split("-");
			for (int j = 0; j < singleMeasures.length; j++) {
				parsedMeasures.add(singleMeasures[j]);
			}
		}
		return parsedMeasures;
	}
}

