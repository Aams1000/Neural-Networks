
/****************************************
 *                                      *
 *              Neural class            *
 *              Adela Yang				*
 *     Edited by Andrew Miller-Smith    *
 *                                      *
 ****************************************/

/*

        Description:    The final version of this project contains a script for running multiple hours worth of tests. I have reworked the final
        				code and included an earlier version that allows the user to run the algorithms on the command line. The arguments for running
        				my Network algorithm are as follows:

        				Training file name      (optdigits32x32.tra or optdigits8x8int.tra)
						Testing file name       (optdigits32x32.tes or optdigits8x8int.tes)
						Input type 	            (32 if using 32x32 bitmaps or 8 if using 8x8 matrices)
						Output representation   (s for a single outputNode or t for ten outputNodes)
						Learning rate 			(The amount the network takes each training session into account. For testing, we used 0.9, 0.7, 0.5, 0.3, 0.1, and 0.01)
						
						An example of a command line call. Note that both files must be 32x32 or 8x8:

						java Neural optdigits8x8int.tra optdigits8x8int.tes 8 t 0.01

						Play around with the output representations and learning rate and see what happens to the network's accuracy!

						- Andrew
						
        */

import java.io.*;
import java.util.*;

public class Neural{

	private static Network network = new Network();

	//file Variables
	private static BufferedReader reader = null;
	private static File fileTraining;
	private static File fileTesting;
	private static String fileNameTraining;
	private static String fileNameTesting;
	private static final int NUM_EPOCHS = 50;

	//variable for timer
	private static long duration;

	//int for tracking number of correct answers
	public static int numberCorrect = 0;

	//(training) vectors of input and output nodes, 2D paths array
	private static Vector<Vector<inputNode>> trainingInputVector = new Vector<Vector<inputNode>>();
	private static Vector<Vector<outputNode>> trainingOutputVector = new Vector<Vector<outputNode>>();
	private static Path[][] paths;

	//(testing) vectors of input and output nodes 
	private static Vector<Vector<inputNode>> testingInputVector = new Vector<Vector<inputNode>>();
	private static Vector<Vector<outputNode>> testingOutputVector = new Vector<Vector<outputNode>>();

	//command line arguments
	private static int fileSize; // 8 or 32
	private static String method; // s = single, or t = ten
	private static double learningRate;


	//it reads in from the command line in this order:
	//file name training
	//file name testing
	//size (8 or 32)
	//method (single or ten)
	//learning rate
	public static void main(String[] args) 
	{
		fileNameTraining = args[0];
		fileNameTesting = args[1];

		fileTraining = new File(fileNameTraining);
		fileTesting = new File(fileNameTesting);


		fileSize = Integer.parseInt(args[2]);
		method = args[3];
		learningRate = Double.parseDouble(args[4]);

		readFileTraining(fileTraining);
		readFileTesting(fileTesting);

		paths = network.generatePaths(trainingInputVector.get(0), trainingOutputVector.get(0));

		Path[][] tempPaths;
		
		//start timer
        long startTime = System.currentTimeMillis();
        long endTime = 0;
        duration = 0;

		//training
        System.out.println("\n******************* Neural Network Training Results *******************");
		int epochs = 0;
		int numTrainingSessions = 0;
		double percentCorrect;
		while (epochs < NUM_EPOCHS){

			for (int i = 0; i < trainingInputVector.size() - 1; i++){

				tempPaths = network.train(trainingInputVector.get(i), trainingOutputVector.get(i), paths, learningRate);
				paths = tempPaths;
				numTrainingSessions++;
			}

			//System.out.println("Number correct: " + numberCorrect + " Training sessions: " + numTrainingSessions);
			epochs++;
			percentCorrect = (double)numberCorrect / (double)numTrainingSessions;
			System.out.println("Percent of training files correct after " + epochs + " epochs: " + percentCorrect*100);
			numberCorrect = 0;
			numTrainingSessions = 0;
		}

		//testing
		int numTestingSessions = 0;
		for (int i = 0; i < testingInputVector.size() - 1; i++){

			boolean testValue = network.test(testingInputVector.get(i), testingOutputVector.get(i), paths, learningRate);
			numTestingSessions++;
		}
		
		percentCorrect = (double)numberCorrect / (double)numTestingSessions;

		//end timer
        endTime = System.currentTimeMillis();
        duration = (endTime - startTime);

 /****************************************
 *                                      *
 *         Modified results display     *
 *           Andrew Miller-Smith        *
 *                                      *
 ****************************************/
		
		//print out our findings
        System.out.println("\n******************* Neural Network Algorithm Results *******************");
        System.out.println("The Enrichment Center reminds you that the Weighted Companion Cube cannot speak. In the event that the Weighted Companion Cube does speak, the Enrichment Center urges you to disregard its advice.");
        System.out.println("Name of training file: " + fileNameTraining);
        System.out.println("Name of testing file: " + fileNameTesting);

        //same for NUM_VARIABLES and number of clauses
        System.out.println("Number of training epochs: " + NUM_EPOCHS);
        
        //we know how many clauses we have satisfied
        System.out.println("Percent of testing files correct after training: " + percentCorrect*100);
        
        System.out.println("\nThis method took: " + duration/1000.0 + " seconds.");

	}

