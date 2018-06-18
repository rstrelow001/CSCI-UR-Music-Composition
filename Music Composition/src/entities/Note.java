package entities;

public class Note {
	
	private int duration, pitch;
	private String durationName;
	private boolean isRest;
	
	
	public Note(int pitch, int duration, String durationName) {
		this.setPitch(pitch);
		this.setDuration(duration);
		this.setDurationName(durationName);
	}
	
	public Note(int duration, String durationName, boolean isRest) {
		this.setDuration(duration);
		this.setDurationName(durationName);
		this.setRest(isRest);
	}
	
	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}

	public String getDurationName() {
		return durationName;
	}

	public void setDurationName(String durationName) {
		this.durationName = durationName;
	}

	public boolean isRest() {
		return isRest;
	}

	public void setRest(boolean isRest) {
		this.isRest = isRest;
	}

}
