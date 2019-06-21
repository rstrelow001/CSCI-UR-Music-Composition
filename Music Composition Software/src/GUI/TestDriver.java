package GUI;


import java.util.ArrayList;

import controllers.*;
import entities.*;

public class TestDriver {
	
	public TestDriver() {

		//this.testIntervalConverter();

		this.testMusicCreation();

	}
	
	
	public void testStructConstructor() {
		
		StructReader structReader = new StructReader("kern.json");
		
		ArrayList<MusicStruct> structs = structReader.makeStruct(40);
		
		ArrayList<String> measures = structReader.structToString(structs);
		
		StructInterpreter si = new StructInterpreter();
		
		measures = si.interpretMeasure(measures);
		
		
		System.out.println("Printing Structs");
		structReader.printSong(structs);
		
		System.out.println("Printing Parsed Measures");
		for (int i = 0; i < measures.size(); i++) {
			System.out.println(measures.get(i));
		}
		
	}
	
	public void testIntervalConverter() {
		ArrayList<String> intervals = new ArrayList<String>();
		intervals.add("1");
		intervals.add("2");
		intervals.add("2");
		intervals.add("4");
		intervals.add("4");
		intervals.add("3");
		intervals.add("2");
		intervals.add("1");
		
		NoteConstructor nc = new NoteConstructor();
			
		System.out.println(nc.convertIntervalsToPitches(intervals));
	}
	
	
	public void testNoteConstructor() {
		ArrayList<String> songIntervals = new ArrayList<String>();
		songIntervals.add("1");
		songIntervals.add("2");
		songIntervals.add("2");
		songIntervals.add("4");
		songIntervals.add("4");
		songIntervals.add("3");
		songIntervals.add("2");
		songIntervals.add("1");
		
		ArrayList<String> songDurations = new ArrayList<String>();
		songDurations.add("1");
		songDurations.add("2");
		songDurations.add("2");
		songDurations.add("4");
		songDurations.add("4");
		songDurations.add("2");
		songDurations.add("2");
		songDurations.add("1");
		
		NoteConstructor nc = new NoteConstructor();
		
		
			
		System.out.println(nc.constructNotes(songDurations, songIntervals));
		
	}
	
	public void testMusicCreation() {
		
		MusicCreator mc = new MusicCreator();
		mc.makeMusicFromJson("medieval",  50);
	}
	
	public static void main(String args[]) {
		new TestDriver();
	}
	
	
}
