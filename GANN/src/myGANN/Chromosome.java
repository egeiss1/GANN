package myGANN;

import java.util.Arrays;

public class Chromosome
{
	private double[] genes;
	private int size;
	private double fitness;
	
	public Chromosome()
	{
		this.size = GANN.defaultChromosomeSize;
		this.fitness = 0;
		this.genes = new double[GANN.defaultChromosomeSize];
		initGenes();
	}
	
	public Chromosome(int chromSize)
	{
		this.size = chromSize;
		this.fitness = 0;
		this.genes = new double[chromSize];
		initGenes();
	}
		
	public Chromosome(double[] genesArr)
	{
		size = genesArr.length;
		fitness = 0;
		this.genes = genesArr;
	}
	
	public void setGeneAt(int loc, double val)
	{
		genes[loc] = val;
	}
	
	public double getGeneAt(int loc)
	{
		return genes[loc];
	}
	
	public int size()
	{
		return size;
	}
	
	public double[] getGenes()
	{
		return genes;
	}
	
	public void computeFitness()
	{
		System.out.println("NN Weights (in GA): " + Arrays.toString(GANN.nn.getWeights()));
		GANN.nn.setWeights(genes);
		System.out.println("try to set Weights to (in GA): " + Arrays.toString(genes));
		System.out.println("Weights are Set to (in GA):" + Arrays.toString(GANN.nn.getWeights()));
		GANN.nn.train();
		this.fitness = GANN.nn.getAccuracy();
		System.out.println("Accuracy (in GA): " + GANN.nn.getAccuracy());
		System.out.println("fitness (in GA): " + this.fitness);
		
	}
	
	public double getFitness()
	{	
		computeFitness();
		return fitness;
	}
	
	public void mutate()
	{
        for (int i = 0; i < this.genes.length; i++) {
            if (Math.random() <= GeneticAlgorithm.mutationRate) {
                double gene = randomWithRange(-1,1);
                this.setGeneAt(i, gene);
            }
        }
	}
	
	public void initGenes()
	{
        for (int i = 0; i < this.genes.length; i++) {
        		double gene = randomWithRange(-1,1);
        		this.setGeneAt(i, gene);
        }
	}
	
	public void initGenes(int s)
	{
        for (int i = 0; i < s; i++) 
        {
               double gene = randomWithRange(-1,1);
               this.setGeneAt(i, gene);
        }
	}
	
	public static double randomWithRange(double min, double max)
	{
	   double range = max - min;     
	   return (Math.random() * range) + min;
	}
	
	public String toString()
	{
		String str = "Chromosome: \n\tChromosomeSize: " + this.size + "\n\tfitness: " + this.fitness
				+ "\n\tGenes: " + Arrays.toString(this.genes);
		return str;
	}
	
	public void printChroms()
	{
		System.out.println(Arrays.toString(this.genes));
	}
	
}
