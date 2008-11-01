import java.util.HashMap;
import java.util.Map;


public abstract class ChatRoom extends Object{
	
	abstract public String getName();
	abstract public Map getUsers();
	abstract public boolean join(String name, ChatUser user);
	abstract public boolean leave(String name);
	abstract public void send(String name, String message);
	
}
