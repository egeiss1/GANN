package myGANN;

import java.io.IOException;
import java.util.Arrays;

public class GeneticAlgorithm extends GANN
{
		
		private Population pop;
		static double mutationRate;
		
		public GeneticAlgorithm()
		{
			this.pop = new Population();
			this.mutationRate = .10;
		}
			
			
		public GeneticAlgorithm(Population pop, int mutationRate)
		{
			this.pop = pop;
			this.mutationRate = mutationRate;
		}
			
		public Population evolve()
		{
			Population temp = new Population();
				// TO DO
			return temp;
		}
					
}
