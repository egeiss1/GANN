package myGANN;

import java.util.Arrays;

public class Chromosome
{
	private double[] genes;
	private int size;
	private double fitness;
	final double minWeight = GANN.minWeight;
	final double maxWeight = GANN.maxWeight;
	
	public Chromosome()
	{
		this.size = GANN.defaultChromosomeSize;
		this.fitness = 0;
		this.genes = new double[GANN.defaultChromosomeSize];
		//initGenes(); -> Done w/ population initialization
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
		GANN.nn.setWeights(this.genes);
		this.fitness = GANN.nn.getTrainingAccuracy();
	}
	
	public double getFitness()
	{	
		this.computeFitness();
		return fitness;
	}
	
	public void mutate()
	{
		int bitToMutate = randomIntWithRange(0, this.genes.length);
        double gene = randomWithRange(minWeight,maxWeight);
        this.setGeneAt(bitToMutate, gene);
	}
	
	public void initGenes()
	{
        for (int i = 0; i < this.genes.length; i++) {
        		double gene = randomWithRange(minWeight,maxWeight);
        		this.setGeneAt(i, gene);
        }
	}
	

	public void initGenes(int s)
	{
        for (int i = 0; i < s; i++) 
        {
               double gene = randomWithRange(minWeight,maxWeight);
               this.setGeneAt(i, gene);
        }
	}
	
	public void setGenes(double[] arr)
	{
		this.genes = arr;
	}
	
	
	public double[] convertDoubleArray(Double[] arr)
	{	
		double[] d = new double[arr.length];
		for(int i = 0; i < arr.length; ++i)
		{
			d[i] = arr[i].doubleValue();
		}
		return d;
	}
	
	
	public static double randomWithRange(double min, double max)
	{
	   double range = max - min;     
	   return (Math.random() * range) + min;
	}
	
	public int randomIntWithRange(int min, int max)
	{
	   int range = max - min;     
	   return (int)((Math.random() * range) + min);
	}
	
    public Chromosome copy()
    {
        Chromosome C = new Chromosome(this.genes);
        return C;
    }
	
	public String toString()
	{
		String str = "Chromosome: \n\tChromosomeSize: " + this.size + "\n\tfitness: " + this.fitness
				+ "\n\tGenes: " + Arrays.toString(this.genes);
		return str;
	}
	
	public void printGenes()
	{
		System.out.println(Arrays.toString(this.genes));
	}
	
}
