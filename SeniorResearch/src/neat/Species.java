package neat;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import neat.Evaluator;
import neat.Organism;

public class Species {
		
		public Organism mascot;
		public List<Organism> members;
		public List<Evaluator.FitnessOrganism> fitnessPop;
		public float totalAdjustedFitness = 0f;
		
		public Species(Organism mascot) {
			this.mascot = mascot;
			this.members = new LinkedList<Organism>(); 
			this.members.add(mascot);
			this.fitnessPop = new ArrayList<Evaluator.FitnessOrganism>(); 
		}
		
		public void addAdjustedFitness(float adjustedFitness) {
			this.totalAdjustedFitness += adjustedFitness;
		}
		
		/*
		 *	 Selects new random mascot + clear members + set totaladjustedfitness to 0f
		 */
		public void reset(Random r) {
			int newMascotIndex = r.nextInt(members.size());
			this.mascot = members.get(newMascotIndex);
			members.clear();
			fitnessPop.clear();
			totalAdjustedFitness = 0f;
		}
	}