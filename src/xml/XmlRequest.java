package xml;

import java.util.ArrayList;
import java.util.List;

public class XmlRequest extends XmlObject{

	private String _id;
	private String _object;
	private String _operation;
	
	private String id;
	private String object;
	private String operation;
	private List parameters = new ArrayList();	
	
	public XmlRequest(String id, String object, String operation){
		_id = id;
		_object = object;
		_operation = operation;
		_codec = new CodecXml();
		_codec.append("<request id=\""+_id+"\">\n");
		_codec.addField(1, "object", _object);
		_codec.addField(1, "operation", _operation);

	}
	
	public void endXml(){
		_codec.append("</request>");
	}
	
	public void beginParameter(){
		_codec.addField(1, "parameters", "BEGIN");
	}
	
	public void endParameter(){
		_codec.addField(1, "parameters", "END");
	}

	public void beginSequence(){
		_codec.addField(2, "sequence", "BEGIN");
	}
	
	public void endSequence(){
		_codec.addField(2, "sequence", "END");
	}
	
	public void addSequence(String type, String value){
		_codec.addField(3, type, value);
	}
	
	public void addSequenceObjectReference(String xml_code){
		_codec.addField(xml_code);
	}
	
	public void addObjectReference(String xml_code) {
		_codec.addField(xml_code);
	}
}
