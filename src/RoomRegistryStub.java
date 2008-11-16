import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RoomRegistryStub extends RoomRegistry {

	public RoomRegistryStub(ObjectReference ref){
		objectReference(ref);
	}
	
	@Override
	public ChatRoom findRoom(String name) {
		//echo("findRom -> "+name);
		Request req = createRequest ("findRoom");
		req.beginParameter();
		req.addString(name);
		req.endParameter();
		req.endXml();
		req.invoke();
		
		String object_reference = req.getObjectReference();
		//echo("object_reference: "+object_reference);
		if (object_reference.equals("null")){
			return null;
		}else{
			String object_host = req.getObjectHost();
			String object_port = req.getObjectPort();
			
			ChatRoom chatroom = null;
		    ObjectReference object_ref = new ObjectReference (object_reference, object_host, object_port);
		    chatroom = new ChatRoomStub(object_ref);
		    
			return chatroom;
		}
	}

	@Override
	public Map getRooms() {
		//echo("getRooms");
		Request req = createRequest ("getRooms");
		req.beginParameter();
		req.endParameter();
		req.endXml();
		req.invoke();
		
		Map rooms = new HashMap();
		List references = req.getSequenceReference();
		String xml_aux = "";
		ObjectXmlReference obj_xml_aux = null;
		ObjectReference obj_ref_aux = null;
		for (int i=0; i < references.size(); i++){
			//chatroom e instancia de chatroom de ChatRoomImpl
			xml_aux = (String) references.get(i);
			obj_xml_aux = objectReference().getObjectXml(xml_aux);
			obj_ref_aux = new ObjectReference(obj_xml_aux.getObject(), obj_xml_aux.getHost(), obj_xml_aux.getPort());
			ChatRoom chatroom = new ChatRoomStub(obj_ref_aux);
			String name = chatroom.getName();
			//echo("Room name: "+name);
			rooms.put(name, chatroom);
		}
		return rooms;
	}

	@Override
	public ChatRoom newRoom(String name) {
		//echo("newRoom -> "+name);
		Request req = createRequest ("newRoom");
		req.beginParameter();
		req.addString(name);
		req.endParameter();
		req.endXml();
		req.invoke();
		
		String object_reference = req.getObjectReference();
		String object_host = req.getObjectHost();
		String object_port = req.getObjectPort();
		
		ChatRoom chatroom = null;
	    ObjectReference object_ref = new ObjectReference (object_reference, object_host, object_port);
	    chatroom = new ChatRoomStub(object_ref);
		
		return chatroom;

	}
	
	private void echo(String msg) {
		System.out.println("[RoomRegistryStub] "+msg);
	}

}
