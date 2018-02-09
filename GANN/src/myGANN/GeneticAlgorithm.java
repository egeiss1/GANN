package myGANN;

public class GeneticAlgorithm
{
		private Population pop;
		static double mutationRate;
		private int tournamentSize;
		private boolean elitism;
		
		public GeneticAlgorithm()
		{
			this.pop = new Population();
			this.pop.initPopulation();
			mutationRate = .9;
			this.tournamentSize = 15;
			this.elitism = false;
		}
			
			
		public GeneticAlgorithm(Population pop, int mutRate, int tournamentSize, boolean elitism)
		{
			this.pop = pop;
			this.pop.initPopulation();
			mutationRate = mutRate;
			this.tournamentSize = tournamentSize;
			this.elitism = elitism;
		}		

		public Chromosome evolve()
		{
			int timer=0;
			System.out.println(pop.getFittest().getFitness());
			while(pop.getFittest().getFitness() < GANN.lowestPossibleFitness && timer < GANN.numEpochs)
			{
				System.out.println("FittestChroms");
				pop.getFittest().printChroms();
				System.out.println("Fitness: " + pop.getFittest().getFitness());			
				Population temp = new Population();
				int elitismOffset = 0;
				if(elitism)
				{
					temp.setChromosomeAt(0, pop.getFittest());
					elitismOffset = 1;
				} 
	        
				// select and crossover
				while(elitismOffset < pop.size()) 
				{
					Chromosome[] selectedChroms = pop.tournamentSelection(tournamentSize);
					selectedChroms = pop.crossover(selectedChroms[0], selectedChroms[1]);
					if(elitismOffset < pop.size())
					{
	            			if(Math.random() < mutationRate)
	            				selectedChroms[0].mutate();
	            			temp.setChromosomeAt(elitismOffset,selectedChroms[0]);
	            			elitismOffset++;
					}
	            
					if(elitismOffset < pop.size())
					{
	            			if(Math.random() < mutationRate)
	            				selectedChroms[1].mutate();
	            			temp.setChromosomeAt(elitismOffset,selectedChroms[1]);
	            			elitismOffset++;
					}
	            
				}
				pop = temp;
				timer++;
				System.out.println("Timer: " + timer);
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
