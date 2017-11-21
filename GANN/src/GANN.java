import java.io.IOException;

public class GANN {
	static NeuralNetwork nn;
	
    public static void main(String[] args) throws IOException {
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
    	    nn.init();
    	    nn.train();
    	    nn.evaluateTrainingSet();
    	    System.out.println("First Change");
    		GANN gann = new GANN();
    }
}
