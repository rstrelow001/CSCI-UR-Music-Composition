package GUI;


import java.util.ArrayList;

import controllers.*;
import entities.*;

import java.util.Scanner;

public class TestDriver {
	
	public TestDriver() {
		this.testMusicCreation();

	}
	
	
	public void testMusicCreation() {
		
		Scanner in = new Scanner(System.in);
		
		MusicCreator mc = new MusicCreator();
		System.out.println("b  -  Baroque\n"
				+ "c  -  Classical\n"
				+ "m  -  Medieval\n"
				+ "re -  Renaissance\n"
				+ "ro -  Romantic\n"
				+ "Choose an Era: ");
		
		String era = in.nextLine();
		
		if (era.toLowerCase().startsWith("b")) era = "baroque";
		else if (era.toLowerCase().startsWith("c")) era = "classical";
		else if (era.toLowerCase().startsWith("m")) era = "medieval";
		else if (era.toLowerCase().startsWith("re")) era = "renaissance";
		else if (era.toLowerCase().startsWith("ro")) era = "romantic";
		
		System.out.println("Number of Measures: ");
		int measures = in.nextInt();
		in.close();
		
		mc.makeMusicFromJson(era,  measures);
	}
	
	public static void main(String args[]) {
		new TestDriver();
	}
	
	
}
