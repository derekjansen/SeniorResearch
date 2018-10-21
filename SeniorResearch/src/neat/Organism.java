package neat;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import neat.NodeGene.TYPE;

public class Organism implements Utils{
	
	
	private final float PROBABILITY_PERTURBING = 0.9f; // rest is probability of assigning new weight
	
	private Map<Integer, ConnectionGene> connections;
	private Map<Integer, NodeGene> nodes;
	
	public Organism() {
		nodes = new HashMap<Integer, NodeGene>();
		connections = new HashMap<Integer, ConnectionGene>();
	}
	
	public Organism(Organism toBeCopied) {
		nodes = new HashMap<Integer, NodeGene>();
		connections = new HashMap<Integer, ConnectionGene>();
		
		for (Integer index : toBeCopied.getNodeGenes().keySet()) {
			nodes.put(index, new NodeGene(toBeCopied.getNodeGenes().get(index)));
		}
		
		for (Integer index : toBeCopied.getConnectionGenes().keySet()) {
			connections.put(index, new ConnectionGene(toBeCopied.getConnectionGenes().get(index)));
		}
	}
	
	public void addNodeGene(NodeGene gene) {
		nodes.put(gene.getId(), gene);
	}
	
	public void addConnectionGene(ConnectionGene gene) {
		connections.put(gene.getInnovation(), gene);
	}
	
	public Map<Integer, ConnectionGene> getConnectionGenes() {
		return connections;
	}
	
	public Map<Integer, NodeGene> getNodeGenes() {
		return nodes;
	}
	
	public void mutation(Random r) {
		for(ConnectionGene con : connections.values()) {
			if (r.nextFloat() < PROBABILITY_PERTURBING) { 			// uniformly perturbing weights
				con.setWeight(con.getWeight()*(r.nextFloat()*4f-2f));
			} else { 												// assigning new weight
				con.setWeight(r.nextFloat()*4f-2f);
			}
		}
	}
	
	public void addConnectionMutation(Random r, Counter innovation, int maxAttempts) {
		int tries = 0;
		boolean success = false;
		while (tries < maxAttempts && success == false) {
			tries++;
			
			Integer[] nodeInnovationNumbers = new Integer[nodes.keySet().size()];
			nodes.keySet().toArray(nodeInnovationNumbers);
			Integer keyNode1 = nodeInnovationNumbers[r.nextInt(nodeInnovationNumbers.length)];
			Integer keyNode2 = nodeInnovationNumbers[r.nextInt(nodeInnovationNumbers.length)];
			
			NodeGene node1 = nodes.get(keyNode1);
			NodeGene node2 = nodes.get(keyNode2);
			float weight = r.nextFloat()*2f-1f;
			
			boolean reversed = false;
			if (node1.getType() == NodeGene.TYPE.HIDDEN && node2.getType() == NodeGene.TYPE.INPUT) {
				reversed = true;
			} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.HIDDEN) {
				reversed = true;
			} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.INPUT) {
				reversed = true;
			}
			
			boolean connectionImpossible = false;
			if (node1.getType() == NodeGene.TYPE.INPUT && node2.getType() == NodeGene.TYPE.INPUT) {
				connectionImpossible = true;
			} else if (node1.getType() == NodeGene.TYPE.OUTPUT && node2.getType() == NodeGene.TYPE.OUTPUT) {
				connectionImpossible = true;
			}
			
			boolean connectionExists = false;
			for (ConnectionGene con : connections.values()) {
				if (con.getInNode() == node1.getId() && con.getOutNode() == node2.getId()) { // existing connection
					connectionExists = true;
					break;
				} else if (con.getInNode() == node2.getId() && con.getOutNode() == node1.getId()) { // existing connection
					connectionExists = true;
					break;
				}
			}
			
			if (connectionExists || connectionImpossible) {
				continue;
			}
			
			ConnectionGene newCon = new ConnectionGene(reversed ? node2.getId() : node1.getId(), reversed ? node1.getId() : node2.getId(), weight, true, innovation.getInnovation());
			connections.put(newCon.getInnovation(), newCon);
			success = true;
		}
		if (success == false) {
			System.out.println("Tried, but could not add more connections");
		}
	}
	
	public void addNodeMutation(Random r, Counter connectionInnovation, Counter nodeInnovation) {
		ConnectionGene con = (ConnectionGene) connections.values().toArray()[r.nextInt(connections.size())];
		
		NodeGene inNode = nodes.get(con.getInNode());
		NodeGene outNode = nodes.get(con.getOutNode());
		
		con.disable();
		
		NodeGene newNode = new NodeGene(TYPE.HIDDEN, nodeInnovation.getInnovation());
		ConnectionGene inToNew = new ConnectionGene(inNode.getId(), newNode.getId(), 1f, true, connectionInnovation.getInnovation());
		ConnectionGene newToOut = new ConnectionGene(newNode.getId(), outNode.getId(), con.getWeight(), true, connectionInnovation.getInnovation());
		
		nodes.put(newNode.getId(), newNode);
		connections.put(inToNew.getInnovation(), inToNew);
		connections.put(newToOut.getInnovation(), newToOut);
	}
	
	/**
	 * @param parent1	More fit parent
	 * @param parent2	Less fit parent
	 */
	public static Organism crossover(Organism parent1, Organism parent2, Random r) {
		Organism child = new Organism();
		
		for (NodeGene parent1Node : parent1.getNodeGenes().values()) {
			child.addNodeGene(new NodeGene(parent1Node));
		}
		
		for (ConnectionGene parent1Node : parent1.getConnectionGenes().values()) {
			if (parent2.getConnectionGenes().containsKey(parent1Node.getInnovation())) { // matching gene
				ConnectionGene childConGene = r.nextBoolean() ? new ConnectionGene(parent1Node) : new ConnectionGene(parent2.getConnectionGenes().get(parent1Node.getInnovation()));
				child.addConnectionGene(childConGene);
			} else { // disjoint or excess gene
				ConnectionGene childConGene = new ConnectionGene(parent1Node);
				child.addConnectionGene(childConGene);
			}
		}
		
		return child;
	}
	
	
}