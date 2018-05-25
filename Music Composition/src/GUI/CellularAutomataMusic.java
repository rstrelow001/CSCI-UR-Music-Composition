package GUI;

/*
 * Algorithmic Music Composition Software
 * @author Tom Donald Richmond
 * @author Ryan Strelow
 * @version 2.0
 * @since 02/12/17
 */

import java.awt.BorderLayout;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.JOptionPane;

import javax.sound.midi.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.HashMap;
import java.util.Set;

import org.yaml.snakeyaml.Yaml;

import entities.Epoch;
import entities.Note;
import entities.DurationProbability;
import controllers.MusicCompositionController;

public class CellularAutomataMusic  extends JFrame{
  
	private static final Color white = Color.WHITE, black = Color.BLACK;
	  
	private Board board;
	private MusicCompositionController musicCompController;
	private JButton start_pause, medieval, renaissance, baroque, classical, romantic, modern;
	// variables to track total number of interval occurrences
	int t;
    // variables to track the occurrences of each interval for testing
    int[] totals = new int[16];
    // variable to hold string value representing era
    String era;
    // Boolean variable representing
    Boolean analysis = false;
    //map that contains all the epochs and their values
    private HashMap<String, Epoch> epochs;
	
	/* 
	* Creates blank board to feature automata, with start button to 
	* commence composition, as well as buttons to select epoch
	* */
	public CellularAutomataMusic(){
		
		board = new Board();
		board.setBackground(white);
		board.setEpochs();
		musicCompController = new MusicCompositionController(epochs);
		    
		/* 
		* Create buttons for start/stop
		* */
		start_pause = new JButton("Compose");
		start_pause.addActionListener(board);
	    
	    /* 
	     * Create buttons for epoch selection
	     * */
	    medieval = new JButton("Medieval");
	    medieval.addActionListener(board);
	    renaissance = new JButton("Renaissance");
	    renaissance.addActionListener(board);
	    baroque = new JButton("Baroque");
	    baroque.addActionListener(board);
	    classical = new JButton("Classical");
	    classical.addActionListener(board);
	    romantic = new JButton("Romantic");
	    romantic.addActionListener(board);
	    modern = new JButton("Modern");
	    modern.addActionListener(board);
	    
	    /* 
	     * Subpanel for epoch selection
	     * */
	    JPanel subPanel = new JPanel();
	    subPanel.setLayout(new java.awt.GridLayout(6, 1));
	    subPanel.add(medieval);
	    subPanel.add(renaissance);
	    subPanel.add(baroque);
	    subPanel.add(classical);
	    subPanel.add(romantic);
		subPanel.add(modern);
	    
	    /* 
	     * Add buttons to layout
	     * */
	    this.add(board, BorderLayout.CENTER);
	    this.add(start_pause, BorderLayout.SOUTH);
	    this.add(subPanel, BorderLayout.WEST);
	    //this.setLocationRelativeTo(null);
	    
	    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	    this.pack();
	    this.setVisible(true);
    
	}
  
	public static void main(String args[]){		
	    new CellularAutomataMusic();
	}
  
	/*
	 * Board object featuring 4x15 Automata model, black and white values
	 * */
	private class Board extends JPanel implements ActionListener{
	    
		// Variables for board dimensions
	    private final Dimension DEFAULT_SIZE = new Dimension(15, 4);
	    private final int DEFAULT_CELL = 40, DEFAULT_INTERVAL = 100, DEFAULT_RATIO = 50;
	    private Dimension board_size;
	    private int cell_size, interval, fill_ratio;
	    
