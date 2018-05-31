package controllers;

/*
 * Algorithmic Music Composition Software
 * @author Tom Donald Richmond
 * @author Ryan Strelow
 * @version 2.0
 * @since 02/12/17
 */

import java.util.HashMap;


import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import entities.Epoch;
import entities.Note;
import entities.DurationProbability;

public class MusicCompositionController {

	// variable to store the current Epoch
	private Epoch epoch;	
    // variables to ensure the composer runs linearly
    public int myOctave = 5, currentDiff = 0, range, defaultDuration;
    // variable to store the probability of each interval
    double uni, step, third, fourth, fifth, sixth, seventh, octave, rest;
    // variables to store the probability of the next note type
	double toTwentyEighthNote, toTwentyForthNote, toTwentiethNote, toSixteenthNote, toTwelfthNote,
		toEighthNote, toDottedEighthNote, toSixthNote, toQuarterNote, toDottedQuarterNote, 
		toHalfNote, toDottedHalfNote, toWholeNote, toDottedWholeNote;		
    double toEighthRest, toQuarterRest, toHalfRest, toDottedHalfRest, toWholeRest, toDottedWholeRest;
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
    	uni = newEpoch.getUni();
    	step = newEpoch.getStep();
    	third = newEpoch.getThird();
    	fourth = newEpoch.getFourth();
    	fifth = newEpoch.getFifth();
    	sixth = newEpoch.getSixth();
    	seventh = newEpoch.getSeventh();
    	octave = newEpoch.getOctave();
    	rest = newEpoch.getRest();
    	range = newEpoch.getRange();
    	defaultDuration = newEpoch.getDefaultDuration();
    	era = newEpoch.getEra();   
    	
    	toTwentyEighthNote = newEpoch.getCurrentProbability().getToTwentyEighthNote();
    	toTwentyForthNote = newEpoch.getCurrentProbability().getToTwentyForthNote();
    	toTwentiethNote = newEpoch.getCurrentProbability().getToTwentiethNote();
    	toSixteenthNote = newEpoch.getCurrentProbability().getToSixteenthNote();
    	toTwelfthNote = newEpoch.getCurrentProbability().getToTwelfthNote();
    	toEighthNote = newEpoch.getCurrentProbability().getToEighthNote();
    	toDottedEighthNote = newEpoch.getCurrentProbability().getToDottedEighthNote();
    	toSixthNote = newEpoch.getCurrentProbability().getToSixthNote();
    	toQuarterNote = newEpoch.getCurrentProbability().getToQuarterNote();
    	toDottedQuarterNote = newEpoch.getCurrentProbability().getToDottedQuarterNote();
    	toHalfNote = newEpoch.getCurrentProbability().getToHalfNote();
    	toDottedHalfNote = newEpoch.getCurrentProbability().getToDottedHalfNote();
    	toWholeNote = newEpoch.getCurrentProbability().getToWholeNote();
    	toDottedWholeNote = newEpoch.getCurrentProbability().getToDottedWholeNote();
    			
    	toEighthRest = newEpoch.getCurrentProbability().getToEighthRest();
    	toQuarterRest = newEpoch.getCurrentProbability().getToQuarterRest();
    	toHalfRest = newEpoch.getCurrentProbability().getToHalfRest();
    	toDottedHalfRest = newEpoch.getCurrentProbability().getToDottedHalfRest();
    	toWholeRest = newEpoch.getCurrentProbability().getToWholeRest();
    	toDottedWholeRest = newEpoch.getCurrentProbability().getToDottedWholeRest();
    }
    
    
