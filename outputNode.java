
        /****************************************
         *                                      *
         *           outputNode class           *
         *         Andrew Miller-Smith          *
         *                                      *
         ****************************************/

        /*

        Description: The outputNode class represents the neurons which are receiving information from the axons of our inputNodes. Each outputNode contains a target value from
                     the input file and a unique identifier. Class includes basic getting and setter functions as well as the equation for calculating the outputNode's value (a function of
                     the signals from the inputNodes).

        */

import java.util.*;
import java.io.*;
import java.util.Random;
import java.lang.Math;

public class outputNode {

    //target value and value received from inputNodes, unique identifier
    private double value = 0;
    private double target;
    private int identifier;

    //variables for sigmoid activation equation
    public final double e = 2.71828;
    private final double OUTPUT_MULTIPLIER = 10;
    private final double SHIFT = 0.5;
 
    //constructor takes target value of output node & identifier. Value depends on output representation specified on command line
    public outputNode(double tar, int index){
 
        target = tar;       
        identifier = index;
    }
 
    public void calculateValue(double input){
        value = 1 / (1 + Math.pow(e, -input + SHIFT));
   }
 
    public double getValue(){ 
        return value;
    }
 
    public double getTarget(){
        return target;
    }

    public int getIdentifier(){
        return identifier;
    }

    public boolean equals(outputNode x){

        if (x.getIdentifier() == identifier){
            return true;
        }
        return false;
    }

}
