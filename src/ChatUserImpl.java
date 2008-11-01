
public class ChatUserImpl extends ChatUserSkel {
	
	private String _username;

	public ChatUserImpl(String username) {
		super();
		_username = username;
	}
	
	@Override
	public void notifyJoin(String name, ChatUser user) {
		// TODO Auto-generated method stub
		show("Usuario "+name+" entrou na sala");
	}

	@Override
	public void notifyLeave(String name) {
		// TODO Auto-generated method stub
		show("Usuario "+name+" saiu da sala");
	}

	@Override
	public void notifyMessage(String sender, String message) {
		// TODO Auto-generated method stub
		show("["+sender+"] disse: "+message);
		
	}
	
	public String getUsername() {
		return _username;
	}
	
	public void show(String msg) {
		System.out.println("> "+msg);
	}

}
