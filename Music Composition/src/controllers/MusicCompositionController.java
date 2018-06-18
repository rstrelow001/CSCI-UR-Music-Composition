package controllers;

/*
 * Algorithmic Music Composition Software
 * @author Tom Donald Richmond
 * @author Ryan Strelow
 * @version 2.0
 * @since 02/12/17
 */

import java.util.HashMap;

import java.util.ArrayList;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import entities.Epoch;
import entities.Note;
import entities.DurationProbability;
import entities.MeasureDurations;
import entities.MeasureIntervals;

public class MusicCompositionController {

	// variable to store the current Epoch
	private Epoch epoch;	
    // variables to ensure the composer runs linearly
    public int myOctave = 5, currentDiff = 0, range, defaultDuration;
    // variable to store the probability of each interval
    // Variable to hold measure types with their durations for the current epoch
    ArrayList<MeasureDurations> measureTypes;
    HashMap<Integer, ArrayList<MeasureIntervals>> measureIntervals;
    // boolean to see if an epoch has been selected
    boolean selected = false;
	// variables to track total length of composition, total notes, and total rests
	int t, totalNotes, totalRests;
    // variables to track the occurrences of each interval for testing
    int[] totals = new int[29];
    // variable to hold string value representing era
    String era;
    //map that contains all the epochs and their values
    private HashMap<String, Epoch> epochs;
	
