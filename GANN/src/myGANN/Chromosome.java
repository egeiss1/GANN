/*
Chromosome.java
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

import java.util.*;

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
		GANN.nn.setWeights(this.genes);
		this.fitness = GANN.nn.getTrainingAccuracyMinusMSError();
		//this.fitness = GANN.nn.getTrainingAccuracy();
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
	
	public void multipleGeneMutation()
	{
        for (int i = 0; i < this.genes.length; i++) 
        {
        		if (Math.random() <= GANN.mutationRate) {
        			double gene = randomWithRange(minWeight,maxWeight);
        	        this.setGeneAt(i, gene);
            }
        }
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
