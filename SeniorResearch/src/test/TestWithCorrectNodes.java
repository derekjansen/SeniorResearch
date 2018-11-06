
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
public class TestWithCorrectNodes {
    public static void main(String argv[]){
        
        Counter nodeInnovation = new Counter();
        Counter connectionInnovation = new Counter();
        
        //make a new organism
        Organism organism = new Organism();
        
        int n1 = nodeInnovation.getInnovation();
        int n2 = nodeInnovation.getInnovation();
        int n3 = nodeInnovation.getInnovation();
        int n4 = nodeInnovation.getInnovation();
        int n5 = nodeInnovation.getInnovation();
        int n6 = nodeInnovation.getInnovation();
        int n7 = nodeInnovation.getInnovation();
        int n8 = nodeInnovation.getInnovation();
        int n9 = nodeInnovation.getInnovation();
        int n10 = nodeInnovation.getInnovation();
        int n11 = nodeInnovation.getInnovation();
        int n12 = nodeInnovation.getInnovation();
        
        //number of input node is associated with the type of input       
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n1));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n2));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n3));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n4));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n5));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n6));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n7));

                
        //number of output node is associated with the "buttons" that can be pressed
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n8));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n9));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n10));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n11));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n12));
        
        
        
        //create the base connection between one and one
        int c1 = connectionInnovation.getInnovation();
        int c2 = connectionInnovation.getInnovation();
        //add these to the organism
        organism.addConnectionGene(new ConnectionGene(n1,n8,0.5f,true,c1));
       // organism.addConnectionGene(new ConnectionGene(n1,n9,0.5f,true,c2));
        
        
        
        
        //create evaluator and pass in the popSize, the starting organism, and the counters for the two types of connections
        Evaluator eval = new Evaluator(organism, nodeInnovation, connectionInnovation){
            @Override
            
            protected float evaluateOrganism(Organism organism){
                
              float[] input = {3.0f, 0.0f, 1.0f, 1.5f, 0.0f, 100.0f, 12.0f};
              NeuralNetwork net = new NeuralNetwork(organism);
                System.out.println("\nNew Organism");
              for(int i = 3; i > 0; i-- ){
                float output[]= net.calculate(input);
                System.out.println("\tOutput node 1= " + output[0] + "\n\tOutput node 2= " + output[1] + "\n\tOutput node 3= " + output[2]+ "\n\tOutput node 4= " + output[3]+ "\n\tOutput node 5= " + output[4]);
              }
              
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
