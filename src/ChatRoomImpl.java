import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class ChatRoomImpl extends ChatRoomSkel {

	private String _name;
	private Map _users; //key: nome do usuario, value: ChatUser object
	
	public ChatRoomImpl() {
		super();
		_users = new HashMap();
	}
	
	public ChatRoomImpl(String name, String key) {
		super(key);
		_name = name;
		_users = new HashMap();
	}
	
	public ChatRoomImpl(String name) {
		super();
		_name = name;
		_users = new HashMap();
	}
	
	@Override
	public String getName() {
		echo("name: "+ _name);
		return _name;
	}

	@Override
	public Map getUsers() {
		echo("getUsers");
		return _users;
	}
	
	protected void register(String name, ChatUser user) {
		echo("Usuario "+name+" migrado registrado");
		_users.put(name, user);
	}
	
	@Override
	public boolean join(String name, ChatUser user) {

		if (_users.containsKey(name) ) {
			echo("usuario ["+name+"] ja existe na sala!");
			return false;
		}else{
			echo("join -> "+name);
			echo("chatuser -> "+user.objectReference().stringify());
			
			_users.put(name, user);
			//notify dos demais usuarios

			Iterator iterator = _users.keySet().iterator();
			while (iterator.hasNext()) {
			   String key = (String) iterator.next();
			   ChatUser chatuser = (ChatUser) _users.get(key);
			   echo("Notificando o usuario "+chatuser.getUsername()+" -> "+name);
			   chatuser.notifyJoin(name, user);
			}

			return true;
		}
	}

	@Override
	public boolean leave(String name) {
		// TODO Auto-generated method stub
		if (_users.containsKey(name)){
			echo("leave -> "+name);
			Iterator iterator = _users.keySet().iterator();
			while (iterator.hasNext()) {
			   String key = (String) iterator.next();
			   ChatUser chatuser = (ChatUser) _users.get(key);
			   chatuser.notifyLeave(name);
			}
			_users.remove(name);
			return true;
		}else{
			echo("usuario "+name+" nao encontrado na sala e por isso nao sera removido!!");
			return false;
		}
	}

	@Override
	public void send(String name, String message) {
		echo("sent from ["+name+"] msg: "+message);
		Iterator iterator = _users.keySet().iterator();
		while (iterator.hasNext()) {
		   String key = (String) iterator.next();
		   ChatUser chatuser = (ChatUser) _users.get(key);
		   chatuser.notifyMessage(name, message);
		}		
	}

	public Map filhos() {
		return _users;
	}

}
