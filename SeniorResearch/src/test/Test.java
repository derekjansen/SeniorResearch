/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import neat.ConnectionGene;
import neat.Counter;
import neat.Evaluator;
import neat.Genome;

import neat.NodeGene;
import neat.NodeGene.TYPE;

/**
 *
 * @author derekgrove
 */
public class Test {
    public static void main(String argv[]){
        Counter nodeInnovation = new Counter();
        Counter connectionInnovation = new Counter();
        
        Genome genome = new Genome();
        
        //create nodeGenes
        int n1 = nodeInnovation.getInnovation();
        int n2 = nodeInnovation.getInnovation();
        int n3 = nodeInnovation.getInnovation();
        genome.addNodeGene(new NodeGene(TYPE.INPUT,n1));
        genome.addNodeGene(new NodeGene(TYPE.INPUT,n2));
        genome.addNodeGene(new NodeGene(TYPE.OUTPUT,n3));
        
        //create connectionGenes
        int c1 = connectionInnovation.getInnovation();
        int c2 = connectionInnovation.getInnovation();
        genome.addConnectionGene(new ConnectionGene(n1,n3,0.5f,true,c1));
        genome.addConnectionGene(new ConnectionGene(n2,n3,0.5f,true,c2));
        
        
        //create evaluator
        Evaluator eval = new Evaluator(100, genome, nodeInnovation, connectionInnovation){
            @Override
            protected float evaluateGenome(Genome genome){
                return genome.getConnectionGenes().values().size();
            }
        };
        
        for(int i = 0; i < 100; i++){
            eval.evaluate();
            System.out.print("Generation: " + i);
            System.out.print("\tHighest fitness: " + eval.getHighestFitness());
            System.out.print("\tAmount of species: " + eval.getSpeciesAmount() + "\n");
        } 
    }
    
}