	public MusicCompositionController(HashMap<String, Epoch> epochs) {
		this.epochs = epochs;
	}
	
	
    /*
     * Method to re-adjust the probability values when new epoch is selected
     * @param String representing epoch
     */
    public void changeEpoch(String epoch) {
    	Epoch newEpoch = epochs.get(epoch); 
    	range = newEpoch.getRange();
    	defaultDuration = newEpoch.getDefaultDuration();
    	era = newEpoch.getEra();   
    	
    	measureTypes = newEpoch.getDurationPatterns();
    	measureIntervals = newEpoch.getIntervalPatterns();
    }
    
      
    /*
     * Method designed to generate a new musical note value based on given previous note value
     * @param Boolean isRest  Represents if the note is a rest or not
     * @returns int newNoteDuration
     * */	
	public MeasureDurations measureDurationsGenerator(){
		
		int i = 0;
		MeasureDurations tempMeasure = measureTypes.get(0);
		/* Resets the valFound var to false for next note generation */
		boolean valFound = false;
		
		double running = 0.0;
		double value = Math.random();
		
		while (!valFound && i < measureTypes.size()) {
			tempMeasure = measureTypes.get(i);
			double measureProbability = tempMeasure.getProbability();
			if (value <= measureProbability + running) 
				valFound = true;
			
			running += measureProbability;
			i++;
		}
		

		return tempMeasure;


	}
	
	
	public MeasureIntervals measureIntervalsGenerator(int size){
		
		int i = 0;
		double newTotalProbability = 0;
		ArrayList<MeasureIntervals> availableIntervals = measureIntervals.get(size);
		for (MeasureIntervals nextMeasure: availableIntervals) {
			newTotalProbability += nextMeasure.getProbability();
		}
		for (MeasureIntervals nextMeasure: availableIntervals) {
			nextMeasure.setProbability(nextMeasure.getProbability()/newTotalProbability);
		}
		MeasureIntervals tempMeasure = availableIntervals.get(0);
		/* Resets the valFound var to false for next note generation */
		boolean valFound = false;
		
		double running = 0.0;
		double value = Math.random();
		
		while (!valFound && i < availableIntervals.size()) {
			tempMeasure = availableIntervals.get(i);
			double measureProbability = tempMeasure.getProbability();
			if (value <= measureProbability + running) 
				valFound = true;
			
			running += measureProbability;
			i++;
		}
		

		return tempMeasure;


	}
	
	
	 /*
     * Method designed to generate a new musical note value based on given previous note value
     * @param int prevVal
     * @returns int newVal
     * */	
	public int playNextNote(int newInterval, int prevVal, int duration){
		if (prevVal == 0){
			//TODO: add stuff to play C
			toNote(1, duration);
			return 1;
		}
		/* Sets ascLim and descLim to half of the average range of the 
		 * given epoch. DescLim gets the ceiling arbitrarily*/
		int ascLim = range/2;
		int descLim= (range/2) + (range%2);

		double running = 0.0;
		double value = Math.random();
		//System.out.println(value);

		int newVal;
		int diff = 0;
		int direction = (int)(Math.random()*2);

		/* determines before each note whether it was generated to be ascending
		 * or descending. This process is regulated with ascLim and descLim */
		boolean ascending = false;
		if(direction == 1)
			ascending = true;

		/* Resets the valFound var to false for next note generation */
		boolean valFound = false;
		
		/* boolean signifying if the note is a rest*/
		boolean isRest = false;



		//System.out.println((currentDiff+diff) +": total diff");
		if (ascending && currentDiff + newInterval >= ascLim) {
			System.out.println("Switched, too high");
			ascending = false;
		}
		if (!ascending && -1*(currentDiff - newInterval) >= descLim) {
			System.out.println("Switched, too low");
			ascending = true;
		}
		System.out.println("Ascending = "+ascending);
		if(ascending){
			currentDiff += newInterval;
			System.out.println("Current Difference = " + currentDiff);
			newVal = prevVal;
			for (int i = 0; i < newInterval; i++){
				if (newVal == 5 || newVal == 12)
					newVal += 1;
				else
					newVal += 2;
				if (newVal > 12) {
					myOctave++;
					newVal -= 12;
				}
			}
		}
		else{
			currentDiff -= newInterval;
			System.out.println("Current Difference = " + currentDiff);
			newVal = prevVal;
			for (int i = 0; i < newInterval; i++){
				if (newVal == 6 || newVal == 13 || newVal == 1)
					newVal -= 1;
				else
					newVal -= 2;
				if (newVal < 1) {
					newVal += 12;
					myOctave--;
				}
			}
		}
		
		//return newVal;

		int	noteVal = toNote(newVal, duration);

		return noteVal;


	}

	
	/*
	 * Method that takes note value representation from binary as integer, prints corresponding
	 * value and plays note using MIDI output
	 * @param int val - Value of note (1-13) generated by the rule system
	 * @returns String letter value equivelant to corresponding int value
	 * */
	public int toNote(int val, int duration) {
		int noteVal;
		int C = myOctave * 12;

		if(val == 1 || val == 13){
			noteVal = C+0;
			System.out.println("New Pitch = C\n");
		}
		else if(val == 2){
			noteVal = C+1;
			System.out.println("New Pitch = C#/D-\n");
		}
		else if(val == 3){
			noteVal = C+2;
			System.out.println("New Pitch = D\n");
		}
		else if(val == 4){
			noteVal = C+3;
			System.out.println("New Pitch = D#/E-\n");
		}
		else if(val == 5){
			noteVal = C+4;
			System.out.println("New Pitch = E\n");
		}
		else if(val == 6){
			noteVal = C+5;
			System.out.println("New Pitch = F\n");
		}
		else if(val == 7){
			noteVal = C+6;
			System.out.println("New Pitch = F#/G-\n");
		}
		else if(val == 8){
			noteVal = C+7;
			System.out.println("New Pitch = G\n");
		}
		else if(val == 9){
			noteVal = C+8;
			System.out.println("New Pitch = G#/A-\n");
		}
		else if(val == 10){
			noteVal = C+9;
			System.out.println("New Pitch = A\n");
		}
		else if(val == 11){
			noteVal = C+10;
			System.out.println("New Pitch = A#/B-\n");
		}
		else if(val == 12){
			noteVal = C+11;
			System.out.println("New Pitch = B\n");
		}
		else {
			return 0;
		}
		//System.out.println(noteVal);
		playNote(noteVal, duration);
		return val;
	}

	
	/*
	 * Method to play note value using MIDI synthesizer based upon input note
	 * @param int representing the MIDI value of desired note.
	 */
	public void playNote(int i, int duration) { 
	    try{
	    	/* Create a new Synthesizer and open it. 
	    	 */
		    	Synthesizer midiSynth = MidiSystem.getSynthesizer(); 
		    	midiSynth.open();
		    	
		    	//get and load default instrument and channel lists
		    	Instrument[] instr = midiSynth.getDefaultSoundbank().getInstruments();
		    	//Instrument[] instr = midiSynth.getAvailableInstruments();
		    	//System.out.println("Number of instruments: " + instr.length);
		    	MidiChannel[] mChannels = midiSynth.getChannels();
		      
		    	midiSynth.loadInstrument(instr[120]);//load an instrument
		    	mChannels[0].noteOff(i);//turn off the previous note
		    	mChannels[0].noteOn(i, 120);//On channel 0, play note number i with velocity 120
		    	try {
		    		//Following line controls duration of notes played. 1000 used for samples of 30 seconds. 750 used for samples of 15 seconds
		    		Thread.sleep(duration); // wait time in milliseconds to control duration
		    	}
		    	catch( InterruptedException e ) {}
	    } 
	    catch (MidiUnavailableException e) {}
	}


