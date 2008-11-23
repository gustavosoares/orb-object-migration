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
		
		//Reply
		String name = null;
		String reply_type = req.getReplyType();
		if (reply_type.equals("return")) {
			name = req.getString();
		} else if (reply_type.equals("error")) {
			echo("erro na mensagem de reply recebida");
		}
		
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
		
		//Reply
		Map users = null; 
		String reply_type = req.getReplyType();
		if (reply_type.equals("return")) {
			users = new HashMap();
			List references = req.getSequenceReference();
			String xml_aux = "";
			ObjectXmlReference obj_xml_aux = null;
			ObjectReference obj_ref_aux = null;
			for (int i=0; i < references.size(); i++){
				//chatroom e instancia de chatroom de ChatRoomImpl
				xml_aux = (String) references.get(i);
				obj_xml_aux = objectReference().getObjectXml(xml_aux);
				obj_ref_aux = new ObjectReference(obj_xml_aux.getObject(), obj_xml_aux.getHost(), obj_xml_aux.getPort());
				users.put(String.valueOf(i), obj_ref_aux);
			}
		} else if (reply_type.equals("error")) {
			echo("erro na mensagem de reply recebida");
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
		
		//Reply
		boolean result = false;
		String reply_type = req.getReplyType();
		if (reply_type.equals("return")) {
			result = req.getBoolean();
		} else if (reply_type.equals("error")) {
			echo("erro na mensagem de reply recebida");
		}
		
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
		
		//Reply
		boolean result = false;
		String reply_type = req.getReplyType();
		if (reply_type.equals("return")) {
			result = req.getBoolean();
		} else if (reply_type.equals("error")) {
			String msg = req.getString();
			List parsed = getParsed(msg, ":");
			String ref_aux = (String) parsed.get(0);
			String host = (String) parsed.get(1);
			String port = (String) parsed.get(2);
			echo("atualizando a referencia");
			this.objectReference().updateReference(ref_aux, host, port);
			echo("encaminhando o request para o endereco novo...");
			return leave(name);
		}
		
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
		
		String reply_type = req.getReplyType();
		if (reply_type.equals("error")) {
			String msg = req.getString();
			List parsed = getParsed(msg, ":");
			String ref_aux = (String) parsed.get(0);
			String host = (String) parsed.get(1);
			String port = (String) parsed.get(2);
			echo("atualizando a referencia");
			this.objectReference().updateReference(ref_aux, host, port);
		}
		
		req = null;
	}

}