	    //boolean whether the composer is active
	    private boolean run;
	    // Timer for playing notes evenly
	    private Timer timer;
	    // variables to ensure the composer runs linearly
	    public int myOctave = 5, currentDiff = 0, range, start;
	    // variable to store the probability of each interval
	    double uni, step, third, fourth, fifth, sixth, seventh, octave, rest;
	    // variables to store the probability of the next note type
		double toEighthNote, toQuarterNote, toHalfNote, toWholeNote;		
	    double toEighthRest, toQuarterRest, toHalfRest, toWholeRest;
	    // boolean to see if an epoch has been selected
	    boolean selected = false;
	    //grid to display automata-model
	    private Color[][] grid;
    
	    
	    /*
	     * Default constructor for Board object
	     */
	    public Board(){
	    		board_size = DEFAULT_SIZE;
	    		cell_size = DEFAULT_CELL;
			interval = DEFAULT_INTERVAL;
			fill_ratio = DEFAULT_RATIO;
			run = false;
			  
			
			grid = new Color[board_size.height + 1][board_size.width + 1];
			for (int h = 0; h < board_size.height; h++)
				for (int w = 0; w < board_size.width; w++){
					//int r = (int)(Math.random() * 100);
					//if (r >= fill_ratio)
					//grid[h][w] = black;
					//else grid[h][w] = white;
					grid[h][w] = white;
				}
			timer = new Timer(interval, this);
			
	    }

	    @Override
	    public Dimension getPreferredSize(){
	    		return new Dimension(board_size.height * cell_size, board_size.width * cell_size);
	    }
    
	    @Override
	    public void paintComponent(Graphics g){
	    		super.paintComponent(g);
	    		for (int h = 0; h < board_size.height; h++){
	    			for (int w = 0; w < board_size.width; w++){
	    				try{
		    				if (grid[h][w] == black)
		    					g.setColor(black);
		    				else if (grid[h][w] == white) 
		    					g.setColor(white);
		    				g.fillRect(h * cell_size, w * cell_size, cell_size, cell_size);
		    			}
	    				catch (ConcurrentModificationException cme){}
	    			}
	    		}
	    }
	    
	    
	    /*
	     * Method to initially set up the epochs with their values
	     */
	    public void setEpochs() {
	    	epochs = new HashMap<String, Epoch>();
	    	HashMap<String, DurationProbability> durationProbabilities = new HashMap<String, DurationProbability>();
	    	
			Yaml yaml = new Yaml();
			try (InputStream in = CellularAutomataMusic.class.getResourceAsStream("../textFiles/configFile.yaml")) {
				Map<String, Map<String, Map<String, Object>>> configs = yaml.load(in);
				System.out.println(configs);

				Set<String> epochNames = configs.keySet();
				for (String currentEpochName : epochNames) {
					Map<String, Map<String, Object>> epochVariables = configs.get(currentEpochName);
					Set<String> epochVariableNames = epochVariables.keySet();
					for (String currentEpochVariable : epochVariableNames) {
						if (currentEpochVariable.equals("interval")) {
							Map<String, Object> epochIntervalValues = epochVariables.get(currentEpochVariable);
							start = (Integer)epochIntervalValues.get("start");
							uni = (Double)epochIntervalValues.get("uni");
							step = (Double)epochIntervalValues.get("step");
							third = (Double)epochIntervalValues.get("third");
							fourth = (Double)epochIntervalValues.get("fourth");
							fifth = (Double)epochIntervalValues.get("fifth");
							sixth = (Double)epochIntervalValues.get("sixth");
							seventh = (Double)epochIntervalValues.get("seventh");
							octave = (Double)epochIntervalValues.get("octave");
							rest = (Double)epochIntervalValues.get("rest");
							range = (Integer)epochIntervalValues.get("range");
							era = (String)epochIntervalValues.get("era");	
						}
						else if (currentEpochVariable.equals("rhythm")) {
							Map<String, Object> epochRhythmValues = epochVariables.get(currentEpochVariable);
							Set<String> rhythmVariableNames = epochRhythmValues.keySet();
							for (String currentRhythmVariableName : rhythmVariableNames) {
								Map<String, Object> currentRhythmValues = (Map<String, Object>)epochRhythmValues.get(currentRhythmVariableName);
								toEighthNote = (Double)currentRhythmValues.get("eighthNote");
								toQuarterNote = (Double)currentRhythmValues.get("quarterNote");	
								toHalfNote = (Double)currentRhythmValues.get("halfNote");	
								toWholeNote = (Double)currentRhythmValues.get("wholeNote");	
								toEighthRest = (Double)currentRhythmValues.get("eighthRest");	
								toQuarterRest = (Double)currentRhythmValues.get("quarterRest");	
								toHalfRest = (Double)currentRhythmValues.get("halfRest");	
								toWholeRest = (Double)currentRhythmValues.get("wholeRest");	
								
								DurationProbability probability = new DurationProbability(toEighthNote, toQuarterNote, toHalfNote, toWholeNote, toEighthRest, toQuarterRest, toHalfRest, toWholeRest);
								durationProbabilities.put(currentRhythmVariableName, probability);
							}					
						}
					}
					Epoch newEpoch = new Epoch(start, uni, step, third, fourth, fifth, sixth, seventh, octave, rest, range, era, durationProbabilities);
					epochs.put(currentEpochName, newEpoch);	

				}									
			}
			catch(IOException ioe) {
				System.out.println("Sorry!");
			}
	    }



