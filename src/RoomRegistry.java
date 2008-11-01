import java.util.Map;


public abstract class RoomRegistry extends Object{
	abstract public ChatRoom newRoom(String name);
	abstract public ChatRoom findRoom(String name);
	abstract public Map getRooms();
}
