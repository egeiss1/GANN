package myGANN;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;

import org.neuroph.adapters.jml.JMLDataSetConverter;
import org.neuroph.core.Layer;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.eval.ClassifierEvaluator;
import org.neuroph.eval.ErrorEvaluator;
import org.neuroph.eval.Evaluation;
import org.neuroph.eval.classification.ClassificationMetrics;
import org.neuroph.eval.classification.ConfusionMatrix;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

import net.sf.javaml.core.Dataset;
import net.sf.javaml.filter.normalize.NormalizeMidrange;
import net.sf.javaml.tools.data.FileHandler;

public class NeuralNetwork 
{
	private String inputFileName;
	private int classIndex;
	private int numNetworkOutputs;
	private int numHiddenNeurons;
	private double learningRate;
	private double maxError;
	private int maxIterations;
	private int trainingPercentage;
	private int testingPercentage;
	private String delimeter;
	private MultiLayerPerceptron neuralNet;
	private DataSet trainingSet;
	private DataSet testSet;
	private String[] classNames;
    Evaluation evaluation = new Evaluation();

	public NeuralNetwork(String fileName, String[] classes, String delimeter, int classindex, int outputs, 
			int hiddenNeurons, double learningRate, double maxError, 
			int maxIterations, int trainingPercentage)
	{
		this.inputFileName = fileName;
		this.delimeter = delimeter;
		this.classIndex = classindex;
		this.numNetworkOutputs = outputs;
		this.numHiddenNeurons = hiddenNeurons;
		this.learningRate = learningRate;
		this.maxError = maxError;
		this.maxIterations = maxIterations;
		this.trainingPercentage = trainingPercentage;
		this.testingPercentage = (100 - this.trainingPercentage);
		this.classNames = classes;
		this.trainingSet = null;
		this.testSet = null;
	}
	
	public void init() throws IOException
	{ 
		this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 
				this.classIndex, this.numHiddenNeurons, this.numNetworkOutputs);
		Dataset dset = FileHandler.loadDataset(new File(this.inputFileName), this.classIndex, this.delimeter);		
		NormalizeMidrange nrm = new NormalizeMidrange(0,1);
		nrm.build(dset);
		nrm.filter(dset);
		DataSet neurophDataSet = JMLDataSetConverter.convertJMLToNeurophDataset(dset, this.classIndex, this.numNetworkOutputs);
		//neurophDataSet.shuffle();
        	
		DataSet[] trainingAndTestSet = neurophDataSet.createTrainingAndTestSubsets(this.trainingPercentage, this.testingPercentage);
		this.trainingSet = trainingAndTestSet[0];
		this.testSet = trainingAndTestSet[1];
		BackPropagation b = (BackPropagation) this.neuralNet.getLearningRule();
		//b.addListener(new LearningListener());
		b.setLearningRate(this.learningRate);	
		b.setMaxError(this.maxError);	
		b.setMaxIterations(this.maxIterations); 
		this.neuralNet.getLearningRule().setErrorFunction(new MeanSquaredError());
		
