package ui;


import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import util.Point;


class TrackSurface extends JPanel {
    Point[] points;
	
    public TrackSurface(){}
    
	public TrackSurface(Point[] trackPoints){
		points = trackPoints.clone();
	};
	
    private void doDrawing(Graphics g) {
    	
    	double[] center = findCenter(points);
    	int centerX = 400 - (int) center[0];
    	int centerY = 400 + (int) center[1];
    	
        Graphics2D g2d = (Graphics2D) g;
        
        // System.out.println(centerX + " " +  centerY);
        
        // Draw coordinate line
        g2d.drawLine(centerX, 0, centerX, 800);
        g2d.drawLine(0, centerY, 800, centerY);
        
        // Draw lines based on the points
        for(int i = 0; i < points.length - 1; i++){
        	if(points[i + 1].segmentType == 0){
                g2d.setColor(Color.red);
        	}else{
        		g2d.setColor(Color.blue);
        	}
        	    	
        	g2d.drawLine((int) points[i].x + centerX, -(int)points[i].y + centerY, (int)points[i+1].x + centerX, -(int)points[i+1].y + centerY);
        }

   } 

    @Override
    public void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        doDrawing(g);
    }    
    
    private double[] findCenter(Point[] points){
    	double maxX, minX, maxY, minY;
    	maxX = minX = maxY = minY = 0;
    	
    	// Find max & min x and y
    	for(Point po: points){
    		maxX = (po.x > maxX) ? po.x : maxX;
    		minX = (po.x < minX) ? po.x : minX;
    		maxY = (po.y > maxY) ? po.y : maxY;
    		minY = (po.y < minY) ? po.y : minY;
    	}
    	
    	// System.out.println("X: " + maxX + " " + minX);
    	// System.out.println("Y: " + maxY + " " + minY);
    	
    	return new double[] { (maxX + minX) / 2, (maxY + minY) / 2 };
    }
}

public class TrackDisplay extends JFrame {

    public TrackDisplay(Point[] points, String name) {

        initUI(points, name);
    }
    
    private void initUI(Point[] points, String name) {
        
        setTitle(name);
        
        add(new TrackSurface(points));
        
        setSize(800, 800);
        setLocationRelativeTo(null);        
    }
    
}