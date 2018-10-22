/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import neat.ConnectionGene;
import neat.Counter;
import neat.Evaluator;
import neat.Organism;

import neat.NodeGene;
import neat.NodeGene.TYPE;

/**
 *
 * @author derekgrove
 */
public class Test {
    public static void main(String argv[]){
        
        int populationSize = 100;
        Counter nodeInnovation = new Counter();
        Counter connectionInnovation = new Counter();
        
        //make a new organism
        Organism organism = new Organism();
        
        //create nodeGenes
        int n1 = nodeInnovation.getInnovation();
        int n2 = nodeInnovation.getInnovation();
        int n3 = nodeInnovation.getInnovation();
        //add these to the organism
        organism.addNodeGene(new NodeGene(TYPE.INPUT,n1));
        organism.addNodeGene(new NodeGene(TYPE.INPUT,n2));
        organism.addNodeGene(new NodeGene(TYPE.OUTPUT,n3));
        
        //create connectionGenes
        int c1 = connectionInnovation.getInnovation();
        int c2 = connectionInnovation.getInnovation();
        //add these to the organism
        organism.addConnectionGene(new ConnectionGene(n1,n3,0.5f,true,c1));
        organism.addConnectionGene(new ConnectionGene(n2,n3,0.5f,true,c2));
        
        
        
        
        //create evaluator and pass in the popSize, the starting organism, and the counters for the two types of connections
        Evaluator eval = new Evaluator(populationSize, organism, nodeInnovation, connectionInnovation){
            @Override
            
            protected float evaluateGenome(Organism organism){
                
                                
                
                return organism.getConnectionGenes().values().size();
            }
        };
        
        
        
        //run for 100 generations
        for(int i = 0; i < 100; i++){
            eval.evaluate();
            System.out.print("Generation: " + i);
            System.out.print("\tHighest fitness: " + eval.getHighestFitness());
            System.out.print("\tAmount of species: " + eval.getSpeciesAmount() + "\n");
        } 
    }
    
}
