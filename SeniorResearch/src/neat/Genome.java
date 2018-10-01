
package neat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import neat.NodeGene.TYPE;

/**
 * The genome contains a list of both node genes and connection genes
 * @author derekgrove
 */
public class Genome implements Util{
    
    private final float PERTURBED_CHANCE = 0.9f;
    private Map<Integer,ConnectionGene> connections;
    private Map<Integer,NodeGene> nodes;
    
    
    //constructor
    public Genome(){
        super();
        nodes = new HashMap();
        connections = new HashMap();
    }
    
    //Copy constructor
    public Genome(Genome copy){
        super();
        nodes = new HashMap<Integer,NodeGene>();
        connections = new HashMap<Integer,ConnectionGene>();
        
        for(Integer index: copy.getNodeGenes().keySet()){
            nodes.put(index, new NodeGene(copy.getNodeGenes().get(index)));
        }
        
        for(Integer index: copy.getConnectionGenes().keySet()){
            connections.put(index, new ConnectionGene(copy.getConnectionGenes().get(index)));
        }
        
    }
    
    
    
    
    /**
     * Adds a ConnectionGene into the Genome's connection list
     * @param connectionToAdd 
     */
    public void addConnectionGene(ConnectionGene connectionToAdd){
        connections.put(connectionToAdd.getInnovationNumber(),connectionToAdd);
    }
    
    /**
     * Adds a NodeGene to the Genomes node list, placed in the index based on id
     * @param nodeToAdd 
     */
    public void addNodeGene(NodeGene nodeToAdd){
        nodes.put(nodeToAdd.getId(), nodeToAdd);
    }
    
    /**
     * Retrieve the Genome's list of NodeGenes
     * @return 
     */
    public Map<Integer,NodeGene> getNodeGenes(){
        return nodes;
    }
    
    /**
     * Retrieve the Genome's list of ConnectionGenes
     * @return 
     */
    public Map<Integer,ConnectionGene> getConnectionGenes(){
        return connections;
    }
    
    
    ////////////////////MUTATIONS///////////////////////
    
    
    /**
     * ADD ConnectionGene Mutation
     * @param r 
     * @param innovation 
     */
    public void addConnectionMutation(Random r, InnovationGenerator innovation, int maxAttempts) {
        int tries = 0;
        boolean success = false;

        while (tries < maxAttempts && success == false) {
            tries++;
            //pick two random nodes 
            NodeGene node1 = nodes.get(r.nextInt(nodes.size()));
            NodeGene node2 = nodes.get(r.nextInt(nodes.size()));
            float weight = r.nextFloat() * 2f - 1f;

            //has to do with network ordering of the nodes
            boolean reversed = false;
            if (node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT) {
                reversed = true; //hidden->input doesnt work so reverse
            } else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN) {
                reversed = true; //output->hidden doesnt work so reverse
            } else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT) {
                reversed = true; //output->input doesnt work so reverse
            }

            
            boolean connectionImpossible = false;
            if(node1.getType() == NodeGene.TYPE.INPUT && node2.getType() == NodeGene.TYPE.INPUT){
                connectionImpossible = true;
            }else if(node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.OUTPUT){
                connectionImpossible = true;
            }
            
            
            
            //checks to make sure the connection isnt already made
            boolean connectionExists = false;
            for (ConnectionGene con : connections.values()) {
                if (con.getInNode() == node1.getId() && con.getOutNode() == node2.getId()) {
                    //checks node1/in and node2/out is not a valid combo already
                    connectionExists = true;
                    break;
                } else if (con.getInNode() == node2.getId() && con.getOutNode() == node1.getId()) {
                    //checks node2/in and node1/out is not a valid combo already
                    connectionExists = true;
                    break;
                }
            }

            if (connectionExists || connectionImpossible) {
                continue;
            }

            //inNode and outNode depending on reversed
            //weight THIS MIGHT NEED CHANGED
            //expressed = true because new gene 
            //innovation# 
            ConnectionGene newCon = new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight, true, innovation.assignNewInnovation());
            connections.put(newCon.getInnovationNumber(), newCon);
            success = true;
        }

        if (success == false) {
            System.out.println("Tried, but could not add more connections");
        }

    }
    
    
    /**
     * Add NodeGene Mutation
     * @param r 
     * @param innovation 
     */
    public void addNodeMutation(Random r, InnovationGenerator connectionInnovation, InnovationGenerator nodeInnovation){
        //grab a ranom connection gene
        ConnectionGene con = connections.get(r.nextInt(connections.size()));
        
        NodeGene inNode = nodes.get(con.getInNode());
        NodeGene outNode = nodes.get(con.getOutNode());
        
        //disable the old connection
        con.disable();
        
        
        NodeGene newNode = new NodeGene(TYPE.HIDDEN, nodeInnovation.assignNewInnovation());
        ConnectionGene newIn = new ConnectionGene(inNode.getId(),newNode.getId(),1f,true, connectionInnovation.assignNewInnovation());
        ConnectionGene newOut = new ConnectionGene(newNode.getId(),outNode.getId(),con.getWeight(),true, connectionInnovation.assignNewInnovation());
        
        nodes.put(newNode.getId(),newNode);
        connections.put(newIn.getInnovationNumber(),newIn);
        connections.put(newOut.getInnovationNumber(),newOut);
    }
    
    /**
     * Genome crossover 
     * *** ASSUMES PARENT ONE IS MORE FIT
     * @param parent1
     * @param parent2
     * @param r
     * @return 
     */
    public static Genome crossover(Genome parent1, Genome parent2, Random r)
    {
        
        Genome child = new Genome();
        
        //copies the parent 1 NodeGenes into the new child ASSUMING Parent1 IS MORE FIT
        for(NodeGene p1Node: parent1.getNodeGenes().values()){
            child.addNodeGene(p1Node.copy());
        
        }
        
        //checks for matching connection genes between parents
        for(ConnectionGene p1Connect: parent1.getConnectionGenes().values()){
            if(parent2.getConnectionGenes().containsKey(p1Connect.getInnovationNumber())){ //matching
               
                //Clever line... if the random boolean is true, copy the parent 1 gene, else copy parent 2
                ConnectionGene childConnection = r.nextBoolean() ? p1Connect.copy() : parent2.getConnectionGenes().get(p1Connect.getInnovationNumber()).copy();
                child.addConnectionGene(childConnection);
                
            }else{ //disjoint or excess
                //copy all from fit parent
                child.addConnectionGene(p1Connect.copy());
            }
        }
        return child;
    }
    
    
    
    /**
     * **SHOULD BE AN 80% CHANCE OF THIS**
     * Alters the weight of every ConnectionGene in the Genome
     * -Chance of weight being uniformly altered: 90%
     * -Chance of brand new weight: 10%
     * @param r 
     */
    public void changeWeightMutation(Random r){
        for(ConnectionGene con: connections.values()){
            if(r.nextFloat() < PERTURBED_CHANCE){
                con.setWeight(con.getWeight() * (r.nextFloat()*4f-2f));
            }else{
                con.setWeight(r.nextFloat()*4f-2f);
            }
        }
    }
    
    
}