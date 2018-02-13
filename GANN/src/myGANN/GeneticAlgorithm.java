package myGANN;

public class GeneticAlgorithm
{
		private Population pop;
		static double mutationRate;
		private int tournamentSize;
		private boolean elitism;
		private boolean tournament;
		
		public GeneticAlgorithm()
		{
			this.pop = new Population();
			this.pop.initPopulation();
			mutationRate = .8;
			this.tournamentSize = 5;
			this.elitism = true;
			this.tournament = true;
		}
			
			
		public GeneticAlgorithm(Population pop, int mutRate, int tournamentSize, boolean elitism, boolean tournament)
		{
			this.pop = pop;
			this.pop.initPopulation();
			mutationRate = mutRate;
			this.tournamentSize = tournamentSize;
			this.elitism = elitism;
			this.tournament = tournament;
		}		

		public Chromosome evolve()
		{
			int timer=0;
			while(pop.getFittest().getFitness() < GANN.lowestPossibleFitness && timer < GANN.numEpochs)
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

				//System.out.println("FittestChrom:");
				//pop.getFittest().printGenes();
				System.out.println("Fitness: " + pop.getFittest().getFitness());			
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
					if(tournament)
					{
						selectedChroms = pop.tournamentSelection(tournamentSize);
					}
					else
					{
						selectedChroms = pop.rouletteSelection();
					}
					
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
				pop = temp.copy();
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
