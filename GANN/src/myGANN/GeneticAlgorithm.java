/*
GeneticAlgorithm.java
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

public class GeneticAlgorithm {
	
		private Population pop;
		private double mutationRate;
		private int tournamentSize;
		private boolean elitism;
		private boolean multiGeneMutate;
		private double maxFitnessReached; 
		
		public GeneticAlgorithm()
		{
			this.pop = new Population();
			this.mutationRate = .8;
			this.tournamentSize = 5;
			this.elitism = true;
			this.multiGeneMutate = true;
			this.maxFitnessReached = 0;
		}
			
		public GeneticAlgorithm(Population population, double mutRate, int tournamentSize, boolean elitism, boolean multiGeneMutation)
		{
			this.pop = population;
			this.mutationRate = mutRate;
			this.tournamentSize = tournamentSize;
			this.elitism = elitism;
			this.multiGeneMutate = multiGeneMutation;
		}		

		public Chromosome evolve()
		{
			int timer=0;
			Chromosome fittestChrom = pop.getFittest();
			this.maxFitnessReached = fittestChrom.getFitness();
			System.out.println("Initial Fitness: " + this.maxFitnessReached);
			
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
				this.maxFitnessReached = pop.getFittest().getFitness();
				System.out.println("Fitness: " + this.maxFitnessReached);
				
				if(this.maxFitnessReached > fittestChrom.getFitness())
					fittestChrom = pop.getFittest();
				
				if(this.maxFitnessReached > GANN.lowestPossibleFitness)
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
						if(multiGeneMutate) 
						{
            					selectedChroms[0].multipleGeneMutation();
						}
						else
						{
	            				if(Math.random() < mutationRate)
	            					selectedChroms[0].mutate();
						}
	            			temp.setChromosomeAt(elitismOffset,selectedChroms[0]);
	            			elitismOffset++;
					}
	            
					if(elitismOffset < pop.size())
					{
						if(multiGeneMutate) 
						{
            					selectedChroms[1].multipleGeneMutation();
						}
						else
						{
	            				if(Math.random() < mutationRate)
	            					selectedChroms[1].mutate();
						}
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
		
		public double getMaxFitnessReached() {
			return this.maxFitnessReached;
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
