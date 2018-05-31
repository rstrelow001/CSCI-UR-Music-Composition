package entities;

public class Note {
	
	private int duration, pitch;
	
	
	public Note(int pitch, int duration) {
		this.setPitch(pitch);
		this.setDuration(duration);
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

}
