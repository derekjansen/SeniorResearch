package neat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class Evaluator implements Tuning{
	
	private FitnessGenomeComparator fitComp = new FitnessGenomeComparator();
	
	private Counter nodeInnovation;
	private Counter connectionInnovation;
	
	private Random random = new Random();
	
	private int populationSize;
	
	private List<Organism> organisms;
	private List<Organism> nextGenOrganisms;
	
	private List<Species> species;
	
	private Map<Organism, Species> mappedSpecies;
	private Map<Organism, Float> scoreMap;
	private float highestScore;
	private Organism mostFitOrganism;
	
	public Evaluator(int populationSize, Organism startingGenome, Counter nodeInnovation, Counter connectionInnovation) {
		this.populationSize = populationSize;
		this.nodeInnovation = nodeInnovation;
		this.connectionInnovation = connectionInnovation;
		organisms = new ArrayList<Organism>(populationSize);
		for (int i = 0; i < populationSize; i++) {
			organisms.add(new Organism(startingGenome));
		}
		nextGenOrganisms = new ArrayList<Organism>(populationSize);
		mappedSpecies = new HashMap<Organism, Species>();
		scoreMap = new HashMap<Organism, Float>();
		species = new ArrayList<Species>();
	}
	
	/**
	 * Runs one generation
	 */
	public void evaluate() {
		// Reset everything for next generation
		for (Species s : species) {
			s.reset(random);
		}
		scoreMap.clear();
		mappedSpecies.clear();
		nextGenOrganisms.clear();
		highestScore = Float.MIN_VALUE;
		mostFitOrganism = null;
		
		// Place organisms into species
		for (Organism g : organisms) {
			boolean foundSpecies = false;
			for (Species s : species) {
				if (Utils.compatibilityDistance(g, s.mascot, C1, C2, C3) < DT) { // compatibility distance is less than DT, so organism belongs to this species
					s.members.add(g);
					mappedSpecies.put(g, s);
					foundSpecies = true;
					break;
				}
			}
			if (!foundSpecies) { // if there is no appropiate species for organism, make a new one
				Species newSpecies = new Species(g);
				species.add(newSpecies);
				mappedSpecies.put(g, newSpecies);
			}
		}
		
		// Remove unused species
		Iterator<Species> iter = species.iterator();
		while(iter.hasNext()) {
			Species s = iter.next();
			if (s.members.isEmpty()) {
				iter.remove();
			}
		}
	
// Evaluate organisms and assign score --- CALLS the evaulate Genome method.
		for (Organism g : organisms) {
			Species s = mappedSpecies.get(g);		// Get species of the organism
			
			float score = evaluateGenome(g);
                        //adjust based on size of the species
			float adjustedScore = score / mappedSpecies.get(g).members.size();
			
			s.addAdjustedFitness(adjustedScore);	
			s.fitnessPop.add(new FitnessGenome(g, adjustedScore));
			scoreMap.put(g, adjustedScore);
			if (score > highestScore) {
				highestScore = score;
				mostFitOrganism = g;
			}
		}
		
                
		// put best organisms from each species into next generation
		for (Species s : species) {
			Collections.sort(s.fitnessPop, fitComp);
			Collections.reverse(s.fitnessPop);
			FitnessGenome mostFitInSpecies = s.fitnessPop.get(0);
			nextGenOrganisms.add(mostFitInSpecies.organism);
		}
		
                
		// Breed the rest of the organisms
		while (nextGenOrganisms.size() < populationSize) { // replace removed organisms by randomly breeding
			Species s = getRandomSpeciesBiasedAjdustedFitness(random);
			
			Organism p1 = getRandomGenomeBiasedAdjustedFitness(s, random);
			Organism p2 = getRandomGenomeBiasedAdjustedFitness(s, random);
			
			Organism child;
			if (scoreMap.get(p1) >= scoreMap.get(p2)) {
				child = Organism.crossover(p1, p2, random);
			} else {
				child = Organism.crossover(p2, p1, random);
			}
			if (random.nextFloat() < MUTATION_RATE) {
				child.mutation(random);
			}
			if (random.nextFloat() < ADD_CONNECTION_RATE) {
				//System.out.println("Adding connection mutation...");
				child.addConnectionMutation(random, connectionInnovation, 10);
			}
			if (random.nextFloat() < ADD_NODE_RATE) {
				//System.out.println("Adding node mutation...");
				child.addNodeMutation(random, connectionInnovation, nodeInnovation);
			}
			nextGenOrganisms.add(child);
		}
		
		organisms = nextGenOrganisms;
		nextGenOrganisms = new ArrayList<Organism>();
	}
	
        
        
	/**
	 * Selects a random species from the species list, where species with a higher total adjusted fitness have a higher chance of being selected
	 */
	private Species getRandomSpeciesBiasedAjdustedFitness(Random random) {
		double completeWeight = 0.0;	// sum of probablities of selecting each species - selection is more probable for species with higher fitness
		for (Species s : species) {
            completeWeight += s.totalAdjustedFitness;
		}
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (Species s : species) {
            countWeight += s.totalAdjustedFitness;
            if (countWeight >= r) {
            	 return s;
            }
        }
        throw new RuntimeException("Couldn't find a species... Number is species in total is "+species.size()+", and the total adjusted fitness is "+completeWeight);
	}
	
        
        
	/**
	 * Selects a random organism from the species chosen, where organisms with a higher adjusted fitness have a higher chance of being selected
	 */
	private Organism getRandomGenomeBiasedAdjustedFitness(Species selectFrom, Random random) {
		double completeWeight = 0.0;	// sum of probablities of selecting each organism - selection is more probable for organisms with higher fitness
		for (FitnessGenome fg : selectFrom.fitnessPop) {
			completeWeight += fg.fitness;
		}
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (FitnessGenome fg : selectFrom.fitnessPop) {
            countWeight += fg.fitness;
            if (countWeight >= r) {
            	 return fg.organism;
            }
        }
        throw new RuntimeException("Couldn't find a organism... Number is organisms in selected species is "+selectFrom.fitnessPop.size()+", and the total adjusted fitness is "+completeWeight);
	}
	
	public int getSpeciesAmount() {
		return species.size();
	}
	
	public float getHighestFitness() {
		return highestScore;
	}
	
	public Organism getFittestGenome() {
		return mostFitOrganism;
	}
	
        public List<Organism> getGenomeList(){
            return organisms;
        }
        
	protected abstract float evaluateGenome(Organism organism);
	
	public class FitnessGenome {
		
		float fitness;
		Organism organism;
		
		public FitnessGenome(Organism organism, float fitness) {
			this.organism = organism;
			this.fitness = fitness;
		}
	}
	
	
	
	public class FitnessGenomeComparator implements Comparator<FitnessGenome> {

		@Override
		public int compare(FitnessGenome one, FitnessGenome two) {
			if (one.fitness > two.fitness) {
				return 1;
			} else if (one.fitness < two.fitness) {
				return -1;
			}
			return 0;
		}
		
	}    
}