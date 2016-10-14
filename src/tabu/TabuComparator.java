package tabu;

import java.util.Comparator;

public class TabuComparator implements Comparator<TrackSolution>{

	@Override
	public int compare(TrackSolution a, TrackSolution b) {
		// TODO Auto-generated method stub
		return (int) (a.getObjectiveValue()[1] - b.getObjectiveValue()[1]);
	}
	
}