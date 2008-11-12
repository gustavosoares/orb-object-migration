import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xml.ObjectXmlReference;


public class OrbManagerStub extends OrbManager {

	public OrbManagerStub(ObjectReference ref){
		objectReference(ref);
	}
	
	@Override
	public void migrate(Map obj_impl) {
		//echo("findRom -> "+name);
		echo("migrate");
		Request req = createRequest ("migrate");
		req.beginParameter();

		req.addSequenceReference(obj_impl);

		req.endParameter();
		req.endXml();
		req.invoke();
		
	}

	
	private void echo(String msg) {
		System.out.println("[OrbManagerStub] "+msg);
	}

}
