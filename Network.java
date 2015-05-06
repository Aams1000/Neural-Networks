/****************************************
 *                                      *
 *             Network class            *
 *         Andrew Miller-Smith          *
 *                                      *
 ****************************************/

/*

        Description:    The Network class contains the basic neural network and machine-learning algorithm. The train and test functions take vectors of inputNodes, outputNodes, a 2D
                        array of paths between them, and the learning rate. InputNodes send weighted signals to the outputNodes, which use this to calculate
                        their values. Path weights are then adjusted based on how close those values are to the outputNodes' specified targets.
                        The train function returns the updated paths array, while the test function returns a boolean value of whether the result
                        was the same as the target.
 */

import java.util.*;
import java.io.*;
import java.util.Random;

public class Network {

	//instance of Neural class
	private static Neural neural = new Neural();

	//output multiplier turns decimal values into ints
	private static final double OUTPUT_MULTIPLIER = 10;

	//number of epochs for each training run
	private static final int NUM_EPOCHS = 50;

	//variable for tracking network performance
	private static int NUM_CORRECT = 0;
	private static int NUM_POSSIBLE = 0;
	private static double PERCENT_CORRECT;

	//vectors of inputNodes and outputNodes, constants storing their sizes
	private static Vector<inputNode> inputs;
	private static Vector<outputNode> outputs;
	private static int NUM_INPUT_NODES;
	private static int NUM_OUTPUT_NODES;

	//learning rate will be specified by command line input
	private static double ALPHA;

	//2D array of paths
	private static Path[][] paths;

	//random number generator for use in methods
	private static Random rand = new Random();

	
	//The train function takes vectors of inputNodes, outputNodes, a 2D array of paths between them, and the learning rate. 
	//InputNodes send weighted values to the outputNodes, which use this to calculate their values. Path weights are then adjusted based 
	//on how close those values are to the outputNodes' specified targets. Returns the updated paths array
	public static Path[][] train(Vector<inputNode> trainingInput, Vector<outputNode> trainingOutput, Path[][]trainingPaths, double alpha){

		//set variables to given training parameters
		inputs = trainingInput;
		outputs = trainingOutput;
		paths = trainingPaths;
		ALPHA = alpha;
		NUM_INPUT_NODES = inputs.size();
		NUM_OUTPUT_NODES = outputs.size();

		//update start and end nodes for each path
		for (int i = 0; i < NUM_INPUT_NODES; i++){		
			for (int j = 0; j < NUM_OUTPUT_NODES; j++){
				
				paths[i][j].setStart(inputs.get(i));
				paths[i][j].setEnd(outputs.get(j));
			}
		}

		//array for storing input sums for each node
		double inputSums[] = new double[NUM_OUTPUT_NODES];
		
		//calculate input sums for each output node
		for (int i = 0; i < NUM_INPUT_NODES; i++){
			for (int j = 0; j < NUM_OUTPUT_NODES; j++){

				inputSums[j] += paths[i][j].getValue();
			}
		}

		//calculate values of output nodes based on weighted inputs
		for (int i = 0; i < NUM_OUTPUT_NODES; i++){
			outputs.get(i).calculateValue(inputSums[i]);
		}

		//update weights based on output values
		for (int i = 0; i < NUM_INPUT_NODES; i++){	
			for (int j = 0; j < NUM_OUTPUT_NODES; j++){
				
				paths[i][j].updateWeight(ALPHA, inputSums[j]);
			}
		}

		//check correctness of tests
		double percentCorrect;
		double result;
		double integerTarget;
		double percentError;

		//case with only one output node
		if (outputs.size() == 1){

			result = (int)(outputs.get(0).getValue() * OUTPUT_MULTIPLIER + 0.5);			
			integerTarget = (int)(outputs.get(0).getTarget() * OUTPUT_MULTIPLIER);

			//check if output is within percent error bounds (i.e. if it is considered correct)
			percentError = Math.abs((integerTarget - result) / integerTarget);
			if (result == integerTarget){
				Neural.incrementNumberCorrect();
			}
		}

		//output is represented in bit string
		else{
			//loop through outputs. The node with the highest value is the network's output
			int bestNode = 0;
			double bestValue = Double.NEGATIVE_INFINITY;
			int actualTarget = 0;
			for (int i = 0; i < NUM_OUTPUT_NODES; i++){

				if (outputs.get(i).getValue() > bestValue){
					bestNode = i;
					bestValue = outputs.get(i).getValue();
				}

				//find which node is the actual target value
				if (outputs.get(i).getTarget() == 1){
					actualTarget = i;
				}
			}

			//check if output is correct
			result = bestNode;
			integerTarget = actualTarget;
			percentError = Math.abs((integerTarget - result) / integerTarget);
			
			if (result == integerTarget){
				Neural.incrementNumberCorrect();;
			}
		}	
		return paths;
	}

