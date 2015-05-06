# Neural-Networks

A project I led to design and test the ability of Neural Networks to teach a machine to understand handwritten digits. I designed and wrote all classes, objects, and algorithms. Other team members implemented the file reading and command line reading code.

In this project we examined two different ways of representing the handwritten digits (input). The first set of files contained 32x32 bitmaps of hand-drawn integers. For example, a four would appear as follows: 

0000000000000000111000000000000000000000000000001110000000000000000000000000000011100000000000000000000000000001110000000000000000000000000000011100000000000000000000000000001111000000000000000000000000000011110000000000000000000000000000111100000000000000000000000000011111000000000000000000000000000111110000000000000000000000000001111000000000000000000000000001111110000011110000000000000000011111000000111100000000000000000111110000001111000000000000000011111100000011110000000000000001111110000001111100000000000000111111100000111111000000000000011111110000001111110000000000001111111100000011111000000000000011111111100000111110000000000000111111111100011111100000000000000111111111111111110000000000000001111111111111111100000000000000000111111111111111000000000000000000000011111111100000000000000000000000011111111000000000000000000000000001111100000000000000000000000000111111000000000000000000000000001111110000000000000000000000000011111100000000000000000000000001111100000000000000000000000000011111000000000000

The second type of input consisted of an 8x8 matrix. To construct these, we partitioned each bitmap into sixty-four 4x4 blocks, each with sixteen bits. The corresponding matrix values were the number of 1s in each section of the partition. Think of it as a lower-resolution version of the bitmaps.

The network contains input nodes, output nodes, and paths between them. Each path has a weight, which alters the signal sent from the input node. We tested two types of output representations: a single output node whose target value matched the hand-drawn digit and a collection of ten output nodes where the index of each node represented a possible digit 0 - 9. For the single node, it's value at the end of the algorithm represented the algorithm's guess regarding the digit. For the ten-node representation, the index of the node with the greatest value served as the guess.

In addition to these input and output representations, we tested different learning rates. These values affect how much the network takes each training session into account when adjusting the path weights. A high value may lead to overcompensation and premature convergence, but a low value may prevent the network from gathering important information.

The algorithm trains the network on a set of training files then tests its ability on a set of testing problems. Training occurs repeatedly for fifty epochs before testing begins.The final version of this project contains a script for running multiple hours worth of tests. I have reworked the final code and included an earlier version that allows the user to run the algorithms on the command line. The arguments for running my Network algorithm are as follows:

Training file name      (optdigits32x32.tra or optdigits8x8int.tra)

Testing file name       (optdigits32x32.tes or optdigits8x8int.tes)

Input type 	            (32 if using 32x32 bitmaps or 8 if using 8x8 matrices)

Output representation   (s for a single outputNode or t for ten outputNodes)

Learning rate 			(The amount the network takes each training session into account. For testing, we used 0.9, 0.7, 0.5, 0.3, 0.1, and 0.01)
						
An example of a command line call. Note that both files must be 32x32 or 8x8:

ava Neural optdigits8x8int.tra optdigits8x8int.tes 8 t 0.01

Here is a brief description of my Network algorithm:

"The Network class contains the basic neural network and machine-learning algorithm. The train and test functions take vectors of inputNodes, outputNodes, a 2D array of paths between them, and the learning rate. InputNodes send weighted signals to the outputNodes, which use this to calculate their values. Path weights are then adjusted based on how close those values are to the outputNodes' specified targets. The train function returns the updated paths array, while the test function returns a boolean value of whether the result was the same as the target."

Play around with the output representations and learning rate and see what happens to the network's accuracy!

- Andrew
						