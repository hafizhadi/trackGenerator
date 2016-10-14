package ga;

import java.util.Comparator;
import problem.Track;

public class GAComparator implements Comparator<Track>{

	@Override
	public int compare(Track a, Track b) {
		// TODO Auto-generated method stub
		return (int) (b.fitness - a.fitness);
	}
	
}