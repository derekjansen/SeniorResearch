
// To compile:  javac -cp MalmoJavaJar.jar JavaExamples_run_mission.java
// To run:      java -cp MalmoJavaJar.jar:. JavaExamples_run_mission  (on Linux)
//              java -cp MalmoJavaJar.jar;. JavaExamples_run_mission  (on Windows)

// To run from the jar file without compiling:   java -cp MalmoJavaJar.jar:JavaExamples_run_mission.jar -Djava.library.path=. JavaExamples_run_mission (on Linux)
//                                               java -cp MalmoJavaJar.jar;JavaExamples_run_mission.jar -Djava.library.path=. JavaExamples_run_mission (on Windows)

package seniorresearch;
import com.microsoft.msr.malmo.*;
import java.io.File;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import neat.ConnectionGene;
import neat.Counter;
import neat.Organism;
import neat.NodeGene;
import org.w3c.dom.Document;
import org.json.*;

public class Runner
{
    static Map<Integer,String> outputButtonNames;
    
    
    static
    {        
        System.load("/Users/derekgrove/Desktop/Malmo/Java_Examples/libMalmoJava.jnilib"); 
    }
    
    public static void main(String argv[]) throws Exception
    {
        
        //////////////////SET UP NEAT CODE/////////////////////
        
        
         Map<Integer,String> inputButtonNames = new HashMap();
        outputButtonNames = new HashMap();
        
        
        
        int populationSize = 25;
        Counter nodeInnovation = new Counter();
        Counter connectionInnovation = new Counter();
        
        //make a new organism
        Organism organism = new Organism();
        
        //create nodeGenes
        int n1 = nodeInnovation.getInnovation();
        int n2 = nodeInnovation.getInnovation();
        int n3 = nodeInnovation.getInnovation();
        int n4 = nodeInnovation.getInnovation();
        int n5 = nodeInnovation.getInnovation();
        int n6 = nodeInnovation.getInnovation();
        
        
        //inputs
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.INPUT,n1));

        
        //map the outputs to their movement "buttons"
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n2));
        outputButtonNames.put(n2,"move 1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n3));
        outputButtonNames.put(n3,"move -1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n4));
        outputButtonNames.put(n4,"turn 1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n5));
        outputButtonNames.put(n5,"turn -1");
        organism.addNodeGene(new NodeGene(NodeGene.TYPE.OUTPUT,n6));
        outputButtonNames.put(n6,"attack 1");
        
        
        
        //create connectionGenes
        int c1 = connectionInnovation.getInnovation();
        //add these to the organism
        organism.addConnectionGene(new ConnectionGene(n1,n3,0.5f,true,c1));
        
//        
//        
//        
//        //create evaluator and pass in the popSize, the starting organism, and the counters for the two types of connections
//        Evaluator eval = new Evaluator(populationSize, organism, nodeInnovation, connectionInnovation){
//            @Override
//            
//            //THIS IS WHERE I CODE HOW TO EVALUATE THE Organism
//            protected float evaluateGenome(Organism organism){
//                
//                runOrganism(argv,organism);

                  //see how well it did, return a score for it

//                //RUN THIS ORGANISM HERE THIS IS WHERE I WANT TO PUT THE DO WHILE CODE
//                
//                
//                return organism.getConnectionGenes().values().size();
//            }
//        };
//        
//        
//        
//        //run for 100 generations
//        for(int i = 0; i < 100; i++){
//            eval.evaluate();
//            System.out.print("Generation: " + i);
//            System.out.print("\tHighest fitness: " + eval.getHighestFitness());
//            System.out.print("\tAmount of species: " + eval.getSpeciesAmount() + "\n");
//        } 
    
        
        runOrganism(argv,organism);
        
        
        
     
    }
    
    static private void runOrganism(String argv[], Organism organism) throws Exception{
        
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

       
        
        ////////////////////////// MAIN LOOP ///////////////////////////////////// 

        //Spawn zombie in the corner
        agent_host.sendCommand("chat /summon zombie -11 228 -11");
            
        do {
            
            ////////////randomized///////////////
            agent_host.sendCommand( "turn 0");
            agent_host.sendCommand( "move 0" );
            Random r = new Random();
            int x = Math.abs(r.nextInt() % 5) + 1;
            
            System.out.println(outputButtonNames.get(x));
            agent_host.sendCommand(outputButtonNames.get(x));
            
            try {
                Thread.sleep(1000);
            } catch(InterruptedException ex) {
                System.err.println( "User interrupted while mission was running." );
                return;
            }

            
            
            world_state = agent_host.getWorldState();

            for( int i = 0; i < world_state.getRewards().size(); i++ ) {
                TimestampedReward reward = world_state.getRewards().get(i);
                System.out.println( "Summed reward: " + reward.getValue() );
            }
            for( int i = 0; i < world_state.getErrors().size(); i++ ) {
                TimestampedString error = world_state.getErrors().get(i);
                System.err.println( "Error: " + error.getText() );
            }
           
            //THIS IS JSON
            //if the obeservations have started
            if(world_state.getObservations().size() > 0){
                System.out.println(world_state.getObservations().get(0).getText());
               
                JSONObject root =  new JSONObject(world_state.getObservations().get(0).getText()); 
                
            }
     
            
        } while(world_state.getIsMissionRunning() );

        System.out.println( "Mission has stopped." );
        
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
}

