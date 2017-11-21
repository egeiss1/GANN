package myGANN;

import java.io.IOException;

public class GANN {
	static NeuralNetwork nn;
	static int defaultPopulationSize = 100;
	static int defaultChromosomeSize;
	
	public void runAlgorithm() {
		String file = "/Users/EricGeiss/Desktop/iris2.txt"; //args[0]
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
	    nn.train();
	    nn.evaluateTrainingSet();
	    defaultPopulationSize = 100;
		defaultChromosomeSize = nn.numWeights();
		
	}
	
    public static void main(String[] args) throws IOException {
    		GANN gann = new GANN();
    		gann.runAlgorithm();
    }
}