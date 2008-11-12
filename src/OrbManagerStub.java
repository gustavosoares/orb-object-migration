import java.util.HashMap;
import java.util.Iterator;
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
		req.beginSequence();
		Iterator iterator = obj_impl.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			ObjectImpl obj_impl_aux = (ObjectImpl) obj_impl.get(key);
			req.beginStruct();
			req.addObjectId(key);
			String classname = obj_impl_aux.getClass().getName();
			echo("classname: "+classname);
			req.endStruct();
		}
		//req.addSequenceReference(obj_impl);
		req.endSequence();
		req.endParameter();
		req.endXml();
		req.invoke();
		
	}

	
	private void echo(String msg) {
		System.out.println("[OrbManagerStub] "+msg);
	}

}
