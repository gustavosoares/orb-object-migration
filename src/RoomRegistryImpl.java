import java.util.HashMap;
import java.util.Map;


public class RoomRegistryImpl extends RoomRegistrySkel {

	protected Map salas_registradas; //key: nome da sala, value: ChatRoom object
	
	public RoomRegistryImpl() {
		super();
		salas_registradas = new HashMap();
	}
	
	@Override
	public ChatRoom findRoom(String name) {
		// TODO Auto-generated method stub
		echo("findRoom -> "+name);
		if (salas_registradas.containsKey(name)){
			return (ChatRoom) salas_registradas.get(name);
		}else{
			return null;
		}
	}

	@Override
	public Map getRooms() {
		echo("getRooms");
		return salas_registradas;
	}

	@Override
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
	
	private void echo(String msg){
		System.out.println("[RoomRegistryImpl] "+msg);
	}

}
