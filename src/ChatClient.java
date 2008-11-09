import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import xml.ObjectXmlReference;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class ChatClient {

	//public static ChatRoom chatroom = null;
	public static Map chatrooms = new HashMap();
	public static RoomRegistry room_registry = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
				
	    BufferedReader infile=null;
	    
	    String ior = "";
	    String xml_file = "roomregistry.xml";
	    String username = "timw";
	    if (args.length == 1){
	    	username = args[0];
	    }

	    //Leio arquivo com a referencia do RoomRegistry
	    StringBuffer xml = new StringBuffer();
	    try {
	    	echo("Lendo o arquivo "+xml_file);
	        infile = new BufferedReader(new FileReader(xml_file));
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
	    	echo("leitura finalizada");
	    }
	    	    
	    XStream xstream = new XStream(new DomDriver());
	    xstream.alias("reference", ObjectXmlReference.class);
	    ObjectXmlReference roomregistry_reference = (ObjectXmlReference) xstream.fromXML(xml.toString());
	    
	    ObjectReference roomregistry_ref = new ObjectReference (roomregistry_reference.getObject(), roomregistry_reference.getHost(), roomregistry_reference.getPort());

	    room_registry = new RoomRegistryStub(roomregistry_ref);
	    
	    //////////////
	    //CHATUSER ///
	    //////////////
	    String server_host = "localhost";
	    Random r = new Random();
	    int server_port = (r.nextInt(2000) + 5000);
	    echo("Iniciando thread...");
	    TCPAddress addr = new TCPAddress (server_host, server_port);
	    ORB.instance().address(addr);
		ChatUser chatuser = null;
	    chatuser = new ChatUserImpl(username);
	    echo("criado skel para o ChatUser -> "+chatuser.objectReference().stringify());
	    
	    //////////////////////////////////
	    //THREAD PARA RECEBER CONEXOES ///
	    //////////////////////////////////
	    ChatClientThread thread = new ChatClientThread(chatuser, username, server_host, server_port);
	    thread.start();

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
		
	    ChatRoom chatroom = null;
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
		    String command;
		    String argument;
		    if (matchFound) {
		        //echo("match found!!!");
	        	command = matcher.group(1);
	        	argument = matcher.group(2);
	        	//echo("command: "+command);
	        	//echo("argument: "+argument);
	        	if (argument!=null) argument = argument.trim();
	        	if (command.equals("rooms")){ //lista sala criadas
	        		Map salas_criadas = ChatClient.room_registry.getRooms();
	        		prompt("salas criadas:");
	        		Iterator iterator = salas_criadas.keySet().iterator();
	        		while (iterator.hasNext()) {
	        		   String name = (String) iterator.next();
	        		   prompt(name);
	        		}
	        	}else if (command.equals("join")) { //entra numa sala e permite criar se nao existe
	        		String name = argument;
	        		chatroom = ChatClient.room_registry.findRoom(name.trim());
	        		if (chatroom == null){
	        			//cria sala?
		       	        try {
		     		        String str = "";
		     		        str = question("Sala "+name+" nao existe... deseja criar? [s/n]");
	     		            if (str.trim().toLowerCase().equals("s")){
	     		            	prompt("a sala ["+name+"] sera criada");
	     		            	chatroom = ChatClient.room_registry.newRoom(name);
	     		            	registerRoom(name, chatroom);
	     		            	//Join no ChatRoom
	     		            	if (! chatroom.join(username, chatuser)) {
	     		            		prompt("Nao foi possivel entrar na sala "+name);
	     		            	}
	     		            }else{
	     		            	prompt("a sala ["+name+"] nao sera criada");
	     		            }
		     		    } catch (IOException e) {
		     		    }
	        		}else{
	        			registerRoom(name, chatroom);
	        			//Join no ChatRoom
	        			if (! chatroom.join(username, chatuser)) {
	        				prompt("Nao foi possivel entrar na sala "+name);
	        			}
	        		}	        		
	        	}else if (command.equals("leave")) { //sai de uma sala
	        		String name = argument;
	        		if (chatroom == null) {
	        			prompt("Voce precisa entrar em uma sala antes!");
	        		}else{
		        		chatroom = ChatClient.getRoom(name);
		        		if (! chatroom.leave(username)) {
		        			prompt("Nao foi possivel sair da sala "+name);
		        		}else{
		        			unRegisterRoom(name);
		        			chatroom = null;
		        		}
	        		}
	        	}else if (command.equals("private")) { //lista usuarios da sala atual e permite selecionar um para batepapo privado
	        		if (chatroom == null) {
	        			prompt("Voce precisa entrar em uma sala antes!");
	        		}else{
	        			//Listar os usuarios
		        		Map user_na_sala = chatroom.getUsers();
		        		prompt("Escolha um usuario da sala "+chatroom.getName()+" para enviar mensagem");
		        		
		        		Iterator iterator = user_na_sala.keySet().iterator();
		        		int i = 0;
		        		List lista_usuarios = new ArrayList();
		        		ObjectReference obj_ref_aux = null;
		        		ChatUser chatuser_aux = null;
		        		String username_aux = "";
		        		while (iterator.hasNext()) {
		        		   String key = (String) iterator.next();
		        		   obj_ref_aux = (ObjectReference) user_na_sala.get(key);
		        		   if (obj_ref_aux.getHost().equals(server_host) && obj_ref_aux.getPort() == server_port) {
		        			   username_aux = username;
		        			   chatuser_aux = null;
		        		   }else{
		        			   chatuser_aux = new ChatUserStub(obj_ref_aux);
		        			   username_aux = chatuser_aux.getUsername();
		        		   }
		        		   prompt((i+1)+" => "+username_aux);
		        		   lista_usuarios.add(chatuser_aux);
		        		   i++;
		        		}
		        		try {
		        			while (true) {
								String user_privado_id = question("Digite o numero do usuario para envio de mensagem privada:");
								prompt("Usuario escolhido: "+user_privado_id);
								int indice = Integer.valueOf(user_privado_id) - 1;
								if (indice == -1) {
									prompt("saindo do envio privado de mensagens");
									break;
								}
								chatuser_aux = (ChatUser) lista_usuarios.get(indice);
								if (chatuser_aux == null) {
									prompt("Voce escolheu a si proprio!!");
								}else{
									String msg = question("Digite a mensagem para envio:");
									chatuser_aux.notifyMessage(username, msg);
									break;
								}
		        			}

						} catch (IOException e) {
							prompt("Erro no envio: "+e.getMessage());
							e.printStackTrace();
							break;
						} catch (NumberFormatException e) {
							prompt("Digite um numero valido");
						} catch (ArrayIndexOutOfBoundsException e) {
							prompt("Digite um numero valido");
						}
		        		
	        		}
	        		
	        	}else if (command.equals("quit")) { //encerra a aplicacao
	        		if (chatroom != null) {
	        			String name = "";
		        		Iterator iterator = ChatClient.chatrooms.keySet().iterator();
		        		while (iterator.hasNext()) {
		        		   name = (String) iterator.next();
		        		   prompt("Sala para sair: "+name);
		        		}
		        		chatroom = ChatClient.getRoom(name);
		        		if (! chatroom.leave(username)) {
		        			prompt("Nao foi possivel sair da sala "+name);
		        		}else{
		        			unRegisterRoom(name);
		        			chatroom = null;
		        		}
	        		}
	        		prompt("Good-bye!");
	        		System.exit(0);
	        	}
		    }else{ //envio de mensagem de texto
		    	//echo("envio do texto: ["+command_line+"]");
        		if (chatroom == null) {
        			prompt("Voce precisa entrar em uma sala antes!");
        		}else{
        			chatroom.send(username, command_line);
        		}
		    }
		    
	    }//fim do while
	    
	}
	
	public static void echo(String msg) {
		System.out.println("[ChatClient] "+msg);
	}
	
	public static void prompt(String msg) {
		System.out.println("> "+msg);
	}
	
	public static void registerRoom(String name, ChatRoom chatroom){
		//echo("sala registrada: "+name);
		ChatClient.chatrooms.put(name, chatroom);
	}
	
	public static void unRegisterRoom(String name){
		ChatClient.chatrooms.remove(name);
	}
	
	public static ChatRoom getRoom(String name) {
		return (ChatRoom) ChatClient.chatrooms.get(name);
	}
	
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

class ChatClientThread extends Thread {
	
	private ChatUser _chatuser;
	private String _username;
    private String _server_host = "localhost";
    private int    _server_port = 0;
    
	public ChatClientThread(ChatUser chatuser, String username, String server_host, int server_port) {
		_chatuser = chatuser;
		_username = username;
		_server_host = server_host;
		_server_port = server_port;
	}
	
	public void run() {
	    
	    echo("username: "+_username);
	    
	    echo("ORB Running on "+_server_host+":"+_server_port);
	    
	    ORB.instance().run();
	    
	    
	}
	
	private void echo(String msg) {
		System.out.println("[ChatClientThread] "+msg);
	}
}

//Exemplos

/*
Account account = new AccountStub(ref);

System.out.println("deposit");
account.deposit (700);
System.out.println("Client: balance is " + account.balance());


System.out.println("withdraw");
account.withdraw (50);
System.out.println("Client: balance is " + account.balance());
*/

/*
 
     try {

        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String str = "";
        while (str != null) {
            System.out.print("> prompt ");
            str = in.readLine();
            process(str);
        }
    } catch (IOException e) {
    }
 
 */