        evaluation.addEvaluator(new ErrorEvaluator(new MeanSquaredError()));
	    evaluation.addEvaluator(new ClassifierEvaluator.MultiClass(this.classNames));
	}
	
	public void train()
	{
        this.neuralNet.learn(this.trainingSet);
        //System.out.println("Done training."); 
	}
	
	public void test()
	{
		int i = 1;
        for(DataSetRow dataRow : this.testSet.getRows()) 
        {
            this.neuralNet.setInput(dataRow.getInput());
            this.neuralNet.calculate();
            double[] networkOutput = this.neuralNet.getOutput();
            double[] desOutput = dataRow.getDesiredOutput();

            System.out.print("Input " + i + ": " + Arrays.toString(dataRow.getInput()));
            System.out.print(" Expected Output: ");
            for(int k = 0; k < desOutput.length; ++k)
            {
            		System.out.print(desOutput[k] + ", ");
            }
            
            System.out.print(" Actual Output: ");
            for(int j = 0; j < networkOutput.length; ++j)
            {
            		System.out.print(round(networkOutput[j]) + ", ");
            }
            
            System.out.print("\n");
            i++;
       }
    }
	
	public void evaluateTrainingSet()
	{
        evaluation.evaluateDataSet(this.neuralNet, this.trainingSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        System.out.println("Confusion matrrix:\n");
        System.out.println(confusionMatrix.toString() + "\n\n");
        System.out.println("Classification metrics: \n");
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        for (ClassificationMetrics cm : metrics) {
            System.out.println(cm.toString() + "\n");
        }
        System.out.println(average.toString());     
	}
	
	public void evaluateTestingSet()
	{
        evaluation.evaluateDataSet(this.neuralNet, this.testSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        System.out.println("Confusion matrrix:\n");
        System.out.println(confusionMatrix.toString() + "\n\n");
        System.out.println("Classification metrics: \n");
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        for (ClassificationMetrics cm : metrics) {
            System.out.println(cm.toString() + "\n");
        }
        System.out.println(average.toString());     
	}
	
	public double[] getAvgTestStats()
	{
		double[] averageStats = new double[6];
        evaluation.evaluateDataSet(this.neuralNet, this.testSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        averageStats[0] = average.accuracy;
        averageStats[1] = average.precision;
        averageStats[2] = average.recall;        
        averageStats[3] = average.fScore;        
        averageStats[4] = average.mserror;
        averageStats[5] = average.correlationCoefficient;
        return averageStats;
 
	}
	
	public double getTrainingAccuracy()
	{
        evaluation.evaluateDataSet(this.neuralNet, this.trainingSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        return average.accuracy;     
	}
	
	public double getTestingAccuracy()
	{
        evaluation.evaluateDataSet(this.neuralNet, this.testSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        return average.accuracy;     
	}
	
	public double getTrainingPrecision()
	{
	        evaluation.evaluateDataSet(this.neuralNet, this.trainingSet);
	        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
	        ConfusionMatrix confusionMatrix = evaluator.getResult();
	        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
	        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
	        return average.precision;     
	}
	
	public double getTestingPrecision()
	{	    
	        evaluation.evaluateDataSet(this.neuralNet, this.testSet);
	        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
	        ConfusionMatrix confusionMatrix = evaluator.getResult();
	        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
	        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
	        return average.precision;     
	}
	
	public double getTrainingMeanSquareError()
	{
        evaluation.evaluateDataSet(this.neuralNet, this.trainingSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        return average.mserror;     
	}
	
	public double getTestMeanSquareError()
	{
        evaluation.evaluateDataSet(this.neuralNet, this.testSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        return average.mserror;     
	}
        
    public void printClassifier() 
    {
    		int i=1;
    		System.out.println("Ouput Weights: ");
        for (Double weight : this.neuralNet.getWeights()) {
            System.out.println("Weight" + i + ": " + weight);
            i++;
        }
        System.out.println();
    }
    
    public Double[] getWeights(){
    		return this.neuralNet.getWeights();
    }
    
    public int numWeights() {
    		return this.neuralNet.getWeights().length;
    }
        
    public void setWeights(double[] weightsIn)
    {
    		if(weightsIn.length == numWeights())
    		{
    			this.neuralNet.setWeights(weightsIn);
    		}
    		else
    		{
    			System.out.println("Error: Cannot set weights - array sizes do not match");
    		}
    }

    public static double round(double inputNumber)
    {
    		return (Math.round(inputNumber * 100.0) / 100.0); 
    }
    
	public void print() 
	{
		System.out.println("NeuralNet Info:");
		System.out.println("\tFile: " + this.inputFileName);
		System.out.println("\tDelimeter: " + this.delimeter);
		System.out.println("\tNumber of Classes: " + this.classIndex);
		System.out.println("\tNumber of Outputs: " + this.numNetworkOutputs);
		System.out.println("\tNumber Hidden Neurons: " + this.numHiddenNeurons);
		System.out.println("\tLearning Rate: " + this.learningRate);
		System.out.println("\tMax Error: " + this.maxError);
		System.out.println("\tMax Iterations: " + this.maxIterations);
		System.out.println("\tTraining Percentage: " + this.trainingPercentage);
		System.out.println("\tTesting Percentage: " + (100 - this.trainingPercentage));
		System.out.println("\tClass Names: " + Arrays.toString(this.classNames));
	}
    public static class LearningListener implements LearningEventListener 
    {
        @Override
        public void handleLearningEvent(LearningEvent event) 
        {
            BackPropagation bp = (BackPropagation) event.getSource();
            if (event.getEventType().equals(LearningEvent.Type.LEARNING_STOPPED)) 
            {
                double error = bp.getTotalNetworkError();
                System.out.println("Training completed in " + bp.getCurrentIteration() + " iterations, ");
                System.out.println("With total error: " + round(error));
            } 
            else 
            {
                System.out.println("Iteration: " + bp.getCurrentIteration() + " | Network error: " + bp.getTotalNetworkError());
            }
        }
    }
    
    
}
