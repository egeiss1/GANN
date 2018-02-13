package myGANN;

import java.io.IOException;
import java.util.Arrays;

public class GANN {
	static NeuralNetwork nn;
	static final int defaultPopulationSize = 10;
	static int defaultChromosomeSize = 30;
	static final double lowestPossibleFitness = .995;
	static final int numEpochs = 500;

	
	public static void runAlgorithm() {
		String file = "datasets/iris.txt";
		int input = 4;		
		int out = 3;		
		int hidden = 15;		
		double lrate = .01;
		double maxer = .01; 		
		int maxIter = 20;	 
		int trainingPerc = 60;		
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
		/*
		System.out.println("initialWeights: ");
		System.out.println(Arrays.toString(nn.getWeights()));
		System.out.println("initial Accuracy: " + nn.getTrainingAccuracy());
		*/

		GeneticAlgorithm g = new GeneticAlgorithm();

		double[] arr = g.evolve().getGenes();
		nn.setWeights(arr);
		nn.test();
		System.out.println("Evaluation on Testing");
		nn.evaluateTestingSet();
		
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
    public static void main(String[] args) throws IOException {
		runAlgorithm();

    }

}