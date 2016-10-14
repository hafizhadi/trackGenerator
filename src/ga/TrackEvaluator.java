package ga;

import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import problem.Constants;
import problem.Evaluation;
import problem.Track;
import problem.Validation;

public class TrackEvaluator implements FitnessEvaluator<Track>{

	private final int accel;
	private final int grip;
	private final int maxSpeed;
	
	public TrackEvaluator(int accel, int grip, int maxSpeed){
		this.accel = accel;
		this.grip = grip;
		this.maxSpeed = maxSpeed;
	}
	
	@Override
	public double getFitness(Track candidate, List<? extends Track> population) {
		// Variable for race profile validation
		double[] validVal = new double[6];
		
		// Variable for race profile evaluation
		double curveVal = 0;
		double speedVal = 0;
		
		// Calculate validation values
		validVal = Validation.countValidityValue(candidate.track);
		
		// Calculate evaluation values
		curveVal = Evaluation.evaluateCurvature(candidate.track);
		speedVal = Evaluation.evaluateSpeed(candidate.track, accel, grip, maxSpeed);
		
		candidate.fitness = (((((int) validVal[0]) * Constants.DISTANCE_WEIGHT) + (validVal[1] * Constants.DIRECTION_WEIGHT) + (validVal[2] * Constants.INTERSECTION_WEIGHT)) * 1000) + (1000 - ((Constants.CURVATURE_WEIGHT * curveVal) + (Constants.SPEED_WEIGHT * speedVal)));
		
		
		// TODO Auto-generated method stub
		return candidate.fitness;
	}

	@Override
	public boolean isNatural() {
		return false;
	}

}
