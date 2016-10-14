package ga;

import java.util.Random;

import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import problem.Track;

public class TrackFactory extends AbstractCandidateFactory<Track>{

	private final int length;
	
	public TrackFactory(int length){
		this.length = length;
	}
	
	@Override
	public Track generateRandomCandidate(Random r) {
		// TODO Auto-generated method stub
		
		return new Track(Track.createNewTrack(this.length, r));
	}

}
