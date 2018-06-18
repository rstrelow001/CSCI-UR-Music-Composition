package entities;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Queue;
import java.util.HashMap;

public class MeasureDurations {
	
	private int length;	
	
	private int currentPosition;
	
	
	private ArrayList<Note> notes;
	
	private double probability;
	
	private final int DEFAULT_WHOLENOTE_DURATION = 4000;
	
	
	
	public MeasureDurations(String type, double probability) {
		this.setProbability(probability);
		notes =  new ArrayList<Note>();
		currentPosition = 0;
		
		int nextPosition = type.indexOf('-');
		if (nextPosition == -1) {
			convert(type);
		}
		else {
			while (nextPosition != -1) {
				convert(type.substring(0, nextPosition));

				type = type.substring(++nextPosition);			
				nextPosition = type.indexOf('-');	
			}
			convert(type);
		}
	}
	
	
	private void convert(String token) {
		
		String convertedToken = "";
		int duration = DEFAULT_WHOLENOTE_DURATION;
		double multiplier = 1.0;
		boolean isDotted = false;
		boolean isRest = false;
		
		//calculate if the note is dotted
		if (token.contains(".")) {
			convertedToken = "Dotted-";
			isDotted = true;
			multiplier = 1.5;
		}
		
		//calculate how long the note is
		if (token.contains("64")) {
			convertedToken += "Sixty-Fourth";
			multiplier /= 64;}
		else if (token.contains("32")) {
			convertedToken += "Thirty-Second";
			multiplier /= 32;}
		else if (token.contains("16")) {
			convertedToken += "Sixteenth";
			multiplier /= 16;}
		else if (token.contains("8")) {
			convertedToken += "Eighth";
			multiplier /= 8;}
		else if (token.contains("4")) {
			convertedToken += "Quarter";
			multiplier /= 4;}
		else if (token.contains("2")) {
			convertedToken += "Half";
			multiplier /= 2;}
		else if (token.contains("1")) {
			convertedToken += "Whole";
			}
		
		//calculate if it is a rest
		if(token.contains("r")) {
			convertedToken += " Rest";
			isRest = true;
		}
		else {
			convertedToken += " Note";
			length++;
		}
		
		duration *= multiplier;
		
		Note nextNote = new Note(duration, convertedToken, isRest);
		notes.add(nextNote);
	}
	
	
	public boolean hasNextDuration() {
		return (currentPosition < notes.size());	
	}
	
	
	public void resetDurations() {
		this.currentPosition = 0;
	}
	
	
	public Note nextDuration() {		
		return notes.get(currentPosition++);
	}


	public double getProbability() {
		return probability;
	}


	public void setProbability(double probability) {
		this.probability = probability;
	}
	
	
	public int getSize() {
		return length;
	}


	public void setSize(int length) {
		this.length = length;
	}
	

}
