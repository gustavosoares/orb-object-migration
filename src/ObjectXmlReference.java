
/**
 * Classe usada para construcao do xml com a referencia do objeto
 * @author gustavosoares
 *
 */
public class ObjectXmlReference {
	private String object;
	private String host;
	private String port;
	
	public ObjectXmlReference(String object, String host, String port){
		echo("ref: "+object);
		this.object = object;
		this.host = host;
		this.port = port;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
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
	
	private void echo(String msg) {
		System.out.println("[ObjectXmlReference] "+msg);
	}
	
}
