/*
GANN.java
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

import java.io.IOException;
import org.neuroph.core.data.DataSet;

public class GANN {
	static NeuralNetwork nn;
	private GeneticAlgorithm ga;
	private int inputs;
	private int outputs;
	private int hidden;
	private String transFuncType;
	private double lrate;
	private double maxer;
	private int maxIter;
	private DataSet trainingSet;
	private DataSet testSet;
	private String[] classNm;
	static double mutationRate;
	static int tournamentSize;
	static double minWeight;
	static double maxWeight;
	static int defaultPopulationSize;
	static int defaultChromosomeSize;
	static double lowestPossibleFitness;
	static int numberOfEpochs;
	static double crossoverPr;
	static int sortingAlgorithm;
	private boolean elitism;
	private boolean multiGeneMutate;
	private double[] avgTestStats;
	private double maxFitnessReached;

	public GANN(DataSet training, DataSet testing, String[] classNames, int inputs, int outputs, int hiddenNeurons, String transferFuncType, double learningRate, 
			double maxError, int maxIterations, double mutRate, int tournSize, double minWeights, double maxWeights,
			int defaultPopSize, int defaultChromSize, double lowestPossFitness, int numEpochs, double crossoverProb, int sortingAlg, boolean elitism, boolean multiGeneMut) 
	{
		this.trainingSet = training;
		this.testSet = testing;
		this.classNm = classNames;
		this.inputs = inputs;		
		this.outputs = outputs;	
		this.hidden = hiddenNeurons;
		this.transFuncType = transferFuncType;
		this.lrate = learningRate;
		this.maxer = maxError; 		
		this.maxIter = maxIterations; 
		mutationRate = mutRate;
		tournamentSize = tournSize;
		minWeight = minWeights;
		maxWeight = maxWeights;
		defaultPopulationSize = defaultPopSize;
		defaultChromosomeSize = defaultChromSize;
		lowestPossibleFitness = lowestPossFitness;
		numberOfEpochs = numEpochs;
		crossoverPr = crossoverProb;
		sortingAlgorithm = sortingAlg;
		this.elitism = elitism;
		this.multiGeneMutate = multiGeneMut;
		this.avgTestStats = new double[6];

		nn = new NeuralNetwork(this.trainingSet, this.testSet, classNm, this.inputs, this.outputs, 
				hidden, transFuncType, lrate, maxer, maxIter);
	    try {
			nn.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
		defaultChromosomeSize = nn.numWeights();

		Population P = new Population();
		ga = new GeneticAlgorithm(P, mutationRate, tournamentSize, this.elitism, this.multiGeneMutate);
		double[] arr = ga.evolve().getGenes();
		this.maxFitnessReached = ga.getMaxFitnessReached();
		nn.setWeights(arr);
		nn.test();
		System.out.println("Evaluation on Testing");
		nn.evaluateTestingSet();
		this.avgTestStats = nn.getAvgTestStats();

	}
	
	public double[] getAvgTestStats() {
		return this.avgTestStats;
	}
	
	public double getMaxFitnessReached() {
		return this.maxFitnessReached;
	}
	
	public void print()
	{
		System.out.println("GANN INFO:");
		System.out.println("\tNumber Inputs: " + this.inputs);
		System.out.println("\tNumber Outputs: " + this.outputs);
		System.out.println("\tNumber Hidden Neurons: " + this.hidden);
		System.out.println("\tTransfer Function Type: " + this.transFuncType);
		System.out.println("\tLearning Rate: " + this.lrate);
		System.out.println("\tMax Error: " + this.maxer);
		System.out.println("\tMax Iterations (for NN): " + this.maxIter);
		System.out.println("\tMutation Rate: " + mutationRate);
		if(tournamentSize > 0) 
		{
			System.out.println("\tSelection Algorithm: Tournament");
			System.out.println("\tTournament Size: " + tournamentSize);
		}
		else
		{
			System.out.println("\tSelection Algorithm: Roulette");
		}
		System.out.println("\tNN Weight Generation Range: [" + minWeight + ", " + maxWeight + "]" );
		System.out.println("\tPopulation Size: " + defaultPopulationSize);
		System.out.println("\tChromosome Size: " + defaultChromosomeSize);
		System.out.println("\tStopping Fitness: " + lowestPossibleFitness);
		System.out.println("\tMax Number Epochs (GANN): " + numberOfEpochs);
		System.out.println("\tCrossover Probability: " + crossoverPr);
		if(sortingAlgorithm == 0)
			System.out.println("\tSorting Algorithm: Merge Sort");
		else if(sortingAlgorithm == 1)
			System.out.println("\tSorting Algorithm: Bubble Sort");
		else
			System.out.println("\tSorting Algorithm: Quick Sort");
		System.out.println("\tElitism: " + elitism);
	}
}