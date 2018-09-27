/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test;

import java.util.Random;
import neat.NodeGene.TYPE;
import neat.Genome;
import neat.NodeGene;
import neat.ConnectionGene;

/**
 *
 * @author derekgrove
 */
public class Test {
    public static void main(String argv[]){
        //REPLICATING THE EXAMPLE IN KEN STANLEY PAPER
        
        //parent 1 and adding the nodes
        Genome parent1 = new Genome();
        for(int i=0; i<3;i++){
            NodeGene newNode = new NodeGene(TYPE.INPUT,i++);
            parent1.addNodeGene(newNode);
        }
        parent1.addNodeGene(new NodeGene(TYPE.OUTPUT,4));
        parent1.addNodeGene(new NodeGene(TYPE.HIDDEN,5));
        
        //adding connections for parent 1
        parent1.addConnectionGene(new ConnectionGene(1,4,1f,true,1));
        parent1.addConnectionGene(new ConnectionGene(2,4,1f,false,2));
        parent1.addConnectionGene(new ConnectionGene(3,4,1f,true,3));
        parent1.addConnectionGene(new ConnectionGene(2,5,1f,true,4));
        parent1.addConnectionGene(new ConnectionGene(5,4,1f,true,5));
        parent1.addConnectionGene(new ConnectionGene(1,5,1f,true,8));
        
        
        //parent 1 and adding the nodes
        Genome parent2 = new Genome();
        for(int i=0; i<3;i++){
            NodeGene newNode = new NodeGene(TYPE.INPUT,i++);
            parent2.addNodeGene(newNode);
        }
        parent2.addNodeGene(new NodeGene(TYPE.OUTPUT,4));
        parent2.addNodeGene(new NodeGene(TYPE.HIDDEN,5));
        parent2.addNodeGene(new NodeGene(TYPE.HIDDEN,6));
        
        //adding connections for parent 1
        parent2.addConnectionGene(new ConnectionGene(1,4,1f,true,1));
        parent2.addConnectionGene(new ConnectionGene(2,4,1f,false,2));
        parent2.addConnectionGene(new ConnectionGene(3,4,1f,true,3));
        parent2.addConnectionGene(new ConnectionGene(2,5,1f,true,4));
        parent2.addConnectionGene(new ConnectionGene(5,4,1f,false,5));
        parent2.addConnectionGene(new ConnectionGene(5,6,1f,true,6));
        parent2.addConnectionGene(new ConnectionGene(6,4,1f,true,7));
        parent2.addConnectionGene(new ConnectionGene(3,5,1f,true,9));
        parent2.addConnectionGene(new ConnectionGene(1,6,1f,true,10));
        
        Genome child = Genome.crossover(parent2, parent1, new Random());
        
    }
    
}
