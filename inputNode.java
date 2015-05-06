
	/****************************************
	 *                                      *
	 *           inputNode class            *
	 *         Andrew Miller-Smith          *
	 *                                      *
	 ****************************************/

	/*

	Description:    The inputNode class represents a neuron that sends information to other neurons (in this case the outputNodes). Each neuron contains a binary value and unique identifier.
                    The value is used in sending signals to the outputNodes. Class includes basic getter and setter functions.

	*/

import java.io.*;
import java.util.*;

public class inputNode{

	private int value;
	private int identifier;
 
    //constructor takes value of node
    public inputNode(int val, int index){      
        
        value = val;
        identifier = index;
    }
 
    public int getValue(){ 
        return value;
    }

    public void setValue(int newValue){
    	value = newValue;
    }

    public int getIdentifier(){
    	return identifier;
    }

    public boolean equals(inputNode x){

    	if (x.getIdentifier() == identifier){
    		return true;
    	}
    	return false;
    }
}