	public static void readFileTraining(File f) 
	{	
		Vector<inputNode> tempA = new Vector<inputNode>();
		Vector<outputNode> tempB = new Vector<outputNode>();
		boolean tempR = true; 
		int counter = 0;

		//testing
		int a = 0; 

		try{
			
			reader = new BufferedReader(new FileReader(f));
			String line;

			if (fileSize == 8){
				
				while ((line = reader.readLine()) != null){

					String tempL = line.trim();
					String[] splitStr = tempL.split(",");

					int[] temp = new int[splitStr.length];
					for (int i = 0; i < splitStr.length-1; i++){
						
						temp[i] = Integer.parseInt(splitStr[i]);
						inputNode tempI = new inputNode(temp[i], i); //unknown identifier
						tempA.add(tempI);
					}
					
					trainingInputVector.add(tempA);
					tempA = new Vector<inputNode>();

					if (method.equals("s")){
						
						outputNode tempC = new outputNode(Double.parseDouble(splitStr[64])/10.0, 0);
						tempB.add(tempC);
						trainingOutputVector.add(tempB);
						tempB = new Vector<outputNode>();
					}
					else if (method.equals("t")){
						
						double[] tempD = new double[10];
						
						//initialize to tempD to 0
						for (int j=0; j < tempD.length; j++){
							
							tempD[j] = 0;
						}
						
						int tempOut = Integer.parseInt(splitStr[64]);
						tempD[tempOut] = 1;
						
						for (int k=0; k<tempD.length; k++){
							
							outputNode tempC = new outputNode(tempD[k], k);
							tempB.add(tempC);		
						}
						
						trainingOutputVector.add(tempB);
						tempB = new Vector<outputNode>();
					}
				}
			}
			else if (fileSize == 32)
			{
				while ((line = reader.readLine()) != null) 
				{
					//skips the first 3 lines of file
					if (tempR == true){
						
						for (int j = 0; j<3; j++){
							
							line = reader.readLine();
						}
						
						tempR = false;
					}

					String tempL = line.trim();
					int[] temp = new int[tempL.length()];
					for (int i = 0; i < tempL.length(); i++){
						
						temp[i] = Character.getNumericValue(tempL.charAt(i));
						inputNode tempI = new inputNode(temp[i], i); //unknown identifier
						tempA.add(tempI);
					}

					counter++;

					if (counter == 32){
						
						trainingInputVector.add(tempA);	
						tempA = new Vector<inputNode>();

						line = reader.readLine();
						String tempM = line.trim();
						if (tempM.length() != 1){
							
							System.out.println("The file is not reading right.");
							System.exit(0);
						}

						if (method.equals("s")){
							
							outputNode tempO = new outputNode((double)Character.getNumericValue(tempM.charAt(0))/10.0, 0);
							tempB.add(tempO);
							trainingOutputVector.add(tempB);
							tempB = new Vector<outputNode>();
						}

						else if (method.equals("t")){
							
							double[] tempD = new double[10];
							
							//initialize to tempD to 0
							for (int j=0; j < tempD.length; j++){
								tempD[j] = 0;
							}
							
							int tempOut = Character.getNumericValue(tempM.charAt(0));
							tempD[tempOut] = 1;
							
							for (int k=0; k<tempD.length; k++){
								outputNode tempO = new outputNode(tempD[k], k);
								tempB.add(tempO);		
							}
							
							trainingOutputVector.add(tempB);
							tempB = new Vector<outputNode>();
						}
						
						counter = 0;
					}
				}
			}
			else{
				
				System.out.println("There was an error reading the file.");
				System.exit(0);
			}

			reader.close();
		} 
		
		catch (Exception e){
			
			System.err.format("Exception occurred trying to read '%s'.", f);
			e.printStackTrace();	
		}
	}

