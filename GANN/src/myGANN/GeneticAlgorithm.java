import java.io.IOException;
import java.util.Arrays;

public class GeneticAlgorithm extends GANN {
		private final int defaultPopulationSize = 100;
		private final int defaultChromosomeSize = nn.numWeights();
		private Population pop;
		private int mutationRate;
			
		public GeneticAlgorithm(Population pop, int mutationRate)
		{
			this.pop = pop;
			this.mutationRate = mutationRate;;
		}
			
		public Population evolve()
		{
			Population temp = new Population();
				// TO DO
			return temp;
		}
			
		public class Population {
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
				if(C2.size == C1.size)
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
		
		public class Chromosome {
			private double[] genes;
			private int size;
			private double fitness;
			
			public Chromosome()
			{
				try {
					nn.init();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
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
			
			public Chromosome mutate()
			{
				Chromosome temp = new Chromosome(this.size);
				//TO DO
				return temp;
			}
			
			public String toString()
			{
				String str = "Chromosome: \n\tSize: " + this.size + "\n\tChromosomes: " + this.fitness
						+ "\n\tGenes: " + Arrays.toString(this.genes);
				return str;
			}
			
		}
}
