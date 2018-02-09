package myGANN;

import java.io.IOException;

public class GANN {
	static NeuralNetwork nn;
	static final int defaultPopulationSize = 75;
	static int defaultChromosomeSize = 20;
	static final double lowestPossibleFitness = .98;
	static final int numEpochs = 500;

	
	public static void runAlgorithm() {
		String file = "datasets/iris.txt"; //args[0]
		int input = 4;		//Integer.parseInt(args[1]);
		int out = 3;		//Integer.parseInt(args[2]);
		int hidden = 15;		//Integer.parseInt(args[3]);
		double lrate = .01; 		// Double.parseDouble(args[4]);
		double maxer = .01; 			// Double.parseDouble(args[5]);
		int maxIter = 3000;		// Integer.parseInt(args[6]);
		int trainingPerc = 60;		// Integer.parseInt(args[7]);
		String delim = ",";
		String[] classNm = {"Virginica", "Setosa", "Versicolor"};
    
		nn = new NeuralNetwork(file, classNm, delim, input, out, 
				hidden, lrate, maxer, maxIter, trainingPerc);
	    try {
			nn.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	    //nn.train();
	    //nn.evaluateTrainingSet();
	    
		defaultChromosomeSize = nn.numWeights();
		System.out.println("NumWeights in NN: " + nn.numWeights());
		/*
		Chromosome c =  new Chromosome();
		System.out.println(c);
		System.out.println("Fitness in GANN: " + c.getFitness());
		System.out.println("Weights in GANN: " + Arrays.toString(nn.getWeights()));
		nn.setWeights(c.getGenes());
		System.out.println(c.getFitness());
		System.out.println("Accuracy in GANN after setting weights: " + nn.getTrainingAccuracy());
		System.out.println("Weights in GANN after setting weights: " + Arrays.toString(nn.getWeights()));
		System.out.println("Final Fitness in GANN: " + c.getFitness());
		System.out.println("Final Accuracy in GANN: " + nn.getTrainingAccuracy());
		*/
		//nn.test();
		//nn.evaluateTestingSet();
		
		GeneticAlgorithm g = new GeneticAlgorithm();
		//g.printPopulation();

		double[] arr = g.evolve().getGenes();
		nn.setWeights(arr);
		nn.test();
		nn.evaluateTestingSet();

	}
	
	
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
	
    public static void main(String[] args) throws IOException {
		runAlgorithm();
    }

}