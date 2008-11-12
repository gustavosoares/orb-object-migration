import java.util.HashMap;
import java.util.List;
import java.util.Map;

import xml.ObjectXmlReference;


abstract class OrbManagerSkel extends OrbManager implements ObjectImpl {

	public OrbManagerSkel() {
		
	    Address addr = ORB.instance().address();
	    ObjectReference ior = new ObjectReference ("IDL:Account:1.0", addr);
	    objectReference (ior);

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
			Map hash_obj = new HashMap();
			List references = req.getSequenceReference();
			//Constroi o HashMap a partir dos xml reference e registra no orb
			String xml_aux = "";
			ObjectXmlReference obj_xml_aux = null;
			ObjectReference obj_ref_aux = null;
			for (int i=0; i < references.size(); i++){

				xml_aux = (String) references.get(i);
				obj_xml_aux = objectReference().getObjectXml(xml_aux);
				obj_ref_aux = new ObjectReference(obj_xml_aux.getObject(), obj_xml_aux.getHost(), obj_xml_aux.getPort());

				hash_obj.put(obj_xml_aux.getObject(), obj_ref_aux);
			}
			
			migrate(hash_obj);
			return true;
		}
		
		return false;
	}
	
	private void echo(String msg){
		System.out.println("[OrbManagerSkel] "+msg);
	}
	
}
