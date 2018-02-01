package myGANN;

import java.io.IOException;
import java.util.Arrays;

public class Chromosome extends Population
{
	private double[] genes;
	private int size;
	private double fitness;
	
	public Chromosome()
	{
		this.size = nn.numWeights();
		fitness = 0;
		genes = new double[nn.numWeights()];
		System.out.println(this.size);

	}
	
	public Chromosome(int size)
	{
		this.size = size;
		fitness = 0;
		genes = new double[size];
	}
	
	public Chromosome(double[] genes)
	{
		size = genes.length;
		fitness = 0;
		this.genes = genes;
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
	
	public void computeFitness()
	{
		nn.setWeights(this.genes);
		nn.train();
		fitness = nn.getAccuracy();
	}
	
	public double getFitness()
	{
		return fitness;
	}
	
	public void mutate()
	{
        for (int i = 0; i < defaultChromosomeSize; i++) {
            if (Math.random() <= mutationRate) {
                double gene = randomWithRange(-1,1);
                this.setGeneAt(i, gene);
            }
        }
	}
	

	
	public double randomWithRange(double min, double max)
	{
	   double range = (max - min) + 1;     
	   return (Math.random() * range) + min;
	}
	
	public String toString()
	{
		String str = "Chromosome: \n\tSize: " + this.size + "\n\tChromosomes: " + this.fitness
				+ "\n\tGenes: " + Arrays.toString(this.genes);
		return str;
	}
	
}
