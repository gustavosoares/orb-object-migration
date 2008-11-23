

public class XmlReply extends XmlObject {
	
	private String _id;
	private String _type;

	public XmlReply(long id, String type) {
		_id = String.valueOf(id);
		_type = type;
		_codec = new CodecXml();
		_codec.append("<reply id=\""+_id+"\">\n");
		_codec.append("  <result kind=\""+_type+"\">\n");
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
	/*
	public void setReplyKind(String type) {
		String tmp = _type;
		_type = type;
		echo("tmp: "+tmp);
		echo("new type: "+type);
		_codec.toString().replaceAll("<result kind=\""+tmp+"\">", "<result kind=\""+type+"\">");
		echo("Buffer");
		echo(_codec.toString());
	}*/
	
	private void echo(String msg) {
		System.out.println("[XmlReply] "+msg);
	}
}
