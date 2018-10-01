/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author derekgrove
 */
public abstract class Evaluator {
 
    private FitnessGenomeComparator fitComp = new FitnessGenomeComparator();
    
    private InnovationGenerator nodeInnovation;
    private InnovationGenerator connectionInnovation;
    
    private Random random = new Random();
    
    //Constants for tuning
    private float c1 = 1.0f;
    private float c2 = 1.0f;
    private float c3 = 0.4f;
    private float DT = 10.0f;
    private float N = 1.0f;
    private float MUTATION_RATE = 0.5f;
    private float ADD_CONNECTION_RATE = 0.1f;
    private float ADD_NODE_RATE = 0.1f;
    
    private int populationSize;
    
    private List<Genome> genomes;
    private List<Genome> nextGenGenomes;
    private List<Species> species;
    
    private Map<Genome, Species> mappedSpecies;
    private Map<Genome, Float> scoreMap;
    private float highestScore;
    private Genome mostFitGenome;
    
    
    
    
    /**
     * 
     * @param populationSize
     * @param startingGenome
     * @param nodeInnovation
     * @param connectionInnovation 
     */
    public Evaluator(int populationSize, Genome startingGenome, InnovationGenerator nodeInnovation, InnovationGenerator connectionInnovation){
        this.populationSize = populationSize;
        this.nodeInnovation = nodeInnovation;
        this.connectionInnovation = connectionInnovation;
        genomes = new ArrayList<Genome>(populationSize);
        for(int i=0; i< populationSize; i++){
            genomes.add(new Genome(startingGenome));
        }
        nextGenGenomes = new ArrayList<Genome>(populationSize);
        mappedSpecies = new HashMap<Genome,Species>();
        scoreMap = new HashMap<Genome,Float>();
        species = new ArrayList<Species>();
    }
     
    
    /**
     * Runs one generation
     */
    public void evaluate(){
        //reset everything for the next generation
        for(Species s : species){
                s.reset(random);
        }
        scoreMap.clear();
        mappedSpecies.clear();
        nextGenGenomes.clear();
        highestScore = Float.MIN_VALUE;
        mostFitGenome = null;
        
        
        //Place Genomes into Species
        for(Genome g : genomes){
            boolean foundSpecies = false;
            for(Species s: species){
                         
                if(Util.compatabilityDistance(g,s.mascot,c1,c2,c3,N) < DT){
                    s.members.add(g);
                    mappedSpecies.put(g, s);
                    foundSpecies = true;
                    break;
                }
            }
            if(!foundSpecies){
                Species newSpecies = new Species(g);
                species.add(newSpecies);
                mappedSpecies.put(g, newSpecies);
            }
        }    
        
        
        //Remove unused species
        Iterator<Species> iter = species.iterator();
        while(iter.hasNext()){
            Species s = iter.next();
            if(s.members.isEmpty()){
                iter.remove();
            }
        }
        
         
        //Evaluate Genomes and assign Score
        for(Genome g: genomes){
            Species s = mappedSpecies.get(g);
                
            float score = evaluateGenome(g);
            float adjustedScore = score/mappedSpecies.get(g).members.size();
            
            s.addAdjustedFitness(adjustedScore);
            s.fitnessPop.add(new FitnessGenome(g,adjustedScore));
            scoreMap.put(g, adjustedScore);
            if(score > highestScore){
                highestScore = score;
                mostFitGenome = g;
            }
        }
        
        //put best genomes from each species into next generation
        for(Species s:species){
            Collections.sort(s.fitnessPop, fitComp);
            Collections.reverse(s.fitnessPop);
            FitnessGenome mostFitInSpecies = s.fitnessPop.get(0);
            nextGenGenomes.add(mostFitInSpecies.genome);
        }
        
        
        //Breed the rest of the genomes
        while(nextGenGenomes.size() < populationSize){ //replace removed genomes by randomly breeding
            Species s = getRandomSpeciesBiasedAdjustedFitness(random);
                      
            Genome p1 = getRandomGenomeBiasedAdjustedFitness(s, random);
            Genome p2 = getRandomGenomeBiasedAdjustedFitness(s, random);
            
            Genome child;
            if(scoreMap.get(p1) >= scoreMap.get(p2)){
                child = Genome.crossover(p1, p2, random);
            }else {
                child = Genome.crossover(p2, p1, random);
            }
            
            if(random.nextFloat() < MUTATION_RATE){
                child.changeWeightMutation(random);
            }
            
            if(random.nextFloat() < ADD_CONNECTION_RATE){
                child.addConnectionMutation(random, connectionInnovation, 10);
            }
            
            if(random.nextFloat() < ADD_NODE_RATE){
                child.addNodeMutation(random, connectionInnovation, nodeInnovation);
            }
            
            nextGenGenomes.add(child);  
        }
        genomes = nextGenGenomes;
        nextGenGenomes = new ArrayList<Genome>();
    }
        
     /**
      * Selects random species from the list where species with a higher total adjusted fitness have a higher change
      */
    private Species getRandomSpeciesBiasedAdjustedFitness(Random random){
        double completeWeight = 0.0;    //sum of prob of selecting each species 
        
        for(Species s: species){
            completeWeight += s.totalAdjustedFitness;
        }
        
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for(Species s: species){
            countWeight += s.totalAdjustedFitness;
            if(countWeight >= r){
                return s;
            }
        }
        throw new RuntimeException("Couldnt find a species... Number is species in total is " + species.size());
        
    }
    
      /**
      * Selects random Genome from the species chosen where genomes with a higher total adjusted fitness have a higher change
      */
    private Genome getRandomGenomeBiasedAdjustedFitness(Species selectFrom, Random random){
        double completeWeight = 0.0;    //sum of prob of selecting each genome 
        
        for(FitnessGenome fg: selectFrom.fitnessPop){
            completeWeight += fg.fitness;
        }
        
        double r = Math.random() * completeWeight;
        double countWeight = 0.0;
        for(FitnessGenome fg: selectFrom.fitnessPop){
            countWeight += fg.fitness;
            if(countWeight >= r){
                return fg.genome;
            }
        }
        throw new RuntimeException("Couldnt find a Genome... Number is species in total is " );
        
    }
    
    public int getSpeciesAmount(){
        return species.size();
    }
    
    public float getHighestFitness(){
        return highestScore;
    }
    
    public Genome getMostFitGenome(){
        return mostFitGenome;
    }
    
    
    protected abstract float evaluateGenome(Genome genome);
    
    
    public class FitnessGenome{
        
        float fitness;
        Genome genome;
        
        public FitnessGenome(Genome genome, float fitness){
            this.genome = genome;
            this.fitness = fitness;
        }
    }

}
  
