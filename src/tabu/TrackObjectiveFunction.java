package tabu;

import org.coinor.opents.*;

import problem.Constants;
import problem.Evaluation;
import problem.Track;
import problem.Validation;
import util.Point;

public class TrackObjectiveFunction implements ObjectiveFunction{

	@Override
	// Evaluate a solution or a result of a move
	/* The value for now represent two things:
	 	1. The distance of the last segment to the first 
	 	2. The arc of the last segment */
	public double[] evaluate(Solution solution, Move move) {
		
		// The track data
		int[][] ttrack =  ((TrackSolution)solution).track;
		double acc =  ((TrackSolution)solution).accel;
		double gr = ((TrackSolution)solution).grip;
		double maxSp = ((TrackSolution)solution).maxspeed;
		
		// The result
		double[] result;
		
		// If move is null, calculate from the beginning
		if(move == null){
			result = calculateTrackValues(ttrack, acc, gr, maxSp);
		}else{	// Else, modify and recalculate
			int position =  ((TrackMove)move).position;
			
			// Apply move to track segment and store the segment
			int[] newSegment = ((TrackMove)move).displacement.clone();
			newSegment = Track.applyMove(ttrack[position], newSegment);
			
			// Clone and modify the track
			int[][] trackCopy = new int[ttrack.length][];
	        for(int i = 0; i < ttrack.length; i++){
	        	trackCopy[i] = ttrack[i].clone();
	        }
	        
	        trackCopy[position] = newSegment;
	        
	        // Calculate the displacement of the new track
	        result = calculateTrackValues(trackCopy, acc, gr, maxSp);    
		}
		
		// Assign objective value
		double[] finResult = new double[8];
		
		finResult[0] = ((((int) result[0]) * Constants.DISTANCE_WEIGHT) + (result[1] * Constants.DIRECTION_WEIGHT) + (result[2] * Constants.INTERSECTION_WEIGHT));
		finResult[1] = ((Constants.CURVATURE_WEIGHT * result[3]) + (Constants.SPEED_WEIGHT * result[4]));
		
		finResult[2] = result[2];
		finResult[3] = result[3];
		finResult[4] = result[4];
		finResult[5] = result[5];
		finResult[6] = result[6];
		finResult[7] = result[7];
		
		return finResult;
	}
	
	// Calculate the values of the track
	public double[] calculateTrackValues(int[][] track, double accel, double grip, double maxSp){
		
		// Variable for race profile validation
		double[] validVal = new double[6];
		
		// Variable for race profile evaluation
		double curveVal = 0;
		double speedVal = 0;
		
		// Calculate validation values
		validVal = Validation.countValidityValue(track);
		
		// Calculate evaluation values
		curveVal = Evaluation.evaluateCurvature(track);
		speedVal = Evaluation.evaluateSpeed(track, accel, grip, maxSp);

		//System.out.println(speedVal);
		
		return new double[] { validVal[0], validVal[1], validVal[2], curveVal, speedVal, validVal[3], validVal[4], validVal[5] };
	}
}
