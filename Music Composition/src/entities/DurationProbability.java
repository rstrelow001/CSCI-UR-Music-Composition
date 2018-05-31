package entities;

/*
 * Entity Class supporting Algorithmic Music Composition Software

 * @author Ryan Strelow
 * @version 1.0
 * @since 05/22/18
 */

public class DurationProbability {

	
	private double toTwentyEighthNote, toTwentyForthNote, toTwentiethNote, toSixteenthNote, toTwelfthNote,
		toEighthNote, toDottedEighthNote, toSixthNote,
		toQuarterNote, toDottedQuarterNote, toHalfNote, toDottedHalfNote,
		toWholeNote, toDottedWholeNote;
	
	private double toEighthRest, toQuarterRest, toHalfRest, toDottedHalfRest,
		toWholeRest, toDottedWholeRest;
	
	public DurationProbability(double toTwentyEighthNote, double toTwentyForthNote, double toTwentiethNote, double toSixteenthNote,
			double toTwelfthNote, double toEighthNote, double toDottedEighthNote, double toSixthNote,
			double toQuarterNote, double toDottedQuarterNote, double toHalfNote, double toDottedHalfNote,
			double toWholeNote, double toDottedWholeNote,
			double toEighthRest, double toQuarterRest, double toHalfRest, double toDottedHalfRest, double toWholeRest, double toDottedWholeRest) {
		
		this.setToTwentyEighthNote(toTwentyEighthNote);
		this.setToTwentyForthNote(toTwentyForthNote);
		this.setToTwentiethNote(toTwentiethNote);
		this.setToSixteenthNote(toSixteenthNote);
		this.setToTwelfthNote(toTwelfthNote);
		this.setToEighthNote(toEighthNote);
		this.setToDottedEighthNote(toDottedEighthNote);
		this.setToQuarterNote(toQuarterNote);
		this.setToDottedQuarterNote(toDottedQuarterNote);
		this.setToHalfNote(toHalfNote);
		this.setToDottedHalfNote(toDottedHalfNote);
		this.setToWholeNote(toWholeNote);
		this.setToDottedWholeNote(toDottedWholeNote);
		this.setToEighthRest(toEighthRest);
		this.setToQuarterRest(toQuarterRest);
		this.setToHalfRest(toHalfRest);
		this.setToDottedHalfRest(toDottedHalfRest);
		this.setToWholeRest(toWholeRest);
		this.setToDottedWholeRest(toDottedWholeRest);	
	}

	public double getToEighthNote() {
		return toEighthNote;
	}

	public void setToEighthNote(double toEighthNote) {
		this.toEighthNote = toEighthNote;
	}

	public double getToQuarterNote() {
		return toQuarterNote;
	}

	public void setToQuarterNote(double toQuarterNote) {
		this.toQuarterNote = toQuarterNote;
	}

	public double getToHalfNote() {
		return toHalfNote;
	}

	public void setToHalfNote(double toHalfNote) {
		this.toHalfNote = toHalfNote;
	}

	public double getToWholeNote() {
		return toWholeNote;
	}

	public void setToWholeNote(double toWholeNote) {
		this.toWholeNote = toWholeNote;
	}

	public double getToEighthRest() {
		return toEighthRest;
	}

	public void setToEighthRest(double toEighthRest) {
		this.toEighthRest = toEighthRest;
	}

	public double getToQuarterRest() {
		return toQuarterRest;
	}

	public void setToQuarterRest(double toQuarterRest) {
		this.toQuarterRest = toQuarterRest;
	}

	public double getToHalfRest() {
		return toHalfRest;
	}

	public void setToHalfRest(double toHalfRest) {
		this.toHalfRest = toHalfRest;
	}

	public double getToWholeRest() {
		return toWholeRest;
	}

	public void setToWholeRest(double toWholeRest) {
		this.toWholeRest = toWholeRest;
	}

	public double getToTwentyEighthNote() {
		return toTwentyEighthNote;
	}

	public void setToTwentyEighthNote(double toTwentyEighthNote) {
		this.toTwentyEighthNote = toTwentyEighthNote;
	}

	public double getToTwentyForthNote() {
		return toTwentyForthNote;
	}

	public void setToTwentyForthNote(double toTwentyForthNote) {
		this.toTwentyForthNote = toTwentyForthNote;
	}

	public double getToTwentiethNote() {
		return toTwentiethNote;
	}

	public void setToTwentiethNote(double toTwentiethNote) {
		this.toTwentiethNote = toTwentiethNote;
	}

	public double getToSixteenthNote() {
		return toSixteenthNote;
	}

	public void setToSixteenthNote(double toSixteenthNote) {
		this.toSixteenthNote = toSixteenthNote;
	}

	public double getToTwelfthNote() {
		return toTwelfthNote;
	}

	public void setToTwelfthNote(double toTwelfthNote) {
		this.toTwelfthNote = toTwelfthNote;
	}

	public double getToDottedEighthNote() {
		return toDottedEighthNote;
	}

	public void setToDottedEighthNote(double toDottedEighthNote) {
		this.toDottedEighthNote = toDottedEighthNote;
	}

	public double getToSixthNote() {
		return toSixthNote;
	}

	public void setToSixthNote(double toSixthNote) {
		this.toSixthNote = toSixthNote;
	}

	public double getToDottedQuarterNote() {
		return toDottedQuarterNote;
	}

	public void setToDottedQuarterNote(double toDottedQuarterNote) {
		this.toDottedQuarterNote = toDottedQuarterNote;
	}

	public double getToDottedHalfNote() {
		return toDottedHalfNote;
	}

	public void setToDottedHalfNote(double toDottedHalfNote) {
		this.toDottedHalfNote = toDottedHalfNote;
	}

	public double getToDottedWholeNote() {
		return toDottedWholeNote;
	}

	public void setToDottedWholeNote(double toDottedWholeNote) {
		this.toDottedWholeNote = toDottedWholeNote;
	}

	public double getToDottedHalfRest() {
		return toDottedHalfRest;
	}

	public void setToDottedHalfRest(double toDottedHalfRest) {
		this.toDottedHalfRest = toDottedHalfRest;
	}

	public double getToDottedWholeRest() {
		return toDottedWholeRest;
	}

	public void setToDottedWholeRest(double toDottedWholeRest) {
		this.toDottedWholeRest = toDottedWholeRest;
	}

}
