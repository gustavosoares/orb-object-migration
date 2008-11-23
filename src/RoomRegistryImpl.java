import java.util.HashMap;
import java.util.Map;


public class RoomRegistryImpl extends RoomRegistrySkel {

	protected Map salas_registradas; //key: nome da sala, value: ChatRoom object
	
	public RoomRegistryImpl() {
		super();
		salas_registradas = new HashMap();
	}
	
	public RoomRegistryImpl(String key) {
		super(key);
		salas_registradas = new HashMap();
	}

	@Override
	public ChatRoom findRoom(String name) {
		// TODO Auto-generated method stub
		echo("findRoom -> "+name);
		if (salas_registradas.containsKey(name)){
			return (ChatRoom) salas_registradas.get(name);
		}else{
			echo("A sala "+name+" procurada nao existe!");
			return null;
		}
	}

	@Override
	public Map getRooms() {
		echo("getRooms");
		return salas_registradas;
	}

	/**
	 * Cria uma nova sala
	 */
	public ChatRoom newRoom(String name) {

		Map rooms = salas_registradas;
		if (! rooms.containsKey("name")){
			//crio impl do chatroom
			ChatRoom chatroom = new ChatRoomImpl(name);
			rooms.put(name, chatroom);
			echo("room created: "+name);
			return chatroom;
		}else{
			echo("room ["+name+"] ja existe!!!");
			return null;
		}
	}
	
	/**
	 * Registra um objeto chatroom migrado de forma transparente para o usuario
	 * @param name
	 * @param chatroom
	 */
	protected void register(String name, ChatRoom chatroom) {
		echo ("Sala "+name+" migrada registrada");
		salas_registradas.put(name, chatroom);
	}

	public Map filhos() {
		return salas_registradas;
	}

}
