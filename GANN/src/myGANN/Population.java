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
		initPopulation();
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
        tournament = bubbleSort(tournament);
        for(int j = 0; j < fittest.length; ++j )
        {
        		fittest[j] = tournament[j];
        }
        return fittest;
    }
    
	public Chromosome[] crossover(Chromosome C1, Chromosome C2)
	{
		Chromosome[] children = new Chromosome[2];
		if(C1.size() == C2.size())
		{
	        //Select a random crossover point
	        int crossoverPt = (int)(Math.random() * (C1.size()-1));
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
		this.chromosomes = bubbleSort(this.chromosomes);
		return this.chromosomes[0];	
	}
	
	public Chromosome getSecondFittest()
	{
		this.chromosomes = bubbleSort(this.chromosomes);
		return this.chromosomes[1];	
	}

    public Chromosome[] bubbleSort(Chromosome[] arr) 
    {
    		Chromosome temp[] = arr;
        int n = temp.length;
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (temp[i].getFitness() > temp[k].getFitness()) {
                    swap(temp, i, k);
                }
            }
        }
        return temp;
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
