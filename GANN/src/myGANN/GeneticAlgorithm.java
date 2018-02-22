package myGANN;

public class GeneticAlgorithm
{
		private Population pop;
		private double mutationRate;
		private int tournamentSize;
		private boolean elitism;
		
		public GeneticAlgorithm()
		{
			this.pop = new Population();
			this.pop.initPopulation();
			this.mutationRate = .8;
			this.tournamentSize = 5;
			this.elitism = true;
		}
			
			
		public GeneticAlgorithm(Population population, double mutRate, int tournamentSize, boolean elitism)
		{
			this.pop = population;
			this.pop.initPopulation();
			this.mutationRate = mutRate;
			this.tournamentSize = tournamentSize;
			this.elitism = elitism;
		}		

		public Chromosome evolve()
		{
			int timer=0;
			double fittest = pop.getFittest().getFitness();
			System.out.println("Initial Fitness: " + fittest);
			Chromosome fittestChrom = new Chromosome();
			while(timer < GANN.numberOfEpochs)
			{

				/* Training:
				   Partially train each network in the population on the training set 
				   for a certain number of epochs
				*/
				for(int i = 0; i < pop.size(); ++i)
				{
					GANN.nn.setWeights(pop.getChromosomeAt(i).getGenes());
					GANN.nn.train();
					pop.getChromosomeAt(i).setGenes(convertDoubleArray(GANN.nn.getWeights()));
				}

				// If stopping criteria is met, break out of loop
				fittest = pop.getFittest().getFitness();
				System.out.println("Fitness: " + fittest);
				
				if(fittest > fittestChrom.getFitness())
					fittestChrom = pop.getFittest();
				
				if(fittest > GANN.lowestPossibleFitness)
					break;
				
				Population temp = new Population();
				int elitismOffset = 0;
				if(elitism)
				{
					temp.setChromosomeAt(0, pop.getFittest());
					elitismOffset = 1;
				} 
	        
				// selection, crossover, and mutation
				while(elitismOffset < pop.size()) 
				{
					
					Chromosome[] selectedChroms = new Chromosome[2];
					if(this.tournamentSize != 0)
					{
						selectedChroms = pop.tournamentSelection(this.tournamentSize);
					}
					else
					{
						selectedChroms = pop.rouletteSelection();
					}
					
					if(Math.random() <= GANN.crossoverPr)
					{
						selectedChroms = pop.crossover(selectedChroms[0], selectedChroms[1]);
					}
					
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
				pop = temp.copy();
				timer++;
				System.out.println("GANN Iteration: " + timer);
			}
			return fittestChrom;
		}


		public double getMutationRate() {
			return mutationRate;
		}


		public void setMutationRate(double mutRate) {
			mutationRate = mutRate;
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
		
		public void printPopulation()
		{
			pop.print();
		}
		
					
}
