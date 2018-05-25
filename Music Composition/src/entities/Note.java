package entities;

public class Note {
	
	private String duration;
	
	private int pitch;
	
	public Note(int pitch, String duration) {
		this.setPitch(pitch);
		this.setDuration(duration);
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public int getPitch() {
		return pitch;
	}

	public void setPitch(int pitch) {
		this.pitch = pitch;
	}



}
