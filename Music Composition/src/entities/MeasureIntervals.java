package entities;

import java.util.ArrayList;

public class MeasureIntervals {
	
	private int length;	
	
	private int currentPosition;
		
	private ArrayList<Integer> intervals;
	
	private double probability;
		
	
	public MeasureIntervals(String type, double probability) {
		this.setProbability(probability);
		intervals =  new ArrayList<Integer>();
		currentPosition = 0;
		
		int nextPosition = type.indexOf('-');
		if (nextPosition == -1) {
			intervals.add(Integer.parseInt(type));
		}
		else {
			while (nextPosition != -1) {
				intervals.add(Integer.parseInt(type.substring(0, nextPosition)));		
				type = type.substring(++nextPosition);			
				nextPosition = type.indexOf('-');	
			}
			intervals.add(Integer.parseInt(type));
		}
		this.length = intervals.size();
	}
	
	
	public boolean hasNextInterval() {
		return (currentPosition < intervals.size());	
	}
	
	
	public void resetIntervals() {
		this.currentPosition = 0;
	}
	
	
	public int nextInterval() {		
		return intervals.get(currentPosition++);
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
