package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.operators.AbstractCrossover;

import problem.Track;

public class TrackCrossover extends AbstractCrossover<Track> {

	public TrackCrossover(){
		this(1);
	}
	
	public TrackCrossover(int crossoverPoints) {
		super(crossoverPoints);
		// TODO Auto-generated constructor stub
	}
	
	public TrackCrossover(int crossoverPoints,
			Probability crossoverProbability) {
		super(crossoverPoints, crossoverProbability);
		// TODO Auto-generated constructor stub
	}
	
	public TrackCrossover(NumberGenerator<Integer> crossoverPointsVariable) {
		super(crossoverPointsVariable);
		// TODO Auto-generated constructor stub
	}
	
	public TrackCrossover(NumberGenerator<Integer> crossoverPointsVariable,
			NumberGenerator<Probability> crossoverProbabilityVariable) {
		super(crossoverPointsVariable, crossoverProbabilityVariable);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected List<Track> mate(Track parent1, Track parent2, int numberOfCrossoverPoints, Random rng) {
		
		if (parent1.track.length != parent2.track.length){
            throw new IllegalArgumentException("Cannot perform cross-over with different length parents.");
        }
		
        Track offspring1 = new Track(parent1.track);
        Track offspring2 = new Track(parent2.track);
        
        // Apply as many cross-overs as required.
        for (int i = 0; i < numberOfCrossoverPoints; i++){
            // Cross-over index is always greater than zero and less than
            // the length of the parent so that we always pick a point that
            // will result in a meaningful cross-over.
            int crossoverIndex = (1 + rng.nextInt(parent1.track.length - 1));
            	
            for (int j = 0; j < crossoverIndex; j++){
            	int[] temp = offspring1.track[j].clone();
            
                offspring1.track[j] = offspring2.track[j].clone();
                offspring2.track[j] = temp;
            }
        }
        
        List<Track> result = new ArrayList<Track>(2);
        result.add(offspring1);
        result.add(offspring2);
        
        return result;
    }

}
