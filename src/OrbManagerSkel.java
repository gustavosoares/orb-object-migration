import java.util.HashMap;
import java.util.List;
import java.util.Map;


abstract class OrbManagerSkel extends OrbManager implements ObjectImpl {

	public OrbManagerSkel() {
		
	    Address addr = ORB.instance().address();
	    ObjectReference ior = new ObjectReference ("IDL:Account:1.0", addr);
	    objectReference(ior);

	    ORB.instance().registerObjectImpl(ior.stringify(),this);
	}
	
	public void invoke(ServerRequest req) {
		boolean a = dispatch(req);
		assert (a):"dispatch Error";
	}
	
	protected boolean dispatch(ServerRequest req) {
		
		/**
		 * TODO: implementar as operacoes
		 */
		if (req.opname().equals("migrate")) {
			echo("migrate received");
			Map hash_obj = new HashMap();
			List struct_striped = req.getStructStriped();
			System.out.println("struct striped\n"+struct_striped);
			XmlMapper xml_mapper = req.getXmlMapper((String) struct_striped.get(0));
			
			boolean result = migrate(xml_mapper);
			req.putBooleanReply(result);
			
			return result;
			

		}
		
		return false;
	}
	
	private void echo(String msg){
		System.out.println("[OrbManagerSkel] "+msg);
	}
	
	private void echo(Object obj){
		System.out.println("[OrbManagerSkel] "+obj);
	}
	
}
