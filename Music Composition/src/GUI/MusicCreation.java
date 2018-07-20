package GUI;

import java.util.HashMap;
import java.util.ArrayList;

import entities.MeasureDurations;
import entities.MeasureIntervals;
import entities.Epoch;
import entities.Note;

import controllers.MusicCompositionController;
import controllers.InputController;
import controllers.midiOutputController;
import controllers.StatisticsOutputController;

public class MusicCreation {

	/*
	 * Controls the compostion of music generation
	 */
	private MusicCompositionController musicCompController;
	 //map that contains all the epochs and their values
    private HashMap<String, Epoch> epochs;
    
    private int prevPitch = 0;
    
    private String newPitchName = "";
    
    private ArrayList<Note> notes;
    
    private ArrayList<Integer> measureMarkers;
    
    // variables to ensure the composer runs linearly
    public int myOctave = 5, currentDiff = 0, range = 14, defaultDuration, newPitch = 60;
	
	public MusicCreation() {
		epochs = InputController.readInput();		
		musicCompController = new MusicCompositionController(epochs);
		musicCompController.changeEpoch("classical");
		
		notes = new ArrayList<Note>();
		measureMarkers = new ArrayList<Integer>();
		
		for(int i = 0; i < 10; i++) {
			runBetterGenerator();
		}
		StatisticsOutputController.outputStatistics(notes, measureMarkers, "/home/rstrelow001/MusicComposition/CSCI-UR-Music-Composition/Audio Files/7-20-18/classical_6_8_organ_stats.txt");
		midiOutputController.outputMidi(notes, "/home/rstrelow001/MusicComposition/CSCI-UR-Music-Composition/Audio Files/7-20-18/classical_6_8_organ.mid");	
		
	}
	
	 public void runBetterGenerator() {
	    	
	    	MeasureDurations newDurations = musicCompController.measureDurationsGenerator();
	    	MeasureIntervals newIntervals;
	    	//if (newDurations.getSize() > 0)
	    	//	newIntervals = musicCompController.measureIntervalsGenerator(newDurations.getSize());
	    	//else
	    	//	newIntervals = musicCompController.measureIntervalsGenerator(2);
	    	newIntervals = musicCompController.EpochIntervalGenerator(newDurations.getSize());
	    	while(newDurations.hasNextDuration()) {
	    		Note newNote = newDurations.nextDuration();
	    		System.out.println("Duration: " + newNote.getDurationName());
	    	    System.out.println("Time Value: " + newNote.getDuration());
	    		if (!newNote.isRest()) {
	    			int newInterval = newIntervals.nextInterval();
	    			System.out.println("Interval: " + newInterval);
	    			prevPitch = playNextNote(newInterval, prevPitch, newNote.getDuration());
	    			newNote.setPitch(newPitch);
	    			newNote.setPitchName(newPitchName);	    			
	    		}
	    		System.out.println();
	    		notes.add(newNote);
	    	}
	    	measureMarkers.add(notes.size()-1);
	    	System.out.println("-------------END OF MEASURE--------------\n");
	    	newDurations.resetDurations();
	    	newIntervals.resetIntervals();
	    }
	 
	 
	 /*
	     * Method designed to generate a new musical note value based on given previous note value
	     * @param int prevVal
	     * @returns int newVal
	     * */	
		public int playNextNote(int newInterval, int prevVal, int duration){
			if (prevVal == 0){
				//TODO: add stuff to play C
				toNote(1);
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

			newPitch = toNote(newVal);

			return newVal;


		}

		
		/*
		 * Method that takes note value representation from binary as integer, prints corresponding
		 * value and plays note using MIDI output
		 * @param int val - Value of note (1-13) generated by the rule system
		 * @returns String letter value equivelant to corresponding int value
		 * */
		public int toNote(int val) {
			int noteVal;
			int C = myOctave * 12;

			if(val == 1 || val == 13){
				noteVal = C+0;
				System.out.println("New Pitch = C");
				newPitchName = "C";
			}
			else if(val == 2){
				noteVal = C+1;
				System.out.println("New Pitch = C#/D-");
				newPitchName = "C#";
			}
			else if(val == 3){
				noteVal = C+2;
				System.out.println("New Pitch = D");
				newPitchName = "D";
			}
			else if(val == 4){
				noteVal = C+3;
				System.out.println("New Pitch = D#/E-");
				newPitchName = "Eb";
			}
			else if(val == 5){
				noteVal = C+4;
				System.out.println("New Pitch = E");
				newPitchName = "E";
			}
			else if(val == 6){
				noteVal = C+5;
				System.out.println("New Pitch = F");
				newPitchName = "F";
			}
			else if(val == 7){
				noteVal = C+6;
				System.out.println("New Pitch = F#/G-");
				newPitchName = "F#";
			}
			else if(val == 8){
				noteVal = C+7;
				System.out.println("New Pitch = G");
				newPitchName = "G";
			}
			else if(val == 9){
				noteVal = C+8;
				System.out.println("New Pitch = G#/A-");
				newPitchName = "Ab";
			}
			else if(val == 10){
				noteVal = C+9;
				System.out.println("New Pitch = A");
				newPitchName = "A";
			}
			else if(val == 11){
				noteVal = C+10;
				System.out.println("New Pitch = A#/B-");
				newPitchName = "Bb";
			}
			else if(val == 12){
				noteVal = C+11;
				System.out.println("New Pitch = B");
				newPitchName = "B";
			}
			else {
				return 0;
			}			
			System.out.println("New Midi Value = " + noteVal);
			return noteVal;
		}
	 
	 public static void main(String args[]) {
		 new MusicCreation();
	 }
}
