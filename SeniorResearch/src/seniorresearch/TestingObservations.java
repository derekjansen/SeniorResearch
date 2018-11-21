
package seniorresearch;
import com.microsoft.msr.malmo.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.json.*;
import static seniorresearch.XmlConversionMethods.createMissionString;


public class TestingObservations implements XmlConversionMethods
{
    static Map<Integer,String> outputButtonNames;
    static double highestScore = 0;
   // static double oldLife = 0;
    static float oldMobKilled = 0;
    static float oldDamageTaken = 0;
    static float oldDamageDealt = 0;
    
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
        for(int i = 0; i < 20; i++){
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

        
        String missionXmlString = createMissionString(1);
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

       
            double xPos = 0;
            double zPos = 0;
            double yaw = 0f;
            //double life = 0;
            float mobKilled = 0;
            float damageTaken = 0;
            float damageDealt = 0;
            float timeAlive = 0;
            float oldTimeAlive = 0;
        ////////////////////////// MAIN LOOP ///////////////////////////////////// 
        
        double pigX;
        double pigZ;
        //Spawn zombie in the corner
        //agent_host.sendCommand("chat /summon pig 1 228 1");
        
        
        do {
            pigX = 0;
            pigZ = 0;
             //////////GET OBSERVATIONS/////////////
            
            if(world_state.getObservations().size() > 0){
                
                try{
                
                System.out.println(world_state.getObservations().get(0).getText());
               
                JSONObject root =  new JSONObject(world_state.getObservations().get(0).getText()); 
               // life = root.getDouble("Life");
                xPos = root.getDouble("XPos");
                zPos = root.getDouble("ZPos");
                mobKilled = root.getInt("MobsKilled");
                damageTaken = root.getInt("DamageTaken");
                damageDealt = root.getInt("DamageDealt");
                timeAlive = root.getInt("TimeAlive");
                yaw = Math.abs(root.getDouble("Yaw"));
                
                //there are nearby things AKA figure out where the zombie is
                if(root.has("Entities")){
                    //get array
                    JSONArray theEntityArray = root.getJSONArray("Entities");
                    int i = theEntityArray.length() -1;
                   
                    while(i > -1){
                        JSONObject theEntity = theEntityArray.getJSONObject(i);
                        
                        if(theEntity.getString("name").equalsIgnoreCase("pig")){
                            //the entity is the pig here.
                            System.out.println("Found the pig");
                            System.out.println(theEntity);
                            System.out.println("The pig' lifepoints are: " + theEntity.getInt("life"));
                            pigX = theEntity.getDouble("x");
                            pigZ = theEntity.getDouble("z");
                            System.out.println("The pig coordinates are: " + pigX + " , " + pigZ);
   
                        }
                        
                        
                        
                        
                        
                        i--;
                    }
                }
                
                System.out.println("PLAYER STATS:    timeAlive: " + timeAlive + ", XPos: " + xPos + ", ZPos: " + zPos + ", damageTaken: " + damageTaken + ", damageDealt: " + damageDealt + ", mobKilled: " + mobKilled + "\n"); 
                }catch(Exception ex){
                    System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nTHERE WAS A WEIRD PROBLEM: " + ex);
                }
                
            }
            
            //zombie positions
        float p10,p11,p12,p13,p14,p15,p16,p17,p20,p21,p22,p23,p24,p25,p26,p27;
        //yaw values;
        float y10,y11,y12,y13,y14,y15,y16,y17;
        p10=p11=p12=p13=p14=p15=p16=p17=p20=p21=p22=p23=p24=p25=p26=p27=y10=y11=y12=y13=y14=y15=y16=y17= 0;
        int yawSector =0;
        
            if(pigX != 0){
                double directionInDegrees = Math.toDegrees(Math.atan2((pigZ-zPos),(pigX-xPos)));
                directionInDegrees = (directionInDegrees+720) % 360;
                int sector = (int)directionInDegrees/45;
                System.out.println("The direction of the pig is: " + sector);
                double distanceToPig = (float)Math.hypot((pigX-xPos), (pigZ-zPos));
                System.out.println("The distance of the pig is: " + distanceToPig);
                yawSector = (int)yaw/45;
                System.out.println("The player yaw is: "+ yawSector);
                 
                 
  
                  if(distanceToPig <= 1.5){
                   
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
            
                  //DETERMINE WHAT SECTOR DIRECTION IS TO LIGHT UP
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
            
            
                float[] input = new float[]{p10,p11,p12,p13,p14,p15,p16,p17,p20,p21,p22,p23,p24,p25,p26,p27,y10,y11,y12,y13,y14,y15,y16,y17};
              System.out.println(Arrays.toString(input) + "\n\n\n\n");
                  
            
            ////////////randomized outputs to mimic button mashing///////////////
            agent_host.sendCommand( "turn 0");
            agent_host.sendCommand( "move 0" );
          
            
            try {
                Thread.sleep(500);
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
        
        System.out.println("");
       // System.out.println("\nORGANISM STATS: TimeAlive: " + timeAlive + ", DamageDealt: " + damageDealt + ", damageTaken: " + damageTaken + ", mobKilled: " + mobKilled);
        
        //calulate score here
        return (float) ((1.0 * timeAlive) + (20.0 * damageDealt) + (50.0 * mobKilled) - (100.00 * damageTaken));
        
        
        
    }
    
    
}

