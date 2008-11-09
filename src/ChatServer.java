import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xml.ObjectXmlReference;

import com.thoughtworks.xstream.XStream;


public class ChatServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		
	    String server_host = "localhost";
	    int server_port = 5555;
	    if (args.length == 1){
	    	try {
	    		server_port = Integer.valueOf(args[0]);
	    	}catch(Exception e){
	    		System.out.println("erro na conversao da porta. Sera usada a porta 5555");
	    		server_port = 5555;
	    	}
	    	
	    }

	    TCPAddress addr = new TCPAddress (server_host, server_port);
	    ORB.instance().address(addr);
	    
	    String ior = "";
	    
	    /////////////////
	    //RoomRegistry //
	    /////////////////
	    RoomRegistry roomregistry = null;
	    roomregistry = new RoomRegistryImpl();
	    
	    ior = ORB.objectToString(roomregistry);
	    ObjectXmlReference obj_xml_reference = new ObjectXmlReference(ior, server_host, String.valueOf(server_port));
		XStream xstream = new XStream();
		xstream.alias("reference", ObjectXmlReference.class);
		
		String ref_xml = xstream.toXML(obj_xml_reference);
		echo("xml com a referencia: ");
		System.out.println(ref_xml);
		writeFile("roomregistry.xml", ref_xml);
		
		obj_xml_reference = null;
		xstream = null;
		
		////////////////////
		// ORBManager.xml //
		////////////////////
	    obj_xml_reference = new ObjectXmlReference("ORB@"+server_host+"@"+String.valueOf(server_port), server_host, String.valueOf(server_port));
		xstream = new XStream();
		xstream.alias("reference", ObjectXmlReference.class);
		ref_xml = xstream.toXML(obj_xml_reference);
		writeFile("_ORB@"+server_host+"@"+String.valueOf(server_port)+".xml", ref_xml);
		
		//Start do ORB
		ORBThread orb_server = new ORBThread(ORB.instance());
	    orb_server.start();
	    
	    ////////////////////////////
	    //TRATAMENTO DOS COMANDOS //
	    ////////////////////////////
	    /*
	    List array_comandos = new ArrayList();
	    array_comandos.add("$join rio");
	    array_comandos.add("testando 1..2...3");
	    array_comandos.add("$rooms");
	    array_comandos.add("$leave rio");
	    */

	    String command_line = "";
	    String patternStr = "^\\$(\\w+)(\\s+(.*))?$";
	    Pattern pattern = Pattern.compile(patternStr);
	    BufferedReader in = null;
	    while (true) {
	    	//command_line = (String) array_comandos.get(i);
        	in = new BufferedReader(new InputStreamReader(System.in));
            try {
				command_line = in.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		            
		    Matcher matcher = pattern.matcher(command_line);
		    boolean matchFound = matcher.find();
		    String command = "";
		    String argument = "";
		    if (matchFound) {
		        //echo("match found!!!");
	        	command = matcher.group(1);
	        	argument = matcher.group(2);
	        	echo("command: "+command);
	        	echo("argument: "+argument);
	        	if (argument!=null) argument = argument.trim();
	        	if (command.equals("migrate")){ //migrate
	        		
	        		/*
	        		Map salas_criadas = ChatClient.room_registry.getRooms();
	        		prompt("salas criadas:");
	        		Iterator iterator = salas_criadas.keySet().iterator();
	        		while (iterator.hasNext()) {
	        		   String name = (String) iterator.next();
	        		   prompt(name);
	        		}
	        		*/
	        	}else if (command.equals("migrated")) { //lista objetos migrados
     		
	        	}else if (command.equals("quit")) { //encerra a aplicacao
	        		prompt("Good-bye!");
	        		System.exit(0);
	        	}
		    }else{ //envio de mensagem de texto
		    		prompt("Comando invalido: "+command);
		    }
		    
	    }//fim do while
	}
	
	/**
	 * Metodo para escrever um arquivo em disco
	 * @param filename
	 * @param conteudo
	 */
	public static void writeFile(String filename, String conteudo) {
		BufferedWriter outfile = null;
		
	    try {
	    	outfile = new BufferedWriter(new FileWriter(filename));
			outfile.write(conteudo);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		    try {
				outfile.close();
			} catch (IOException e) {
			}
		}
		
	}
	
	public static void echo(String msg) {
		System.out.println("[ChatServer] "+msg);
	}
	
	public static void prompt(String msg) {
		System.out.println("> "+msg);
	}
	
	/**
	 * Metodo para fazer uma pergunta na console
	 * @param question
	 * @return
	 * @throws IOException
	 */
	public static String question(String question) throws IOException {
		String answer = "";
		BufferedReader in = null;
        prompt(question);
       	in = new BufferedReader(new InputStreamReader(System.in));
	    String str = "";
        answer = in.readLine();
		
		return answer;
	}

}

class ORBThread extends Thread {
	
    private ORB _orb = null;
    
	public ORBThread(ORB orb) {
		_orb = orb;
	}
	
	public void run() {
	    
		TCPAddress addr = (TCPAddress) _orb.address();
	    String server_port = addr.host();
		String server_host = String.valueOf(addr.port());
		echo("ORB Running on "+server_host+":"+server_port);
		echo("running...");
	    _orb.instance().run();
	    
	    
	}
	
	private void echo(String msg) {
		System.out.println("[ChatServerORB] "+msg);
	}
}
