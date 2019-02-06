/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author derekgrove
 */
public interface Utils {
    
    	public static List<Integer> tmpList1 = new ArrayList<Integer>();
	public static List<Integer> tmpList2 = new ArrayList<Integer>();
    
    
    public static float compatibilityDistance(Organism genome1, Organism genome2, float c1, float c2, float c3) {
		int excessGenes = countExcessGenes(genome1, genome2);
		int disjointGenes = countDisjointGenes(genome1, genome2);
		float avgWeightDiff = averageWeightDiff(genome1, genome2);
		
		return (excessGenes * c1)/100 + (disjointGenes * c2)/100 + avgWeightDiff * c3;
	}
	
	public static int countMatchingGenes(Organism genome1, Organism genome2) {
		int matchingGenes = 0;
		
		List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet(), tmpList1);
		List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet(), tmpList2);
		
		int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
		int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
		int indices = Math.max(highestInnovation1, highestInnovation2);
		
		for (int i = 0; i <= indices; i++) { 					// loop through genes -> i is innovation numbers
			NodeGene node1 = genome1.getNodeGenes().get(i);
			NodeGene node2 = genome2.getNodeGenes().get(i);
			if (node1 != null && node2 != null) { 
				// both genomes has the gene w/ this innovation number
				matchingGenes++;
			}
		}
		
		List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tmpList1);
		List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tmpList2);
		
		highestInnovation1 = conKeys1.get(conKeys1.size()-1);
		highestInnovation2 = conKeys2.get(conKeys2.size()-1);
		
		indices = Math.max(highestInnovation1, highestInnovation2);
		for (int i = 0; i <= indices; i++) { 					// loop through genes -> i is innovation numbers
			ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
			ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
			if (connection1 != null && connection2 != null) { 
				// both genomes has the gene w/ this innovation number
				matchingGenes++;
			}
		}
		
		return matchingGenes;
	}
	
	public static int countDisjointGenes(Organism genome1, Organism genome2) {
		int disjointGenes = 0;
		
		List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet(), tmpList1);
		List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet(), tmpList2);
		
		int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
		int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
		int indices = Math.max(highestInnovation1, highestInnovation2);
		
		for (int i = 0; i <= indices; i++) {
			NodeGene node1 = genome1.getNodeGenes().get(i);
			NodeGene node2 = genome2.getNodeGenes().get(i);
			if (node1 == null && highestInnovation1 > i && node2 != null) {
				// genome 1 lacks gene, genome 2 has gene, genome 1 has more genes w/ higher innovation numbers
				disjointGenes++;
			} else if (node2 == null && highestInnovation2 > i && node1 != null) {
				disjointGenes++;
			}
		}
		
		List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tmpList1);
		List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tmpList2);
		
		highestInnovation1 = conKeys1.get(conKeys1.size()-1);
		highestInnovation2 = conKeys2.get(conKeys2.size()-1);
		
		indices = Math.max(highestInnovation1, highestInnovation2);
		for (int i = 0; i <= indices; i++) {
			ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
			ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
			if (connection1 == null && highestInnovation1 > i && connection2 != null) {
				disjointGenes++;
			} else if (connection2 == null && highestInnovation2 > i && connection1 != null) {
				disjointGenes++;
			}
		}
		
		return disjointGenes;
	}
	
        
        //got a null somehow here
	public static int countExcessGenes(Organism genome1, Organism genome2) {
		int excessGenes = 0;
		
		List<Integer> nodeKeys1 = asSortedList(genome1.getNodeGenes().keySet(), tmpList1);
		List<Integer> nodeKeys2 = asSortedList(genome2.getNodeGenes().keySet(), tmpList2);
		
		int highestInnovation1 = nodeKeys1.get(nodeKeys1.size()-1);
		int highestInnovation2 = nodeKeys2.get(nodeKeys2.size()-1);
		int indices = Math.max(highestInnovation1, highestInnovation2);
		
		for (int i = 0; i <= indices; i++) {
			NodeGene node1 = genome1.getNodeGenes().get(i);
			NodeGene node2 = genome2.getNodeGenes().get(i);
			if (node1 == null && highestInnovation1 < i && node2 != null) {
				excessGenes++;
			} else if (node2 == null && highestInnovation2 < i && node1 != null) {
				excessGenes++;
			}
		}
		
		List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tmpList1);
		List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tmpList2);
		
		highestInnovation1 = conKeys1.get(conKeys1.size()-1);
		highestInnovation2 = conKeys2.get(conKeys2.size()-1);
		
		indices = Math.max(highestInnovation1, highestInnovation2);
		for (int i = 0; i <= indices; i++) {
			ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
			ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
			if (connection1 == null && highestInnovation1 < i && connection2 != null) {
				excessGenes++;
			} else if (connection2 == null && highestInnovation2 < i && connection1 != null) {
				excessGenes++;
			}
		}
		
		return excessGenes;
	}
	
	public static float averageWeightDiff(Organism genome1, Organism genome2) {
		int matchingGenes = 0;
		float weightDifference = 0;
		
		List<Integer> conKeys1 = asSortedList(genome1.getConnectionGenes().keySet(), tmpList1);
		List<Integer> conKeys2 = asSortedList(genome2.getConnectionGenes().keySet(), tmpList2);
		
		int highestInnovation1 = conKeys1.get(conKeys1.size()-1);
		int highestInnovation2 = conKeys2.get(conKeys2.size()-1);
		
		int indices = Math.max(highestInnovation1, highestInnovation2);
		for (int i = 0; i <= indices; i++) { 					// loop through genes -> i is innovation numbers
			ConnectionGene connection1 = genome1.getConnectionGenes().get(i);
			ConnectionGene connection2 = genome2.getConnectionGenes().get(i);
			if (connection1 != null && connection2 != null) { 
				// both genomes has the gene w/ this innovation number
				matchingGenes++;
				weightDifference += Math.abs(connection1.getWeight()-connection2.getWeight());
			}
		}
		
		return weightDifference/matchingGenes;
	}
	
	/**
	 * Note: Will sort in ascending order
	 */
	public static List<Integer> asSortedList(Collection<Integer> c, List<Integer> list) {
	  list.clear();
	  list.addAll(c);
	  java.util.Collections.sort(list);
	  return list;
	}
    
}
