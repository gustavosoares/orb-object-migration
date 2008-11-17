

import java.util.ArrayList;
import java.util.List;

public class ChatRoomXml implements XmlMapper {
	private String name;
	private String objectid;
	private String classname;
	private List<ChatUserXml> chatuserstub = new ArrayList();
	
	public ChatRoomXml(String name, String objectid, String classname) {
		this.name = name;
		this.objectid = objectid;
		this.classname = classname;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
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
	
	public List<ChatUserXml> getLista_chatuser() {
		return chatuserstub;
	}
	
	public void addChatUser(ChatUserXml chatuser_xml) {
		chatuserstub.add(chatuser_xml);
	}
	
}
