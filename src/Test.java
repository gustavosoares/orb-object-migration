import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		/////Teste de regexp
	    //String patternStr = "^\\$(\\w+)(\\s+(.*))?$";
		String field_name = "sequence";
		//String patternStr = "^\\s+?<"+field_name+">(.*?)</"+field_name+">";
		//String patternStr = "(\\n)";
		String replaceStr = "";
	    String patternStr = "<"+field_name+">(\\s?(.*\\s?)+)</"+field_name+">\\n?";
	    Pattern pattern = Pattern.compile(patternStr);
		StringBuffer _buffer_xml = null;
		_buffer_xml = new StringBuffer();
		//_buffer_xml.append("<reference>ZZZZZZZZ</reference>\n");
		_buffer_xml.append("<request id=\"4\">\n");
		_buffer_xml.append("  <object>ObjectReference@21f34</object>\n");
		_buffer_xml.append("  <operation>join</operation>\n");
		_buffer_xml.append("  <parameters>\n");
		_buffer_xml.append("    <sequence>\n");
		_buffer_xml.append("      <string>gustavo</string>\n");
		_buffer_xml.append("<reference>\n");
		_buffer_xml.append("   <object>ObjectReference@2b249</object>\n");
		_buffer_xml.append("   <host>localhost</host>\n");
		_buffer_xml.append("   <port>5555</port>\n");
		_buffer_xml.append("</reference>\n");
		_buffer_xml.append("    </sequence>\n");
		_buffer_xml.append("  </parameters>\n");
		_buffer_xml.append("</request>\n");
		echo (_buffer_xml.toString());
		
		
		StringBuffer new_buff = new StringBuffer();
		StringTokenizer st = new StringTokenizer(_buffer_xml.toString(),"\n");
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

		echo("-----------\n"+new_buff.toString());
		System.exit(0);
		
		echo(_buffer_xml.toString());
	    Matcher matcher = pattern.matcher(_buffer_xml.toString());
	    
	    /*
	    String output = matcher.replaceAll(replaceStr);
	    System.out.println("*****");
	    System.out.println(output);
	    System.out.println("*****");
	    */
	    boolean matchFound = matcher.find();
	    
	    if (matchFound) {
	    	System.out.println("*****");
	    	
	    	for (int i=0; i<= matcher.groupCount(); i++){
	    		System.out.println(i+" -> "+matcher.group(i));
	    	}
	    	
	    	//System.out.println(matcher.group(1).trim());
	    	System.out.println("*****");
	    	echo("Match found!!");
	    }else{
	    	echo("Match not found!!");
	    }
		
	}
	
	public static void echo(String msg) {
		System.out.println(msg);
	}

}
