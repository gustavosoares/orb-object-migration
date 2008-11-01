package xml;

import java.util.ArrayList;
import java.util.List;

public class SequenceStrings {

	private List strings = new ArrayList();
	
	public SequenceStrings() {
		
	}
	
	public void addStrings(String value){
		strings.add(value);
	}
	
	public List getStrings() {
		return strings;
	}
	
}
