package entities;

/*
 * Entity Class supporting Algorithmic Music Composition Software
 * @author Ryan Strelow
 * @version 1.0
 * @since 05/22/18
 */

import java.util.HashMap;
import java.util.ArrayList;

public class Epoch {
	
	private int range, defaultDuration;
	
	//private double uni, step, third, fourth, fifth, sixth, seventh, octave, rest;
	
	private String era;
	
	//private HashMap<String, DurationProbability> noteProbabilities;
	
	//private DurationProbability currentProbability;
	
	private ArrayList<MeasureDurations> durationPatterns;
	
	private HashMap<Integer, ArrayList<MeasureIntervals>> intervalPatterns;
	
	public Epoch(ArrayList<MeasureDurations> durationPatterns, HashMap<Integer, ArrayList<MeasureIntervals>> intervalPatterns, int range, int defaultDuration, String era) {
		
		this.setRange(range);
		this.setDefaultDuration(defaultDuration);		
		this.setEra(era);
		this.setDurationPatterns(durationPatterns);
		this.setIntervalPatterns(intervalPatterns);
		

		
	}
	
	
//	public Epoch(double uni, double step, double third, double fourth, double fifth,
//			double sixth, double seventh, double octave, double rest, int range, int defaultDuration, String era, HashMap<String, DurationProbability> noteProbabilities) {
//		
//		this.setUni(uni);
//		this.setStep(step);
//		this.setThird(third);
//		this.setFourth(fourth);
//		this.setFifth(fifth);
//		this.setSixth(sixth);
//		this.setSeventh(seventh);
//		this.setOctave(octave);
//		this.setRest(rest);
//		this.setRange(range);
//		this.setDefaultDuration(defaultDuration);		
//		this.setEra(era);
//		this.setNoteProbabilities(noteProbabilities);
//		
//		
//		//remove once prev rhythm is incorporated
//		this.setCurrentProbability(noteProbabilities.get("DEFAULT_PROBABILITIES"));
//		
//	}


	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}


	public String getEra() {
		return era;
	}

	public void setEra(String era) {
		this.era = era;
	}

	public int getDefaultDuration() {
		return defaultDuration;
	}

	public void setDefaultDuration(int defaultDuration) {
		this.defaultDuration = defaultDuration;
	}


	public ArrayList<MeasureDurations> getDurationPatterns() {
		return durationPatterns;
	}

	public void setDurationPatterns(ArrayList<MeasureDurations> durationPatterns) {
		this.durationPatterns = durationPatterns;
	}


	public HashMap<Integer, ArrayList<MeasureIntervals>> getIntervalPatterns() {
		return intervalPatterns;
	}


	public void setIntervalPatterns(HashMap<Integer, ArrayList<MeasureIntervals>> intervalPatterns) {
		this.intervalPatterns = intervalPatterns;
	}
	
	
	

}
