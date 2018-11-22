
package seniorresearch;
import com.microsoft.msr.malmo.*;
import java.util.Arrays;
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
import test.OrganismPrinter;

public class Runner implements XmlConversionMethods,FitnessTune
{
    
    //hold node number and input/output actions
    static Map<Integer,String> outputButtonNames;
    static float oldMobKilled = 0;
    static double oldDamageTaken = 0;
    static double oldDamageDealt = 0;
    static float oldTimeAlive = 0;
    
    static
    {        
        System.load("/Users/derekgrove/Desktop/Malmo/Java_Examples/libMalmoJava.jnilib"); 
    }
    
    public static void main(String argv[]) throws Exception
    {
///////////////////////////SET UP NEAT CODE/////////////////////
        
        //associate nodes with output
        outputButtonNames = new HashMap();
        
        
        
        Counter nodeInnovation = new Counter();
        Counter connectionInnovation = new Counter();
        
        
        //THE BASE ORGANISM THAT ALL WILL EVOLVE FROM. AKA THE RIGHT INPUTS(OBSERVATIONS) AND OUTPUTS(MOVES)
        //make a new organism
        Organism organism = new Organism();
        
        //bias
        int n1 = nodeInnovation.getInnovation();
        //damage taken
        int n2 = nodeInnovation.getInnovation();
        //damage dealt
        int n3 = nodeInnovation.getInnovation();
        
        //zombie location
        int n4 = nodeInnovation.getInnovation();
        int n5 = nodeInnovation.getInnovation();
        int n6 = nodeInnovation.getInnovation();
        int n7 = nodeInnovation.getInnovation();
        int n8 = nodeInnovation.getInnovation();
        int n9 = nodeInnovation.getInnovation();
        int n10 = nodeInnovation.getInnovation();
        int n11 = nodeInnovation.getInnovation();
        int n12 = nodeInnovation.getInnovation();
        int n13 = nodeInnovation.getInnovation();
        int n14 = nodeInnovation.getInnovation();
        int n15 = nodeInnovation.getInnovation();
        int n16 = nodeInnovation.getInnovation();
        int n17 = nodeInnovation.getInnovation();
        int n18 = nodeInnovation.getInnovation();
        int n19 = nodeInnovation.getInnovation();
        
        //player looking direction
        int n20 = nodeInnovation.getInnovation();
        int n21 = nodeInnovation.getInnovation();
        int n22 = nodeInnovation.getInnovation();
        int n23 = nodeInnovation.getInnovation();
        int n24 = nodeInnovation.getInnovation();
        int n25 = nodeInnovation.getInnovation();
        int n26 = nodeInnovation.getInnovation();
        int n27 = nodeInnovation.getInnovation();
        
        //outputs
        int n28 = nodeInnovation.getInnovation();
        int n29 = nodeInnovation.getInnovation();
        int n30 = nodeInnovation.getInnovation();
        int n31 = nodeInnovation.getInnovation();
        int n32 = nodeInnovation.getInnovation();
        
        
        //number of input node is associated with the type of input       
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n1));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n2));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n3));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n4));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n5));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n6));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n7));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n8));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n9));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n10));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n11));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n12));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n13));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n14));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n15));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n16));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n17));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n18));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n19));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n20));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n21));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n22));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n23));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n24));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n25));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n26));
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n27));  
        
        
        //number of output node is associated with the "buttons" that can be pressed
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n28));
        outputButtonNames.put(n28,"move 1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n29));
        outputButtonNames.put(n29,"move -1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n30));
        outputButtonNames.put(n30,"turn 1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n31));
        outputButtonNames.put(n31,"turn -1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n32));
        outputButtonNames.put(n32,"attack 1");
        
       
        
        
        
        //create the base bias connection between one and one
        int c1 = connectionInnovation.getInnovation();
        //add these to the organism
        organism.addConnectionGene(new ConnectionGene(n1,n32,0.5f,true,c1));
        
        
        //create evaluator and pass in the starting organism, and the counters for the two types of connections
        Evaluator eval = new Evaluator(organism, nodeInnovation, connectionInnovation){
            @Override
            
            protected float evaluateOrganism(Organism organism){
 
                try{
                    System.out.println("Run Next Organism");
                    //run the organism that is passed in and return a fitness score
                    float score = runOrganism(organism);
                    
                    System.out.println("The organism's score is: " + score);
                    
                    //if score is negative, we give it a very low score so that it is > 0
                    if(score < 0){
                        score = 0.01f;
                    }
                    
                    return score;
                       
                } catch (Exception ex) {
                    Logger.getLogger(Runner.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return 0;
 
            }
        };
        
        
        
        
        //run for 10 generations and print out scores and such
        for(int i = 0; i < 25; i++){
            
            eval.evaluate();
            
            System.out.print("Generation: " + i);
            System.out.print("\tHighest fitness: " + eval.getHighestFitness());
            System.out.print("\tAmount of species: " + eval.getSpeciesAmount() + "\n");
            
            if (i % 5 == 0) {
                    OrganismPrinter printer = new OrganismPrinter();
                    printer.showOrganism(eval.getMostFitOrganism(), "" + i);
            }
            
        }  
        
    }
    
    
    
    
    
    static private float runOrganism(Organism organism) throws Exception{
        
        //create the neural network that will run by passing in the organism 
        NeuralNetwork network = new NeuralNetwork(organism);
        
        //observation values will be passed into here
        float input[];
        //output values will be passed into here
        float output[]; 
        
        //other needed variables 
        double life = 0;
        double xPos = 0;
        double zPos = 0;
        float mobKilled = 0;
        double damageTaken = 0;
        double damageDealt = 0;
        float timeAlive = 0;
        float zombieXPos = -1;
        float zombieZPos = -1;
        double playerYaw = 0;
        float instantDamageTaken = 0;
        float oldInstantDamageTaken = 0;
        float instantDamageDealt = 0;
        float oldInstantDamageDealt = 0;
        
/////////////////////////////////////////// SET UP THE WOLRD AND THE MALMO AGENT /////////////////////////////////
        
        AgentHost agent_host = new AgentHost();
        
        String missionXmlString = createMissionString(0);
        MissionSpec my_mission = new MissionSpec(missionXmlString, true);
      
        MissionRecordSpec my_mission_record = new MissionRecordSpec("./saved_data.tgz");

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

       
        
///////////////////////////////////// MAIN LOOP ////////////////////////////////////////////////////////// 
        
        
        
        
        //pause on daylight for a bit
        try {
                Thread.sleep(20000);
            } catch(InterruptedException ex) {
                System.err.println( "User interrupted while mission was running." );
                return 0;
            }
        agent_host.sendCommand("chat /tp 4 228 4");
        //turn to night
        agent_host.sendCommand("chat /time set 15000");
        //Spawn zombie in the corner
        agent_host.sendCommand("chat /summon zombie 11 228 11 {IsBaby:0}");
        
        
        //SOME Vars
        
        //if a zombie was seen
        boolean zombieTrigger;
        float bias = 1.0f;
        //zombie positions
        float p10,p11,p12,p13,p14,p15,p16,p17,p20,p21,p22,p23,p24,p25,p26,p27;
        //yaw values;
        float y10,y11,y12,y13,y14,y15,y16,y17;
        
        
        
    do {
        
        //reset to not seen each time
        zombieTrigger = false;
        //used for damage node
        oldInstantDamageTaken = instantDamageTaken;
        instantDamageTaken = 0;
        oldInstantDamageDealt = instantDamageDealt;
        instantDamageDealt = 0;
///////////////////////////GET OBSERVATIONS/////////////////////////////////
            
            if(world_state.getObservations().size() > 0){
                System.out.println(world_state.getObservations().get(0).getText());
               try{
                   
                JSONObject root =  new JSONObject(world_state.getObservations().get(0).getText()); 
                life = root.getDouble("Life");
                xPos = root.getDouble("XPos");
                zPos = root.getDouble("ZPos");
                playerYaw = root.getDouble("Yaw");
                mobKilled = root.getInt("MobsKilled");
                damageTaken = root.getInt("DamageTaken");
                damageDealt = root.getInt("DamageDealt");
                instantDamageTaken = root.getInt("DamageTaken");
                instantDamageDealt = root.getInt("DamageDealt");
                //protects against dying
                if(root.getInt("TimeAlive") != 0){
                    timeAlive = root.getInt("TimeAlive");
                }
                
                //there are nearby things AKA figure out where the zombie is
                if(root.has("Entities")){
                    //get array
                    JSONArray theEntityArray = root.getJSONArray("Entities");
                    int i = theEntityArray.length() -1;
                   
                    while(i > -1){
                        JSONObject theEntity = theEntityArray.getJSONObject(i);
           
                        if(theEntity.getString("name").equalsIgnoreCase("Zombie")){
                            //zombie was found
                            zombieTrigger = true;
                            //the entity is the zombie here.
                        //    System.out.println("zombie found");
                        //    System.out.println(theEntity);
                            zombieXPos = (float)theEntity.getDouble("x");
                            zombieZPos = (float)theEntity.getDouble("z");
                        //    System.out.println("The zombies' lifepoints are: " + theEntity.getInt("life"));
                        //    System.out.println("The zombies coordinates are: " + theEntity.getDouble("x") + " , " + theEntity.getDouble("z"));
   
                        }
                        
                        i--;
                    }
                    
                }
                
                //System.out.println("Life: " + life + ", timeAlive: " + timeAlive + ", XPos: " + xPos + ", ZPos: " + zPos + ", damageTaken: " + damageTaken + ", damageDealt: " + damageDealt + ", mobKilled: " + mobKilled + "\n"); 
               }catch(Exception ex){
                   
               }
                   
            }
            
            
            
            
            
///////////////////////////plug in observations and send to neural net////////////////
            
            //SCALE DAMAGE THINGS TO 0-1
            float damageTakenNode = 0f;
            float damageDealtNode = 0f;
            if(instantDamageTaken > oldInstantDamageTaken && oldInstantDamageTaken != 0){
                //System.out.println("Player was hit");
                damageTakenNode = 1;
            }
            if(instantDamageDealt > oldInstantDamageDealt && oldInstantDamageDealt != 0){
                //System.out.println("Player was hit");
                damageDealtNode = 1;
            }
            

            
            float directionOfZombie = -1;
            float distanceToZombie = -1;
            p10=p11=p12=p13=p14=p15=p16=p17=p20=p21=p22=p23=p24=p25=p26=p27=y10=y11=y12=y13=y14=y15=y16=y17= 0;
            int sector,yawSector = 0;
           
            //if a zombie was found, figure out direction and distance
            if(zombieTrigger){
                
                //Calculate Distance of Zombie
                distanceToZombie = (float)Math.hypot((zombieXPos-xPos), (zombieZPos-zPos));
                //CALCULATE DIRECTION OF ZOMBIE
                directionOfZombie = (float)Math.toDegrees(Math.atan2((zombieZPos-zPos),(zombieXPos-xPos)));
                //determine which section around the player is active
                sector = (int)directionOfZombie/45;                
                yawSector = (int)playerYaw/45;
                
                
////////////////////////////////////////DETERMINE NODES TO LIGHT UP////////////////////
                
                //IF THE ZOMBIE IS WITHIN 1.5 blocks
                if(distanceToZombie <= 2.0){
                   
                    switch(sector){
                        case 0: p10 = 1;
                            break;
                        case 1: p11 = 1;
                            break;
                        case 2: p12 = 1;
                            break;
                        case 3: p13 = 1;
                            break;
                        case 4: p14 = 1;
                            break;
                        case 5: p15 = 1;
                            break;
                        case 6: p16 = 1;
                            break;
                        case 7: p17 = 1;
                            break;
                        default:
                            break;
                    }
                 
                }else{ //ZOMBIE IS OUTSIDE 1.5 blocks
                    switch(sector){
                        case 0: p20 = 1;
                            break;
                        case 1: p21 = 1;
                            break;
                        case 2: p22 = 1;
                            break;
                        case 3: p23 = 1;
                            break;
                        case 4: p24 = 1;
                            break;
                        case 5: p25 = 1;
                            break;
                        case 6: p26 = 1;
                            break;
                        case 7: p27 = 1;
                            break;
                        default:
                            break;
                    }
                }
               
            }
            
          //DETERMINE WHAT SECTOR TO LIGHT UP FOR PLAYER YAW
                switch(yawSector){
                        case 0: y10 = 1;
                            break;
                        case 1: y11 = 1;
                            break;
                        case 2: y12 = 1;
                            break;
                        case 3: y13 = 1;
                            break;
                        case 4: y14 = 1;
                            break;
                        case 5: y15 = 1;
                            break;
                        case 6: y16 = 1;
                            break;
                        case 7: y17 = 1;
                            break;
                        default:
                            break;
                }
               
          input = new float[]{bias, damageTakenNode, damageDealtNode, p10,p11,p12,p13,p14,p15,p16,p17,p20,p21,p22,p23,p24,p25,p26,p27,y10,y11,y12,y13,y14,y15,y16,y17};
          System.out.println("The input array: "+Arrays.toString(input));
          output = network.calculate(input);
           
            
            
            

///////////////////////////Take output and select the action//////////////////////
            int selection = 0;
            float score = 0f;
            
            
            
            
            //TAKE THIS CODE OUT, JUST A TEST THING
            
            if(output[0] == 0.5 || output[1] == 0.5)
                agent_host.sendCommand("quit");
            
            if(output[2] == 0.5 || output[3] == 0.5)
                agent_host.sendCommand("quit");    
                
                
                
                
                
            for(int i = 0; i < 5; i++){
                System.out.println("Output " + i + " = " + output[i]);
                if(output[i] > score){
                    
                    score = output[i];
                  //  System.out.println("Score = " + score);
                    selection = i;
                  //  System.out.println("Selection = " + selection);
                }   
            }
            
            //fix spot in map
            selection += 27;
            
            
            System.out.println(outputButtonNames.get(selection) + "\n");
            agent_host.sendCommand(outputButtonNames.get(selection));
                        
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
        
        
        
        
        
        //COULD SLEEP HERE  ON NIGHT TIME IF NEED BE
        
        
        
        
        
////////////////////////////////calculate  and return score ///////////////////////////////
        
        //player died
        if(life == 0){
            oldTimeAlive = 0;
        }


        timeAlive = timeAlive - oldTimeAlive;
        oldTimeAlive = oldTimeAlive + timeAlive; 
         
        damageDealt = damageDealt - oldDamageDealt;
        oldDamageDealt = oldDamageDealt + damageDealt;
        
        damageTaken = damageTaken - oldDamageTaken;
        oldDamageTaken = oldDamageTaken + damageTaken;
        
        mobKilled = mobKilled - oldMobKilled;
        oldMobKilled = oldMobKilled + mobKilled;
        
        
        System.out.println("\nORGANISM STATS: TimeAlive: " + timeAlive + ", DamageDealt: " + damageDealt + ", damageTaken: " + damageTaken + ", mobKilled: " + mobKilled);
        
        return ((timeReward * timeAlive) + (damageDealtReward * (float)damageDealt) + (zombiesKilledReward * mobKilled) - (damageRecievedReward * (float)damageTaken));
    }
       
}

