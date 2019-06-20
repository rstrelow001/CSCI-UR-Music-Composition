package GUI;


import java.util.ArrayList;

import controllers.*;
import entities.*;

public class TestDriver {
	
	public TestDriver() {
		//this.testNoteConstructor();
		this.testIntervalConverter();
	}
	
	
	public void testNoteConstructor() {
		
		StructReader structReader = new StructReader("kern.json");
		
		ArrayList<MusicStruct> structs = structReader.makeStruct(1);
		
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
	
	public static void main(String args[]) {
		new TestDriver();
	}
	
	
}
