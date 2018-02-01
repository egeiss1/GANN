package myGANN;

public class Population extends GeneticAlgorithm
{
	private Chromosome[] chromosomes;
	private int size;
	
	public Population()
	{
		this.chromosomes = new Chromosome[defaultPopulationSize];
		this.size = defaultChromosomeSize;
	}
	
	public Population(Chromosome[] chromosomes)
	{
		size = chromosomes.length;
		this.chromosomes = chromosomes;
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
	
	public Chromosome select()
	{
		Chromosome temp = new Chromosome();
		// TO DO
		
		return temp;
	}
	
    public Chromosome[] tournamentSelection() {
        // Create a tournament population
        Chromosome[] tournament = new Chromosome[6];
        // For each place in the tournament get a random individual
        for (int i = 0; i < tournament.length; i++) {
            int rand = (int) (Math.random() * this.chromosomes.length);
            tournament[i] = this.chromosomes[rand];
        }
        // Get the fittest
        Chromosome[] fittest = new Chromosome[2];
        for(int j = 0; j < fittest.length; ++j )
        {
        		
        }

        return fittest;
    }
	
	public Population crossover(Chromosome C1, Chromosome C2)
	{
		Population temp = new Population();
		if(C2.size() == C1.size())
		{
			// TO DO
		
		}
		else
		{
			System.out.println("Error: Chromosomes are of different sizes");
		}
		return temp;			
	}
	
	public Chromosome getFittest()
	{
		bubbleSort(this.chromosomes);
		return this.chromosomes[0];	
	}

    public static void bubbleSort(Chromosome arr[]) 
    {
        int n = arr.length;
        int k;
        for (int m = n; m >= 0; m--) {
            for (int i = 0; i < n - 1; i++) {
                k = i + 1;
                if (arr[i].getFitness() > arr[k].getFitness()) {
                    swap(i, k, arr);
                }
            }
        }
    }
	
    private static void swap(int i, int j, Chromosome[] arr) 
    { 
        Chromosome temp;
        temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

	
	public String toString()
	{
		String str = "Population: \n\tSize: " + this.size + "\n\tChromosomes: " +
					this.chromosomes.toString();
		return str;
	}
	
}
