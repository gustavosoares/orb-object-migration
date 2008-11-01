package xml;

public class XmlReply extends XmlObject {
	
	private String _id;
	private String _type;

	public XmlReply(long id, String type) {
		_id = String.valueOf(id);
		_type = type;
		_codec = new CodecXml();
		_codec.append("<reply id=\""+_id+"\">\n");
		_codec.append("  <result type=\""+_type+"\">\n");
	}
	
	public void endXml(){
		_codec.append("</reply>");
	}
	
	public void endResult(){
		_codec.addField(1, "result", "END");
	}
	
	public void addInteger(int value){
		_codec.addField(2, "integer", String.valueOf(value));
	}

	public void addString(String value){
		_codec.addField(2, "string", value);
	}
}
