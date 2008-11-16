import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ChatRoomStub extends ChatRoom {

	public ChatRoomStub(ObjectReference ref){
		objectReference(ref);
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		Request req = createRequest ("getName");
		req.beginParameter();
		req.endParameter();
		req.endXml();
		req.invoke();
		
		String name = req.getString();
		req = null;
		return name;
	}

	@Override
	public Map getUsers() {
		Request req = createRequest ("getUsers");
		req.beginParameter();
		req.endParameter();
		req.endXml();
		req.invoke();
		
		Map users = new HashMap();
		List references = req.getSequenceReference();
		String xml_aux = "";
		ObjectXmlReference obj_xml_aux = null;
		ObjectReference obj_ref_aux = null;
		for (int i=0; i < references.size(); i++){
			//chatroom e instancia de chatroom de ChatRoomImpl
			xml_aux = (String) references.get(i);
			obj_xml_aux = objectReference().getObjectXml(xml_aux);
			obj_ref_aux = new ObjectReference(obj_xml_aux.getObject(), obj_xml_aux.getHost(), obj_xml_aux.getPort());
			//ChatUser chatuser = new ChatUserStub(obj_ref_aux);
			//String name = chatuser.getUsername();

			users.put(String.valueOf(i), obj_ref_aux);
		}
		return users;
	}

	@Override
	public boolean join(String name, ChatUser user) {
		Request req = createRequest ("join");
		req.beginParameter();
		req.beginSequence();
		req.addSequence("string", name);
		req.addObjectReference(user.objectReference().getXmlReference());
		req.endSequence();
		req.endParameter();
		req.endXml();
		req.invoke();
		
		boolean result = req.getBoolean();
		req = null;
		return result;
	}

	@Override
	public boolean leave(String name) {
		Request req = createRequest ("leave");
		req.beginParameter();
		req.addString(name);
		req.endParameter();
		req.endXml();
		req.invoke();
		
		boolean result = req.getBoolean();
		req = null;
		return result;
	}

	@Override
	public void send(String name, String message) {
		Request req = createRequest ("send");
		req.beginParameter();
		req.beginSequence();
		req.addSequence("string", name);
		req.addSequence("string", message);
		req.endSequence();
		req.endParameter();
		req.endXml();
		req.invoke();
		req = null;
	}

}
