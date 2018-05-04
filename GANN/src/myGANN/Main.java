/*
Main.java
Written by Eric Geiss
Copyright (c) 2018

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation 
files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, 
modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the 
Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR 
COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, 
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package myGANN;

import java.io.*;
import java.text.DecimalFormat;

import org.neuroph.adapters.jml.JMLDataSetConverter;
import org.neuroph.core.data.DataSet;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.filter.normalize.NormalizeMidrange;
import net.sf.javaml.tools.data.FileHandler;
import sun.audio.*;





public class Main {

	public static void main(String[] args) throws IOException {
		String file = "datasets/adultDataset.txt"; // file location
		String[] classNm = {"<=50K", ">50K"}; // class names
		String delim = ", "; // delimiter
		int inputs = 14;	// index of the inputs
		int outputs = 2;		// number of outputs
		int hidden = 2;		// number of hidden neurons
		int classIndex = 14; 
		String transferFunctionType = "sigmoid"; // sigmoid/step/tanh/linear
		double lrate = .34;	// learning rate for neural network
		double maxer = .01;	// maximum error for neural network		
		int maxIter = 67;	// max NN training iterations for each epoch of GANN
		int trainingPercentage = 60;	// training percentage of dataset
		double mutRate = .006;	 // mutation rate
		int tournamentSize = 0; // enter 0 for roulette, anything else for tournament selection
		double minWeight = -2.9;  // min random weight initialization for GANN
		double maxWeight = 2.9;	// max random weight initialization for GANN
		int defaultPopulationSize = 36;
		int defaultChromosomeSize = 1;
		double lowestPossibleFitness = .9; //.906; // GANN Stopping Criteria: minimum fitness to stop
		int numEpochs = 467;		// GANN Stopping Criteria: maximum number of epochs allowed for GANN
		double crossoverProb = .95; // probability crossover will occur each iteration
		int sortingAlgorithm = 0; // 0 for merge, 1 for bubble , 2 for quick (merge fastest/quick slowest)
		boolean elitism = true; // If true fittest chromosome is automatically selected from population each epoch
		boolean multiGeneMut = true; // Multiple Gene Mutation each w/ small prob or Single Gene Mutation w/ small prob
		int numberOfTests = 10;
		int NNMaxIter = 2750;	// set back to 3000

		System.out.println("");
		double startTime;
		double totalTime;
		double averageNNTime;
		double sumNNTime = 0;
		double sumGANNTime = 0;
		double averageGANNTime;
		double[] avgGANNStats = new double[6];
		double[] avgNNStats = new double[6];
		
		double NNSumAccuracy = 0.0;
		double NNSumPrecision = 0.0;
		double NNSumRecall = 0.0;
		double NNSumfScore = 0.0;
		double NNSumMserror = 0.0;
		double NNSumCorrCoef = 0.0;
		double GANNSumAccuracy = 0.0;
		double GANNSumPrecision = 0.0;
		double GANNSumRecall = 0.0;
		double GANNSumfScore = 0.0;
		double GANNSumMserror = 0.0;
		double GANNSumCorrCoef = 0.0;
		double GANNSumMaxFitnessReached = 0.0;
		
		double NNAvgAccuracy = 0.0;
		double NNAvgPrecision = 0.0;
		double NNAvgRecall = 0.0;
		double NNAvgfScore = 0.0;
		double NNAvgMserror = 0.0;
		double NNAvgCorrCoef = 0.0;
		double GANNAvgAccuracy = 0.0;
		double GANNAvgPrecision = 0.0;
		double GANNAvgRecall = 0.0;
		double GANNAvgfScore = 0.0;
		double GANNAvgMserror = 0.0;
		double GANNAvgCorrCoef = 0.0;
		double GANNAvgMaxFitnessReached = 0.0;
		
		double[] NNRunTimesArr = new double[numberOfTests];
		double[] NNAccuracyArr = new double[numberOfTests];
		double[] NNPrecisionArr = new double[numberOfTests];
		double[] NNRecallArr = new double[numberOfTests];
		double[] NNfScoreArr = new double[numberOfTests];
		double[] NNMserrorArr = new double[numberOfTests];
		double[] NNCorrCoefArr = new double[numberOfTests];
		
		double[] GANNRunTimesArr = new double[numberOfTests];
		double[] GANNAccuracyArr = new double[numberOfTests];
		double[] GANNPrecisionArr = new double[numberOfTests];
		double[] GANNRecallArr = new double[numberOfTests];
		double[] GANNfScoreArr = new double[numberOfTests];
		double[] GANNMserrorArr = new double[numberOfTests];
		double[] GANNCorrCoefArr = new double[numberOfTests];
		double[] GANNMaxFitnessReached = new double[numberOfTests];
		
		
		Dataset dset = FileHandler.loadDataset(new File(file), classIndex, delim);		
		NormalizeMidrange nrm = new NormalizeMidrange(0,1);
		nrm.build(dset);
		nrm.filter(dset);
		DataSet neurophDataSet = JMLDataSetConverter.convertJMLToNeurophDataset(dset, inputs, outputs);
		
		//neurophDataSet.shuffle();
		
		DataSet[] trainingAndTestSet = neurophDataSet.createTrainingAndTestSubsets(trainingPercentage, 100-trainingPercentage);
		DataSet trainingSet = trainingAndTestSet[0];
		DataSet testSet = trainingAndTestSet[1];
		

		for(int i= 0; i < numberOfTests; ++i)
		{
			System.out.println("");
			
			startTime = System.nanoTime();
			NeuralNetwork NN = new NeuralNetwork(trainingSet, testSet, classNm, inputs, outputs, 
					hidden, transferFunctionType, lrate, maxer, NNMaxIter);
			NN.init();
			System.out.println("NN " + i + ": ");
			NN.train();
			NN.test();
			NN.evaluateTestingSet();
			NN.print();
			
			avgNNStats = NN.getAvgTestStats();
			
			// store NN test round stats for each round in array
			NNAccuracyArr[i] = avgNNStats[0];
			NNPrecisionArr[i] = avgNNStats[1];
			NNRecallArr[i] = avgNNStats[2];
			NNfScoreArr[i] = avgNNStats[3];
			NNMserrorArr[i] = avgNNStats[4];
			NNCorrCoefArr[i] = avgNNStats[5];
			
			// calculate sum of NN test stats for all rounds
			NNSumAccuracy += avgNNStats[0];
			NNSumPrecision += avgNNStats[1];
			NNSumRecall += avgNNStats[2];
			NNSumfScore += avgNNStats[3];
			NNSumMserror += avgNNStats[4];
			NNSumCorrCoef += avgNNStats[5];
			
			// get run time for NN for each round and store in array
			totalTime = System.nanoTime() - startTime;
			NNRunTimesArr[i] = totalTime;
			
			// get sum of NN run times for all rounds
			sumNNTime += totalTime;
			System.out.println("\tNN Total Time: " + totalTime);
			
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			
			System.out.println("\nGANN " + i + ": ");
			startTime = System.nanoTime();

			GANN G = new GANN(trainingSet, testSet, classNm, inputs, outputs, hidden, transferFunctionType, lrate, maxer, maxIter, mutRate, tournamentSize,
					minWeight, maxWeight, defaultPopulationSize, defaultChromosomeSize, lowestPossibleFitness, numEpochs, crossoverProb, sortingAlgorithm, elitism, multiGeneMut);
			avgGANNStats = G.getAvgTestStats();
			double fit = G.getMaxFitnessReached();
			double mse = avgGANNStats[4];

			System.out.println("Max Fitness Reached: " + fit);
			System.out.println("MSError: " + mse);
			G.print();
			
			// store GANN test round stats for each round in array
			GANNAccuracyArr[i] = avgGANNStats[0];
			GANNPrecisionArr[i] = avgGANNStats[1];
			GANNRecallArr[i] = avgGANNStats[2];
			GANNfScoreArr[i] = avgGANNStats[3];
			GANNMserrorArr[i] = mse;
			GANNCorrCoefArr[i] = avgGANNStats[5];
			GANNMaxFitnessReached[i] = fit;
			
			// calculate sum of GANN test stats for all rounds
			GANNSumAccuracy += avgGANNStats[0];
			GANNSumPrecision += avgGANNStats[1];
			GANNSumRecall += avgGANNStats[2];
			GANNSumfScore += avgGANNStats[3];
			GANNSumMserror += avgGANNStats[4];
			GANNSumCorrCoef += avgGANNStats[5];
			GANNSumMaxFitnessReached += fit;
			
			// get run time for GANN for each round and store in array
			totalTime = System.nanoTime() - startTime;
			GANNRunTimesArr[i] = totalTime;
			
			// get sum of GANN run times for all rounds
			sumGANNTime += totalTime;
			System.out.println("\tGANN Total Time: " + new DecimalFormat("#.##########").format(totalTime/1000000000));
			
		}
				
		// Calculate average NN stats for all rounds
		averageNNTime = (sumNNTime / numberOfTests);
		averageGANNTime = (sumGANNTime / numberOfTests);
		
		NNAvgAccuracy = (NNSumAccuracy / numberOfTests);
		NNAvgPrecision = (NNSumPrecision / numberOfTests);
		NNAvgRecall = (NNSumRecall / numberOfTests);
		NNAvgfScore = (NNSumfScore / numberOfTests);
		NNAvgMserror = (NNSumMserror / numberOfTests);
		NNAvgCorrCoef = (NNSumCorrCoef / numberOfTests);
		
		// Calculate average GANN stats for all rounds
		GANNAvgAccuracy = (GANNSumAccuracy / numberOfTests);
		GANNAvgPrecision = (GANNSumPrecision / numberOfTests);
		GANNAvgRecall = (GANNSumRecall / numberOfTests);
		GANNAvgfScore = (GANNSumfScore / numberOfTests);
		GANNAvgMserror = (GANNSumMserror / numberOfTests);
		GANNAvgCorrCoef = (GANNSumCorrCoef / numberOfTests);
		GANNAvgMaxFitnessReached = (GANNSumMaxFitnessReached / numberOfTests);
		
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		
		System.out.println("\nRound Stats: ");
		
		System.out.println("NN Run Times (s)\tGANN Run Times (s)");
		for(int i = 0 ; i < numberOfTests; ++i) {
			System.out.println(new DecimalFormat("#.##########").format(NNRunTimesArr[i]/1000000000) + "\t" 
					+ new DecimalFormat("#.##########").format(GANNRunTimesArr[i]/1000000000) );
		}
		
		System.out.println("NN Accuracy \tGANN Accuracy");
		for(int i = 0 ; i < numberOfTests; ++i)
			System.out.println(NNAccuracyArr[i] + "\t" + GANNAccuracyArr[i]);
		
		System.out.println("NN Precision \tGANN Precision");
		for(int i = 0 ; i < numberOfTests; ++i)
			System.out.println(NNPrecisionArr[i] + "\t" + GANNPrecisionArr[i]);
		
		System.out.println("NN Recall \tGANN Recall");
		for(int i = 0 ; i < numberOfTests; ++i)
			System.out.println(NNRecallArr[i] + "\t" + GANNRecallArr[i]);
		
		System.out.println("NN fScore \tGANN fScore");
		for(int i = 0 ; i < numberOfTests; ++i)
			System.out.println(NNfScoreArr[i] + "\t" + GANNfScoreArr[i]);
		
		System.out.println("NN MSError \tGANN MSError");
		for(int i = 0 ; i < numberOfTests; ++i)
			System.out.println(NNMserrorArr[i] + "\t" + GANNMserrorArr[i]);
		
		System.out.println("NN Correlation Coefficient \tGANN Correlation Coefficient");
		for(int i = 0 ; i < numberOfTests; ++i)
			System.out.println(NNCorrCoefArr[i] + "\t" + GANNCorrCoefArr[i]);
		System.out.println("\t\tGANN Max Fitness Reached");
		for(int i = 0 ; i < numberOfTests; ++i)
			System.out.println("\t\t" + GANNMaxFitnessReached[i]);
		
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		
		System.out.println("\nFile Info: ");
		System.out.println("\tFilename: " + file);
		System.out.println("\tTraining Percentage: " + trainingPercentage + "%");
		System.out.println("\tTesting Percentage: " + (100 - trainingPercentage) + "%");
		
		
		System.out.println("\nAverage Stats: ");
		System.out.println("\tAverage Run TImes: ");
		System.out.println("\t\tAverage NN Run Time: " + new DecimalFormat("#.##########").format(averageNNTime/1000000000) + " seconds");
		System.out.println("\t\tAverage GANN Run Time: " + new DecimalFormat("#.##########").format(averageGANNTime/1000000000) + " seconds");
		System.out.println("\t\tAverage NN Accuracy: " + NNAvgAccuracy);
		System.out.println("\t\tAverage GANN Accuracy: " + GANNAvgAccuracy);
		System.out.println("\t\tAverage NN Precision: " + NNAvgPrecision);
		System.out.println("\t\tAverage GANN Precision: " + GANNAvgPrecision);
		System.out.println("\t\tAverage NN Recall: " + NNAvgRecall);
		System.out.println("\t\tAverage GANN Recall: " + GANNAvgRecall);	
		System.out.println("\t\tAverage NN FScore: " + NNAvgfScore);
		System.out.println("\t\tAverage GANN FScore: " + GANNAvgfScore);
		System.out.println("\t\tAverage NN MSError: " + NNAvgMserror);
		System.out.println("\t\tAverage GANN MSError: " + GANNAvgMserror);
		System.out.println("\t\tAverage NN CorrCoef: " + NNAvgCorrCoef);
		System.out.println("\t\tAverage GANN CorrCoef: " + GANNAvgCorrCoef);
		System.out.println("\t\tAverage GANN MaxFitnessReached: " + GANNAvgMaxFitnessReached);	
		
		//Play alarm
	    String alarm = "/Users/EricGeiss/Downloads/alarm.wav";
	    InputStream in = new FileInputStream(alarm);
	    // create an audiostream from the inputstream
	    AudioStream audioStream = new AudioStream(in);
	    // play the audio clip with the audioplayer class
	    AudioPlayer.player.start(audioStream);
	}
}

		