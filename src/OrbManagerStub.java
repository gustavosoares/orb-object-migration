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
			req.addObjectImplementation(obj_impl);
			//req.addObjectId(key);
			//String classname = obj_impl_aux.getClass().getName();
			//req.addClassName(classname);
			req.endStruct();
		}

		req.endSequence();
		req.endParameter();
		req.endXml();
		req.invoke();
		req = null;
	}

	
	private void echo(String msg) {
		System.out.println("[OrbManagerStub] "+msg);
	}
	
	private void getXml(ObjectImpl obj_impl) {
		XStream xstream = new XStream(new DomDriver());
		String xml = xstream.toXML(obj_impl);
		System.out.println("XML:");
		System.out.println(xml);
		System.out.println("---------");
	}

}