	/*
	 * method that returns string that prints composition statistics for visual analysis
	 * @returns String statistics
	 */
	public String printResults() {
		
		return "Total length of composition: "+t+"\n"
				+"\tStatistics:\n"
				+"\nTotal Notes:\t " + totalNotes + "\n"
				+"\nTotal Rests:\t " + totalRests + "\n"
				+"\nUnison:\t "+((double)totals[0]/t)
				+"\nStep:\t "+((double)totals[1]/t)
				+"\nThird:\t "+((double)totals[2]/t)
				+"\nForth:\t "+((double)totals[3]/t)
				+"\nFifth:\t "+((double)totals[4]/t)
				+"\nSixth:\t "+((double)totals[5]/t)
				+"\nSeventh:\t "+((double)totals[6]/t)
				+"\nOctave:\t "+((double)totals[7]/t)
				+"\nRests:\t "+((double)totals[8]/t) + "\n"
				+"\nTwenty-Eighth Notes:\t "+((double)totals[9]/totalNotes)
				+"\nTwenty-Forth Notes:\t "+((double)totals[10]/totalNotes)
				+"\nTwentieth Notes:\t "+((double)totals[11]/totalNotes)
				+"\nSixteenth Notes:\t "+((double)totals[12]/totalNotes)
				+"\nTwelfth Notes:\t "+((double)totals[13]/totalNotes)
				+"\nEighth Notes:\t "+((double)totals[14]/totalNotes)
				+"\nDotted-Eighth Notes:\t "+((double)totals[15]/totalNotes)
				+"\nSixth Notes:\t "+((double)totals[16]/totalNotes)
				+"\nQuarter Notes:\t "+((double)totals[17]/totalNotes)
				+"\nDotted-Quarter Notes:\t "+((double)totals[18]/totalNotes)
				+"\nHalf Notes:\t "+((double)totals[19]/totalNotes)
				+"\nDotted-Half Notes:\t "+((double)totals[20]/totalNotes)
				+"\nWhole Notes:\t "+((double)totals[21]/totalNotes)
				+"\nDotted-Whole Notes:\t "+((double)totals[22]/totalNotes)+"\n"
				+"\nEighth Rests:\t "+((double)totals[23]/totalRests)
				+"\nQuarter Rests:\t "+((double)totals[24]/totalRests)
				+"\nHalf Rests:\t "+((double)totals[25]/totalRests)
				+"\nDotted-Half Rests:\t "+((double)totals[26]/totalRests)
				+"\nWhole Rests:\t "+((double)totals[27]/totalRests)
				+"\nDotted-Whole Rests:\t "+((double)totals[28]/totalRests);
	}
	
	
	/*
	 * method that returns string that prints composition statistics for analysis
	 * @returns String statistics
	 */
	public String kernResults() {
		//variable to store percentage of most common interval
		int max = 0;
		
		// computes the most common interval
		for(int i = 0; i<8;i++) {
			if(totals[i] > max){
				max = totals[i];
			}
		}
		
		//returns expected String output based on totals array and above computation
		return ""+((double)totals[0]/t)
				+","+((double)totals[1]/t)
				+","+((double)totals[2]/t)
				+","+((double)totals[3]/t)
				+","+((double)totals[4]/t)
				+","+((double)totals[5]/t)
				+","+((double)totals[6]/t)
				+","+((double)totals[7]/t)
				+","+((double)totals[8]/t)
				+","+((double)totals[9]/totalNotes)
				+","+((double)totals[10]/totalNotes)
				+","+((double)totals[11]/totalNotes)
				+","+((double)totals[12]/totalNotes)
				+","+((double)totals[13]/totalNotes)
				+","+((double)totals[14]/totalNotes)
				+","+((double)totals[15]/totalNotes)
				+","+((double)totals[16]/totalNotes)
				+","+((double)totals[17]/totalNotes)
				+","+((double)totals[18]/totalNotes)
				+","+((double)totals[19]/totalNotes)
				+","+((double)totals[20]/totalNotes)
				+","+((double)totals[21]/totalNotes)
				+","+((double)totals[22]/totalNotes)
				+","+((double)totals[23]/totalRests)
				+","+((double)totals[24]/totalRests)
				+","+((double)totals[25]/totalRests)
				+","+((double)totals[26]/totalRests)
				+","+((double)totals[27]/totalRests)
				+","+((double)totals[28]/totalRests)
				+","+era;
	}

	
	/*
	 * Method to clear the statistics after terminations for next composition
	 */
	public void clearStats() {
		//loops through all saved data and resets to 0 for future processing
		for (int i = 0; i < totals.length; i++) {
			totals[i] = 0;
		}
		t = 0;
		totalNotes = 0;
		totalRests = 0;
	}

}


