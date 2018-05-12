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


public class Main {

	public static void main(String[] args) throws IOException {
		String file = "datasets/iris.txt"; // file location
		String[] classNm = {"Virginica", "Setosa", "Versicolor"}; // class names
		String delim = ","; // delimiter
		int inputs = 4;	// inputs
		int outputs = 3;		// number of outputs
		int hidden = 15;		// number of hidden neurons
		int classIndex = 4; // classIndex
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
		double lowestPossibleFitness = .99; //.906; // GANN Stopping Criteria: minimum fitness to stop
		int numEpochs = 467;		// GANN Stopping Criteria: maximum number of epochs allowed for GANN
		double crossoverProb = .95; // probability crossover will occur each iteration
		int sortingAlgorithm = 0; // 0 for merge, 1 for bubble , 2 for quick (merge fastest/quick slowest)
		boolean elitism = true; // If true fittest chromosome is automatically selected from population each epoch
		boolean multiGeneMut = true; // Multiple Gene Mutation each w/ small prob or Single Gene Mutation w/ small prob

		System.out.println("");
		double startTime;
		double totalTime;
		
		Dataset dset = FileHandler.loadDataset(new File(file), classIndex, delim);		
		NormalizeMidrange nrm = new NormalizeMidrange(0,1);
		nrm.build(dset);
		nrm.filter(dset);
		DataSet neurophDataSet = JMLDataSetConverter.convertJMLToNeurophDataset(dset, inputs, outputs);
		
		//neurophDataSet.shuffle();
		
		DataSet[] trainingAndTestSet = neurophDataSet.createTrainingAndTestSubsets(trainingPercentage, 100-trainingPercentage);
		DataSet trainingSet = trainingAndTestSet[0];
		DataSet testSet = trainingAndTestSet[1];

		startTime = System.nanoTime();

		GANN G = new GANN(trainingSet, testSet, classNm, inputs, outputs, hidden, transferFunctionType, lrate, maxer, maxIter, mutRate, tournamentSize,
					minWeight, maxWeight, defaultPopulationSize, defaultChromosomeSize, lowestPossibleFitness, numEpochs, crossoverProb, sortingAlgorithm, elitism, multiGeneMut);
		double[] avgGANNStats = G.getAvgTestStats();
		double fit = G.getMaxFitnessReached();
		double mse = avgGANNStats[4];

		G.print();
			
		double GANNAccuracy = avgGANNStats[0];
		double GANNPrecision = avgGANNStats[1];
		double GANNRecall = avgGANNStats[2];
		double GANNfScore = avgGANNStats[3];
		double GANNMserror = mse;
		double GANNCorrCoef= avgGANNStats[5];

		// get run time for GANN 
		totalTime = System.nanoTime() - startTime;

		System.out.println("\nFile Info: ");
		System.out.println("\tFilename: " + file);
		System.out.println("\tTraining Percentage: " + trainingPercentage + "%");
		System.out.println("\tTesting Percentage: " + (100 - trainingPercentage) + "%");
		
		System.out.println("\nStats: ");
		System.out.println("\t\tRun Time: " + new DecimalFormat("#.##########").format(totalTime/1000000000) + " seconds");
		System.out.println("\t\tAccuracy: " + GANNAccuracy);
		System.out.println("\t\tPrecision: " + GANNPrecision);
		System.out.println("\t\tRecall: " + GANNRecall);	
		System.out.println("\t\tFScore: " + GANNfScore);
		System.out.println("\t\tMSError: " + GANNMserror);
		System.out.println("\t\tCorrCoef: " + GANNCorrCoef);
		System.out.println("\t\tMaxFitnessReached: " + fit);	

	}
}

		