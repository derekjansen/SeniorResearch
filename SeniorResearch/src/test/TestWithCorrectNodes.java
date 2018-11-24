
package test;

import java.io.FileNotFoundException;
import java.util.Random;
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
    public static void main(String argv[]) throws FileNotFoundException{
        
        //PrintStream out = new PrintStream(new FileOutputStream("testoutput.txt", true), true);
        //System.setOut(out);
        
        Counter nodeInnovation = new Counter();
        Counter connectionInnovation = new Counter();
        
        //make a new organism
        Organism organism = new Organism();
        
        //bias
        int n1 = nodeInnovation.getInnovation();
        //damage taken
        int n2 = nodeInnovation.getInnovation();
        //damage dealt
        int n3 = nodeInnovation.getInnovation();
        
        //zombie location
        int n4 = nodeInnovation.getInnovation();
        int n5 = nodeInnovation.getInnovation();
        int n6 = nodeInnovation.getInnovation();
        int n7 = nodeInnovation.getInnovation();
        int n8 = nodeInnovation.getInnovation();
        int n9 = nodeInnovation.getInnovation();
        int n10 = nodeInnovation.getInnovation();
        int n11 = nodeInnovation.getInnovation();
        int n12 = nodeInnovation.getInnovation();
        int n13 = nodeInnovation.getInnovation();
        int n14 = nodeInnovation.getInnovation();
        int n15 = nodeInnovation.getInnovation();
        int n16 = nodeInnovation.getInnovation();
        int n17 = nodeInnovation.getInnovation();
        int n18 = nodeInnovation.getInnovation();
        int n19 = nodeInnovation.getInnovation();
        
        //player looking direction
        int n20 = nodeInnovation.getInnovation();
        int n21 = nodeInnovation.getInnovation();
        int n22 = nodeInnovation.getInnovation();
        int n23 = nodeInnovation.getInnovation();
        int n24 = nodeInnovation.getInnovation();
        int n25 = nodeInnovation.getInnovation();
        int n26 = nodeInnovation.getInnovation();
        int n27 = nodeInnovation.getInnovation();
        
        //outputs
        int n28 = nodeInnovation.getInnovation();
        int n29 = nodeInnovation.getInnovation();
        int n30 = nodeInnovation.getInnovation();
        int n31 = nodeInnovation.getInnovation();
        int n32 = nodeInnovation.getInnovation();
        
        
        //number of input node is associated with the type of input       
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n1));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n2));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n3));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n4));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n5));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n6));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n7));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n8));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n9));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n10));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n11));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n12));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n13));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n14));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n15));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n16));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n17));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n18));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n19));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n20));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n21));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n22));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n23));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n24));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n25));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n26));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n27));  
        
        
        //number of output node is associated with the "buttons" that can be pressed
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n28));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n29));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n30));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n31));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n32));
        
        
        
        //create the base connection between one and one
        int c1 = connectionInnovation.getInnovation();
        //add these to the organism
        organism.addConnectionGene(new ConnectionGene(n1,n32,0.5f,true,c1));
       
        
        
        
        
        //create evaluator and pass in the popSize, the starting organism, and the counters for the two types of connections
        Evaluator eval = new Evaluator(organism, nodeInnovation, connectionInnovation){
            @Override
            
            protected float evaluateOrganism(Organism organism){
                
              
              float[] input = {1, 0, 1, 0,0,0,0,1,0,0,0,0,0,0,0,0,0,0,0, 0,0,0,0,0,0,0,0};
              
              NeuralNetwork net = new NeuralNetwork(organism);
               System.out.println("\nNew Organism");
              
                float output[]= net.calculate(input);
               System.out.println("\tOutput node 1= " + output[0] + "\n\tOutput node 2= " + output[1] + "\n\tOutput node 3= " + output[2]+ "\n\tOutput node 4= " + output[3]+ "\n\tOutput node 5= " + output[4]);
              
              
              return organism.getConnectionGenes().values().size();
                
            }
        };
        
        
        
        //run for 100 generations
        for(int i = 0; i < 100; i++){
            eval.evaluate();
            System.out.print("Generation: " + i);
            System.out.print("\tHighest fitness: " + eval.getHighestFitness());
            System.out.print("\tAmount of species: " + eval.getSpeciesAmount() + "\n");
            if (i % 99 == 0) {
                  //  OrganismPrinter printer = new OrganismPrinter();
                  //  printer.showOrganism(eval.getMostFitOrganism(), "" + i);
            }
            
        }
    }
}
