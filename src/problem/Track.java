package problem;

import java.util.List;
import java.util.Random;

import tabu.TrackSolution;
import util.Point;

public class Track {
	
	// Variables
	public int[][] track;
	public double fitness;
	
	// Constructors
	public Track(){
		
	}
	
	public Track(int length){
		
	}
	
	public Track(int[][] template){
		
        this.track = new int[template.length][];
        
        for(int i = 0; i < template.length; i++){
        	this.track[i] = template[i].clone();
        }
	}
	
	// Static Methods
	
	public static int[][] createNewTrack(int segments, Random r){
		int[][] track = new int[segments][5];
		
		// For now, just initialize the segment randomly
		for(int i = 0; i < segments; i++){
			// Initialize track informations
			track[i][0] = r.nextInt(1);
			track[i][1] = r.nextInt(80) + 20;
			track[i][2] = r.nextInt(540) - 360;
			track[i][3] = r.nextInt(35) + 15;
			track[i][4] = r.nextInt(35) + 15;
		}
		
		return track;
	}
	
	public static int[] applyMove(int[] segment, int[] displacement){
		int[] newSegment = new int[5];
		
		newSegment[0] = (segment[0] + displacement[0]) % 2;
		newSegment[1] = (segment[1] + displacement[1]) % 101;
		newSegment[2] = (segment[2] + displacement[2]) % 271;
		newSegment[3] = (segment[3] + displacement[3]) % 51;
		newSegment[4] = (segment[4] + displacement[4]) % 51;
		
		newSegment[1] = (newSegment[1] < 20) ? newSegment[1] + 20 : newSegment[1]; 
		newSegment[3] = (newSegment[3] < 15) ? newSegment[3] + 15 : newSegment[3];
		newSegment[4] = (newSegment[4] < 15) ? newSegment[4] + 15 : newSegment[4];
	
		
		return newSegment;
	}
	
	// Calculate the points of the track given the segments
	public static Point[] getPoints(int[][] track){
		// Variable for displacement calculation result
		double x, y, direction;
		x = 0; y = 0; direction = 0;
		
		// Points are stored after each segment's displacement calculation
		Point[] points = new Point[track.length * 2 + 1];
		points[0] = new Point(0, 0, 0);
		int pointsPointer = 1;
		
		// Displacement calculation
		for(int i = 0; i < track.length; i++){		
			double[] res = Validation.countDisplacement(track[i], direction);
			x += res[0];
			y += res[1];
			direction += res[2];
			
			// Store the points for later
			if(track[i][0] == 0){ // straight segment with one point
				points[pointsPointer] = new Point(x, y, 0);
				pointsPointer++;
			}else{ // curved segment with two points
				points[pointsPointer] = new Point(x - res[3], y - res[4], 1);
				pointsPointer++;
				points[pointsPointer] = new Point(x, y, 1);
				pointsPointer++;
			}
		}
		
		// Trim the buffer
		Point[] pointsTrimmed = new Point[pointsPointer];
		System.arraycopy(points, 0, pointsTrimmed, 0, pointsPointer);
		
		return pointsTrimmed;
	}
	
	// Check whether the track contains any curve
	public static boolean checkCurve(int[][] track){
		boolean found = false;
			
		for(int i = 0; i < track.length; i++){
			if(track[i][0] == 1){ 
				found = true;
				break;
			}
		}
		
		return found;
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer();
		
		/*s.append("RACE SEGMENTS \n");
		s.append("----------------\n");
		for(int i = 0; i < track.length; i++){
			if(track[i][0] == 0){
				s.append("SEGMENT " + i + ": Straight\n");
				s.append("Length: " + track[i][1] + "\n");
			}else{
				s.append("SEGMENT " + i + ": Curve\n");
				s.append("Arc: " + track[i][2] + "\n");
				s.append("Radius Left: " + track[i][3] + "\n");
				s.append("Radius Right: " + track[i][4] + "\n");
			}
		
			s.append("\n");
		}*/
		
		s.append("FITNESS: " + getFitness() + "\n");
		
		return s.toString();
	}
	
	public double getFitness() {
		// Variable for race profile validation
		double[] validVal = new double[6];
		
		// Variable for race profile evaluation
		double curveVal = 0;
		double speedVal = 0;
		
		// Calculate validation values
		validVal = Validation.countValidityValue(track);
		
		// Calculate evaluation values
		curveVal = Evaluation.evaluateCurvature(track);
		speedVal = Evaluation.evaluateSpeed(track, 5, 60, 50);
		
		// TODO Auto-generated method stub
		return (((((int) validVal[0]) * Constants.DISTANCE_WEIGHT) + (validVal[1] * Constants.DIRECTION_WEIGHT) + (validVal[2] * Constants.INTERSECTION_WEIGHT)) * 1000) + (1000 - ((Constants.CURVATURE_WEIGHT * curveVal) + (Constants.SPEED_WEIGHT * speedVal)));
	}
}
