/*
Population.java
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

import java.util.Arrays;

public class Population
{
	private Chromosome[] chromosomes;
	private int size;
	
	public Population()
	{
		this.chromosomes = new Chromosome[GANN.defaultPopulationSize];
		this.size = chromosomes.length;
		this.initPopulation();
	}
	
	public Population(Chromosome[] chromosomes)
	{
		size = chromosomes.length;
		this.chromosomes = chromosomes;
	}
	
	public void initPopulation()
	{
		/*
		 * takes weights of first run of NN and inserts them into population
		 * It's a little bit of a hack so I left it out
		double[] d = new double[GANN.nn.numWeights()];
		Double[] weights = GANN.nn.getWeights();
		
		for(int i = 0; i < weights.length; ++i)
		{
			d[i] = weights[i].doubleValue();
		}

		chromosomes[0] = new Chromosome(d);
		*/
		
		for(int i = 0; i < chromosomes.length; ++i)
		{
			chromosomes[i] = new Chromosome();
		}
	}
	
	public Chromosome[] getChromosomes()
	{
		return this.chromosomes;
	}
	
	public void setChromosomeAt(int loc, Chromosome val)
	{
		chromosomes[loc] = val;
	}
	
	public Chromosome getChromosomeAt(int loc)
	{
		return chromosomes[loc];
	}
	
	public int size()
	{
		return size;
	}
		
    public Chromosome[] tournamentSelection(int tournamentSize) {
        // Create a tournament population
        Chromosome[] tournament = new Chromosome[tournamentSize];
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournament.length; i++) 
        {
        		int rand1 = 0;
        		int rand2 = 0;
        		// makes sure random numbers are different
        		while(rand1 == rand2)
        		{
                rand1 = (int) (Math.random() * this.chromosomes.length);
                rand2 = (int) (Math.random() * this.chromosomes.length);
        		}
        		// tournament between two random individuals
        		if(this.chromosomes[rand1].getFitness() >= this.chromosomes[rand2].getFitness())
        			tournament[i] = this.chromosomes[rand1];
        		else
        			tournament[i] = this.chromosomes[rand2];
        }
        // Get the fittest
        Chromosome[] fittest = new Chromosome[2];
		if(GANN.sortingAlgorithm == 0)
			mergeSort(tournament, 0, tournament.length-1);
		else if(GANN.sortingAlgorithm == 1)
			bubbleSort(tournament);
		else
			quickSort(tournament, 0, tournament.length-1);

        for(int j = 0; j < fittest.length; ++j )
        {
        		fittest[j] = tournament[j];
        }
        return fittest;
    }
    
    public double getPopulationFitness()
    {
    		double totalfitness = 0;
    		for(int i = 0; i < this.chromosomes.length; ++i)
    		{
    			totalfitness += this.chromosomes[i].getFitness();
    		}
    		return totalfitness;
    }
    
    public Chromosome[] rouletteSelection()
    {
    		
		Chromosome[] selectedChroms = new Chromosome[2];
		double populationFitness = this.getPopulationFitness();
		for(int i = 0; i < 2; ++i)
		{
			double rouletteWheelPosition = Math.random() * populationFitness;
			double spinWheel = 0;
			for (Chromosome chrom : chromosomes) {
				spinWheel += chrom.getFitness();
				if (spinWheel >= rouletteWheelPosition) 
					selectedChroms[i] = chrom;
			}
			selectedChroms[i] = this.chromosomes[chromosomes.length - 1];  
		}
		return selectedChroms;
    }
    
	public Chromosome[] crossover(final Chromosome C1, final Chromosome C2)
	{
		int p1size = C1.size();
		int p2size = C2.size();
		Chromosome child1 = new Chromosome(p1size);
		Chromosome child2 = new Chromosome(p2size);
		Chromosome[] children = new Chromosome[2];

		if(p1size == p2size)
		{
	        //Select a random crossover point
	        int crossoverPt = (int)(Math.random() * ((p1size - 2) + 1)) + 1;
	        for (int i = 0; i < crossoverPt; i++) {
                child1.setGeneAt(i, C1.getGeneAt(i));
                child2.setGeneAt(i, C2.getGeneAt(i));
	        }
	        
	        for (int i = crossoverPt; i < p1size; i++) {
	                child1.setGeneAt(i, C2.getGeneAt(i));
	                child2.setGeneAt(i, C1.getGeneAt(i));
	        }
	        children[0] = child1;
	        children[1] = child2;
		}
		else
		{
			System.out.println("Error: Chromosomes are of different sizes");
		}
		return children;
	}
	
	public Chromosome getFittest()
	{
		if(GANN.sortingAlgorithm == 0)
			mergeSort(this.chromosomes, 0, this.chromosomes.length-1);
		else if(GANN.sortingAlgorithm == 1)
			bubbleSort(this.chromosomes);
		else
			quickSort(this.chromosomes, 0, this.chromosomes.length-1);
		
		return this.chromosomes[this.chromosomes.length - 1];	
	}
	
	public Chromosome getSecondFittest()
	{
		if(GANN.sortingAlgorithm == 0)
			mergeSort(this.chromosomes, 0, this.chromosomes.length-1);
		else if(GANN.sortingAlgorithm == 1)
			bubbleSort(this.chromosomes);
		else
			quickSort(this.chromosomes, 0, this.chromosomes.length-1);
		
		return this.chromosomes[this.chromosomes.length - 2];	
	}

    public void bubbleSort(Chromosome[] arr) 
    {
        int n = arr.length;
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (arr[i].getFitness() > arr[k].getFitness()) {
                    swap(arr, i, k);
                }
            }
        }
    }
    
    public void merge(Chromosome[] arr, int l, int m, int r)
    {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;
 
        /* Create temp arrays */
        Chromosome[] L = new Chromosome[n1];
        Chromosome[] R = new Chromosome[n2];
 
        /*Copy data to temp arrays*/
        for (int i=0; i<n1; ++i)
            L[i] = arr[l + i];
        for (int j=0; j<n2; ++j)
            R[j] = arr[m + 1+ j];
 
        /* Merge the temp arrays */
 
        // Initial indexes of first and second subarrays
        int i = 0, j = 0;
 
        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2)
        {
            if (L[i].getFitness() <= R[j].getFitness())
            {
                arr[k] = L[i];
                i++;
            }
            else
            {
                arr[k] = R[j];
                j++;
            }
            k++;
        }
        /* Copy remaining elements of L[] if any */
        while (i < n1)
        {
            arr[k] = L[i];
            i++;
            k++;
        }
        /* Copy remaining elements of R[] if any */
        while (j < n2)
        {
            arr[k] = R[j];
            j++;
            k++;
        }
    }
 

    public void mergeSort(Chromosome[] arr, int l, int r)
    {
        if (l < r)
        {
            // Find the middle point
            int m = (l+r)/2;
            // Sort first and second halves
            mergeSort(arr, l, m);
            mergeSort(arr, m+1, r);
            // Merge the sorted halves
            merge(arr, l, m, r);
        }
    }
    
	public int partition(Chromosome[] arr, int low, int high)
    {
        Chromosome pvt = arr[high]; 
        int i = (low - 1); 
        for (int j = low; j < high; j++)
        {
            if (arr[j].getFitness() <= pvt.getFitness())
            {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i+1, high); 
        return i+1;
    }
 

    public void quickSort(Chromosome[] arr, int low, int high)
    {
        if (low < high)
        {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi-1);
            quickSort(arr, pi+1, high);
        }
    }
	
    public static void swap(Chromosome[] arr, int loc1, int loc2) 
    { 
        Chromosome temp = new Chromosome();
        temp = arr[loc1];
        arr[loc1] = arr[loc2];
        arr[loc2] = temp;
    }
    
    public Population copy()
    {
    		Population P = new Population(this.chromosomes);
    		return P;
    }
    	
	public String toString()
	{
		String str = "Population: \n\tSize: " + this.size + "\n\tChromosomes: ";
			str += Arrays.toString(this.chromosomes);
		return str;
	}
	
	public void print()
	{
		System.out.println("Population: \n\tPopulationSize: " + this.size + "\n\tChromosomes: ");
		for(int i = 0; i < chromosomes.length; ++i)
		{
			System.out.println(this.chromosomes[i]);
		}

	}
	
}
