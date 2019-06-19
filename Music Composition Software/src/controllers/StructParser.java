package controllers;


import java.io.FileReader;

import java.util.ArrayList;
import java.util.Arrays;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import entities.MusicStruct;

/**
 * StructParser reads a JSON file that is structured
 * as a signature(String), adjacentStruct(A list of Strings)
 * and adjacentStructFrequencies(A list of Integers) and prepares it
 * to be read by CSCI-UR-MUSIC-COMPOSITION.
 * 
 * @author Andrew Joseph Heroux
 * @version 1.0
 * @since June 11, 2019
 */
public class StructParser {
	
	//instance variable
	ArrayList<MusicStruct> musicStructs = new ArrayList<MusicStruct>();
	
	
	/**
	 * Public constructor takes in a file name of an appropriately
	 * structured JSON file for parsing into a MusicStruct object.
	 * 
	 * @param fileName
	 */
	public StructParser(String fileName) {
		this.loadData(fileName);
	}
	
	
	/**
	 * Loads data from a specified file into the instance variable
	 * musicStructs.
	 * 
	 * @param fileName
	 */
	public void loadData(String fileName) {
		
		
		JSONParser jsonParser = new JSONParser();
		
		try {
			
			//Load the entire JSON file
			JSONArray jsonArray = (JSONArray) jsonParser.parse(new FileReader("jsonFiles/" + fileName));
			
			//For loop iterates through the file one struct at a time
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObject = (JSONObject) jsonArray.get(i);
				
				//Get the signature sequence
				String signatureSequenceStr = (String) jsonObject.get("measureSignature");
				ArrayList<String> signatureSequence = new ArrayList<String>(Arrays.asList(signatureSequenceStr.split("&&")));
				
				//Get the signature
				String signature = signatureSequence.get(signatureSequence.size() - 1);
				
				//Get the list of adjacent MusicStructs
				@SuppressWarnings("unchecked")
				ArrayList<String> adjacentStructs = (ArrayList<String>) jsonObject.get("adjacentMeasures");
				
				//Get the list of adjacent frequencies
				@SuppressWarnings("unchecked")
				ArrayList<Integer> adjacentFrequencies = (ArrayList<Integer>) jsonObject.get("adjacentMeasureFrequencies");
				
				//Construct a new MusicStruct with the signature
				MusicStruct newMusicStruct = new MusicStruct(signature, signatureSequence);
				
				//For loop adds the adjacent structs along with their frequencies to the new MusicStruct
				for (int j = 0; j < adjacentStructs.size(); j++) {
					String adjacentStruct = adjacentStructs.get(j);
					int frequency = Integer.parseInt("" + adjacentFrequencies.get(j));
					newMusicStruct.addAdjacentStruct(adjacentStruct, frequency);
				}
				newMusicStruct.sortByFrequency();
				newMusicStruct.assignProbabilities();
				this.musicStructs.add(newMusicStruct);
				
				
			}
		} catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	/**
	 * Getter for the ArrayList musicStrucs
	 * @return musicStructs
	 */
	public ArrayList<MusicStruct> getMusicStructs(){
		return this.musicStructs;
	}
}