//    public Note createNote(Note prevNote) { 
//    	int newNotePitch = ruleGenerator(prevNote.getPitch());
//    	if (newNotePitch == -1)
//    		return prevNote;
//    	int newNoteDuration = rhythmRuleGenerator(prevNote.getDuration(), newNotePitch);
//    	
//		System.out.println("New Value = " + newNotePitch);
//    	//pitch will be 14 if it is supposed to be a rest
//    	if (newNotePitch == 14) {
//			try  {
//				Thread.sleep(newNoteDuration); }
//			catch( InterruptedException e ) {}
//    	}
//    	else {
//    		toNote(newNotePitch, newNoteDuration);
//    	}
//    	
//    	return new Note(newNotePitch, newNoteDuration);
//    }
	    
    /*
     * Method designed to generate a new musical note value based on given previous note value
     * @param Boolean isRest  Represents if the note is a rest or not
     * @returns int newNoteDuration
     * */	
	public int rhythmRuleGenerator(int prevDuration, int newPitch){

		double running = 0.0;
		double value = Math.random();

		/* Resets the valFound var to false for next note generation */
		boolean valFound = false;

		/* checks which range the generated number falls in and produces a
		 * note duration based on this value. Once note duration is found, valFound is set to
		 * true, and no other if statements are reached. It will access each
		 * if statement until the correct is found, increasing running total
		 * as it goes. */
		//int defaultDuration = 4000;
		int newNoteDuration = defaultDuration;
		if (newPitch != 14) {
			if (value <= toTwentyEighthNote){
				totals[9]+=1;
				newNoteDuration = defaultDuration/28;
				valFound = true;
				System.out.println("Duration: Twenty-Eighth Note");
			}
			running += toTwentyEighthNote;
			if (value <= toTwentyForthNote + running && valFound == false){
				totals[10]+=1;
				newNoteDuration = defaultDuration/24;
				valFound = true;
				System.out.println("Duration: Twenty-Forth Note");
			}
			running += toTwentyForthNote;
			if (value <= toTwentiethNote + running && valFound == false){
				totals[11]+=1;
				newNoteDuration = defaultDuration/20;
				valFound = true;
				System.out.println("Duration: Twentieth Note");
			}
			running += toTwentiethNote;
			if (value <= toSixteenthNote + running && valFound == false){
				totals[12]+=1;
				newNoteDuration = defaultDuration/16;
				valFound = true;
				System.out.println("Duration: Sixteenth Note");
			}
			running += toSixteenthNote;
			if (value <= toTwelfthNote + running && valFound == false){
				totals[13]+=1;
				newNoteDuration = defaultDuration/12;
				valFound = true;
				System.out.println("Duration: Twelfth Note");
			}
			running += toTwelfthNote;
			if (value <= toEighthNote + running && valFound == false){
				totals[14]+=1;
				newNoteDuration = defaultDuration/8;
				valFound = true;
				System.out.println("Duration: Eighth Note");
			}
			running += toEighthNote;
			if (value <= toDottedEighthNote + running && valFound == false){
				totals[15]+=1;
				newNoteDuration = defaultDuration/8 + defaultDuration/16;
				valFound = true;
				System.out.println("Duration: Dotted-Eighth Note");
			}
			running += toDottedEighthNote;
			if (value <= toSixthNote + running && valFound == false){
				totals[16]+=1;
				newNoteDuration = defaultDuration/8 + defaultDuration/16;
				valFound = true;
				System.out.println("Duration: Sixth Note");
			}
			running += toSixthNote;
			if ((value <= toQuarterNote + running) && valFound == false){
				totals[17]+=1;
				newNoteDuration = defaultDuration/4;
				valFound = true;
				System.out.println("Duration: Quarter Note");
			}
			running += toQuarterNote;
			if (value <= toDottedQuarterNote + running && valFound == false){
				totals[18]+=1;
				newNoteDuration = defaultDuration/4 + defaultDuration/8;
				valFound = true;
				System.out.println("Duration: Dotted-Quarter Note");
			}
			running += toDottedQuarterNote;
			if (value <= toHalfNote + running && valFound == false){
				totals[19]+=1;
				newNoteDuration = defaultDuration/2;
				valFound = true;
				System.out.println("Duration: Half Note");
			}
			running += toHalfNote;
			if (value <= toDottedHalfNote + running && valFound == false){
				totals[20]+=1;
				newNoteDuration = defaultDuration/2 + defaultDuration/4;
				valFound = true;
				System.out.println("Duration: Dotted-Half Note");
			}
			running += toDottedHalfNote;
			if (value <= toWholeNote + running && valFound == false){
				totals[21]+=1;
				newNoteDuration = defaultDuration;
				valFound = true;
				System.out.println("Duration: Whole Note");
			}
			running += toWholeNote;
			if (value <= toDottedWholeNote + running && valFound == false){
				totals[22]+=1;
				newNoteDuration = defaultDuration + defaultDuration/2;
				valFound = true;
				System.out.println("Duration: Dotted-Whole Note");
			}
			
		}
		else {
			if (value <= toEighthRest){
				totals[23]+=1;
				newNoteDuration = defaultDuration/8;
				valFound = true;
				System.out.println("Duration: Eighth Rest");
			}
			running += toEighthRest;
			if ((value <= toQuarterRest + running) && valFound == false){
				totals[24]+=1;
				newNoteDuration = defaultDuration/4;
				valFound = true;
				System.out.println("Duration: Quarter Rest");
			}
			running += toQuarterRest;
			if (value <= toHalfRest + running && valFound == false){
				totals[25]+=1;
				newNoteDuration = defaultDuration/2;
				valFound = true;
				System.out.println("Duration: Half Rest");
			}
			running += toHalfRest;
			if (value <= toDottedHalfRest + running && valFound == false){
				totals[26]+=1;
				newNoteDuration = defaultDuration/2 + defaultDuration/4;
				valFound = true;
				System.out.println("Duration: Dotted-Half Rest");
			}
			running += toDottedHalfRest;
			if (value <= toWholeNote + running && valFound == false){
				totals[27]+=1;
				newNoteDuration = defaultDuration;
				valFound = true;
				System.out.println("Duration: Whole Rest");
			}
			running += toWholeRest;
			if (value <= toDottedWholeRest + running && valFound == false){
				totals[28]+=1;
				newNoteDuration = defaultDuration + defaultDuration/2;
				valFound = true;
				System.out.println("Duration: Dotted-Whole Rest");
			}
			
		}

		return newNoteDuration;
	}
	
	
    /*
     * Method designed to generate a new musical note value based on given previous note value
     * @param int prevVal
     * @returns int newVal
     * */	
	public int ruleGenerator(int prevVal){
		if (prevVal == 0){
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

		/* checks which range the generated number falls in and produces a
		 * note based on this value. Once note is found, valFound is set to
		 * true, and no other if statements are reached. It will access each
		 * if statement until the correct is found, increasing running total
		 * as it goes. */
		
		if (value <= uni){
			totals[0]+=1;
			t+=1;
			totalNotes+=1;
			diff = 0;
			valFound = true;
			System.out.println("Interval: Unison");
		}
		running += uni;
		if ((value <= step + running) && valFound == false){
			totals[1]+=1;
			t+=1;
			totalNotes+=1;
			diff =  1;
			valFound = true;
			System.out.println("Interval: Step");
		}
		running += step;
		if (value <= third + running && valFound == false){
			totals[2]+=1;
			t+=1;
			totalNotes+=1;
			diff =  2;
			valFound = true;
			System.out.println("Interval: Third");
		}
		running += third;
		if (value <= fourth + running && valFound == false){
			totals[3]+=1;
			t+=1;
			totalNotes+=1;
			diff =  3;
			valFound = true;
			System.out.println("Interval: Forth");
		}
		running += fourth;
		if (value <= fifth + running && valFound == false){
			totals[4]+=1;
			t+=1;
			totalNotes+=1;
			diff =  4;
			valFound = true;
			System.out.println("Interval: Fifth");
		}
		running += fifth;
		if (value <= sixth + running && valFound == false){
			totals[5]+=1;
			t+=1;
			totalNotes+=1;
			diff =  5;
			valFound = true;
			System.out.println("Interval: Sixth");
		}
		running += sixth;
		if (value <= seventh + running && valFound == false){
			totals[6]+=1;
			t+=1;
			totalNotes+=1;
			diff =  6;
			valFound = true;
			System.out.println("Interval: Seventh");
		}
		running += seventh;
		if (value <= octave + running && valFound == false){
			totals[7]+=1;
			t+=1;
			totalNotes+=1;
			diff =  7;
			valFound = true;
			System.out.println("Interval: Octave");
		}
		running += octave;
		if (value <= rest + running && valFound == false){
			totals[8]+=1;
			t+=1;
			totalRests+=1;
			valFound = true;
			isRest = true;
			prevVal = 14;
			System.out.println("Interval: Rest");
		}
		if (!valFound)
			return -1;

		//System.out.println((currentDiff+diff) +": total diff");
		if (ascending && currentDiff + diff >= ascLim) {
			System.out.println("Switched, too high");
			ascending = false;
		}
		if (!ascending && -1*(currentDiff - diff) >= descLim) {
			System.out.println("Switched, too low");
			ascending = true;
		}
		System.out.println("Ascending = "+ascending);
		if(ascending){
			currentDiff += diff;
			System.out.println("Current Difference = " + currentDiff);
			newVal = prevVal;
			for (int i = 0; i < diff; i++){
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
			currentDiff -= diff;
			System.out.println("Current Difference = " + currentDiff);
			newVal = prevVal;
			for (int i = 0; i < diff; i++){
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
		if (valFound) {
			int duration= rhythmRuleGenerator(0, newVal);
			System.out.println("New Value = " + newVal);
			
			int noteVal;
			if (isRest) {
				System.out.println();				
				noteVal = prevVal;
				try  {
					Thread.sleep(duration); }
				catch( InterruptedException e ) {}
			}
			else {				
				noteVal = toNote(newVal, duration);
			}
			return noteVal;
		}
		else 
			return prevVal;
	}

	
	/*
	 * Method designed to generate a new musical note value based on given previous note value
	 * @param int prevVal
	 * @returns int newVal
	 * */
	public void ruleGeneratorAnalysis(){

		double running = 0.0;
		double value = Math.random();

		/* Resets the valFound var to false for next note generation */
		boolean valFound = false;

		/* checks which range the generated number falls in and produces a
		 * note based on this value. Once note is found, valFound is set to
		 * true, and no other if statements are reached. It will access each
		 * if statement until the correct is found, increasing running total
		 * as it goes. */
		if (value <= uni){
			totals[0]+=1;
			t+=1;
			valFound = true;
		}
		running += uni;
		if ((value <= step + running) && valFound == false){
			totals[1]+=1;
			t+=1;
			valFound = true;
		}
		running += step;
		if (value <= third + running && valFound == false){
			totals[2]+=1;
			t+=1;
			valFound = true;
		}
		running += third;
		if (value <= fourth + running && valFound == false){
			totals[3]+=1;
			t+=1;
			valFound = true;
		}
		running += fourth;
		if (value <= fifth + running && valFound == false){
			totals[4]+=1;
			t+=1;
			valFound = true;
		}
		running += fifth;
		if (value <= sixth + running && valFound == false){
			totals[5]+=1;
			t+=1;
			valFound = true;
		}
		running += sixth;
		if (value <= seventh + running && valFound == false){
			totals[6]+=1;
			t+=1;
			valFound = true;
		}
		running += seventh;
		if (value <= octave + running && valFound == false){
			totals[7]+=1;
			t+=1;
			valFound = true;
		}

		/* When the composer has generated 100 notes, 
		 * it automatically calculates the results and prints 
		 * for analysis process */
		if(t==100) {
			System.out.println(kernResults());
			//JOptionPane.showMessageDialog(null,kernResults());
			clearStats();
		}
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
		    	MidiChannel[] mChannels = midiSynth.getChannels();
		      
		    	midiSynth.loadInstrument(instr[0]);//load an instrument
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
				+","+((double)max/t)
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
	}

}


