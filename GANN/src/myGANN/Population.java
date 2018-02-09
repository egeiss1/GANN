package myGANN;

import java.util.Arrays;
import java.util.Random;

public class Population
{
	private Chromosome[] chromosomes;
	private int size;
	
	public Population()
	{
		this.chromosomes = new Chromosome[GANN.defaultPopulationSize];
		this.size = chromosomes.length;
		//this.initPopulation();
	}
	
	public Population(Chromosome[] chromosomes)
	{
		size = chromosomes.length;
		this.chromosomes = chromosomes;
	}
	
	public void initPopulation()
	{
		for(int i = 0; i < chromosomes.length; ++i)
		{
			chromosomes[i] = new Chromosome();
			chromosomes[i].initGenes();
		}
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
        for (int i = 0; i < tournament.length; i++) {
            int rand = (int) (Math.random() * this.chromosomes.length);
            tournament[i] = this.chromosomes[rand];
        }
        // Get the fittest
        Chromosome[] fittest = new Chromosome[2];
        //bubbleSort(tournament);
        mergeSort(tournament,0,tournament.length-1);

        for(int j = 0; j < fittest.length; ++j )
        {
        		fittest[j] = tournament[j];
        }
        return fittest;
    }
    
    public Chromosome rouletteSelection()
    {
    		
    		return null;
    }
    
	public Chromosome[] crossover(Chromosome C1, Chromosome C2)
	{
		Chromosome[] children = new Chromosome[2];
		int C1size = C1.size();
		if(C1size == C2.size())
		{
	        //Select a random crossover point
	        int crossoverPt = (int)(Math.random() * (((C1size - 1) - 1) + 1)) + 1;

	        children[0] = C1;
	        children[1] = C2;
	        
	        for (int i = crossoverPt; i < C1.size(); i++) {
	                children[0].setGeneAt(i, C2.getGeneAt(i));
	                children[1].setGeneAt(i, C1.getGeneAt(i));
	        }
		}
		else
		{
			System.out.println("Error: Chromosomes are of different sizes");
		}
		return children;
	}
	
	public Chromosome getFittest()
	{
		//bubbleSort(this.chromosomes);
		mergeSort(this.chromosomes, 0, this.chromosomes.length-1);

		return this.chromosomes[0];	
	}
	
	public Chromosome getSecondFittest()
	{
		//bubbleSort(this.chromosomes);
		mergeSort(this.chromosomes, 0, this.chromosomes.length-1);
		return this.chromosomes[1];	
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
	
    public static void swap(Chromosome[] arr, int loc1, int loc2) 
    { 
        Chromosome temp;
        temp = arr[loc1];
        arr[loc1] = arr[loc2];
        arr[loc2] = temp;
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
