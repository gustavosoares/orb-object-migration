

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class CodecXml extends CodecXmlObject{

	public CodecXml(){
		super();
	}
	
	public CodecXml(StringBuffer buffer) {
		setBuffer(buffer);
	}

	public void putString(String arg) {
		addField(4, "string", arg);
	}

	public void putInteger(int arg) {
		addField(4, "integer", String.valueOf(arg));		
	}

	public void putBoolean(boolean arg) {
		addField(4, "boolean", String.valueOf(arg));		
	}

	public void putDecimal(Double arg) {
		addField(4, "decimal", String.valueOf(arg));		
	}

	public void putObjectId(String key) {
		addField(4, "objectid", key);
	}

	public void putClassName(String arg) {
		addField(4, "classname", arg);
	}
	
	public String getString() {
		return getField("string");
	}

	public String getObjectId() {
		return getField("objectid");
	}
	
	public String getClassName() {
		return getField("classname");
	}

	public int getInteger() {
		return Integer.valueOf(getField("integer"));
	}

	public boolean getBoolean() {
		return Boolean.valueOf(getField("boolean"));
	}

	public boolean getDouble() {
		// TODO Auto-generated method stub
		return false;
	}
	
	////////////////////
	//ObjectReference //
	////////////////////
	public String getObjectReference(){
		return getField("object");
	}
	
	public String getObjectHost() {
		return getField("host");
	}
	
	public String getObjectPort() {
		return getField("port");
	}
	
	public void putObjectReference(String xml_reference) {
		addField(xml_reference);
	}
	
	public List getSequence (String field_name) {
		
		List fields = new ArrayList();
		
		StringBuffer new_buff = new StringBuffer();
		StringTokenizer st = new StringTokenizer(getBuffer().toString(),"\n");
		int count_tokens = st.countTokens();
		//echo("tokens count: "+st.countTokens());
		String linha ="";
		boolean found = false;
		while (st.hasMoreTokens()) {
			while (st.hasMoreTokens()) {
				linha = st.nextToken();
				if (linha.trim().equals("<"+field_name+">")) {
					found = true;
					break;
				}
			}
			if (found) {
				new_buff.append(linha+"\n");
				while (st.hasMoreTokens()) {
					linha = st.nextToken();
					if (linha.trim().equals("</"+field_name+">")) {
						new_buff.append(linha);
						fields.add(new_buff.toString());
						new_buff.delete(0, new_buff.length());
						found = false;
						break;
					}
					new_buff.append(linha+"\n");
				}
			}
		}
		
		return fields;
		
	}
	
	/**
	 * Obtem lista com a sequencia de referencias
	 * @return
	 */
	public List getSequenceReference() {
		return getSequence("reference");
	}
	
	/**
	 * Obtem sequencia de struct
	 * @return
	 */
	public List getSruct() {
		return getSequence("struct");
	}
	
	public List getSequenceString() {

		String sequence_xml = getXmlStrip("sequence");
		
		XStream xstream = new XStream(new DomDriver());
		xstream.addImplicitCollection(SequenceStrings.class, "strings");
		xstream.alias("sequence", SequenceStrings.class);
		SequenceStrings sequence = (SequenceStrings) xstream.fromXML(sequence_xml);
		xstream = null;
		
		return sequence.getStrings();
		
	}

	//Extrai reference de uma sequence
	public String getObjectReferenceFromSequence() {
		String xml_ref = getXmlStrip("reference");
		
	    XStream xstream = new XStream(new DomDriver());
	    xstream.alias("reference", ObjectXmlReference.class);
	    ObjectXmlReference obj_xml_reference = (ObjectXmlReference) xstream.fromXML(xml_ref);
	    
		return obj_xml_reference.getObject();
	}
	
	public String getOperation(){
		return getField("operation");
	}
	
	public String getField(String field_name) {
		
	    String patternStr = "<"+field_name+">(.*?)</"+field_name+">";
	    String field_value = null;
	    
	    //System.out.println("[CodecXml] pattern: "+patternStr);
	    
	    //Regexp para extrair informacao do xml
	    Pattern pattern = Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(getBuffer().toString());
	    boolean matchFound = matcher.find();
	    
	    if (matchFound) {
	    	field_value = matcher.group(matcher.groupCount());
	    }else{
	    	echo("field tag "+field_name+" not found!");
	    }
	    
	    return field_value;
	}
	
	//Retorna parte do xml
	public String getXmlStrip(String field_name) {

		StringBuffer new_buff = new StringBuffer();
		StringTokenizer st = new StringTokenizer(getBuffer().toString(),"\n");
		int count_tokens = st.countTokens();
		//echo("tokens count: "+st.countTokens());

		String linha ="";
		boolean found = false;
		while (st.hasMoreTokens()) {
			linha = st.nextToken();
			if (linha.trim().equals("<"+field_name+">")) {
				found = true;
				break;
			}
		}
		if (found) {
			new_buff.append("<"+field_name+">\n");
			while (st.hasMoreTokens()) {
				linha = st.nextToken();
				if (linha.trim().equals("</"+field_name+">")) {
					new_buff.append("</"+field_name+">\n");
					found = false;
					break;
				}
				new_buff.append(linha+"\n");
			}
		}
		
		return new_buff.toString();
	}
	
	public String getReplyType(){
		String field_name = "result";
	    String patternStr = "<"+field_name+" type=\"(.*?)\">";
	    String field_value = null;
	    
	    //System.out.println("[CodecXml] pattern: "+patternStr);
	    
	    //Regexp
	    Pattern pattern = Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(getBuffer().toString());
	    boolean matchFound = matcher.find();
	    
	    if (matchFound) {
	        // Get all groups for this match
	    	field_value = matcher.group(matcher.groupCount());
	    }else{
	    	echo("field tag "+field_name+" not found!");
	    }
	    
	    return field_value;
	}
	
	
	/**
	 * Adiciona um objectImplentation no xml
	 * @param obj_impl
	 */
	public void putObjectImplementation(Map obj_impl) {
		Iterator iterator = obj_impl.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			ObjectImpl obj_impl_aux = (ObjectImpl) obj_impl.get(key);
			String classname = obj_impl_aux.getClass().getName();
			echo("classname: "+classname);
			if (classname.equals("RoomRegistryImpl")){
				RoomRegistryXml roomregistryxml = new RoomRegistryXml(key, classname);
				//////////////////
				//Pego as salas //
				//////////////////
				Map salas_criadas = ((RoomRegistryImpl) obj_impl_aux).getRooms();
				Iterator iterator_salas = salas_criadas.keySet().iterator();
				while(iterator_salas.hasNext()){
					String room_name = (String) iterator_salas.next();
					ChatRoomImpl chatroom_impl = (ChatRoomImpl) salas_criadas.get(room_name);
					//CRIO O XML
					ChatRoomXml chatroomxml_aux = new ChatRoomXml(room_name, chatroom_impl.getKey(), chatroom_impl.getClass().getName());
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
						chatroomxml_aux.addChatUser(chatuserxml_aux);
					}
					//Adiciono o chatroom no roomregistry
					roomregistryxml.addChatroom(chatroomxml_aux);
				}
				
				//ENCODE XML
				XStream xstream = new XStream(new DomDriver());
				xstream.alias("roomregistry", RoomRegistryXml.class);
				xstream.alias("chatroom", ChatRoomXml.class);
				xstream.alias("chatuser", ChatUserXml.class);
				xstream.addImplicitCollection(RoomRegistryXml.class, "chatroomskel");
				xstream.addImplicitCollection(ChatRoomXml.class, "chatuserstub");
				String xml = xstream.toXML(roomregistryxml);
				echo("XML TO ENCODE:");
				System.out.println(xml);
				append(xml+"\n");
			}else if (classname.equals("ChatRoomImpl")){
				echo("TODO");
			}
		}
		
	}
		
	private void echo(String msg) {
		System.out.println("[CodecXml] "+msg);
	}
}
