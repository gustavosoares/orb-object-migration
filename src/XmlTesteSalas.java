

import java.util.List;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XmlTesteSalas {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String objectid = "ObjectReference@c3c749";
		String classname = "RoomRegistryImpl";
		RoomRegistryXml roomregistryxml = new RoomRegistryXml(objectid, classname);
		//adiciono o chatroom
		ChatRoomXml chatroomxml_aux = new ChatRoomXml("rio","id1", "ChatRoomImpl");
		roomregistryxml.addChatroom(chatroomxml_aux);
		roomregistryxml.addChatroom(new ChatRoomXml("sp","id2", "ChatRoomImpl"));
		roomregistryxml.addChatroom(new ChatRoomXml("minas","id3", "ChatRoomImpl"));
		
		//adiciono o chatuser
		chatroomxml_aux.addChatUser(new ChatUserXml("gustavo", "id1", "ChatUserStub", "abc123", "localhost", "5555"));
		
		//ENCODE XML
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("roomregistry", RoomRegistryXml.class);
		xstream.alias("chatroom", ChatRoomXml.class);
		xstream.alias("chatuser", ChatUserXml.class);
		xstream.addImplicitCollection(RoomRegistryXml.class, "chatroomskel");
		xstream.addImplicitCollection(ChatRoomXml.class, "chatuserstub");
		String xml = xstream.toXML(roomregistryxml);
		
		
		System.out.println("XML:");
		System.out.println(xml);
		System.out.println("---------");
		
		//DECODE XML
		System.out.println("decoding xml...");
		xstream.alias("roomregistry", RoomRegistryXml.class);
		xstream.alias("chatroom", ChatRoomXml.class);
		RoomRegistryXml roomregistryxml_decoded = (RoomRegistryXml) xstream.fromXML(xml);
		List lista_chatrooms = roomregistryxml_decoded.getLista_chatroom();
		for (int i=0; i < lista_chatrooms.size(); i++) {
			ChatRoomXml chatroomxml = (ChatRoomXml) lista_chatrooms.get(i);
			System.out.println("-----");
			System.out.println(chatroomxml.getName());
			System.out.println(chatroomxml.getObjectid());
			System.out.println(chatroomxml.getClassname());
			System.out.println("-----");
		}

		System.out.println("Done");
	}

}
