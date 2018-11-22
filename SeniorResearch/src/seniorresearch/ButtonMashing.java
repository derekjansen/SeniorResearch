
package seniorresearch;
import com.microsoft.msr.malmo.*;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.json.*;
import static seniorresearch.XmlConversionMethods.createMissionString;



public class ButtonMashing implements XmlConversionMethods,FitnessTune
{
    static Map<Integer,String> outputButtonNames;
    static double highestScore = 0;
    
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
        
        //Set up stream to print to
       // PrintStream out = new PrintStream(new FileOutputStream("buttonMashingData.txt", true), true);
       // System.setOut(out);
        
        System.out.println("Organism Number,Time Alive,Damage Dealt,Damage Taken,Score");
        
        outputButtonNames = new HashMap(); 
        outputButtonNames.put(0,"move 1");
        outputButtonNames.put(1,"move -1");
        outputButtonNames.put(2,"turn 1");
        outputButtonNames.put(3,"turn -1");
        outputButtonNames.put(4,"attack 1");
        highestScore = 0.0;
        
         //run for 100 organisms
        double tempScore = 0;
        for(int i = 0; i < 100; i++){
            System.out.print(i + ",");
           // System.out.println("Run Started for Organism: " + i);
            tempScore = runOrganism();
          //  System.out.println("The score for this organism: " + tempScore + "\n");
            if(tempScore > highestScore)
                highestScore = tempScore;
            
            System.out.println(tempScore);
            
        } 
        System.out.println("The highest Fitness achieved was: " + highestScore);
        
     
    }
    
    static private float runOrganism() throws Exception{
        
         ///////////////////////////////////// SET UP THE WORLD AND THE MALMO AGENT /////////////////////////////////
        
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

        //System.out.print( "Waiting for the mission to start" );
        do {
            //System.out.print( "." );
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
        

            double life = 0;
            double xPos = 0;
            double zPos = 0;
            float mobKilled = 0;
            double damageTaken = 0;
            double damageDealt = 0;
            float timeAlive = 0;
//            float instantDamageTaken = 0;
//            float oldInstantDamageTaken = 0;
//            float instantDamageDealt = 0;
//            float oldInstantDamageDealt = 0;
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
        agent_host.sendCommand("chat /summon zombie 11 228 11 {IsBaby:0}");
        
        
        do {
//            oldInstantDamageTaken = instantDamageTaken;
//            instantDamageTaken = 0;
//            oldInstantDamageDealt = instantDamageDealt;
//            instantDamageDealt = 0;
            
             //////////GET OBSERVATIONS/////////////
            
            if(world_state.getObservations().size() > 0){
               try{ 
                
               //System.out.println(world_state.getObservations().get(0).getText());
               
                JSONObject root =  new JSONObject(world_state.getObservations().get(0).getText()); 
                life = root.getDouble("Life");
                xPos = root.getDouble("XPos");
                zPos = root.getDouble("ZPos");
                mobKilled = root.getInt("MobsKilled");
                
                damageTaken = root.getInt("DamageTaken");
                damageDealt = root.getInt("DamageDealt");
//                instantDamageTaken = root.getInt("DamageTaken");
//                instantDamageDealt = root.getInt("DamageDealt");
                //protects against dying
                if(root.getInt("TimeAlive") != 0){
                    timeAlive = root.getInt("TimeAlive");
                }
                
                //System.out.println("PLAYER STATS:    life: " + life + ", timeAlive: " + timeAlive +  ", damageTaken: " + damageTaken + ", damageDealt: " + damageDealt  + "\n"); 
               }catch(Exception ex){
                   
               }
            }
            
//            if(instantDamageTaken > oldInstantDamageTaken && oldInstantDamageTaken != 0){
//                System.out.println("Player was hit");
//            }
//            if(instantDamageDealt > oldInstantDamageDealt && oldInstantDamageDealt != 0){
//                System.out.println("Player attacked!");
//            }
            
            
            
            ////////////randomized outputs to mimic button mashing///////////////
            agent_host.sendCommand( "turn 0");
            agent_host.sendCommand( "move 0" );
            Random r = new Random();
            int x = Math.abs(r.nextInt() % 5);
            
          //  System.out.println(outputButtonNames.get(x));
            agent_host.sendCommand(outputButtonNames.get(x));
            
            //varied sleep time based on kind of command being sent
            int sleepTime = 0;
            if(x == 0 || x == 1){           //move
                sleepTime = 1000;
            } else if(x == 2 || x == 3){    //turn
                sleepTime = 500;
            } else{                         //attack
                sleepTime = 100;
            }
            
            try {
                Thread.sleep(sleepTime);
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

        
       // System.out.println( "Mission has stopped." );
       
       
        //player died
        if(life == 0){
            oldTimeAlive = 0;
        }
       
        
        //logic makes sense in theory...
        timeAlive = timeAlive - oldTimeAlive;
        oldTimeAlive = oldTimeAlive + timeAlive; 
       
        damageDealt = damageDealt - oldDamageDealt;
        oldDamageDealt = oldDamageDealt + damageDealt;
        
        damageTaken = damageTaken - oldDamageTaken;
        oldDamageTaken = oldDamageTaken + damageTaken;

        mobKilled = mobKilled - oldMobKilled;
        oldMobKilled = oldMobKilled + mobKilled;
        
        System.out.print(timeAlive+","+damageDealt+","+damageTaken + ",");
      // System.out.println("\nORGANISM STATS: TimeAlive: " + timeAlive + ", DamageDealt: " + damageDealt + ", damageTaken: " + damageTaken + ", mobKilled: " + mobKilled);

        //calulate score here
        return ((timeReward * timeAlive) + (damageDealtReward * (float)damageDealt) + (zombiesKilledReward * mobKilled) - (damageRecievedReward * (float)damageTaken));
        
        
        
    } 
}

