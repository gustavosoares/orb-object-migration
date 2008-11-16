

import java.util.ArrayList;
import java.util.List;

public class SequenceReference {

	//private List strings = new ArrayList();
	private List reference = new ArrayList();
	
	public SequenceReference() {
		
	}
	
	public void addReference(ObjectXmlReference value){
		reference.add(value);
	}
	
	public List getReference() {
		return reference;
	}
	
	/*
	public void addString(String value){
		strings.add(value);
	}
	
	public List getStrings() {
		return strings;
	}
	*/
}