	//The test function takes vectors of inputNodes, outputNodes, a 2D array of paths between them, and the learning rate. 
	//InputNodes send weighted values to the outputNodes, which use this to calculate their values. Returns a boolean
	//value regarding whether the output is the same as the target value
	public static boolean test(Vector<inputNode> testInput, Vector<outputNode> testOutput, Path[][]testPaths, double alpha){

		//set value and data structures equal to new testing parameters
		inputs = testInput;
		outputs = testOutput;
		paths = testPaths;
		ALPHA = alpha;
		NUM_INPUT_NODES = inputs.size();
		NUM_OUTPUT_NODES = outputs.size();

		//update start and end nodes for each path
		for (int i = 0; i < NUM_INPUT_NODES; i++){	
			for (int j = 0; j < NUM_OUTPUT_NODES; j++){

				paths[i][j].setStart(inputs.get(i));
				paths[i][j].setEnd(outputs.get(j));
			}
		}

		//array for storing input sums for each node
		double inputSums[] = new double[NUM_OUTPUT_NODES];

		//calculate input sums for each output node
		for (int i = 0; i < NUM_INPUT_NODES; i++){
			for (int j = 0; j < NUM_OUTPUT_NODES; j++){

				inputSums[j] += paths[i][j].getValue();
			}
		}

		//calculate values of output nodes based on weighted inputs
		for (int i = 0; i < NUM_OUTPUT_NODES; i++){
			outputs.get(i).calculateValue(inputSums[i]);
		}
		
		//check correctness of tests
		int numCorrect = 0;
		double percentCorrect;
		double result;
		double integerTarget;
		double percentError;

		//case with only one output node
		if (outputs.size() == 1){

			result = (int)(outputs.get(0).getValue() * OUTPUT_MULTIPLIER + 0.5);
			integerTarget = (int)(outputs.get(0).getTarget() * OUTPUT_MULTIPLIER);

			//check if output is correct
			percentError = Math.abs((integerTarget - result) / integerTarget);
			if (result == integerTarget){

				Neural.incrementNumberCorrect();
				return true;
			}
			else{
				return false;
			}
		}

		//output is represented in bit string
		else{
			//loop through outputs - the node with the highest value is the network's output
			int bestNode = 0;
			double bestValue = Double.NEGATIVE_INFINITY;
			int actualTarget = 0;
			for (int i = 0; i < NUM_OUTPUT_NODES; i++){

				if (outputs.get(i).getValue() > bestValue){
					bestNode = i;
					bestValue = outputs.get(i).getValue();
				}

				//find which node is the actual target value
				if (outputs.get(i).getTarget() == 1){		
					actualTarget = i;
				}
			}

			//check if output is correct
			result = bestNode;
			integerTarget = actualTarget;
			percentError = Math.abs((integerTarget - result) / integerTarget);
			
			if (result == integerTarget){		
				Neural.incrementNumberCorrect();
				return true;
			}
			else{
				return false;
			}
		}
	}

	//function to generate inputNodes for testing. DON'T DELETE ME, I AM A TIMELESS RELIC OF THE PAST
	public static void generateInputNodes(){

		for (int i = 0; i < NUM_INPUT_NODES; i++){

			double randomDouble = rand.nextDouble();
			int value;

			if (randomDouble <= 0.5){			
				value = 0;
			}
			else{	
				value = 1;
			}

			//create random inputNode, add to inputs vector
			inputNode newInput = new inputNode(value, i);
			inputs.add(newInput);
		}
	}

	//function to generate outputNodes for testing
	public static void generateOutputNodes(){

		for (int i = 0; i < NUM_OUTPUT_NODES; i++){

			//int randomInt = rand.nextInt(9);
			double randomDouble = rand.nextDouble();
			double value;

			if (NUM_OUTPUT_NODES == 1){	
				value = randomDouble;
			}
			else if (randomDouble <= 0.5){	
				value = 0;
			}
			else{	
				value = 1;
			}

			//create random inputNode, add to inputs vector
			outputNode newOutput = new outputNode(value, i);
			outputs.add(newOutput);
		}
	}

	/****************************************
	 *                                      *
	 *        generatePaths function        *
	 *         Andrew Miller-Smith          *
	 *                                      *
	 ****************************************/

	//function creates paths between every inputNode and every outputNode. Called in Neural class after files have been read in.
	//Takes vectors of inputNodes and outputNodes as parameters, returns void
	public static Path[][] generatePaths(Vector<inputNode> inputNodes, Vector<outputNode> outputNodes){

		int numInputs = inputNodes.size();
		int numOutputs = outputNodes.size();
		
		//initialize paths array
		Path[][] newPaths = new Path[numInputs][numOutputs];

		int uniqueIdentifer = 0;
		
		//create paths
		for (int i = 0; i < numInputs; i++){
			for (int j = 0; j < numOutputs; j++){

				Path newPath = new Path(inputNodes.get(i), outputNodes.get(j), uniqueIdentifer);
				newPaths[i][j] = newPath;
				uniqueIdentifer++;
			}
		}
		return newPaths;
	}
}