	    /*
	     * (non-Javadoc)
	     * Action Listener for all buttons, compose, terminate, medieval,
	     * renaissance, baroque, classical, romantic and modern.
	     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	     */
	    public void actionPerformed(ActionEvent e) {
		    	
		    //reads binary value of last sequence
		    	int a = 0, b = 0, c = 0, d = 0, val = 0;
		      
		    	//counts binary from board for conversion to decimal
		    	if (grid[0][board_size.width-1]  == black)
		    		a = 1;
		    	if (grid[1][board_size.width-1]  == black)
		    		b = 1;
		    	if (grid[2][board_size.width-1]  == black)
		    		c = 1;
		    	if (grid[3][board_size.width-1]  == black)
		    		d = 1;
		    
		    	//converts binary sequence into decimal with variable val
		    	if(a==1)
		    		val+=8;
		    	if(b==1)
		    		val+=4;
		    	if(c==1)
		    		val+=2;
		    	if(d==1)
		    		val+=1;
		    	
		    	//accounts for rests (need to know last non rest)
		    	if (val == 14) {
		    		val = checkLastNote(2);		    		
		    	}
		      
		    	//shifts bottom n-1 sequences up to make room for next sequence
		    	for (int h = 0; h < board_size.height; h++){
		    		for (int w = 0; w < board_size.width-1; w++){
		    			grid[h][w] = grid[h][w+1];
		    		}
		    	}
	      
		    	//repaints the bottom line sequence based on rule
		    	if (e.getSource().equals(timer) && analysis == false){
		    		int newNote = musicCompController.ruleGenerator(val);
		    		if (newNote >= 8){
		    			grid[0][board_size.width-1] = black;
		    			newNote = newNote-8;
		    		}
		    		else
		    			grid[0][board_size.width-1] = white;
		    		if (newNote >= 4){
		    			grid[1][board_size.width-1] = black;
		    			newNote = newNote-4;
		    		}
		    		else
		    			grid[1][board_size.width-1] = white;
		    		if (newNote >= 2){
		    			grid[2][board_size.width-1] = black;
		    			newNote = newNote-2;
		    		}
		    		else
		    			grid[2][board_size.width-1] = white;
		    		if (newNote >= 1){
		    			grid[3][board_size.width-1] = black;
		    			newNote = newNote-1;
		    		}
		    		else
		    			grid[3][board_size.width-1] = white;
		    		repaint();
		    		Color[][] newGrid = new Color[board_size.height][board_size.width];
		    	}
		    	
		    	//repaints the bottom line sequence based on rule
		    	if (e.getSource().equals(timer) && analysis == true){
		    		musicCompController.ruleGeneratorAnalysis();
		    	}
	      
			//Start-Pause button processing
			else if(e.getSource().equals(start_pause)){
			    	if(run){
			    		timer.stop();
			    		//JOptionPane.showMessageDialog(null,printResults());
			    		JOptionPane.showMessageDialog(null,musicCompController.printResults());
			    		start_pause.setText("Compose");
			    }
			    else {
			        	if (selected) {
			        		timer.restart();
			        		start_pause.setText("Terminate");
			    }
			    else {
			        		JOptionPane.showMessageDialog(null, "Must first select an epoch from which to compose");
			        		run = !run;
			    }
			}
			run = !run;
			}
		    	
		    	//Medieval button processing
			else if(e.getSource().equals(medieval)){
				medieval.setEnabled(false);
			    renaissance.setEnabled(true);
			    baroque.setEnabled(true);
			    classical.setEnabled(true);
			    romantic.setEnabled(true);
			    modern.setEnabled(true);
			    musicCompController.changeEpoch("medieval");
			    selected = true;
			}
		    	//Renaissance button processing
			else if(e.getSource().equals(renaissance)){
			    medieval.setEnabled(true);
			    renaissance.setEnabled(false);
			    baroque.setEnabled(true);
			    classical.setEnabled(true);
		        romantic.setEnabled(true);
		        modern.setEnabled(true);
		        musicCompController.changeEpoch("renaissance");
		        selected = true;
		    }
		    	//Baroque button processing
		    else if(e.getSource().equals(baroque)){
		        medieval.setEnabled(true);
		        renaissance.setEnabled(true);
		        baroque.setEnabled(false);
		        classical.setEnabled(true);
		        romantic.setEnabled(true);
		        modern.setEnabled(true);
		        musicCompController.changeEpoch("baroque");
		        selected = true;
		    }
		    	//Classical button processing
		    else if(e.getSource().equals(classical)){
		        medieval.setEnabled(true);
		        renaissance.setEnabled(true);
		        baroque.setEnabled(true);
		        classical.setEnabled(false);
		        romantic.setEnabled(true);
		        modern.setEnabled(true);
		        musicCompController.changeEpoch("classical");
		        selected = true;
		    }
		    	//Romantic button processing
		    else if(e.getSource().equals(romantic)){
		        medieval.setEnabled(true);
		        renaissance.setEnabled(true);
		        baroque.setEnabled(true);
		        classical.setEnabled(true);
		        romantic.setEnabled(false);
		        modern.setEnabled(true);
		        musicCompController.changeEpoch("romantic");
		        selected = true;
		    }
		    	//Modern button processing
		    else if(e.getSource().equals(modern)){
		        medieval.setEnabled(true);
		        renaissance.setEnabled(true);
		        baroque.setEnabled(true);
		        classical.setEnabled(true);
		        romantic.setEnabled(true);
		        modern.setEnabled(false);
		        musicCompController.changeEpoch("modern");
		        selected = true;
		    }
	    }
	    
	    
	    /*
	     * helper method to find the last none rest
	     * @param offset  the offset from bottom of the grid
	     */
	    private int checkLastNote(int offset) {
		    //reads binary value of last sequence
	    	int a = 0, b = 0, c = 0, d = 0, val = 0;
	      
	    	//counts binary from board for conversion to decimal
	    	if (grid[0][board_size.width-offset]  == black)
	    		a = 1;
	    	if (grid[1][board_size.width-offset]  == black)
	    		b = 1;
	    	if (grid[2][board_size.width-offset]  == black)
	    		c = 1;
	    	if (grid[3][board_size.width-offset]  == black)
	    		d = 1;
	    
	    	//converts binary sequence into decimal with variable val
	    	if(a==1)
	    		val+=8;
	    	if(b==1)
	    		val+=4;
	    	if(c==1)
	    		val+=2;
	    	if(d==1)
	    		val+=1;
	    	
	    	//recursively call to find last usable note
	    	if (val == 14)
	    		return checkLastNote(++offset);
	    	else
	    		return val;
	    }
	}  
}