import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class OrbManagerStub extends OrbManager {
	
	public OrbManagerStub(ObjectReference ref){
		objectReference(ref);
	}

	@Override
	public boolean migrate(XmlMapper xmlmapper) {
		//echo("findRom -> "+name);
		echo("migrate");
		Request req = createRequest ("migrate");
		req.beginParameter();
		req.beginSequence();
		req.beginString();
		req.addXmlMapper(xmlmapper);
		req.endString();

		req.endSequence();
		req.endParameter();
		req.endXml();
		req.invoke();
		
		//Leio o reply
		boolean result = req.getBoolean();
		req = null;
		return result;
		
	}
	
	/**
	 * Lista os objetos registrados
	 * Nao precisa ser implementado no Stubg
	 */
	public void list() {
		// TODO Auto-generated method stub

	}

	/**
	 * Lista objetos migrados. Nao precisa nao stub
	 */
	public void migrated() {
		// TODO Auto-generated method stub
		
	}
	
	private void echo(String msg) {
		System.out.println("[OrbManagerStub] "+msg);
	}

}
