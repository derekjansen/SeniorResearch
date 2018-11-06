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
public interface Tuning {
    
    /* Constants for tuning */
	public final float C1 = 1.0f;
	public final float C2 = 1.0f;
	public final float C3 = 0.4f;
	public final float DT = 10.0f;
	public final float MUTATION_RATE = 0.8f;
	public final float ADD_CONNECTION_RATE = 0.5f;
	public final float ADD_NODE_RATE = 0.03f;
        
        public final int populationSize = 3;
        
}
