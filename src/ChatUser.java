
public abstract class ChatUser extends Object {
	
	abstract public void notifyJoin(String name, ChatUser user);
	abstract public void notifyLeave(String name);
	abstract public void notifyMessage(String sender, String message);
	abstract public String getUsername();
	  
}
