package tabu;

import org.coinor.opents.*;
import problem.Track;

public class TrackMoveManager implements MoveManager {

	@Override
	// Return all legal moves at the moment
	public Move[] getAllMoves(Solution sol) {
		
		// Initialize variables
		Move[] buffer;
		int bufferpos = 0;
		int[][] ttrack = ((TrackSolution)sol).track;
		
		// If there is no curve segment in the solution yet, the only legal move is to change the segment type
		if(!Track.checkCurve(ttrack) || sol.getObjectiveValue()[2] > 0){
			buffer = new Move[ttrack.length];
			bufferpos = ttrack.length;
			
			for(int i = 0; i < ttrack.length; i++){
				buffer[i] = new TrackMove(i, new int[]{1, 0, 0, 0, 0});
			}
		}else{ // Else, all moves become legal			
			buffer = new Move[ttrack.length * (2 + 11 + 41 + 6 + 6)];
			
			for(int i = 0; i < ttrack.length; i++){
				// Moves that changes the segment's type
				for(int j = 0; j < 2; j++){
					buffer[bufferpos++] = new TrackMove(i, new int[]{j, 0, 0, 0, 0});
				}
				
				// Possible moves for straight segments
				if(ttrack[i][0] == 0){
					// Moves that changes the segment's length
					for(int j = 0; j < 11; j++){
						buffer[bufferpos++] = new TrackMove(i, new int[]{0, j, 0, 0, 0});
					}
				}else{ // Possible moves for curved segments
					// Moves that changes the segment's arc
					for(int j = 0; j < 41; j++){
						buffer[bufferpos++] = new TrackMove(i, new int[]{0, 0, j-20, 0, 0});
					}
					
					// Moves that changes the segment's radius 1
					for(int j = 0; j < 6; j++){
						buffer[bufferpos++] = new TrackMove(i, new int[]{0, 0, 0, j, 0});
					}
					
					// Moves that changes the segment's radius 2
					for(int j = 0; j < 6; j++){
						buffer[bufferpos++] = new TrackMove(i, new int[]{0, 0, 0, 0, j});
					}
				}
			}	
		}
		
		// Trim the buffer
		Move[] moves = new Move[bufferpos];
        System.arraycopy( buffer, 0, moves, 0, bufferpos );
        
		
		return moves;
	}

}
