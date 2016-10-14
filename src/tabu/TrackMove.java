package tabu;

import org.coinor.opents.*;

import problem.Track;

public class TrackMove implements Move {
	int position;
	int[] displacement;

	public TrackMove(int _position, int[] _displacement){
		this.position = _position;
		this.displacement = _displacement.clone();
	}
	
	@Override
	public void operateOn(Solution sol) {
		int[][] ttrack = ((TrackSolution)sol).track; 
		ttrack[position] = Track.applyMove(ttrack[position], displacement);
	}

	// Hashcode for tabu list
	public int hashCode(){
		// Hashcode is the hashcode of the string containing the appended values
		return ("" + position + displacement[0] + displacement[1] + displacement[2]
				+ displacement[3] + displacement[4]).hashCode();
	}
	
}
