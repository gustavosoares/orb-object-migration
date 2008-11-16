

public class ChatUserXml {
	
	private String name;
	private String classname;
	private String reference;
	private String host;
	private String port;
	
	public ChatUserXml(String name, String classname,
			String reference, String host, String port) {

		this.name = name;
		this.classname = classname;
		this.reference = reference;
		this.host = host;
		this.port = port;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassname() {
		return classname;
	}

	public void setClassname(String classname) {
		this.classname = classname;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
	
	public ObjectXmlReference getObjectXmlReference(){
		return new ObjectXmlReference(getReference(),getHost(), getPort());
	}

}
