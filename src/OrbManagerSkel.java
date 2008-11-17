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
			List struct = req.getStruct();
			List struct_striped = req.getStructStriped();
			System.out.println("struct\n"+struct);
			System.out.println("struct striped\n"+struct_striped);
			//Constroi o HashMap a partir dos xml reference e registra no orb
			/*
			String xml_aux = "";
			ObjectXmlReference obj_xml_aux = null;
			ObjectReference obj_ref_aux = null;
			for (int i=0; i < references.size(); i++){

				xml_aux = (String) references.get(i);
				obj_xml_aux = objectReference().getObjectXml(xml_aux);
				obj_ref_aux = new ObjectReference(obj_xml_aux.getObject(), obj_xml_aux.getHost(), obj_xml_aux.getPort());

				hash_obj.put(obj_xml_aux.getObject(), obj_ref_aux);
			}
			*/

			boolean result = migrate(hash_obj);
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
