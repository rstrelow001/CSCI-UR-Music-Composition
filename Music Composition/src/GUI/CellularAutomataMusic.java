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

import java.util.HashMap;

import entities.MeasureDurations;
import entities.MeasureIntervals;
import entities.Epoch;
import entities.Note;

import controllers.MusicCompositionController;
import controllers.InputController;

public class CellularAutomataMusic  extends JFrame{

	/*
	 * Colors used to recreate musical notes visually
	 */
	private static final Color white = Color.WHITE, black = Color.BLACK;
	  
	private Board board;
	/*
	 * Controls the compostion of music generation
	 */
	private MusicCompositionController musicCompController;
	/*
	 * buttons to allow the user to select different epochs
	 */
	private JButton start_pause, medieval, renaissance, baroque, classical, romantic, modern;
	// variables to track total number of interval occurrences
	int t;
    // variables to track the occurrences of each interval for testing
    int[] totals = new int[16];
    // Boolean variable representing
    Boolean analysis = true;
    //map that contains all the epochs and their values
    private HashMap<String, Epoch> epochs;
	
	/* 
	* Creates blank board to feature automata, with start button to 
	* commence composition, as well as buttons to select epoch
	* */
	public CellularAutomataMusic(){
		
		board = new Board();
		board.setBackground(white);
		epochs = InputController.readInput();
		
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
	    private int cell_size, interval, fill_ratio, prevPitch = 0;
	    
	    //boolean whether the composer is active
	    private boolean run, running;
	    // Timer for playing notes evenly
	    private Timer timer;
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
	     * method to generate a new measure with durations and intervals
	     */
	    public void runGenerator() {
	    	
	    	MeasureDurations newDurations = musicCompController.measureDurationsGenerator();
	    	MeasureIntervals newIntervals;
	    	if (newDurations.getSize() > 0)
	    		newIntervals = musicCompController.measureIntervalsGenerator(newDurations.getSize());
	    	else
	    		newIntervals = musicCompController.measureIntervalsGenerator(2);
	    	while(newDurations.hasNextDuration()) {
	    		Note newNote = newDurations.nextDuration();
	    		System.out.println("Duration: " + newNote.getDurationName());
	    	    System.out.println("Time Value: " + newNote.getDuration());
	    		if (!newNote.isRest()) {
	    			int newInterval = newIntervals.nextInterval();
	    			System.out.println("Interval: " + newInterval);
	    			prevPitch = musicCompController.playNextNote(newInterval, prevPitch, newNote.getDuration());	    			
	    			drawSequence(prevPitch);
	    			
	    		}
	    		else {
	    			System.out.println(); 
	    			//System.out.println(newDurations.getSize());
	    			drawSequence(14);
	    			try  {
	    				Thread.sleep(newNote.getDuration()); }
	    			catch( InterruptedException e ) {}
	    		}
	    	}
	    	System.out.println("-------------END OF MEASURE--------------\n");
	    	newDurations.resetDurations();
	    	newIntervals.resetIntervals();
	    	
	    }

	    
	    /*
	     * method to visually show the music being generated
	     * @param newVal the value to show
	     */
	    public void drawSequence(int newVal) {
	    	//System.out.println(newVal);
	    	System.out.println("tried to draw!"); 
	    	//shifts bottom n-1 sequences up to make room for next sequence
	    	for (int h = 0; h < board_size.height; h++){
	    		for (int w = 0; w < board_size.width-1; w++){
	    			grid[h][w] = grid[h][w+1];
	    		}
	    	}
      
	    	
	    	if (newVal >= 8){
    			grid[0][board_size.width-1] = black;
    			newVal = newVal-8;
    		}
    		else
    			grid[0][board_size.width-1] = white;
    		if (newVal >= 4){
    			grid[1][board_size.width-1] = black;
    			newVal = newVal-4;
    		}
    		else
    			grid[1][board_size.width-1] = white;
    		if (newVal >= 2){
    			grid[2][board_size.width-1] = black;
    			newVal = newVal-2;
    		}
    		else
    			grid[2][board_size.width-1] = white;
    		if (newVal >= 1){
    			grid[3][board_size.width-1] = black;
    			newVal = newVal-1;
    		}
    		else
    			grid[3][board_size.width-1] = white;
    		repaint();
    		Color[][] newGrid = new Color[board_size.height][board_size.width];
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
		    		//int newNote = runGenerator();
		    		int newNote = 0;
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
		    		//musicCompController.ruleGeneratorAnalysis();
		    		runGenerator();
		    	}
	      
			//Start-Pause button processing
			else if(e.getSource().equals(start_pause)){
			    	if(run){
			    		timer.stop();
			    		//JOptionPane.showMessageDialog(null,printResults());
			    		//JOptionPane.showMessageDialog(null,musicCompController.printResults());
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
			    //runGenerator();
			    //System.out.println("test");
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