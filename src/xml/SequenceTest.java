package xml;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class SequenceTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/*
		SequenceStrings sequence = new SequenceStrings();
		
		sequence.addStrings("gustavo");
		sequence.addStrings("silvia");
		
		
		XStream xstream = new XStream();

		xstream.addImplicitCollection(SequenceStrings.class, "strings");
		xstream.alias("sequence", SequenceStrings.class);
		*/
		
		SequenceReference sequence = new SequenceReference();
		
		ObjectXmlReference xml_1 = new ObjectXmlReference("bla", "localhost", "12");
		ObjectXmlReference xml_2 = new ObjectXmlReference("ble", "localhost", "1222");
		//sequence.addString("gustavo");
		sequence.addReference(xml_1);
		//sequence.addString("silvia");
		sequence.addReference(xml_2);
		XStream xstream = new XStream();

		xstream.addImplicitCollection(SequenceReference.class, "reference");
		//xstream.addImplicitCollection(SequenceReference.class, "strings");
		xstream.alias("sequence", SequenceReference.class);
		xstream.alias("reference", xml.ObjectXmlReference.class);
		
		String xml_sequence = xstream.toXML(sequence);
		System.out.println(xml_sequence);
		
		/////////////////////
		//Leitura do xml
		
		
		String field_name = "reference";
		
		StringBuffer new_buff = new StringBuffer();
		StringTokenizer st = new StringTokenizer(xml_sequence,"\n");
		int count_tokens = st.countTokens();
		echo("tokens count: "+st.countTokens());
		List fields = new ArrayList();
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
		
		for (int i=0; i < fields.size(); i++){
			echo("-------------------------");
			echo((String) fields.get(i));
		}
		

	}
	
	public static void echo(String msg) {
		System.out.println(msg);
	}

}
