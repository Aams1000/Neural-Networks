
        /****************************************
         *                                      *
         *              Path class              *
         *         Andrew Miller-Smith          *
         *                                      *
         ****************************************/

        /*

        Description: A path represents the axon of a neuron (inputNode) connecting to another neuron (an outputNode). Each path has a weight, which is used 
        in determining the strength of its signals to the outputNodes. Weight is initialized to a random value between -0.15 and 0.15. Class includes basic
        getter and setter functions, as well as the equations used for updating the weights.

        */

import java.io.*;
import java.util.*;
import java.util.Random;
import java.lang.Math;

public class Path{
 
	//start and end nodes of the path, weight of path, unique identifier
	private inputNode start;
	private outputNode end;
	private double weight;
	private int identifier;

	//values for sigmoid activation equation
	public final double e = 2.71828;
	private final double INITIAL_WEIGHT_LIMIT = 0.15;

	private final Random weightGenerator = new Random();
	 
	//constructor
	public Path(inputNode begin, outputNode destination, int index){
	   
	    start = begin;
	    end = destination;
	    identifier = index;

	    //INITIALIZE WEIGHT. I LIKE CAPS
	    double potentialWeight = weightGenerator.nextDouble();
	    while (potentialWeight > INITIAL_WEIGHT_LIMIT*2){	
	    	potentialWeight = weightGenerator.nextDouble();
	    }

	    weight = INITIAL_WEIGHT_LIMIT - potentialWeight;
	}

	public void setStart(inputNode newStart){
		start = newStart;
	}

	public void setEnd(outputNode newEnd){
		end = newEnd;
	}
	 
	public void updateWeight(double alpha, double inputSum){
	    weight += alpha * start.getValue() * (end.getTarget() - end.getValue()) * getDerivative(inputSum);
	}
	 
	public double getDerivative(double x){  
	    return Math.pow(e, x) / Math.pow(1 + Math.pow(e, x), 2);
	}
	 
	public double getValue(){ 
	    return (double)weight*(double)start.getValue();
	}

	public inputNode getStart(){
		return start;
	}

	public outputNode getEnd(){
		return end;
	}

	public int getIdentifier(){
    	return identifier;
    }

    public boolean equals(Path x){
    	if (x.getIdentifier() == identifier){
    		return true;
    	}
    	return false;
    }
 
}