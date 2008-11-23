

public class CodecXmlObject {
	
	private StringBuffer xml_buffer = new StringBuffer();
	private StringBuffer tmp_xml_buffer = new StringBuffer();
	
	public CodecXmlObject(){
		
	}
	
	public String toString(){
		return xml_buffer.toString();
	}
	
	public void addField(int tab, String field_name, String field_value){
		for (int i = 0; i < tab; i++) {
			append("  ");
		}
		if (field_value.equals("BEGIN")) {
			append("<"+field_name+">");
		}else if (field_value.equals("END")){
			append("</"+field_name+">");
		}else{
			append("<"+field_name+">"+field_value+"</"+field_name+">");
		}
		
		append("\n");
			
	}
	
	public void addFieldWithAtribute(int tab, String field_name, String field_value, String atribute_name, String atribute_value){
		for (int i = 0; i < tab; i++) {
			append("  ");
		}
		if (field_value.equals("BEGIN")) {
			append("<"+field_name+" "+atribute_name+"="+"\""+atribute_value+"\">");
		}else if (field_value.equals("END")){
			addField(tab, field_name, field_value);
		}else{
			append("<"+field_name+">"+field_value+"</"+field_name+">");
		}
		
		append("\n");
			
	}
	
	public void append(String str){
		xml_buffer.append(str);
	}
	
	public StringBuffer getBuffer() {
		return xml_buffer;
	}
	
	public void clearBuffer() {
		xml_buffer.delete(0, xml_buffer.length());
	}
	
	public void setBuffer(StringBuffer buffer){
		xml_buffer = buffer;
	}

	public void addField(String xml_code) {
		// TODO Auto-generated method stub
		append(xml_code);
	}
	
	public void setTmpBuffer(StringBuffer buffer) {
		tmp_xml_buffer = buffer;
	}
	
	public StringBuffer getTmpBuffer() {
		return tmp_xml_buffer;
	}
	
	public void clearTmpBuffer() {
		tmp_xml_buffer.delete(0, tmp_xml_buffer.length());
	}
	
}
