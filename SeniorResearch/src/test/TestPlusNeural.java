
package test;

import neat.ConnectionGene;
import neat.Counter;
import neat.Evaluator;
import neat.NeuralNetwork;
import neat.NodeGene;
import neat.Organism;

/**
 *
 * @author derekgrove
 */
public class TestPlusNeural {
    
    public static void main(String argv[]){
        
        Counter nodeInnovation = new Counter();
        Counter connectionInnovation = new Counter();
        
        //make a new organism
        Organism organism = new Organism();
        
        //create nodeGenes
        int n1 = nodeInnovation.getInnovation();
        int n2 = nodeInnovation.getInnovation();
        int n3 = nodeInnovation.getInnovation();
        int n4 = nodeInnovation.getInnovation();
        
        //add these to the organism
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n1));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n2));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n3));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n4));
        
        //create connectionGenes
        int c1 = connectionInnovation.getInnovation();
        int c2 = connectionInnovation.getInnovation();
        
        //add these to the organism... does not work with no initial connections
        organism.addConnectionGene(new ConnectionGene(n1,n3,0.5f,true,c1));
        organism.addConnectionGene(new ConnectionGene(n2,n3,0.5f,true,c2));
        
        
        
        
        //create evaluator and pass in the popSize, the starting organism, and the counters for the two types of connections
        Evaluator eval = new Evaluator(organism, nodeInnovation, connectionInnovation){
            @Override
            
            protected float evaluateOrganism(Organism organism){
                
              float[] input = {1.2f,1.2f};
              NeuralNetwork net = new NeuralNetwork(organism);
              float output[]= net.calculate(input);
              System.out.println("\tOutput node 1= " + output[0] + "\t\nOutput node 2= " + output[1]);
              
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
