package entities;

public class Epoch {
	
	private int start, range;
	
	private double uni, step, third, fourth, fifth, sixth, seventh, octave;
	
	private String era;
	
	public Epoch(int start, double uni, double step, double third, double fourth, double fifth,
			double sixth, double seventh, double octave, int range, String era) {
		
		this.setStart(start);
		this.setUni(uni);
		this.setStep(step);
		this.setThird(third);
		this.setFourth(fourth);
		this.setFifth(fifth);
		this.setSixth(sixth);
		this.setSeventh(seventh);
		this.setOctave(octave);
		this.setRange(range);
		this.setEra(era);
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public double getUni() {
		return uni;
	}

	public void setUni(double uni) {
		this.uni = uni;
	}

	public double getStep() {
		return step;
	}

	public void setStep(double step) {
		this.step = step;
	}

	public double getThird() {
		return third;
	}

	public void setThird(double third) {
		this.third = third;
	}

	public double getFourth() {
		return fourth;
	}

	public void setFourth(double fourth) {
		this.fourth = fourth;
	}

	public double getFifth() {
		return fifth;
	}

	public void setFifth(double fifth) {
		this.fifth = fifth;
	}

	public double getSixth() {
		return sixth;
	}

	public void setSixth(double sixth) {
		this.sixth = sixth;
	}

	public double getSeventh() {
		return seventh;
	}

	public void setSeventh(double seventh) {
		this.seventh = seventh;
	}

	public double getOctave() {
		return octave;
	}

	public void setOctave(double octave) {
		this.octave = octave;
	}

	public String getEra() {
		return era;
	}

	public void setEra(String era) {
		this.era = era;
	}
	
	
	

}
