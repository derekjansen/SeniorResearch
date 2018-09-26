/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
