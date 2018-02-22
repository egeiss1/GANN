package myGANN;

import java.io.IOException;
import java.util.Arrays;

public class GANN {
	static NeuralNetwork nn;
	private String file;
	private int classIndex;
	private int outputs;
	private int hidden;
	private double lrate;
	private double maxer;
	private int maxIter;
	private int trainingPercentage;
	private String delim;
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
	private double[] avgTestStats;

	public GANN(String fileName,  String[] classNames, String delimeter, int classIndex, int outputs, int hiddenNeurons, double learningRate, 
			double maxError, int maxIterations, int trainingPercentage, double mutRate, int tournSize, double minWeights, double maxWeights,
			int defaultPopSize, int defaultChromSize, double lowestPossFitness, int numEpochs, double crossoverProb, int sortingAlg, boolean elitism) 
	{
		this.file = fileName;
		this.classNm = classNames;
		this.delim = delimeter;
		this.classIndex = classIndex;		
		this.outputs = outputs;	
		this.hidden = hiddenNeurons;	
		this.lrate = learningRate;
		this.maxer = maxError; 		
		this.maxIter = maxIterations; 
		this.trainingPercentage = trainingPercentage;		
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
		this.avgTestStats = new double[6];

		nn = new NeuralNetwork(file, classNm, delim, this.classIndex, this.outputs, 
				hidden, lrate, maxer, maxIter, this.trainingPercentage);
	    try {
			nn.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    //nn.train();
	    //nn.evaluateTrainingSet();
		defaultChromosomeSize = nn.numWeights();
		/*
		System.out.println("initialWeights: ");
		System.out.println(Arrays.toString(nn.getWeights()));
		System.out.println("initial Accuracy: " + nn.getTrainingAccuracy());
		*/
		

		Population P = new Population();
		GeneticAlgorithm g = new GeneticAlgorithm(P, mutationRate, tournamentSize, this.elitism);
		double[] arr = g.evolve().getGenes();
		nn.setWeights(arr);
		nn.test();
		System.out.println("Evaluation on Testing");
		nn.evaluateTestingSet();
		this.avgTestStats = nn.getAvgTestStats();

	}
	
	public double[] getAvgTestStats() {
		return this.avgTestStats;
	}
	
	public void print()
	{
		System.out.println("GANN INFO:");
		System.out.println("\tFile: " + this.file);
		System.out.println("\tClass Index: " + this.classIndex);
		System.out.println("\tNumber Outputs: " + this.outputs);
		System.out.println("\tNumber Hidden Neurons: " + this.hidden);
		System.out.println("\tLearning Rate: " + this.lrate);
		System.out.println("\tMax Error: " + this.maxer);
		System.out.println("\tMax Iterations (NN): " + this.maxIter);
		System.out.println("\tTraining Percentage: " + this.trainingPercentage + "%");
		System.out.println("\tTesting Percentage: " + (100 - this.trainingPercentage) + "%");
		System.out.println("\tMutation Rate: " + mutationRate);
		if(tournamentSize > 0) 
		{
			System.out.println("\tSelection Algorithm: Tournament");
			System.out.println("\tTournament Size: " +tournamentSize);
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
		System.out.println("\tElitism: " + elitism);
	}

	
	/*
	public NeuralNetwork getNn() {
		return nn;
	}

	public void setNn(NeuralNetwork nnIn) {
		nn = nnIn;
	}
	
	public int getDefaultChromosomeSize() {
		return defaultChromosomeSize;
	}


	public void setDefaultChromosomeSize(int def) {
		defaultChromosomeSize = def;
	}
	*/
}