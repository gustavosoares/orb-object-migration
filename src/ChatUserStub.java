
public class ChatUserStub extends ChatUser {
	
	private ObjectReference _ref;
	
	public ChatUserStub(ObjectReference ref) {
		_ref = ref;
		objectReference(_ref);
	}
	
	@Override
	public void notifyJoin(String name, ChatUser user) {
		// TODO Auto-generated method stub
		Request req = createRequest ("notifyJoin");
		req.beginParameter();
		req.beginSequence();
		req.addSequence("string", name);
		req.addObjectReference(user.objectReference().getXmlReference());
		req.endSequence();
		req.endParameter();
		req.endXml();
		req.invoke();
		req = null;
	}

	@Override
	public void notifyLeave(String name) {

		Request req = createRequest ("notifyLeave");
		req.beginParameter();
		req.addString(name);
		req.endParameter();
		req.endXml();
		req.invoke();
		req = null;
		
	}

	@Override
	public void notifyMessage(String sender, String message) {

		Request req = createRequest ("notifyMessage");
		req.beginParameter();
		req.beginSequence();
		req.addSequence("string", sender);
		req.addSequence("string", message);
		req.endSequence();
		req.endParameter();
		req.endXml();
		req.invoke();
		req = null;
		
	}

	@Override
	public String getUsername() {
		Request req = createRequest ("getUsername");
		req.beginParameter();
		req.endParameter();
		req.endXml();
		req.invoke();
		
		//Reply
		String name = null;
		String reply_type = req.getReplyType();
		if (reply_type.equals("return")) {
			name = req.getString();
		} else {
			echo("erro na mensagem de reply recebida");
		}

		req = null;
		return name;
	}

	public ObjectReference getObjectReference(){
		return _ref;
	}
	
}