	//(training) reads file and stores the cities coordinates and identifier
	public static void readFileTesting(File f) 
	{	
		Vector<inputNode> tempA = new Vector<inputNode>();
		Vector<outputNode> tempB = new Vector<outputNode>();
		boolean tempR = true; 
		int counter = 0;

		//testing
		int a = 0; 

		try{
			
			reader = new BufferedReader(new FileReader(f));
			String line;
			
			if (fileSize == 8){
				
				while ((line = reader.readLine()) != null){

					String tempL = line.trim();
					String[] splitStr = tempL.split(",");
					int[] temp = new int[splitStr.length];
					for (int i = 0; i < splitStr.length-1; i++){
						
						temp[i] = Integer.parseInt(splitStr[i]);
						inputNode tempI = new inputNode(temp[i], i); //unknown identifier
						tempA.add(tempI);
					}
				
					testingInputVector.add(tempA);
					tempA = new Vector<inputNode>();
					if (method.equals("s")){
						
						outputNode tempC = new outputNode(Double.parseDouble(splitStr[64])/10.0, 0);
						tempB.add(tempC);
						testingOutputVector.add(tempB);
						tempB = new Vector<outputNode>();
					}
					else if (method.equals("t")){
						
						double[] tempD = new double[10];
						
						//initialize to tempD to 0
						for (int j=0; j < tempD.length; j++){
							
							tempD[j] = 0;
						}
						
						int tempOut = Integer.parseInt(splitStr[64]);
						tempD[tempOut] = 1;
						for (int k=0; k<tempD.length; k++){
							
							outputNode tempC = new outputNode(tempD[k], k);
							tempB.add(tempC);		
						}
						
						testingOutputVector.add(tempB);
						tempB = new Vector<outputNode>();
					}
				}
			}
			else if (fileSize == 32){
				
				while ((line = reader.readLine()) != null){
					
					//skips the first 3 lines of file
					if (tempR == true){
						
						for (int j = 0; j<3; j++){
							
							line = reader.readLine();
						}
						
						tempR = false;
					}

					String tempL = line.trim();
					int[] temp = new int[tempL.length()];
					for (int i = 0; i < tempL.length(); i++){
						
						temp[i] = Character.getNumericValue(tempL.charAt(i));
						inputNode tempI = new inputNode(temp[i], i); //unknown identifier
						tempA.add(tempI);
					}

					counter++;

					if (counter == 32){
						
						testingInputVector.add(tempA);	
						tempA = new Vector<inputNode>();

						line = reader.readLine();
						String tempM = line.trim();
						
						if (tempM.length() != 1){
							
							System.out.println("The file is not reading right.");
							System.exit(0);
						}
						if (method.equals("s")){
							
							outputNode tempO = new outputNode((double)Character.getNumericValue(tempM.charAt(0))/10.0, 0);
							tempB.add(tempO);
							testingOutputVector.add(tempB);
							tempB = new Vector<outputNode>();
						}
						else if (method.equals("t")){
							
							double[] tempD = new double[10];
							
							//initialize to tempD to 0
							for (int j=0; j < tempD.length; j++){
								
								tempD[j] = 0;
							}
							
							int tempOut = Character.getNumericValue(tempM.charAt(0));
							tempD[tempOut] = 1;
							
							for (int k=0; k<tempD.length; k++){
								
								outputNode tempO = new outputNode(tempD[k], k);
								tempB.add(tempO);		
							}
							
							testingOutputVector.add(tempB);
							tempB = new Vector<outputNode>();
						}
						
						counter = 0;
					}
				}
			}
			else{
				
				System.out.println("There was an error reading the file.");
				System.exit(0);
			}

			reader.close();
		} 
		catch (Exception e){
			
			System.err.format("Exception occurred trying to read '%s'.", f);
			e.printStackTrace();	
		}
	}

	public static void incrementNumberCorrect(){

		numberCorrect++;
	}	

}
