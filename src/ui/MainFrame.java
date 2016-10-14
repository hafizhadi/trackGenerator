package ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.coinor.opents.BestEverAspirationCriteria;
import org.coinor.opents.MoveManager;
import org.coinor.opents.ObjectiveFunction;
import org.coinor.opents.SimpleTabuList;
import org.coinor.opents.SingleThreadedTabuSearch;
import org.coinor.opents.Solution;
import org.coinor.opents.TabuList;
import org.coinor.opents.TabuSearch;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.SigmaScaling;
import org.uncommons.watchmaker.framework.termination.GenerationCount;
import ga.GAComparator;
import ga.TrackCrossover;
import ga.TrackEvaluator;
import ga.TrackFactory;
import ga.TrackMutation;
import problem.Constants;
import problem.Track;
import tabu.TabuComparator;
import tabu.TrackMoveManager;
import tabu.TrackObjectiveFunction;
import tabu.TrackSolution;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JRadioButton;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JCheckBox;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ButtonGroup;
import javax.swing.JTextArea;

public class MainFrame extends JFrame {

	private JPanel contentPane;
	private JTextField producedTrack;
	private JTextField shownTrack;
	private JTextField elitism;
	private JTextField mutationProb;
	private JTextField crossoverPoint;
	private JTextField gaPopSize;
	private JTextField carAccel;
	private JTextField carSpeed;
	private JTextField carGrip;
	private JTextField tabuListSize;
	private JTextField iterationNum;
	private JRadioButton gaButton;
	private JRadioButton tabuButton;
	private JTextArea consoleWindow;
	
	private int prodTrack;
	private int showTrack;
	private int popSize;
	private int eliteIndividual;
	private float mutateProb;
	private int crossPoint;
	private int accel;
	private int speed;
	private int grip;
	private int tabuSize;
	private int iteration;
	private final ButtonGroup buttonGroup = new ButtonGroup();

	

	/**
	 * Create the frame.
	 */
	public MainFrame() {
		setTitle("Race Track Profile Generator");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 398);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Experiment", null, panel, null);
		panel.setLayout(null);
		
		JButton startButton = new JButton("Start");
		startButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				prodTrack = Integer.parseInt(producedTrack.getText());
				boolean GA = buttonGroup.isSelected(gaButton.getModel());
				
				Thread experiment = new Thread(){
					public void run(){
						startButton.setEnabled(false);
						DoExperiment(prodTrack, GA);
						startButton.setEnabled(true);
					}
				};
				
