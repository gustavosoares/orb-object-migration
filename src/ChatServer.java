import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class ChatServer {

	public static String orb_filename = "";
	public static OrbManager orbmanager_skel = null;;
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
	    ORB.setRoomRegistry(roomregistry);
	    
	    ObjectXmlReference obj_xml_reference = new ObjectXmlReference(ior, server_host, String.valueOf(server_port));
		XStream xstream = new XStream();
		xstream.alias("reference", ObjectXmlReference.class);
		
		String ref_xml = xstream.toXML(obj_xml_reference);
		echo("xml com a referencia: roomregistry.xml");
		System.out.println(ref_xml);
		writeFile("roomregistry.xml", ref_xml);
		
		obj_xml_reference = null;
		xstream = null;
		ref_xml = null;
		////////////////////
		// OrbManager //////
		////////////////////
		orbmanager_skel = new OrbManagerImpl(ORB.instance());
		orb_filename = "__ORB@"+server_host+"@"+String.valueOf(server_port)+".xml";
	    //ior = ORB.objectToString(roomregistry);
		ior = ORB.objectToString(orbmanager_skel);
	    obj_xml_reference = new ObjectXmlReference(ior, server_host, String.valueOf(server_port));
		xstream = new XStream();
		xstream.alias("reference", ObjectXmlReference.class);
		
		ref_xml = xstream.toXML(obj_xml_reference);
		echo("xml com a referencia: "+orb_filename);
		System.out.println(ref_xml);
		writeFile(orb_filename, ref_xml);
		
		obj_xml_reference = null;
		xstream = null;
		ref_xml = null;
		////////////////
		//Start do ORB /
		////////////////
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
	    String answer = "";
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
	        	command = matcher.group(1).trim();
	        	argument = matcher.group(2);
	        	echo("command: "+command);
	        	//echo("argument: "+argument);
	        	if (argument!=null) argument = argument.trim();
	        	if (command.equals("migrate")){ //migrate
	        		migrate();
	        	}else if (command.equals("migrated")) { //lista objetos migrados
	        		migrated();
	        	}else if (command.equals("quit")) { //encerra a aplicacao
	        		quit();
	        	}else if (command.equals("list")) {
	        		list();
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
	
	public static String readFile(String filename) {
		StringBuffer xml = new StringBuffer();
		BufferedReader infile=null;
	    try {
	        infile = new BufferedReader(new FileReader(filename));
	        String str;
	        while ((str = infile.readLine()) != null) {
	            xml.append(str+"\n");
	        }
	        infile.close();
	    } catch (IOException e) {
	    	e.printStackTrace();
	    }finally{
	    	try {
				infile.close();
			} catch (IOException e) {
			}
	    }
		return xml.toString();
	}
	
	public static void list() {
		orbmanager_skel.list();
	}
	
	/**
	 * Migracao de objetos
	 */
	public static void migrate() {
		String answer = "";
		XStream xstream = null;
		Map table_registrados = ORB.instance().getListaObjRegistrados();
		Map table_registrados_aux = new HashMap();
		List registrados_aux = new ArrayList();
		Iterator iterator = table_registrados.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
		   String name = (String) iterator.next();
		   if (!table_registrados.get(name).equals(orbmanager_skel)){ //nao listo o proprio orbmanager
			   prompt(i+". "+name+" -> "+table_registrados.get(name));
			   registrados_aux.add(name);
			   table_registrados_aux.put(name, table_registrados.get(name));
			   i++;
		   }
		}

		try {
			while (true) {
				answer = question("Escolha o numero do objeto que deseja migrar \n> ou digite all para todos\n> ou none para abortar");
				if (answer.toLowerCase().trim().equals("none")){
					prompt("saindo da operacao de migracao");
					break;

				}else{
					if (answer.toLowerCase().trim().equals("all")){
						prompt("Todos os objetos serao migrados");
					}else{
						table_registrados_aux = null;
						table_registrados_aux = new HashMap();
						//vejo se o numero esta valido
						int obj_id = 0;
						try {
							obj_id = Integer.valueOf(answer);
						}catch(NumberFormatException e) {
							prompt("Numero invalido!");
							obj_id = -1;
						}
						if (obj_id == -1) break;
						prompt("Numero escolhido: "+answer);
						table_registrados_aux.put(registrados_aux.get(obj_id), table_registrados.get(registrados_aux.get(obj_id)));
					}
					//Lista os arquivos em disco no formato __ORB
					String curDir = System.getProperty("user.dir");
				    File dir = new File(curDir);
				    List lista_orb = new ArrayList();
				    String[] children = dir.list();
					    if (children == null) {
				    	echo("Nao foi possivel listar o diretorio");
				    } else {
				    	int k = 0;
				        for (int j=0; j<children.length; j++) {
				            // Get filename of file or directory
				            String filename = children[j];
				            if (filename.toLowerCase().startsWith("__orb@")) {
				            	if (! filename.equals(orb_filename)) {
					            	String conteudo_xml = readFile(filename);
					            	lista_orb.add(conteudo_xml);
					            	prompt(k+". "+filename);
					            	k++;
				            	}
				            }
				        }   
				    }
					
					answer = question("Escolha o ORB de destino\n> ou none para abortar");
					if (answer.toLowerCase().trim().equals("none")){
    					prompt("saindo da operacao de migracao");
    					break;
					}else{
						//checo se realmente e numero
						int orb_id = 0;
						try {
							orb_id = Integer.valueOf(answer);
						}catch(NumberFormatException e) {
							prompt("Numero invalido!");
							orb_id = -1;
						}
						if (orb_id == -1) break;
						prompt("ORB escolhido: "+answer);
						
						//////////////////////
						// ORB MANAGER STUB //
						//////////////////////
					    OrbManager orb_manager_stub = null;
		            	
		        	    xstream = new XStream(new DomDriver());
		        	    xstream.alias("reference", ObjectXmlReference.class);
		        	    ObjectXmlReference orbmanager_reference = (ObjectXmlReference) xstream.fromXML((String) lista_orb.get(orb_id));
		        	    ObjectReference orbmanager_ref = new ObjectReference (orbmanager_reference.getObject(), orbmanager_reference.getHost(), orbmanager_reference.getPort());
		        	    
		        	    orb_manager_stub = new OrbManagerStub(orbmanager_ref);
		        	    XmlMapper xmlmapper = getXmlMapper(table_registrados_aux);
		        	    orb_manager_stub.migrate(xmlmapper);
					}
					break; //saio do loop

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Lista objetos migrados
	 */
	public static void migrated() {
		
	}
	
	/**
	 * Encerra a aplicacao
	 */
	public static void quit() {
		prompt("Good-bye!");
		System.exit(0);
	}
	
	/**
	 * Retorna XmlMapper a partir de um hashmap
	 * @param hashmap
	 * @return
	 */
	public static XmlMapper getXmlMapper(Map obj_impl) {
		XmlMapper xml_mapper = null;
		
		Iterator iterator = obj_impl.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			ObjectImpl obj_impl_aux = (ObjectImpl) obj_impl.get(key);
			String classname = obj_impl_aux.getClass().getName();
			echo("classname: "+classname);
			if (classname.equals("RoomRegistryImpl")){
				xml_mapper = new RoomRegistryXml(key, classname);
				//////////////////
				//Pego as salas //
				//////////////////
				Map salas_criadas = ((RoomRegistryImpl) obj_impl_aux).filhos();
				Iterator iterator_salas = salas_criadas.keySet().iterator();
				while(iterator_salas.hasNext()){
					String room_name = (String) iterator_salas.next();
					ChatRoomImpl chatroom_impl = (ChatRoomImpl) salas_criadas.get(room_name);
					//CRIO O XML
					ChatRoomXml chatroomxml_aux = new ChatRoomXml(room_name, chatroom_impl.getKey(), chatroom_impl.getClass().getName());
					/////////////////////
					// PEGO OS USUARIOS//
					/////////////////////
					Map usuarios_sala = chatroom_impl.filhos();
					Iterator iterator_users = usuarios_sala.keySet().iterator();
					while (iterator_users.hasNext()) {
						String user_name = (String) iterator_users.next();
						ChatUserStub chatuser_stub = (ChatUserStub) usuarios_sala.get(user_name);
						String classname_chatuser = chatuser_stub.getClass().getName();
						String host_chatuser = chatuser_stub.getObjectReference().getHost();
						String port_chatuser = String.valueOf(chatuser_stub.getObjectReference().getPort());
						String ref_chatuser = chatuser_stub.getObjectReference().stringify();
						ChatUserXml chatuserxml_aux = new ChatUserXml(user_name, classname_chatuser, ref_chatuser, host_chatuser, port_chatuser);
						//Adiciono o chatuser no chatroom
						chatroomxml_aux.addChatUser(chatuserxml_aux);
					}
					//Adiciono o chatroom no roomregistry
					((RoomRegistryXml) xml_mapper).addChatroom(chatroomxml_aux);
				}
				
			}else if (classname.equals("ChatRoomImpl")){

				ChatRoomImpl chatroom_impl = (ChatRoomImpl) obj_impl_aux;
				String room_name = chatroom_impl.getName();

				//CRIO O XML
				xml_mapper = new ChatRoomXml(room_name, chatroom_impl.getKey(), chatroom_impl.getClass().getName());
				/////////////////////
				// PEGO OS USUARIOS//
				/////////////////////
				Map usuarios_sala = chatroom_impl.getUsers();
				Iterator iterator_users = usuarios_sala.keySet().iterator();
				while (iterator_users.hasNext()) {
					String user_name = (String) iterator_users.next();
					ChatUserStub chatuser_stub = (ChatUserStub) usuarios_sala.get(user_name);
					String classname_chatuser = chatuser_stub.getClass().getName();
					String host_chatuser = chatuser_stub.getObjectReference().getHost();
					String port_chatuser = String.valueOf(chatuser_stub.getObjectReference().getPort());
					String ref_chatuser = chatuser_stub.getObjectReference().stringify();
					ChatUserXml chatuserxml_aux = new ChatUserXml(user_name, classname_chatuser, ref_chatuser, host_chatuser, port_chatuser);
					//Adiciono o chatuser no chatroom
					((ChatRoomXml) xml_mapper).addChatUser(chatuserxml_aux);
				}
				
			}
		}
		return xml_mapper;
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
