
package seniorresearch;
import com.microsoft.msr.malmo.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.*;
import static seniorresearch.XmlConversionMethods.createMissionString;


public class RuledBased implements XmlConversionMethods
{
    static Map<Integer,String> outputButtonNames;
    static double highestScore = 0;
   // static double oldLife = 0;
    static int oldMobKilled = 0;
    static int oldDamageTaken = 0;
    static int oldDamageDealt = 0;
    static int oldTimeAlive = 0;
    static
    {        
        System.load("/Users/derekgrove/Desktop/Malmo/Java_Examples/libMalmoJava.jnilib"); 
    }
    
    public static void main(String argv[]) throws Exception
    {
        outputButtonNames = new HashMap(); 
        outputButtonNames.put(0,"move 1");
        outputButtonNames.put(1,"move -1");
        outputButtonNames.put(2,"turn 1");
        outputButtonNames.put(3,"turn -1");
        outputButtonNames.put(4,"attack 1");
        highestScore = 0.0;
        
         //run for 10 organisms
        double tempScore = 0;
        for(int i = 0; i < 5; i++){
            System.out.println("Run Started for Organism: " + i);
            tempScore = runOrganism();
            System.out.println("The score for this organism: " + tempScore + "\n");
            if(tempScore > highestScore)
                highestScore = tempScore;
        } 
        System.out.println("The highest Fitness achieved was: " + highestScore);
     
    }
    
    static private float runOrganism() throws Exception{
        
         ///////////////////////////////////// SET UP THE WORLD AND THE MALMO AGENT /////////////////////////////////
        
        AgentHost agent_host = new AgentHost();

        
        String missionXmlString = createMissionString();
        MissionSpec my_mission = new MissionSpec(missionXmlString, true);
      
        MissionRecordSpec my_mission_record = new MissionRecordSpec("./saved_data.tgz");
        my_mission_record.recordCommands();
        my_mission_record.recordMP4(20, 400000);
        my_mission_record.recordRewards();
        my_mission_record.recordObservations();
        

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

       
            double xPos = 0;
            double zPos = 0;
            //double life = 0;
            int mobKilled = 0;
            int damageTaken = 0;
            int damageDealt = 0;
            int timeAlive = 0;
            Double zombieXPos = null;
            Double zombieZPos = null;
        ////////////////////////// MAIN LOOP ///////////////////////////////////// 
        
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
        agent_host.sendCommand("chat /summon zombie 11 228 11");
        
        
        do {
                zombieXPos = null;
                zombieZPos = null;        
            
             //////////GET OBSERVATIONS/////////////
            
            if(world_state.getObservations().size() > 0){
            //    System.out.println(world_state.getObservations().get(0).getText());
               
                JSONObject root =  new JSONObject(world_state.getObservations().get(0).getText()); 
               // life = root.getDouble("Life");
                xPos = root.getDouble("XPos");
                zPos = root.getDouble("ZPos");
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
                            System.out.println("Found the zombie");
                            zombieXPos = theEntity.getDouble("x");
                            zombieZPos = theEntity.getDouble("z");
                            System.out.println("The zombies' lifepoints are: " + theEntity.getInt("life"));
                            System.out.println("The zombies coordinates are: " + theEntity.getDouble("x") + " , " + theEntity.getDouble("z"));
   
                        }
                        i--;
                    }
                }
               //have player stats and zombie position here
               System.out.println("PLAYER STATS:    timeAlive: " + timeAlive + ", XPos: " + xPos + ", ZPos: " + zPos + ", damageTaken: " + damageTaken + ", damageDealt: " + damageDealt + ", mobKilled: " + mobKilled + "\n"); 
                
            }
            
            //stop
            agent_host.sendCommand( "turn 0");
            agent_host.sendCommand( "move 0" );
            int sleeptime = 0;
            //do things with some basic logic
            
            //saw zombie and know where it is
            if(zombieXPos != null && zombieZPos != null){
                double tempX = zombieXPos - xPos;
                double tempZ = zombieZPos - zPos;
                
                if(tempX > 0 || tempZ > 0){
                    agent_host.sendCommand("attack 1");
                    agent_host.sendCommand("turn 1");
                    System.out.println("Saw zombie: turn 1");
                }else if (tempX < 0 || tempZ < 0){
                    agent_host.sendCommand("attack 1");
                    agent_host.sendCommand("turn -1");
                     System.out.println("Saw zombie: turn -1");
                }else{
                    agent_host.sendCommand("attack 1");
                }
                  sleeptime = 25;  
                
            }else{  //no zombie in sight just move around
                Random r = new Random();
                int toDo = r.nextInt() % 5;
                
                if(toDo > 2){
                    agent_host.sendCommand("move 1");
                    sleeptime = 500;
                }
                else if(toDo == 1){
                    agent_host.sendCommand("turn 1");
                    sleeptime = 200;
                }else{  //is 0
                    agent_host.sendCommand("turn -1");
                    sleeptime = 200;
                }
            }
            
            
            
            
            
            try {
                Thread.sleep(sleeptime);
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
       
        
        //FIX THESE CUZ THEY WACK
        
        
        //get correct run scores since the stats for the organism persists between runs
         timeAlive = timeAlive - oldTimeAlive;
         if(oldTimeAlive != 0){
            oldTimeAlive = oldTimeAlive + timeAlive; 
         }
         
        damageDealt = damageDealt - oldDamageDealt;
        oldDamageDealt = oldDamageDealt + damageDealt;
        
        damageTaken = damageTaken - oldDamageTaken;
        oldDamageTaken = oldDamageTaken +damageTaken;
        
        mobKilled = mobKilled - oldMobKilled;
        oldMobKilled = oldMobKilled + mobKilled;
        
        
        System.out.println("\nORGANISM STATS: TimeAlive: " + timeAlive + ", DamageDealt: " + damageDealt + ", damageTaken: " + damageTaken + ", mobKilled: " + mobKilled);
        
        //calulate score here
        return (float) ((1.0 * timeAlive) + (20.0 * damageDealt) + (50.0 * mobKilled) - (100.00 * damageTaken));
        
        
        
    }
    
    
}

