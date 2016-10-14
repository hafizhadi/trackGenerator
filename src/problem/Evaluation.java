package problem;

import problem.Constants;

// Static class that contains the evaluation functions of the problem
public class Evaluation {
	
	/* EVALUATION FUNCTIONS */
	// Evaluate the fun and the difficulty of the track
	
	// The fun is evaluated by using two things: the track profile and the track top speed
	// Curvature profile
	public static double evaluateCurvature(int[][] track){
		int[] segTypes, segLengths, segArcs, segRads;
		int straightCount, curvedCount;
		double typeDev, typeDiffDev, lengthDev, arcDev, radDev;
		
		// Initialize containers
		segTypes = new int[track.length];
		segArcs = new int[track.length];
		
		// segLengths = new int[track.length];
		// segRads= new int[track.length];
		
		straightCount = curvedCount = 0;
		
		// Split the track into arrays of data
		for(int i = 0; i < track.length; i++){			
			// Add type data
			segTypes[i] = track[i][0];
			
			// Other data						
			if(track[i][0] == 0){
				// segLengths[straightCount] = track[i][1];
				straightCount++;
			}else{
				segArcs[curvedCount] = track[i][2];
				// segRads[curvedCount] = track[i][3] + track[i][4];
				curvedCount++;
			}
		}
		
		// Compute the standard deviation for every type
		typeDev = standardDeviation(segTypes, track.length);
		typeDiffDev = orderDeviation(segTypes, track.length);
		arcDev = standardDeviation(segArcs, curvedCount);
		// lengthDev = standardDeviation(segLengths, straightCount);
		// radDev = standardDeviation(segRads, curvedCount);
		
		// Weight properly, sum, and return
		return (typeDev * Constants.CURVETYPE_WEIGHT) + (typeDiffDev * Constants.CURVETYPEDIFF_WEIGHT) +  (arcDev * Constants.CURVEARC_WEIGHT);
	}
	
	// Speed profile
	public static double evaluateSpeed(int[][] track, double grip, double acceleration, double maxSpeed){
		// Speed after each segment
		double[] speedData = new double[track.length];
		
		speedData[0] = calculateFinalSpeed(0, acceleration, grip, maxSpeed, track[0]);
		
		// Calculate speed after each segment
		for(int i = 1; i < track.length; i++){
			speedData[i] = calculateFinalSpeed(speedData[i-1], acceleration, grip, maxSpeed, track[i]);
			//System.out.println("SPEED " + i + " " + speedData[i]);
		}
		
		// From speed data, get speed value. For now is the standard deviation
		
		return standardDeviation(speedData, speedData.length);
	}
	
	/* UTILITY FUNCTIONS */
	
	// Count the final speed given the initial speed, grip, acceleration, and the segment
	private static double calculateFinalSpeed(double v0, double a, double grip, double maxSpeed, int[] segment){
		
		double result = 0;
		
		// If straight segment, just count as usual
		if(segment[0] == 0){
			result = (v0 * v0) + (2 * a * segment[1]);
		}else{	// If curved, a more difficult calculation is needed
			
			// Calculate the minimal time possible to complete the turn
			double minTime = segment[2] / grip;
			
			// Calculate the maximum acceleration to make that time
			double maxAcc = 2 * ((segment[3] + segment[4]) - (v0 * minTime)) / (minTime * minTime);
			
			// Is the current acceleration bigger or smaller?
			if(a > maxAcc){ // If bigger, use the maximum acceleration
				result = (v0 * v0) + (2 * maxAcc * (segment[3] + segment[4]));
			}else{	// If smaller, use that acceleration
				result = (v0 * v0) + (2 * a * (segment[3] + segment[4]));
			}
		}
		
		double res = Math.sqrt(result);
		
		return (res > maxSpeed) ? maxSpeed : res;
	}
	
	// Count the standard deviation of an array of int, checks null
	private static double standardDeviation(int[] data, int limit){
		if(limit > 0){
			int n = 0;
		    double sum = 0;
		    double sum2 = 0;
		    
		    for(int i = 0; i < limit; i++){
		    	n++;
				sum += data[i];
				sum2 += data[i] * data[i];		
		    }
			
			return Math.sqrt(sum2/n - sum*sum/n/n);
		}else{
			return 0;
		}
	}
	
	private static double standardDeviation(double[] data, int limit){
		if(limit > 0){
			int n = 0;
		    double sum = 0;
		    double sum2 = 0;
		    
		    for(int i = 0; i < limit; i++){
		    	n++;
				sum += data[i];
				sum2 += data[i] * data[i];		
		    }
			
			return Math.sqrt(sum2/n - sum*sum/n/n);
		}else{
			return 0;
		}
	}
	
	// Makeshift calculation of order-conscious diversity
	private static double orderDeviation(int[] data, int limit){
		double diff = 0;
		
		for(int i = 0; i < limit - 1; i++){
			diff += Math.abs(data[i + 1] - data[i]);  
		}
		
		return diff / (limit - 1);
	}
}
