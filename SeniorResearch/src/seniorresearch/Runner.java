
// To compile:  javac -cp MalmoJavaJar.jar JavaExamples_run_mission.java
// To run:      java -cp MalmoJavaJar.jar:. JavaExamples_run_mission  (on Linux)
//              java -cp MalmoJavaJar.jar;. JavaExamples_run_mission  (on Windows)

// To run from the jar file without compiling:   java -cp MalmoJavaJar.jar:JavaExamples_run_mission.jar -Djava.library.path=. JavaExamples_run_mission (on Linux)
//                                               java -cp MalmoJavaJar.jar;JavaExamples_run_mission.jar -Djava.library.path=. JavaExamples_run_mission (on Windows)

package seniorresearch;
import com.microsoft.msr.malmo.*;
import java.io.File;
import java.io.StringWriter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;

public class Runner
{
    static
    {        
        System.load("/Users/derekgrove/Desktop/Malmo/Java_Examples/libMalmoJava.jnilib"); 
    }

    
    private static Document convertXMLFileToXMLDocument(String filePath) 
{
	//Parser that produces DOM object trees from XML content
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	
	//API to obtain DOM Document instance
	DocumentBuilder builder = null;
	try 
	{
		//Create DocumentBuilder with default configuration
		builder = factory.newDocumentBuilder();
		
		//Parse the content to Document object
		Document xmlDocument = builder.parse(new File(filePath));
		
		return xmlDocument;
	} 
	catch (Exception e) 
	{
		e.printStackTrace();
	}
	return null;
}
    
    public static void main(String argv[]) throws Exception
    {
        
        ///////////////////////////////////// SET UP THE WOLRD AND THE MALMO AGENT /////////////////////////////////
        
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
        
        //new 
        
        final String xmlFilePath = "/Users/derekgrove/NetBeansProjects/SeniorResearch/SeniorResearch/src/seniorresearch/MainMission.xml";
        Document xmlDoc = convertXMLFileToXMLDocument(xmlFilePath);
        
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer;
        String missionXmlString;
        try {
            transformer = tf.newTransformer();
            
            // Uncomment if you do not require XML declaration
            // transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            
            //Print XML or Logs or Console
            StringWriter writer = new StringWriter();
            transformer.transform(new DOMSource(xmlDoc), new StreamResult(writer));
             missionXmlString = writer.getBuffer().toString();	
           		
        }catch(Exception e){
            System.out.println("Conversion from xml to string errored out");
            missionXmlString = null; 
        }
        
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
                return;
            }
            world_state = agent_host.getWorldState();
            for( int i = 0; i < world_state.getErrors().size(); i++ )
                System.err.println( "Error: " + world_state.getErrors().get(i).getText() );
        } while( !world_state.getIsMissionRunning() );
        System.out.println( "" );


        ///////////////////////// SET UP NEAT ///////////////////////////////////
        
        
        
        
        
        
        
        
        
        ////////////////////////// MAIN LOOP ///////////////////////////////////// 
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
           
            
            
          //  if(agent_host.getWorldState().getNumberOfObservationsSinceLastState() > 0){
                
            
            System.out.println(agent_host.getWorldState().getObservations());
             System.out.println(agent_host.getWorldState().getObservations().size());
            
         //   }
            
            
            
            
        } while(world_state.getIsMissionRunning() );

        System.out.println( "Mission has stopped." );
    }
}

