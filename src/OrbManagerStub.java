import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.lang.reflect.*;



public class OrbManagerStub extends OrbManager {
	
	public OrbManagerStub(ObjectReference ref){
		objectReference(ref);
	}

	@Override
	public boolean migrate(XmlMapper xmlmapper) {
		//echo("findRom -> "+name);
		echo("migrate");
		Request req = createRequest ("migrate");
		req.beginParameter();
		req.beginSequence();
		req.beginString();
		req.addXmlMapper(xmlmapper);
		req.endString();

		req.endSequence();
		req.endParameter();
		req.endXml();
		req.invoke();
		
		//Leio o reply
		boolean result = req.getBoolean();
		req = null;
		return result;
		
	}
	
	/**
	 * Lista os objetos registrados
	 * Nao precisa ser implementado no Stubg
	 */
	public void list() {
		// TODO Auto-generated method stub

	}

	/**
	 * Lista objetos migrados. Nao precisa no stub
	 */
	public void migrated() {
		// TODO Auto-generated method stub
		
	}
	
	public  XmlMapper translateToXmlMapper(Map obj_impl) {
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
