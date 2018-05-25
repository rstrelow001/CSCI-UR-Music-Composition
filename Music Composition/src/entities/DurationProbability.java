package entities;

public class DurationProbability {

	
	private double toEighthNote, toQuarterNote, toHalfNote, toWholeNote;
	
	private double toEighthRest, toQuarterRest, toHalfRest, toWholeRest;
	
	public DurationProbability(double toEighthNote, double toQuarterNote, double toHalfNote, double toWholeNote,
			double toEighthRest, double toQuarterRest, double toHalfRest, double toWholeRest) {
		
		this.setToEighthNote(toEighthNote);
		this.setToQuarterNote(toQuarterNote);
		this.setToHalfNote(toHalfNote);
		this.setToWholeNote(toWholeNote);
		this.setToEighthRest(toEighthRest);
		this.setToQuarterRest(toQuarterRest);
		this.setToHalfRest(toHalfRest);
		this.setToWholeRest(toWholeRest);
	
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

}
