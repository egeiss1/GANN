/*
NeuralNetwork.java
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
import java.util.Arrays;
import org.neuroph.core.data.DataSet;
import org.neuroph.core.data.DataSetRow;
import org.neuroph.core.events.LearningEvent;
import org.neuroph.core.events.LearningEventListener;
import org.neuroph.core.learning.error.MeanSquaredError;
import org.neuroph.eval.ClassifierEvaluator;
import org.neuroph.eval.ErrorEvaluator;
import org.neuroph.eval.Evaluation;
import org.neuroph.eval.EvaluationResult;
import org.neuroph.eval.classification.ClassificationMetrics;
import org.neuroph.eval.classification.ConfusionMatrix;
import org.neuroph.nnet.MultiLayerPerceptron;
import org.neuroph.nnet.learning.BackPropagation;
import org.neuroph.util.TransferFunctionType;

public class NeuralNetwork 
{
	private int inputs;
	private int numNetworkOutputs;
	private int numHiddenNeurons;
	private String transferFunctionType;
	private double learningRate;
	private double maxError;
	private int maxIterations;
	private MultiLayerPerceptron neuralNet;
	private DataSet trainingSet;
	private DataSet testSet;
	private String[] classNames;
    Evaluation evaluation = new Evaluation();

	public NeuralNetwork(DataSet training, DataSet testing, String[] classes, int inputs, int outputs, 
			int hiddenNeurons, String transferFuncType, double learningRate, double maxError, 
			int maxIterations)
	{
		this.inputs = inputs;
		this.numNetworkOutputs = outputs;
		this.numHiddenNeurons = hiddenNeurons;
		this.transferFunctionType = transferFuncType;
		this.learningRate = learningRate;
		this.maxError = maxError;
		this.maxIterations = maxIterations;
		this.classNames = classes;
		this.trainingSet = training;
		this.testSet = testing;
	}
	
	public void init() throws IOException
	{
		switch(transferFunctionType.toLowerCase()) {
			case "sigmoid":
				this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 
						this.inputs, this.numHiddenNeurons, this.numNetworkOutputs);
				break;
			case "linear":
				this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.LINEAR, 
						this.inputs, this.numHiddenNeurons, this.numNetworkOutputs);
				break;
			case "tanh":
				this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.TANH, 
						this.inputs, this.numHiddenNeurons, this.numNetworkOutputs);
				break;
			case "step":
				this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.STEP, 
						this.inputs, this.numHiddenNeurons, this.numNetworkOutputs);
				break;
			default:
				this.neuralNet = new MultiLayerPerceptron(TransferFunctionType.SIGMOID, 
						this.inputs, this.numHiddenNeurons, this.numNetworkOutputs);
				break;
		}
	
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
		EvaluationResult EvalResult = new EvaluationResult();
        EvalResult = evaluation.evaluateDataSet(this.neuralNet, this.testSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        averageStats[0] = average.accuracy;
        averageStats[1] = average.precision;
        averageStats[2] = average.recall;        
        averageStats[3] = average.fScore;        
        averageStats[4] = EvalResult.getMeanSquareError();
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
	    EvaluationResult EvalResult = new EvaluationResult();
        EvalResult = evaluation.evaluateDataSet(this.neuralNet, this.trainingSet);
        return EvalResult.getMeanSquareError();    
	}
	
	public double getTestMeanSquareError()
	{
	    EvaluationResult EvalResult = new EvaluationResult();
        EvalResult = evaluation.evaluateDataSet(this.neuralNet, this.testSet);       
        return EvalResult.getMeanSquareError();   
	}
	
	public double getTrainingAccuracyMinusMSError()
	{
		EvaluationResult EvalResult = new EvaluationResult();
        EvalResult = evaluation.evaluateDataSet(this.neuralNet, this.trainingSet);
        ClassifierEvaluator evaluator = evaluation.getEvaluator(ClassifierEvaluator.MultiClass.class);
        ConfusionMatrix confusionMatrix = evaluator.getResult();
        ClassificationMetrics[] metrics = ClassificationMetrics.createFromMatrix(confusionMatrix);
        ClassificationMetrics.Stats average = ClassificationMetrics.average(metrics);
        return (average.accuracy - EvalResult.getMeanSquareError());
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
		System.out.println("\tNumber of Inputs: " + this.inputs);
		System.out.println("\tNumber of Outputs: " + this.numNetworkOutputs);
		System.out.println("\tNumber Hidden Neurons: " + this.numHiddenNeurons);
		System.out.println("\tTransfer Function Type: " + this.transferFunctionType);
		System.out.println("\tLearning Rate: " + this.learningRate);
		System.out.println("\tMax Error: " + this.maxError);
		System.out.println("\tMax Iterations: " + this.maxIterations);
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
