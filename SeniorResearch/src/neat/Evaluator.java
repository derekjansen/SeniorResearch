package neat;
//
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public abstract class Evaluator implements Tuning,Serializable{
	
	private FitnessOrganismComparator fitComp = new FitnessOrganismComparator();
	
	private Counter nodeInnovation;
	private Counter connectionInnovation;
	
	private Random random = new Random();
	
	
	private List<Organism> organisms;
	private List<FitnessOrganism> fitnessOrganisms;
	private List<Organism> nextGenOrganisms;
	
	private List<Species> species;
	
	private Map<Organism, Species> mappedSpecies;
	private Map<Organism, Float> scoreMap;
	private float highestScore;
	private int stagnation = 0;
	private Organism mostFitOrganism;
	
	public Evaluator(Organism startingOrganism, Counter nodeInnovation, Counter connectionInnovation) {
		this.nodeInnovation = nodeInnovation;
		this.connectionInnovation = connectionInnovation;
		
		organisms = new ArrayList<Organism>(populationSize);
		for (int i = 0; i < populationSize; i++) {
			organisms.add(new Organism(startingOrganism));
		}
		species = new ArrayList<Species>();
	}
	
	/**
	 * Runs one generation
	 */
	public void evaluate() {
		stagnation++;
		
		// Reset everything for next generation
		for (Species s : species) {
			s.reset(random);
		}
		scoreMap = new HashMap<Organism, Float>();
		mappedSpecies = new HashMap<Organism, Species>();
		nextGenOrganisms = new LinkedList<Organism>();
		fitnessOrganisms = new LinkedList<FitnessOrganism>();
		
		// Place organisms into species
		int added = 0;
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
				added++;
			}
		}
		//System.out.println("Added "+added+" species");
		
		// Remove unused species
		removeEmptySpecies();
		
		// Evaluate organisms and assign score
		for (Organism g : organisms) {
			Species s = mappedSpecies.get(g);		// Get species of the organism
			
			float score = evaluateOrganism(g);
			float adjustedScore = score / mappedSpecies.get(g).members.size();
			
			FitnessOrganism fitOrganism = new FitnessOrganism(g, adjustedScore);
			
			s.addAdjustedFitness(adjustedScore);	
			s.fitnessPop.add(fitOrganism);
			
			fitnessOrganisms.add(fitOrganism);
			
			scoreMap.put(g, adjustedScore);
			if (score > highestScore) {
				stagnation = 0;
				highestScore = score;
				mostFitOrganism = g;
			}
		}
		
		// put best organisms from each species into next generation
		nextGenOrganisms.add(mostFitOrganism);
		for (Species s : species) {
			if (s.members.size() > 5) {
				Collections.sort(s.fitnessPop, fitComp);
				Collections.reverse(s.fitnessPop);
				FitnessOrganism fittestInSpecies = s.fitnessPop.get(0);
				//System.out.println("Adding organism with fitness: "+fittestInSpecies.fitness);
				nextGenOrganisms.add(fittestInSpecies.organism);
			}
		}
		
		if (stagnation > 40) {
			System.out.println("CLEANING UPUUUP!");
			Collections.sort(fitnessOrganisms, fitComp);
			Collections.reverse(fitnessOrganisms);
			for (int i = 5; i < fitnessOrganisms.size(); i++) {
				FitnessOrganism fg = fitnessOrganisms.get(i);
				organisms.remove(fg.organism);
				for (Species s : species) {
					s.members.remove(fg.organism);
					s.fitnessPop.remove(fg);
					mappedSpecies.remove(fg.organism);
					scoreMap.remove(fg.organism);
				}
			}
			removeEmptySpecies();
			stagnation = 0;
		}
		
		// Breed the rest of the organisms
		while (nextGenOrganisms.size() < populationSize) { // replace removed organisms by randomly breeding
			Species s = getRandomSpeciesBiasedAjdustedFitness(random);
			
			Organism p1 = getRandomOrganismBiasedAdjustedFitness(s, random);
			Organism p2 = getRandomOrganismBiasedAdjustedFitness(s, random);
			
			if (random.nextFloat() < 0.2f) {
				if (scoreMap.get(p1) >= scoreMap.get(p2)) {
					Organism mutated = new Organism(p1);
					mutated.mutation(random);
					nextGenOrganisms.add(mutated);
				} else {
					Organism mutated = new Organism(p2);
					mutated.mutation(random);
					nextGenOrganisms.add(mutated);
				}
			} else {
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
		}
		
		organisms = nextGenOrganisms;
		nextGenOrganisms = new ArrayList<Organism>();
	}
	
        private void removeEmptySpecies() {
		Iterator<Species> iter = species.iterator();
		while(iter.hasNext()) {
			Species s = iter.next();
			if (s.members.isEmpty()) {
				iter.remove();
			}
		}
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
            throw new RuntimeException("Couldn't find a species... Number is species in total is " + species.size() + ", and the total adjusted fitness is " + completeWeight);
        }
	
        
        
	/**
	 * Selects a random organism from the species chosen, where organisms with a higher adjusted fitness have a higher chance of being selected
	 */
	private Organism getRandomOrganismBiasedAdjustedFitness(Species selectFrom, Random random) {
		double completeWeight = 0.0;	// sum of probablities of selecting each organism - selection is more probable for organisms with higher fitness
		for (FitnessOrganism fg : selectFrom.fitnessPop) {
			completeWeight += fg.fitness;
		}
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for (FitnessOrganism fg : selectFrom.fitnessPop) {
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
	
	public Organism getMostFitOrganism() {
		return mostFitOrganism;
	}
	
        public List<Organism> getOrganismList(){
            return organisms;
        }
        
	protected abstract float evaluateOrganism(Organism organism);
	
	public class FitnessOrganism implements Serializable {
		
		float fitness;
		Organism organism;
		
		public FitnessOrganism(Organism organism, float fitness) {
			this.organism = organism;
			this.fitness = fitness;
		}
	}
	
	
	
	public class FitnessOrganismComparator implements Comparator<FitnessOrganism>,Serializable {

		@Override
		public int compare(FitnessOrganism one, FitnessOrganism two) {
			if (one.fitness > two.fitness) {
				return 1;
			} else if (one.fitness < two.fitness) {
				return -1;
			}
			return 0;
		}
		
	}    
}