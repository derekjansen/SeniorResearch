
package neat;

/**
 *
 * @author derekgrove
 */
public class NodeGene {
    
    public enum TYPE{
        INPUT,
        HIDDEN,
        OUTPUT;
    }
    
    private TYPE type;
    private int id;
    
    
    //constructor
    public NodeGene(TYPE type, int id){
        super();
        this.type = type;
        this.id = id;
    }
    
    //copy constructor
    public NodeGene(NodeGene ng){
        id = ng.id;
        type = ng.type;
    }
    
    //GETTERS
    public TYPE getType(){
        return type;
    }
    
    public int getId(){
        return id;
    }
    
    /**
     * returns a copy of the NodeGene
     * @return 
     */
    public NodeGene copy(){
        return new NodeGene(type,id);
    }
    
}