				experiment.start();
			}
		});
		startButton.setBounds(300, 190, 109, 23);
		panel.add(startButton);
		
		gaButton = new JRadioButton("Genetic Algorithm");
		buttonGroup.add(gaButton);
		gaButton.setSelected(true);
		gaButton.setBounds(198, 11, 109, 23);
		panel.add(gaButton);
		
		tabuButton = new JRadioButton("Tabu Search");
		buttonGroup.add(tabuButton);
		tabuButton.setBounds(198, 33, 109, 23);
		panel.add(tabuButton);
		
		JLabel lblAlgoritma = new JLabel("Algorithm: ");
		lblAlgoritma.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblAlgoritma.setBounds(10, 11, 68, 14);
		panel.add(lblAlgoritma);
		
		JLabel lblJumlahLintasan = new JLabel("Number of Track to Produce:");
		lblJumlahLintasan.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblJumlahLintasan.setBounds(10, 69, 178, 14);
		panel.add(lblJumlahLintasan);
		
		producedTrack = new JTextField();
		producedTrack.setBounds(198, 67, 86, 20);
		panel.add(producedTrack);
		producedTrack.setColumns(10);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Algorithm Config", null, tabbedPane_1, null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane_1.addTab("Genetic Algorithm", null, panel_1, null);
		panel_1.setLayout(null);
		
		elitism = new JTextField();
		elitism.setBounds(165, 11, 86, 20);
		panel_1.add(elitism);
		elitism.setColumns(10);
		
		JLabel lblElitism = new JLabel("Elitism Percentage:");
		lblElitism.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblElitism.setBounds(10, 11, 130, 15);
		panel_1.add(lblElitism);
		
		mutationProb = new JTextField();
		mutationProb.setColumns(10);
		mutationProb.setBounds(165, 67, 86, 20);
		panel_1.add(mutationProb);
		
		JLabel lblMutationProbability = new JLabel("Mutation Percentage:");
		lblMutationProbability.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMutationProbability.setBounds(10, 67, 130, 15);
		panel_1.add(lblMutationProbability);
		
		crossoverPoint = new JTextField();
		crossoverPoint.setColumns(10);
		crossoverPoint.setBounds(165, 95, 86, 20);
		panel_1.add(crossoverPoint);
		
		JLabel lblCrossoverPoint = new JLabel("Crossover Point:");
		lblCrossoverPoint.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCrossoverPoint.setBounds(10, 95, 130, 15);
		panel_1.add(lblCrossoverPoint);
		
		gaPopSize = new JTextField();
		gaPopSize.setColumns(10);
		gaPopSize.setBounds(165, 123, 86, 20);
		panel_1.add(gaPopSize);
		
		JLabel label = new JLabel("Population Size: ");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setBounds(10, 121, 104, 15);
		panel_1.add(label);
		
		JPanel panel_2 = new JPanel();
		tabbedPane_1.addTab("Tabu Search", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Tabu List Size:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_1.setBounds(10, 14, 93, 14);
		panel_2.add(lblNewLabel_1);
		
		tabuListSize = new JTextField();
		tabuListSize.setBounds(165, 11, 86, 20);
		panel_2.add(tabuListSize);
		tabuListSize.setColumns(10);
		
		JPanel panel_3 = new JPanel();
		tabbedPane.addTab("Other Configs", null, panel_3, null);
		panel_3.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Car Acceleration:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(10, 68, 101, 14);
		panel_3.add(lblNewLabel);
		
		carAccel = new JTextField();
		carAccel.setBounds(165, 68, 86, 20);
		panel_3.add(carAccel);
		carAccel.setColumns(10);
		
		JLabel lblCarMaxSpeed = new JLabel("Car Max Speed:");
		lblCarMaxSpeed.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCarMaxSpeed.setBounds(10, 96, 101, 14);
		panel_3.add(lblCarMaxSpeed);
		
		carSpeed = new JTextField();
		carSpeed.setColumns(10);
		carSpeed.setBounds(165, 96, 86, 20);
		panel_3.add(carSpeed);
		
		JLabel lblCarGrip = new JLabel("Car Grip:");
		lblCarGrip.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblCarGrip.setBounds(10, 127, 101, 14);
		panel_3.add(lblCarGrip);
		
		carGrip = new JTextField();
		carGrip.setColumns(10);
		carGrip.setBounds(165, 127, 86, 20);
		panel_3.add(carGrip);
		
		JLabel label_1 = new JLabel("Number of Iteration:");
		label_1.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_1.setBounds(10, 11, 130, 15);
		panel_3.add(label_1);
		
		iterationNum = new JTextField();
		iterationNum.setColumns(10);
		iterationNum.setBounds(165, 11, 86, 20);
		panel_3.add(iterationNum);
		
		producedTrack.setText(Constants.DEF_PRODUCEDTRACK + "");
		
		shownTrack = new JTextField();
		shownTrack.setText("1");
		shownTrack.setColumns(10);
		shownTrack.setBounds(198, 94, 86, 20);
		panel.add(shownTrack);
		
		JLabel lblNumberOfTrack = new JLabel("Number of Track to Show:");
		lblNumberOfTrack.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNumberOfTrack.setBounds(10, 97, 178, 14);
		panel.add(lblNumberOfTrack);
		
		consoleWindow = new JTextArea();
		consoleWindow.setBounds(10, 224, 399, 87);
		panel.add(consoleWindow);
		producedTrack.setText(Constants.DEF_PRODUCEDTRACK + "");
		shownTrack.setText(Constants.DEF_SHOWNTRACK + "");
		elitism.setText(Constants.DEF_ELITISM_PERCENTAGE + "");
		mutationProb.setText(Constants.DEF_MUTATION_PERCENTAGE + "");
		crossoverPoint.setText(Constants.DEF_CROSSOVER_POINT + "");
		gaPopSize.setText(Constants.DEF_POPULATION_SIZE + "");
		carAccel.setText(Constants.DEF_ACCEL + "");
		carSpeed.setText(Constants.DEF_MAXSPEED + "");
		carGrip.setText(Constants.DEF_GRIP + "");
		tabuListSize.setText(Constants.DEF_TABU_SIZE + "");
		iterationNum.setText(Constants.DEF_ITERATION + "");
	}
	
	public void DoExperiment(int numberProduced, boolean GA){
		ArrayList<TrackSolution> resultTabu = new ArrayList<TrackSolution>();
        ArrayList<Track> resultGA = new ArrayList<Track>();
        double sum = 0;		
        long starttime = System.nanoTime();
        
        prodTrack = Integer.parseInt(producedTrack.getText());
        showTrack = Integer.parseInt(shownTrack.getText());
    	popSize = Integer.parseInt(gaPopSize.getText());
    	eliteIndividual = popSize * Integer.parseInt(elitism.getText()) / 100;
    	mutateProb = Float.parseFloat(mutationProb.getText()) / 100f;
    	crossPoint = Integer.parseInt(crossoverPoint.getText());
    	accel = Integer.parseInt(carAccel.getText());
    	speed = Integer.parseInt(carSpeed.getText());
    	grip = Integer.parseInt(carGrip.getText());
    	tabuSize = Integer.parseInt(tabuListSize.getText());
    	iteration = Integer.parseInt(iterationNum.getText());
        
    	consoleWindow.setText("Starting experiment...");			    		   
    	
        if(!GA){     
			for(int i = 0; i < numberProduced; i++){
			    TrackSolution best = SearchSolutionTabu();
			    
			    while(best.getObjectiveValue()[0] > 0){
			        // Show solution
			        best = SearchSolutionTabu();
			        // System.out.println("KE " + i);
			    }
			    
			    consoleWindow.setText("Finished " + (i+1)  + " track out of " + numberProduced + "...");			    
			    resultTabu.add(best);
			}
        }else{
		    Track best = SearchSolutionGA();
		    
		    TrackFactory factory = new TrackFactory(20);
	        List<EvolutionaryOperator<Track>> operators = new ArrayList<EvolutionaryOperator<Track>>(2);
	        operators.add(new TrackMutation(new Probability(mutateProb)));
	        operators.add(new TrackCrossover(crossPoint));
	        EvolutionaryOperator<Track> pipeline = new EvolutionPipeline<Track>(operators);
	        EvolutionEngine<Track> engine = new GenerationalEvolutionEngine<Track>(factory,
	                                                                                 pipeline,
	                                                                                 new TrackEvaluator(accel, grip, speed),
	                                                                                 new SigmaScaling(),
	                                                                                 new MersenneTwisterRNG());
	        //engine.addEvolutionObserver(new EvolutionLogger());
        	
        	for(int i = 0; i < numberProduced; i++){
        		// Show solution
		    	best =  engine.evolve(popSize, // individuals in the population.
                        eliteIndividual, // 1% elitism.
                        new GenerationCount(iteration));
			    
			    while(best.fitness > 1000){
			        // Show solution
			    	best =  engine.evolve(popSize, // individuals in the population.
                             eliteIndividual, // elitism.
                             new GenerationCount(iteration));
			        // System.out.println("KE " + i);
			    }
			    
			    consoleWindow.setText("Finished " + (i+1)  + " track out of " + numberProduced + "...");
			    resultGA.add(best);
			}
		}
		
		long time = (System.nanoTime() - starttime)/1000000;
		
		Collections.sort(resultTabu, new TabuComparator());
		Collections.sort(resultGA, new GAComparator());
		
		String resultText = "";
		
		if(!GA){
			for(int i = 0; i < resultTabu.size(); i++){
				sum += resultTabu.get(i).getObjectiveValue()[1];
			}
			
			for(int j = 0; j < resultTabu.size() && j < showTrack; j++){
				Display(resultTabu.get(resultTabu.size() - j - 1).track, "Tabu " + j + ": " + resultTabu.get(resultTabu.size() - j - 1).getObjectiveValue()[1]);
			}
			
			resultText += "TABU EXPERIMENT RESULT\n";
			resultText += "======================\n";
			
		}else{
			for(int i = 0; i < resultGA.size(); i++){
				sum += 1000 - resultGA.get(i).fitness;
			}
			
			for(int j = 0; j < resultGA.size() && j < showTrack; j++){
				Display(resultGA.get(resultGA.size() - j - 1).track, "GA " + j +": " + (1000 - resultGA.get(resultGA.size() - j - 1).fitness));
			}
			
			resultText += "GA EXPERIMENT RESULT\n";
			resultText += "====================\n";
		}
		
		resultText += "Fitness Average: " + (sum/numberProduced) + "\n";
		resultText += "Time Average: " + (time/numberProduced) + " ms\n";
				
		consoleWindow.setText(resultText);
	}
	
	public TrackSolution SearchSolutionTabu(){
		// Initialize objects and problem
		ObjectiveFunction objFunc = new TrackObjectiveFunction();
       Solution initialSolution  = new TrackSolution(20, grip, accel, speed);
       MoveManager   moveManager = new TrackMoveManager();
       TabuList         tabuList = new SimpleTabuList(tabuSize);
		
       // Create Tabu Search object
       TabuSearch tabuSearch = new SingleThreadedTabuSearch(
               initialSolution,
               moveManager,
               objFunc,
               tabuList,
               new BestEverAspirationCriteria(), // In OpenTS package
               false ); // maximizing = yes/no; false means minimizing
       
       
       // Start solving
       tabuSearch.setIterationsToGo(iteration);
       tabuSearch.startSolving();
       
       // Show solution
       return (TrackSolution)tabuSearch.getBestSolution();
	}
	
	public Track SearchSolutionGA(){
		TrackFactory factory = new TrackFactory(20);
       List<EvolutionaryOperator<Track>> operators = new ArrayList<EvolutionaryOperator<Track>>(2);
       operators.add(new TrackMutation(new Probability(mutateProb)));
       operators.add(new TrackCrossover(crossPoint));
       EvolutionaryOperator<Track> pipeline = new EvolutionPipeline<Track>(operators);
       EvolutionEngine<Track> engine = new GenerationalEvolutionEngine<Track>(factory,
                                                                                pipeline,
                                                                                new TrackEvaluator(accel, grip, speed),
                                                                                new SigmaScaling(),
                                                                                new MersenneTwisterRNG());
       // engine.addEvolutionObserver(new EvolutionLogger());
       
       Track result =  engine.evolve(popSize, // 200 individuals in the population.
                            eliteIndividual, // 1% elitism.
                            new GenerationCount(iteration));
		
		return result;
	}
	
	public static void Display(int[][] track, String name){
		SwingUtilities.invokeLater(new Runnable() {

           @Override
           public void run() {
               
               TrackDisplay lines = new TrackDisplay(Track.getPoints(track), name);
               lines.setVisible(true);
           }
		});
	}
	
	private static class EvolutionLogger implements EvolutionObserver<Track>
   {
       public void populationUpdate(PopulationData<? extends Track> data)
       {
           System.out.printf("Generation %d: %s\n", data.getGenerationNumber(), data.getBestCandidate());
       }
   }
}
