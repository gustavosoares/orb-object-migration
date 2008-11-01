
public class UserInfo {
	private String name;
	private ChatUser user;
	
	public UserInfo(String name, ChatUser user) {
		super();
		this.name = name;
		this.user = user;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ChatUser getUser() {
		return user;
	}

	public void setUser(ChatUser user) {
		this.user = user;
	}
	
}
