
package neat;

/**
 * @author derekgrove
 */
public class ConnectionGene {
    

    private int inNode;
    private int outNode;
    private float weight;
    private boolean expressed;
    private int innovationNumber;
    
    
    //constructor
    public ConnectionGene(int inNode, int outNode, float weight, boolean expressed, int innovationNumber){
        super();
        this.inNode = inNode;
        this.outNode = outNode;
        this.weight = weight;
        this.expressed = expressed;
        this.innovationNumber = innovationNumber;
    }
    
    //copy constructor
    public ConnectionGene(ConnectionGene cg){
        inNode = cg.inNode;
        outNode = cg.outNode;
        weight = cg.weight;
        expressed = cg.expressed;
        innovationNumber = cg.innovationNumber;
    }
    
    
    //GETTERS
    public int getInNode(){
        return inNode;
    }
    
    public int getOutNode(){
        return outNode;
    }
    
    public float getWeight(){
        return weight;
    }
    
    public boolean isExpressed(){
        return expressed;
    }
    
    public int getInnovationNumber(){
        return innovationNumber;
    }
    
    public void setWeight(Float f){
        this.weight = f;
    }
    
    public void enable(){
        this.expressed = true;
    }
    
    public void disable(){
        this.expressed = false;
    }
    
    /**
     * returns a copy of the ConnectionGene
     * @return 
     */
    public ConnectionGene copy(){
        return new ConnectionGene(inNode,outNode,weight,expressed,innovationNumber);
    }  
}
