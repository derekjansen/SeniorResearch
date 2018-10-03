
package neat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author derekgrove
 */
public interface Util {
    
    
    public static List<Integer> tempList1 = new ArrayList();
    public static List<Integer> tempList2 = new ArrayList();
    
    
    /**
     * Check how similar two genomes are to see if they belong in the same species
     * @param genome1
     * @param genome2
     * @param c1
     * @param c2
     * @param c3
     * @param N
     * @return 
     */
    public static float compatabilityDistance(Genome genome1, Genome genome2, float c1, float c2, float c3){
        int E = countExcessGenes(genome1,genome2);
        int D = countDisjointGenes(genome1,genome2);
        float W = averageWeightDiff(genome1,genome2);  
        
        return (c1*E)+(c2*D)+(c3*W);
    }
    
    
    
    
    /**
     * Count Matching Genes between two genomes
     * @param genome1
     * @param genome2
     * @return 
     */ 
    public static int countMatchingGenes(Genome genome1, Genome genome2){
        int matchingGenes = 0;
        
        //get keys from the nodeGenes which essentially show the order they were created
        List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet(), tempList1);
        List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet(), tempList2);
        
        //get the largest number of the NodeGenes
        int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
        int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
        int indices = Math.max(highestInnovation1, highestInnovation2);
        
        for(int i=0; i<indices ;i++){
            NodeGene node1 = genome1.getNodeGenes().get(i);
            NodeGene node2 = genome2.getNodeGenes().get(i);
            if(node1 != null && node2 != null){
                matchingGenes++;
            }
        }
        
        //get keys from the ConnectionGenes which essentially show the order they were created
        List<Integer> connectionKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tempList1);
        List<Integer> connectionKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tempList2);
        
        //get the largest number of the NodeGenes
        highestInnovation1 = connectionKeys1.get(connectionKeys1.size()-1);
        highestInnovation2 = connectionKeys2.get(connectionKeys2.size()-1);
        indices = Math.max(highestInnovation1, highestInnovation2);
        
        for(int i=0; i<indices ;i++){
            ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
            ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
            if(connection1 != null && connection2 != null){
                matchingGenes++;
            }
        }
        
        return matchingGenes;
    }
    
    
    
    
    /**
     * Count Disjoint Genes between two genomes
     * @param genome1
     * @param genome2
     * @return 
     */
    public static int countDisjointGenes(Genome genome1, Genome genome2){
        int disjointGenes = 0;
        
        //get keys from the nodeGenes which essentially show the order they were created
        List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet(), tempList1);
        List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet(), tempList2);
        
        //get the largest number of the NodeGenes
        int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
        int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
        int indices = Math.max(highestInnovation1, highestInnovation2);
        
        for(int i=0; i<indices ;i++){
            NodeGene node1 = genome1.getNodeGenes().get(i);
            NodeGene node2 = genome2.getNodeGenes().get(i);
            if(node1 != null && highestInnovation1 > i && node2 != null){
                disjointGenes++;
            }else if(node2 != null && highestInnovation2 > i && node1 != null){
                disjointGenes++;
            }
        }
        
        //get keys from the ConnectionGenes which essentially show the order they were created
        List<Integer> connectionKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tempList1);
        List<Integer> connectionKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tempList2);
        
        //get the largest number of the NodeGenes
        highestInnovation1 = connectionKeys1.get(connectionKeys1.size()-1);
        highestInnovation2 = connectionKeys2.get(connectionKeys2.size()-1);
        indices = Math.max(highestInnovation1, highestInnovation2);
        
        for(int i=0; i<indices ;i++){
            ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
            ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
            if(connection1 != null && highestInnovation1 > i && connection2 != null){
                disjointGenes++;
            }else if(connection2 != null && highestInnovation2 > i && connection1 != null){
                disjointGenes++;
            }
        }
        
        return disjointGenes;
    }
    
 
    
    /**
     * Count Excess Genes between two genomes
     * @param genome1
     * @param genome2
     * @return 
     */
    public static int countExcessGenes(Genome genome1, Genome genome2){
        int excessGenes = 0;
        
        //get keys from the nodeGenes which essentially show the order they were created
        List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet(), tempList1);
        List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet(), tempList2);
        
        //get the largest number of the NodeGenes
        int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
        int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
        int indices = Math.max(highestInnovation1, highestInnovation2);
        
        for(int i=0; i<indices ;i++){
            NodeGene node1 = genome1.getNodeGenes().get(i);
            NodeGene node2 = genome2.getNodeGenes().get(i);
            if(node1 == null && highestInnovation1 < i && node2 != null){
                excessGenes++;
            }else if(node2 == null && highestInnovation2 < i && node1 != null){
                excessGenes++;
            }
        }
        
        //get keys from the ConnectionGenes which essentially show the order they were created
        List<Integer> connectionKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tempList1);
        List<Integer> connectionKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tempList2);
        
        //get the largest number of the NodeGenes
        highestInnovation1 = connectionKeys1.get(connectionKeys1.size()-1);
        highestInnovation2 = connectionKeys2.get(connectionKeys2.size()-1);
        indices = Math.max(highestInnovation1, highestInnovation2);
        
        for(int i=0; i<indices ;i++){
            ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
            ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
            if(connection1 == null && highestInnovation1 < i && connection2 != null){
                excessGenes++;
            }else if(connection2 == null && highestInnovation2 < i && connection1 != null){
                excessGenes++;
            }
        }
        
        return excessGenes;
    }
    
    
    
    /**
     * Computes the average difference in connection weights between two genomes
     * @param genome1
     * @param genome2
     * @return 
     */
    public static float averageWeightDiff(Genome genome1, Genome genome2){
         float matchingGenes = 0f;
         float weightDifference = 0f;
        
        //get keys from the ConnectionGenes which essentially show the order they were created
        List<Integer> connectionKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tempList1);
        List<Integer> connectionKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tempList2);
        
        //get the largest number of the NodeGenes
        int highestInnovation1 = connectionKeys1.get(connectionKeys1.size()-1);
        int highestInnovation2 = connectionKeys2.get(connectionKeys2.size()-1);
        int indices = Math.max(highestInnovation1, highestInnovation2);
        
        for(int i=0; i<indices ;i++){
            ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
            ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
            if(connection1 != null && connection2 != null){
                matchingGenes++;
                weightDifference += Math.abs(connection1.getWeight()-connection2.getWeight());
            }
        }
        
        return weightDifference/matchingGenes;
    }
    
    
    

    /**
     * Takes a list and sorts it
     * @param <T>
     * @param c
     * @return 
     */
    public static List<Integer> asSortedList(Collection<Integer> c, List<Integer> list){
        list.clear();
        list.addAll(c);
        java.util.Collections.sort(list);
        return list;
    }
            
    
}
