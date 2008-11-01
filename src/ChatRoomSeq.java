import java.util.ArrayList;


public class ChatRoomSeq {
	private ArrayList chat_room_seq;
	
	public ChatRoomSeq(ArrayList array){
		chat_room_seq = array;
	}
	
	public void addChatRoom(ChatRoom chatroom){
		chat_room_seq.add(chatroom);
	}
	
	public ArrayList getRooms(){
		return chat_room_seq;
	}
}
