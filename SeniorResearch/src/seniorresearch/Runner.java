
// To compile:  javac -cp MalmoJavaJar.jar JavaExamples_run_mission.java
// To run:      java -cp MalmoJavaJar.jar:. JavaExamples_run_mission  (on Linux)
//              java -cp MalmoJavaJar.jar;. JavaExamples_run_mission  (on Windows)

// To run from the jar file without compiling:   java -cp MalmoJavaJar.jar:JavaExamples_run_mission.jar -Djava.library.path=. JavaExamples_run_mission (on Linux)
//                                               java -cp MalmoJavaJar.jar;JavaExamples_run_mission.jar -Djava.library.path=. JavaExamples_run_mission (on Windows)

package seniorresearch;

import com.microsoft.msr.malmo.*;

public class Runner
{
    static
    {        
        System.load("/Users/derekgrove/Desktop/Malmo/Java_Examples/libMalmoJava.jnilib"); 
    }

    public static void main(String argv[]) throws Exception
    {
        
        AgentHost agent_host = new AgentHost();
        try
        {
            StringVector args = new StringVector();
            args.add("JavaExamples_run_mission");
            for( String arg : argv )
                args.add( arg );
            agent_host.parse( args );
        }
        catch( Exception e )
        {
            System.err.println( "ERROR: " + e.getMessage() );
            System.err.println( agent_host.getUsage() );
            System.exit(1);
        }
        if( agent_host.receivedArgument("help") )
        {
            System.out.println( agent_host.getUsage() );
            System.exit(0);
        }
        
        MissionSpec my_mission = new MissionSpec("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
"<Mission xmlns=\"http://ProjectMalmo.microsoft.com\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\n" +
"<About><Summary>Defaut Mission</Summary></About><ServerSection><ServerInitialConditions><Time><StartTime>1000</StartTime><AllowPassageOfTime>false</AllowPassageOfTime></Time></ServerInitialConditions><ServerHandlers>\n" +
"<FlatWorldGenerator generatorString=\"3;7,220*1,5*3,2;3;,biome_1\"/>\n" +
"<DrawingDecorator><DrawLine x1=\"2\" y1=\"227\" z1=\"2\" x2=\"2\" y2=\"227\" z2=\"-12\" type=\"stone\"/><DrawLine x1=\"2\" y1=\"227\" z1=\"-12\" x2=\"-12\" y2=\"227\" z2=\"-12\" type=\"stone\" /><DrawLine x1=\"-12\" y1=\"227\" z1=\"-12\" x2=\"-12\" y2=\"227\" z2=\"2\" type=\"stone\" /><DrawLine x1=\"-12\" y1=\"227\" z1=\"2\" x2=\"2\" y2=\"227\" z2=\"2\" type=\"stone\" /></DrawingDecorator>\n" +
"<ServerQuitFromTimeUp timeLimitMs=\"15000\"/><ServerQuitWhenAnyAgentFinishes/></ServerHandlers></ServerSection><AgentSection><Name>A default agent</Name><AgentStart><Placement x=\"0\" y=\"228\" z=\"0\" yaw=\"90\"/><Inventory><InventoryItem slot=\"0\" type=\"diamond_sword\" /></Inventory></AgentStart><AgentHandlers>\n" +
"<ObservationFromFullStats/><ContinuousMovementCommands/><VideoProducer viewpoint=\"1\"><Width>800</Width><Height>600</Height></VideoProducer><RewardForReachingPosition><Marker x=\"19.5\" y=\"0\" z=\"19.5\" reward=\"100\" tolerance=\"1.10000002\"/></RewardForReachingPosition></AgentHandlers></AgentSection></Mission>\n" +
"", true);
        
      
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
                return;
            }
            world_state = agent_host.getWorldState();
            for( int i = 0; i < world_state.getErrors().size(); i++ )
                System.err.println( "Error: " + world_state.getErrors().get(i).getText() );
        } while( !world_state.getIsMissionRunning() );
        System.out.println( "" );


        
        
        //MAIN LOOP:
        do {
            
            
            
            
            
            
            
            agent_host.sendCommand( "move 0" );
           
            agent_host.sendCommand( "turn " + Math.random() );
            try {
                Thread.sleep(500);
            } catch(InterruptedException ex) {
                System.err.println( "User interrupted while mission was running." );
                return;
            }
            
            agent_host.sendCommand( "turn 0" );
            
            agent_host.sendCommand( "move 0.75");
            try {
                Thread.sleep(2000);
            } catch(InterruptedException ex) {
                System.err.println( "User interrupted while mission was running." );
                return;
            }
            
            world_state = agent_host.getWorldState();

            for( int i = 0; i < world_state.getErrors().size(); i++ ) {
                TimestampedString error = world_state.getErrors().get(i);
                System.err.println( "Error: " + error.getText() );
            }
            
            
            System.out.println(agent_host.getWorldState().getObservations());

            
            
            
            
        } while(world_state.getIsMissionRunning() );

        System.out.println( "Mission has stopped." );
    }
}

