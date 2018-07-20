package controllers;

import entities.Note;

import java.io.File;
import java.io.IOException;

import java.util.ArrayList;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;

public class midiOutputController {
	
	public static void outputMidi(ArrayList<Note> notes, String fileName) {

		try {
			Sequence sequence = new Sequence(Sequence.PPQ, 500);
			addTrack(sequence, 16, notes);

			int[] allowedTypes = MidiSystem.getMidiFileTypes(sequence);
			if (allowedTypes.length == 0) {
				System.err.println("No supported MIDI file types.");
			} else {
				MidiSystem.write(sequence, allowedTypes[0], new File(fileName));
				System.exit(0);
			}
		}
		catch (InvalidMidiDataException imde) {}
		catch(IOException ioe) {}
	}
	
	
	public static void addTrack(Sequence s, int instrument, ArrayList<Note> notes) {
		
		 Track track = s.createTrack(); // Begin with a new track

		  // Set the instrument on channel 0	  
		  ShortMessage sm = new ShortMessage();

		  try {
			  sm.setMessage(ShortMessage.PROGRAM_CHANGE, 0, instrument, 0);
			  track.add(new MidiEvent(sm, 0));

			  int n = 0; // current note in the array list
			  int t = 0; // time in ticks for the composition

			  // These values persist and apply to all notes 'till changed
			  int noteLength = 16; // default to quarter notes
			  int velocity = 64; // default to middle volume


			  while (n < notes.size()) {
				  Note note = notes.get(n++);
				  noteLength = note.getDuration();
				  if (note.isRest()) 
					  t += noteLength;			  
				  else {
					  ShortMessage on = new ShortMessage();
					  on.setMessage(ShortMessage.NOTE_ON, 0, note.getPitch(), velocity);
					  //on.setMessage(ShortMessage.NOTE_ON, 9, 56, velocity);
					  ShortMessage off = new ShortMessage();
					  off.setMessage(ShortMessage.NOTE_OFF, 0, note.getPitch(), velocity);
					  //off.setMessage(ShortMessage.NOTE_OFF, 9, 56, velocity);
					  track.add(new MidiEvent(on, t));
					  track.add(new MidiEvent(off, t + noteLength));
					  t += noteLength;
				  }
			  }
		  } catch (InvalidMidiDataException imde) {}
	}

	  // A convenience method to add a note to the track on channel 0
	  public static void addNote(Track track, int startTick, int tickLength, int key, int velocity)
	      throws InvalidMidiDataException {
	    ShortMessage on = new ShortMessage();
	    on.setMessage(ShortMessage.NOTE_ON, 0, key, velocity);
	    //on.setMessage(ShortMessage.NOTE_ON, 9, 56, velocity);
	    ShortMessage off = new ShortMessage();
	    off.setMessage(ShortMessage.NOTE_OFF, 0, key, velocity);
	    //off.setMessage(ShortMessage.NOTE_OFF, 9, 56, velocity);
	    track.add(new MidiEvent(on, startTick));
	    track.add(new MidiEvent(off, startTick + tickLength));
	  }

}
