package myGANN;

import java.io.IOException;
import java.util.Arrays;

public class Main {

	public static void main(String[] args) throws IOException {
		String file = "datasets/iris.txt";
		String[] classNm = {"Virginica", "Setosa", "Versicolor"};
		String delim = ",";
		int classIndex = 4;	
		int outputs = 3;	
		int hidden = 15;	
		double lrate = .01;
		double maxer = .01; 		
		int maxIter = 20;	 
		int trainingPercentage = 60;	
		double mutRate = .8;
		int tournamentSize = 0;
		final double minWeight = -2.5;
		final double maxWeight = 2.5;
		int defaultPopulationSize = 60;
		int defaultChromosomeSize = 30;
		double lowestPossibleFitness = .983;
		int numEpochs = 500;
		double crossoverProb = .8;
		boolean elitism = false;
		int numberOfTests = 10;
		
		/* NeuralNetwork(String fileName, String[] classes, String delimeter, int inputs, int outputs, 
				int hiddenNeurons, double learningRate, double maxError, 
				int maxIterations, int trainingPercentage)*/
		/* GANN(String fileName,  String[] classNames, String delimeter, int classIndex, int outputs, int hiddenNeurons, double learningRate, 
				double maxError, int maxIterations, int trainingPercentage, double mutRate, int tournSize, int minWeights, int maxWeights,
				int defaultPopulationSize, int defaultChromosomeSize, double lowestPossibleFitness, int numEpochs, boolean elitism) */
		System.out.println("");
		long startTime;
		long endTime;
		long totalTime;
		long averageNNTime;
		long sumNNTime = 0;
		long sumGANNTime = 0;
		long averageGANNTime;
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

		for(int i= 0; i < numberOfTests; ++i)
		{
			System.out.println("");
			startTime = System.nanoTime();
			NeuralNetwork NN = new NeuralNetwork(file, classNm, delim, classIndex, outputs, hidden, lrate, maxer, 3000, trainingPercentage);
			NN.init();
			NN.train();
			NN.test();
			NN.evaluateTestingSet();
			NN.print();
			avgNNStats = NN.getAvgTestStats();
			NNSumAccuracy += avgNNStats[0];
			NNSumPrecision += avgNNStats[1];
			NNSumRecall += avgNNStats[2];
			NNSumfScore += avgNNStats[3];
			NNSumMserror += avgNNStats[4];
			NNSumCorrCoef += avgNNStats[5];
			
			endTime   = System.nanoTime();
			totalTime = endTime - startTime;
			sumNNTime += totalTime;
			System.out.println("\tNN Total Time: " + totalTime);
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
			startTime = System.nanoTime();
			GANN G = new GANN(file, classNm, delim, classIndex, outputs, hidden, lrate, maxer, maxIter, trainingPercentage, mutRate, tournamentSize,
					minWeight, maxWeight, defaultPopulationSize, defaultChromosomeSize, lowestPossibleFitness, numEpochs, crossoverProb, elitism);
			G.print();
			avgGANNStats = G.getAvgTestStats();
			GANNSumAccuracy += avgGANNStats[0];
			GANNSumPrecision += avgGANNStats[1];
			GANNSumRecall += avgGANNStats[2];
			GANNSumfScore += avgGANNStats[3];
			GANNSumMserror += avgGANNStats[4];
			GANNSumCorrCoef += avgGANNStats[5];
			endTime   = System.nanoTime();
			totalTime = endTime - startTime;
			sumGANNTime += totalTime;
			System.out.println("\tGANN Total Time: " + totalTime);
		}
		averageNNTime = (sumNNTime / numberOfTests);
		averageGANNTime = (sumGANNTime / numberOfTests);
		NNAvgAccuracy = (NNSumAccuracy / numberOfTests);
		NNAvgPrecision = (NNSumPrecision / numberOfTests);
		NNAvgRecall = (NNSumRecall / numberOfTests);
		NNAvgfScore = (NNSumfScore / numberOfTests);
		NNAvgMserror = (NNSumMserror / numberOfTests);
		NNAvgCorrCoef = (NNSumCorrCoef / numberOfTests);
		GANNAvgAccuracy = (GANNSumAccuracy / numberOfTests);
		GANNAvgPrecision = (GANNSumPrecision / numberOfTests);
		GANNAvgRecall = (GANNSumRecall / numberOfTests);
		GANNAvgfScore = (GANNSumfScore / numberOfTests);
		GANNAvgMserror = (GANNSumMserror / numberOfTests);
		GANNAvgCorrCoef = (GANNSumCorrCoef / numberOfTests);
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
		System.out.println("Average Stats: ");
		System.out.println("\tAverage Run TImes: ");
		System.out.println("\t\tAverage NN Run Time: " + averageNNTime);
		System.out.println("\t\tAverage GANN Run Time: " + averageGANNTime);
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
	}

}
