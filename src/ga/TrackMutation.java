package ga;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.uncommons.maths.number.ConstantGenerator;
import org.uncommons.maths.number.NumberGenerator;
import org.uncommons.maths.random.Probability;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import problem.Track;
import problem.Validation;

public class TrackMutation implements EvolutionaryOperator<Track> {
	
    private final NumberGenerator<Probability> mutationProbability;
	
    public TrackMutation(Probability mutationProbability)
    {
        this(new ConstantGenerator<Probability>(mutationProbability));
    }


    /**
     * Creates a mutation operator that is applied with the given
     * probability and draws its characters from the specified alphabet.
     * @param alphabet The permitted values for each character in a string.
     * @param mutationProbability The (possibly variable) probability that a
     * given character is changed.
     * @return 
     */
    public TrackMutation(NumberGenerator<Probability> mutationProbability)
    {
        this.mutationProbability = mutationProbability;
    }
    
	@Override
	public List<Track> apply(List<Track> selectedCandidates, Random rng) {
        List<Track> mutatedPopulation = new ArrayList<Track>(selectedCandidates.size());
        
        for (Track t : selectedCandidates)
        {
        	mutatedPopulation.add(mutateTrack(t, rng));
        }
        
        return mutatedPopulation;
	}

	private Track mutateTrack(Track t, Random rng){
		
		// Clone old track
		int[][] newTrack = new int[t.track.length][];		
        for(int i = 0; i < t.track.length; i++){
        	newTrack[i] = t.track[i].clone();
        }
		
        // Mutate
		for(int i = 0; i < t.track.length; i++){
			if(mutationProbability.nextValue().nextEvent(rng)){
				// The displacement
				int[] displacement;
				
				if(!Track.checkCurve(t.track)){
					displacement = new int[]{ 1, 0, 0, 0, 0 };
				}else{		
					int type = rng.nextInt(5);
				
					switch(type){
						case 0:
							displacement = new int[]{ 0, rng.nextInt(11), 0, 0, 0 };
							break;
						case 1:
							displacement = new int[]{ 0, 0, rng.nextInt(41) - 20, 0, 0 };
							break;
						case 2:
							displacement = new int[]{ 0, 0, 0, rng.nextInt(6), 0 };
							break;
						case 3:
							displacement = new int[]{ 0, 0, 0, 0, rng.nextInt(6) };
							break;
						case 4:
							displacement = new int[]{ rng.nextInt(2), 0, 0, 0, 0 };
							break;
						default:
							displacement = new int[]{ 0, 0, 0, 0, 0 };
							break;
					}
				}
				
				newTrack[i] = Track.applyMove(newTrack[i], displacement);
			}
		}
		
		// Return
		return new Track(newTrack);
	}
	
}
