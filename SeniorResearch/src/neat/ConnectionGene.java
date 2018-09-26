
package neat;

/**
 * @author derekgrove
 */
public class ConnectionGene {
    
    //idk about using ints here but we will roll with it for now
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
    
    
    
    public void enable(){
        this.expressed = true;
    }
    
    public void disable(){
        this.expressed = false;
    }
    
    
}
