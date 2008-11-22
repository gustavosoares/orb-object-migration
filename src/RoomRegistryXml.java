

import java.util.ArrayList;
import java.util.List;

public class RoomRegistryXml implements XmlMapper{
	
	private String objectid;
	private String classname;
	private List<ChatRoomXml> chatroomskel = new ArrayList();
	
	public RoomRegistryXml(String objectid, String classname) {
		this.objectid = objectid;
		this.classname = classname;
	}
	
	public String getObjectid() {
		return objectid;
	}
	
	public void setObjectid(String objectid) {
		this.objectid = objectid;
	}
	
	public String getClassname() {
		return classname;
	}
	
	public void setClassname(String classname) {
		this.classname = classname;
	}
	
	public List<ChatRoomXml> getLista_chatroom() {
		return chatroomskel;
	}
	
	public void addChatroom(ChatRoomXml chatroom_xml) {
		chatroomskel.add(chatroom_xml);
	}

	public List filhos() {
		return chatroomskel;
	}
	
	
}
