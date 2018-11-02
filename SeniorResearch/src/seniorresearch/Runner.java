
package seniorresearch;
import com.microsoft.msr.malmo.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import neat.ConnectionGene;
import neat.Counter;
import neat.Evaluator;
import neat.NeuralNetwork;
import neat.Organism;
import neat.NodeGene;
import org.json.*;
import static seniorresearch.XmlConversionMethods.createMissionString;

public class Runner implements XmlConversionMethods
{
    //hold node number and input/output actions
    static Map<Integer,String> outputButtonNames;
    
    static
    {        
        System.load("/Users/derekgrove/Desktop/Malmo/Java_Examples/libMalmoJava.jnilib"); 
    }
    
    public static void main(String argv[]) throws Exception
    {
        
        //////////////////SET UP NEAT CODE/////////////////////
        
        //associate nodes with output
        outputButtonNames = new HashMap();
        
        
        
        Counter nodeInnovation = new Counter();
        Counter connectionInnovation = new Counter();
        
        
        //THE BASE ORGANISM THAT ALL WILL EVOLVE FROM. AKA THE RIGHT INPUTS(OBSERVATIONS) AND OUTPUTS(MOVES)
        //make a new organism
        Organism organism = new Organism();
        
        //create nodeGenes
        int n1 = nodeInnovation.getInnovation();
        int n2 = nodeInnovation.getInnovation();
        int n3 = nodeInnovation.getInnovation();
        int n4 = nodeInnovation.getInnovation();
        int n5 = nodeInnovation.getInnovation();
        int n6 = nodeInnovation.getInnovation();
        int n7 = nodeInnovation.getInnovation();
        int n8 = nodeInnovation.getInnovation();
        int n9 = nodeInnovation.getInnovation();
        int n10 = nodeInnovation.getInnovation();
        int n11 = nodeInnovation.getInnovation();
        int n12 = nodeInnovation.getInnovation();
        
        //number of input node is associated with the type of input
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n1));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n2));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n3));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n4));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n5));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n6));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n7));

                
        //number of output node is associated with the "buttons" that can be pressed
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n8));
        outputButtonNames.put(n8,"move 1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n9));
        outputButtonNames.put(n9,"move -1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n10));
        outputButtonNames.put(n10,"turn 1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n11));
        outputButtonNames.put(n11,"turn -1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n12));
        outputButtonNames.put(n12,"attack 1");
        
        
        
        //create the base connection between one and one
        int c1 = connectionInnovation.getInnovation();
        //add these to the organism
        organism.addConnectionGene(new ConnectionGene(n1,n8,0.5f,true,c1));
        
        
         //create evaluator and pass in the starting organism, and the counters for the two types of connections
        Evaluator eval = new Evaluator(organism, nodeInnovation, connectionInnovation){
            @Override
            
            //THIS IS WHERE I CODE HOW TO EVALUATE THE Organism
            protected float evaluateOrganism(Organism organism){
 
                try {
                    
                    //run the organism that is passed in and return a fitness score
                    return runOrganism(organism);
                    
                    
                } catch (Exception ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return 0;
 
            }
        };
        
        
        
        
        //run for 10 generations and print out scores and such
        for(int i = 0; i < 10; i++){
            
            eval.evaluate();
            
            System.out.print("Generation: " + i);
            System.out.print("\tHighest fitness: " + eval.getHighestFitness());
            System.out.print("\tAmount of species: " + eval.getSpeciesAmount() + "\n");
        }         
        
    }
    
    
    
    
    
    static private float runOrganism(Organism organism) throws Exception{
        
        //create the neural network that will run by passing in the organism 
        NeuralNetwork network = new NeuralNetwork(organism);
        //observation values will be passed into here
        float input[];
        //output values will be passed into here
        float output[]; 
        
        float life = 0;
        float xPos = 0;
        float zPos = 0;
        float mobKilled = 0;
        float damageTaken = 0;
        float damageDealt = 0;
        float timeAlive = 0;
        float zombieXPos = 0;
        float zombieZPos = 0;
        
        
        ///////////////////////////////////// SET UP THE WOLRD AND THE MALMO AGENT /////////////////////////////////
        
        AgentHost agent_host = new AgentHost();
        
        String missionXmlString = createMissionString(0);
        MissionSpec my_mission = new MissionSpec(missionXmlString, true);
      
        MissionRecordSpec my_mission_record = new MissionRecordSpec("./saved_data.tgz");
        //my_mission_record.recordCommands();
        //my_mission_record.recordMP4(20, 400000);
        //my_mission_record.recordRewards();
        //my_mission_record.recordObservations();
        

        try {
            agent_host.startMission( my_mission, my_mission_record);
        }
        catch (MissionException e) {
            System.err.println( "Error starting mission: " + e.getMessage() );
            System.err.println( "Error code: " + e.getMissionErrorCode() );
            // We can use the code to do specific error handling, eg:
            if (e.getMissionErrorCode() == MissionException.MissionErrorCode.MISSION_INSUFFICIENT_CLIENTS_AVAILABLE)
            {
                // Caused by lack of available Minecraft clients.
                System.err.println( "Is there a Minecraft client running?");
            }
            System.exit(1);
        }

        WorldState world_state;

        System.out.print( "Waiting for the mission to start" );
        do {
            System.out.print( "." );
            try {
                Thread.sleep(100);
            } catch(InterruptedException ex) {
                System.err.println( "User interrupted while waiting for mission to start." );
                return 0;
            }
            world_state = agent_host.getWorldState();
            for( int i = 0; i < world_state.getErrors().size(); i++ )
                System.err.println( "Error: " + world_state.getErrors().get(i).getText() );
        } while( !world_state.getIsMissionRunning() );
        System.out.println( "" );

       
        
        //////////////////////////// MAIN LOOP ////////////////////////////////////////////////////////// 

        //Spawn zombie in the corner
        agent_host.sendCommand("chat /summon zombie -11 228 -11");
            
    do {

             //////////GET OBSERVATIONS/////////////
            
            if(world_state.getObservations().size() > 0){
                System.out.println(world_state.getObservations().get(0).getText());
               
                JSONObject root =  new JSONObject(world_state.getObservations().get(0).getText()); 
                life = root.getInt("Life");
                xPos = root.getInt("XPos");
                zPos = root.getInt("ZPos");
                mobKilled = root.getInt("MobsKilled");
                damageTaken = root.getInt("DamageTaken");
                damageDealt = root.getInt("DamageDealt");
                timeAlive = root.getInt("TimeAlive");
                
                //there are nearby things AKA figure out where the zombie is
                if(root.has("Entities")){
                    //get array
                    JSONArray theEntityArray = root.getJSONArray("Entities");
                    int i = theEntityArray.length() -1;
                   
                    while(i > -1){
                        JSONObject theEntity = theEntityArray.getJSONObject(i);
           
                        if(theEntity.getString("name").equalsIgnoreCase("Zombie")){
                            //the entity is the zombie here.
                            System.out.println("we found the zombie");
                            System.out.println(theEntity);
                            zombieXPos = (float)theEntity.getDouble("x");
                            zombieZPos = (float)theEntity.getDouble("z");
                            System.out.println("The zombies' lifepoints are: " + theEntity.getInt("life"));
                            System.out.println("The zombies coordinates are: " + theEntity.getDouble("x") + " , " + theEntity.getDouble("z"));
   
                        }
                        i--;
                    }
                }
                
                System.out.println("Life: " + life + ", timeAlive: " + timeAlive + ", XPos: " + xPos + ", ZPos: " + zPos + ", damageTaken: " + damageTaken + ", damageDealt: " + damageDealt + ", mobKilled: " + mobKilled + "\n"); 
               
            }
            
            
            ///////////////// plug in observations and send to neural net////////////////
            
            
            
            input = new float[]{xPos,zPos, zombieXPos, zombieZPos, damageTaken, damageDealt};           
            output = network.calculate(input);
            

            
            /////////////////////Take output and select the action//////////////////////
            
            
            
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                System.err.println( "User interrupted while mission was running." );
                return 0;
            }

            world_state = agent_host.getWorldState();

            for( int i = 0; i < world_state.getErrors().size(); i++ ) {
                TimestampedString error = world_state.getErrors().get(i);
                System.err.println( "Error: " + error.getText() );
            }
              
            
        } while(world_state.getIsMissionRunning() );

        System.out.println( "Mission has stopped." );
        
        //////////////////////////calculate score ///////////////////////////////
        
        return (float) ((1.0 * timeAlive) + (20.0 * damageDealt) + (50.0 * mobKilled) - (100.00 * damageTaken));
        
    }
       
}

