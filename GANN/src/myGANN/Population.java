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
		return this.rank().getChromosomeAt(0);
	}
	
	public Population rank()
	{
		Population temp = new Population();
		// TO DO Choose a sorting algorithm to sort population by fitness
		return temp;
	}
	
	public String toString()
	{
		String str = "Population: \n\tSize: " + this.size + "\n\tChromosomes: " +
					this.chromosomes.toString();
		return str;
	}
	
}
