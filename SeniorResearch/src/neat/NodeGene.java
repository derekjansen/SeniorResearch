
package neat;

/**
 *
 * @author derekgrove
 */
public class NodeGene {
    
    enum TYPE{
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
    
    //GETTERS
    public TYPE getType(){
        return type;
    }
    
    public int getId(){
        return id;
    }
    
    
    
}
