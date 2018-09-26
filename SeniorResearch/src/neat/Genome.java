
package neat;

import java.util.List;
import java.util.Random;
import neat.NodeGene.TYPE;

/**
 * The genome contains a list of both node genes and connection genes
 * @author derekgrove
 */
public class Genome {
    
    private List<ConnectionGene> connections;
    private List<NodeGene> nodes;
    
    //constructor
    public Genome(){
        super();
    }
    
    
    /**
     * ADD CONNECTION GENE
     * @param r 
     */
    public void addConnectionMutation(Random r){
        //pick two random nodes 
        NodeGene node1 = nodes.get(r.nextInt(nodes.size()));
        NodeGene node2 = nodes.get(r.nextInt(nodes.size()));
        float weight = r.nextFloat()*2f-1f;
        
        //has to do with network ordering of the nodes
        boolean reversed = false;
        if(node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT){
            reversed = true; //hidden->input doesnt work so reverse
        }else if(node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN){
            reversed = true; //output->hidden doesnt work so reverse
        }else if(node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT){
            reversed = true; //output->input doesnt work so reverse
        }
        
        
        //checks to make sure the connection isnt already made
        boolean connectionExists = false;
        for(ConnectionGene con: connections){
            if(con.getInNode() == node1.getId() && con.getOutNode() == node2.getId()){
                //checks node1/in and node2/out is not a valid combo already
                connectionExists = true;
                break;
            } else if(con.getInNode() == node2.getId() && con.getOutNode() == node1.getId()){
                //checks node2/in and node1/out is not a valid combo already
                connectionExists = true;
                break;
            }
        }
        
        if(connectionExists){
            return;
        }
        
        //inNode and outNode depending on reversed
        //weight THIS MIGHT NEED CHANGED
        //expressed = true because new gene 
        //innovation# IDK ABOUT THIS
        connections.add(new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight, true, 0)); 
        
    }
    
    
    /**
     * ADD NODE GENE
     * @param r 
     */
    public void addNodeMutation(Random r){
        ConnectionGene con = connections.get(r.nextInt(connections.size()));
        
        NodeGene inNode = nodes.get(con.getInNode());
        NodeGene outNode = nodes.get(con.getOutNode());
        
        //disable the old connection
        con.disable();
        
        
        NodeGene newNode = new NodeGene(TYPE.HIDDEN,nodes.size());
        ConnectionGene newIn = new ConnectionGene(inNode.getId(),newNode.getId(),1f,true,0);
        ConnectionGene newOut = new ConnectionGene(newNode.getId(),outNode.getId(),con.getWeight(),true,0);
        
        nodes.add(newNode);
        connections.add(newIn);
        connections.add(newOut);
    }
    
    
    
}
