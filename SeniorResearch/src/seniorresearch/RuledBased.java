
package seniorresearch;
import com.microsoft.msr.malmo.*;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.*;
import static seniorresearch.XmlConversionMethods.createMissionString;


public class RuledBased implements XmlConversionMethods,FitnessTune
{
    static Map<Integer,String> outputButtonNames;
    
    static float oldMobKilled = 0;
    static float oldDamageTaken = 0;
    static float oldDamageDealt = 0;

    static
    {        
        System.load("/Users/derekgrove/Desktop/Malmo/Java_Examples/libMalmoJava.jnilib"); 
    }
    
    public static void main(String argv[]) throws Exception
    {
        //Set up stream to print to
      //  PrintStream out = new PrintStream(new FileOutputStream("RuleBasedData.txt", true), true);
      //  System.setOut(out);
        
        System.out.println("Organism Number,Time Alive,Damage Dealt,Damage Taken,Score");
        
        outputButtonNames = new HashMap(); 
        outputButtonNames.put(0,"move 1");
        outputButtonNames.put(1,"move -1");
        outputButtonNames.put(2,"turn 1");
        outputButtonNames.put(3,"turn -1");
        outputButtonNames.put(4,"attack 1");
        float highestScore = 0;
        
         //run for 10 organisms
        float tempScore = 0;
        for(int i = 0; i < 5; i++){
          //  System.out.println("Run Started for Organism: " + i);
          System.out.println("Run Started for Organism: " + i);  
          tempScore = runOrganism();
          
          System.out.println(tempScore);
         //   System.out.println("The score for this organism: " + tempScore + "\n");
            if(tempScore > highestScore)
                highestScore = tempScore;
        } 
      //  System.out.println("The highest Fitness achieved was: " + highestScore);
     
    }
    
    static private float runOrganism() throws Exception{
        
         ///////////////////////////////////// SET UP THE WORLD AND THE MALMO AGENT /////////////////////////////////
        
        AgentHost agent_host = new AgentHost();

        
        String missionXmlString = createMissionString(0);
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
            float mobKilled = 0;
            float damageTaken = 0;
            float damageDealt = 0;
            float timeAlive = 0;
            Double zombieXPos = null;
            Double zombieZPos = null;
            
            
            float oldTimeAlive = 0;
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
                
                //CALCULATE DIRECTION OF ZOMBIE
                double directionInDegrees = Math.toDegrees(Math.atan2((zombieZPos-zPos),(zombieXPos-xPos)));
                
                
                
                //ADD THING HERE BECAUSE THE DIRECTION DOES NOT MEAN LEFT OR RIGHT. IT IS 
                // A DEGREE IN THE COORDINATE PLANE SYSTEM.
                
                
                
                //turn in correct direction
                if(directionInDegrees > 0){
                    agent_host.sendCommand("attack 1");
                    agent_host.sendCommand("turn 1");
                    System.out.println("Saw zombie: turn 1");
                }else if (directionInDegrees < 0){
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
       
        //get correct run scores since the stats for the organism persists between runs
         timeAlive = timeAlive - oldTimeAlive;
        oldTimeAlive = oldTimeAlive + timeAlive; 
          
        damageDealt = damageDealt - oldDamageDealt;
        oldDamageDealt = oldDamageDealt + damageDealt;
        
        damageTaken = damageTaken - oldDamageTaken;
        oldDamageTaken = oldDamageTaken +damageTaken;
        
        mobKilled = mobKilled - oldMobKilled;
        oldMobKilled = oldMobKilled + mobKilled;
        
        
        System.out.println("\nORGANISM STATS: TimeAlive: " + timeAlive + ", DamageDealt: " + damageDealt + ", damageTaken: " + damageTaken + ", mobKilled: " + mobKilled);
        
        
        System.out.print(timeAlive+","+damageDealt+","+damageTaken + ",");
        
        //calulate score here
        return ((timeReward * timeAlive) + (damageDealtReward * damageDealt) + (zombiesKilledReward * mobKilled) - (damageRecievedReward * damageTaken));
        
        
        
    }
    
    
}

