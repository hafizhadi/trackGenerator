package tabu;
import java.util.Random;

import org.coinor.opents.*;

import util.Point;

// Solution class for race track creation problem
// The solution is basically a representation of the race track

public class TrackSolution extends SolutionAdapter{

	// ATTRIBUTES
	
	/* 	Solution is represented in an array of array of integer containing the segment information	
	 	1 Segment is represented in 1 array of 5 integer: 
	  		[0] = segment type; 0 = straight, 1 = curve, 
			[1] = length (for straights),
			[2] = arc (for curves),
			[3] = left radius (for curves), and
			[4] = right radius (for curves) */
	public int[][] track;
	public double grip, accel, maxspeed;
	
	// METHODS
	
	// Constructor to appease clone()
	public TrackSolution(){}
	
	// True constructor with parameter, initialize solution
	public TrackSolution(int segments, double _grip, double _accel, double _max){
		grip = _grip;
		accel = _accel;
		maxspeed = _max;
		
		Random r = new Random();
		track = new int[segments][5];
		
		// For now, just initialize the segment randomly
		for(int i = 0; i < segments; i++){
			// Initialize track informations
			track[i][0] = r.nextInt(1);
			track[i][1] = r.nextInt(80) + 20;
			track[i][2] = r.nextInt(540) - 360;
			track[i][3] = r.nextInt(35) + 15;
			track[i][4] = r.nextInt(35) + 15;
		}
	
	}
	
	// Clone
	public Object clone(){
        TrackSolution copy = (TrackSolution)super.clone();
        
        copy.track = new int[this.track.length][];
        for(int i = 0; i < this.track.length; i++){
        	copy.track[i] = this.track[i].clone();
        }
        
        return copy;
	}
	
	public String toString(){
		StringBuffer s = new StringBuffer();
		
		s.append("RACE SEGMENTS \n");
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
		}
		
		s.append("Validation Value: " + getObjectiveValue()[0] + "\n");
		s.append("Evaluation Value: " + getObjectiveValue()[1] + "\n");
		s.append("Intersection value: " + getObjectiveValue()[2] + "\n");
		s.append("Curvature value: " + getObjectiveValue()[3] + "\n");
		s.append("Speed value: " + getObjectiveValue()[4] + "\n");
		s.append("Last coordinate: " + getObjectiveValue()[5] + ", " + getObjectiveValue()[6] + "\n");
		s.append("Last angle: " + getObjectiveValue()[7] + "\n");
	
		return s.toString();
	}
	
}
