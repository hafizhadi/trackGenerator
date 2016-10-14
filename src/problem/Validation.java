package problem;

import util.Point;

//Static class that contains the validation functions of the problem
public class Validation {
	/* VALIDATION FUNCTIONS */
	// Checkers for the two requirement of the track: 
	// 1. it's closed (starts and ends on the same point
	// 2. It doesn't cross in the middle
	
	public static double[] countValidityValue(int[][] track){
		// Variable for displacement calculation result
		double x, y, direction;
		x = 0; y = 0; direction = 0;
		
		// Calculated validity values
		double distanceVal, directionVal, intersectionVal;
		distanceVal = directionVal = intersectionVal = 0;
		
		// Points are stored after each segment's displacement calculation
		Point[] points = new Point[track.length * 2 + 1];
		points[0] = new Point(0, 0, 0);
		int pointsPointer = 1;
		
		// Displacement calculation
		for(int i = 0; i < track.length; i++){		
			double[] res = countDisplacement(track[i], direction);
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
		
		// Calculate values
		distanceVal = Math.sqrt((x*x) + (y*y));
		directionVal = Math.abs(direction % 360);
		intersectionVal = countIntersection(points, pointsPointer);
		
		return new double[] {distanceVal, directionVal, intersectionVal, x, y, direction };
	}
	
	// Calculate the displacement in x and y caused by a segment on a current direction
	// on the result, [0] is x, [1] is y, [2] is the angle displacement
	// Also contains the midpoint displacement for calculating intersection between segments in [3] and [4]
	public static double[] countDisplacement(int[] segment, double cDirection){
		double[] result = new double[3];		
		double radDir = Math.toRadians(cDirection);
		
		// Calculate for straight )segments
		if(segment[0] == 0){		
			
			result = new double[3];
			result[0] = segment[1] * Math.cos(radDir);
			result[1] = segment[1] * Math.sin(radDir);
			result[2] = 0;
		}else{	// Else for curved segments

			result = new double[5];	
			result[0] = segment[3] * Math.cos(Math.toRadians(cDirection + ((double) segment[2]/4)));
			result[1] = segment[3] * Math.sin(Math.toRadians(cDirection + ((double) segment[2]/4)));
	
			result[3] = (segment[4] * Math.cos(Math.toRadians(cDirection + (3* ((double) segment[2]/4)))));
			result[4] = (segment[4] * Math.sin(Math.toRadians(cDirection + (3* ((double) segment[2]/4)))));
			
			result[0] += result[3];
			result[1] += result[4];
	
			// Calculate direction displacement
			result[2] = segment[2];
		}
			
		return result;
	}
	
	// Return the number of intersection in the track
	public static int countIntersection(Point[] track, int limit){
		int result = 0;
		
		for(int i = 0; i < limit - 3; i++){
			for(int j = i + 2; j < limit - 1; j++){
				if(Point.doIntersect(track[i], track[i+1], track[j], track[j+1])){
					result += 1;
				}
			}
		}
		
		return result;
	}
	
}
