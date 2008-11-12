package xml;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import xml.CodecXmlObject;
import xml.SequenceStrings;

public class CodecXml extends CodecXmlObject{

	public CodecXml(){
		super();
	}
	
	public CodecXml(StringBuffer buffer) {
		setBuffer(buffer);
	}

	public void putString(String arg) {
		addField(4, "string", arg);
	}

	public void putInteger(int arg) {
		addField(4, "integer", String.valueOf(arg));		
	}

	public void putBoolean(boolean arg) {
		addField(4, "boolean", String.valueOf(arg));		
	}

	public void putDecimal(Double arg) {
		addField(4, "decimal", String.valueOf(arg));		
	}

	public void putObjectId(String key) {
		addField(4, "objectid", key);
	}

	public void putClassName(String arg) {
		addField(4, "classname", arg);
	}
	
	public String getString() {
		return getField("string");
	}

	public String getObjectId() {
		return getField("objectid");
	}
	
	public String getClassName() {
		return getField("classname");
	}

	public int getInteger() {
		return Integer.valueOf(getField("integer"));
	}

	public boolean getBoolean() {
		return Boolean.valueOf(getField("boolean"));
	}

	public boolean getDouble() {
		// TODO Auto-generated method stub
		return false;
	}
	
	////////////////////
	//ObjectReference //
	////////////////////
	public String getObjectReference(){
		return getField("object");
	}
	
	public String getObjectHost() {
		return getField("host");
	}
	
	public String getObjectPort() {
		return getField("port");
	}
	
	public void putObjectReference(String xml_reference) {
		addField(xml_reference);
	}
	
	public List getSequence (String field_name) {
		
		List fields = new ArrayList();
		
		StringBuffer new_buff = new StringBuffer();
		StringTokenizer st = new StringTokenizer(getBuffer().toString(),"\n");
		int count_tokens = st.countTokens();
		//echo("tokens count: "+st.countTokens());
		String linha ="";
		boolean found = false;
		while (st.hasMoreTokens()) {
			while (st.hasMoreTokens()) {
				linha = st.nextToken();
				if (linha.trim().equals("<"+field_name+">")) {
					found = true;
					break;
				}
			}
			if (found) {
				new_buff.append(linha+"\n");
				while (st.hasMoreTokens()) {
					linha = st.nextToken();
					if (linha.trim().equals("</"+field_name+">")) {
						new_buff.append(linha);
						fields.add(new_buff.toString());
						new_buff.delete(0, new_buff.length());
						found = false;
						break;
					}
					new_buff.append(linha+"\n");
				}
			}
		}
		
		return fields;
		
	}
	
	//Lista de ObjectReference dentro de uma sequence
	public List getSequenceReference() {
		return getSequence("reference");
	}
	
	public List getSequenceString() {

		String sequence_xml = getXmlStrip("sequence");
		
		XStream xstream = new XStream(new DomDriver());
		xstream.addImplicitCollection(SequenceStrings.class, "strings");
		xstream.alias("sequence", SequenceStrings.class);
		SequenceStrings sequence = (SequenceStrings) xstream.fromXML(sequence_xml);
		xstream = null;
		
		return sequence.getStrings();
		
	}

	//Extrai reference de uma sequence
	public String getObjectReferenceFromSequence() {
		String xml_ref = getXmlStrip("reference");
		
	    XStream xstream = new XStream(new DomDriver());
	    xstream.alias("reference", ObjectXmlReference.class);
	    ObjectXmlReference obj_xml_reference = (ObjectXmlReference) xstream.fromXML(xml_ref);
	    
		return obj_xml_reference.getObject();
	}
	
	public String getOperation(){
		return getField("operation");
	}
	
	public String getField(String field_name) {
		
	    String patternStr = "<"+field_name+">(.*?)</"+field_name+">";
	    String field_value = null;
	    
	    //System.out.println("[CodecXml] pattern: "+patternStr);
	    
	    //Regexp para extrair informacao do xml
	    Pattern pattern = Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(getBuffer().toString());
	    boolean matchFound = matcher.find();
	    
	    if (matchFound) {
	    	field_value = matcher.group(matcher.groupCount());
	    }else{
	    	echo("field tag "+field_name+" not found!");
	    }
	    
	    return field_value;
	}
	
	//Retorna parte do xml
	public String getXmlStrip(String field_name) {

		StringBuffer new_buff = new StringBuffer();
		StringTokenizer st = new StringTokenizer(getBuffer().toString(),"\n");
		int count_tokens = st.countTokens();
		//echo("tokens count: "+st.countTokens());

		String linha ="";
		boolean found = false;
		while (st.hasMoreTokens()) {
			linha = st.nextToken();
			if (linha.trim().equals("<"+field_name+">")) {
				found = true;
				break;
			}
		}
		if (found) {
			new_buff.append("<"+field_name+">\n");
			while (st.hasMoreTokens()) {
				linha = st.nextToken();
				if (linha.trim().equals("</"+field_name+">")) {
					new_buff.append("</"+field_name+">\n");
					found = false;
					break;
				}
				new_buff.append(linha+"\n");
			}
		}
		
		return new_buff.toString();
	}
	
	public String getReplyType(){
		String field_name = "result";
	    String patternStr = "<"+field_name+" type=\"(.*?)\">";
	    String field_value = null;
	    
	    //System.out.println("[CodecXml] pattern: "+patternStr);
	    
	    //Regexp
	    Pattern pattern = Pattern.compile(patternStr);
	    Matcher matcher = pattern.matcher(getBuffer().toString());
	    boolean matchFound = matcher.find();
	    
	    if (matchFound) {
	        // Get all groups for this match
	    	field_value = matcher.group(matcher.groupCount());
	    }else{
	    	echo("field tag "+field_name+" not found!");
	    }
	    
	    return field_value;
	}
	
	private void echo(String msg) {
		System.out.println("[CodecXml] "+msg);
	}
		
}
