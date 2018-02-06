package myGANN;

public class GeneticAlgorithm
{
		private Population pop;
		static double mutationRate;
		private int tournamentSize;
		private boolean elitism;
		final double lowestPossibleFitness = .99;
		final int numEpochs = 50;

		
		public GeneticAlgorithm()
		{
			this.pop = new Population();
			mutationRate = .10;
			this.tournamentSize = 10;
			this.elitism = false;
		}
			
			
		public GeneticAlgorithm(Population pop, int mutRate, int tournamentSize, boolean elitism)
		{
			this.pop = pop;
			mutationRate = mutRate;
			this.tournamentSize = tournamentSize;
			this.elitism = elitism;
		}		

		public Chromosome evolve()
		{
			int timer=0;
			System.out.println("Timer: " + timer);
			System.out.println("fitest: ");
			pop.getFittest().printChroms();
			while(pop.getFittest().getFitness() < lowestPossibleFitness && timer < numEpochs)
			{
				Population temp = new Population();
				int elitismOffset = 0;
				if(elitism)
				{
					temp.setChromosomeAt(0, pop.getFittest());
					elitismOffset = 1;
				} 
	        
				// select and crossover
				int counter = elitismOffset;
				while(counter < pop.size()) 
				{
					Chromosome[] selectedChroms = pop.tournamentSelection(tournamentSize);
					selectedChroms = pop.crossover(selectedChroms[0], selectedChroms[1]);
					if(counter < pop.size())
					{
	            			if(Math.random() < mutationRate)
	            				selectedChroms[0].mutate();
	            			temp.setChromosomeAt(counter,selectedChroms[0]);
	            			counter++;
					}
	            
					if(counter < pop.size())
					{
	            			if(Math.random() < mutationRate)
	            				selectedChroms[0].mutate();
	            			temp.setChromosomeAt(counter,selectedChroms[1]);
	            			counter++;
					}
	            
				}
				pop = temp;
				timer++;
			}
			return pop.getFittest();
		}


		public double getMutationRate() {
			return mutationRate;
		}


		public void setMutationRate(double mutRate) {
			mutationRate = mutRate;
		}
		
		public void printPopulation()
		{
			pop.print();
		}
					
}
