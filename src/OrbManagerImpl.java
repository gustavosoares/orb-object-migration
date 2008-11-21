import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class OrbManagerImpl extends OrbManagerSkel {
	
	private ORB _orb = null;
	
	public OrbManagerImpl(ORB orb) {
		super();
		_orb = orb;
	}
	
	@Override
	boolean migrate(XmlMapper xmlmapper) {
		boolean result = true;
		// TODO Auto-generated method stub
		echo("Registrando os objetos recebidos");
		echo("Classname: "+xmlmapper.getClassname());
		if (xmlmapper.getClassname().equals("RoomRegistryImpl")) {
			RoomRegistryXml room_registry_xml = (RoomRegistryXml) xmlmapper;
			String key = room_registry_xml.getObjectid();
			String classname = room_registry_xml.getClassname();
			RoomRegistryImpl new_room_registry = new RoomRegistryImpl();
			_orb.updateKey(new_room_registry.objectReference().toString(), key);
			List lista_chatroom = room_registry_xml.getLista_chatroom();
		}else if (xmlmapper.getClassname().equals("ChatRoomImpl")) {
			
		}
		
		return result;
	}

	/**
	 * Lista os objetos registrados
	 */
	public void list() {
		Map table_registrados = _orb.getListaObjRegistrados();
		Iterator iterator = table_registrados.keySet().iterator();
		int i = 0;
		while (iterator.hasNext()) {
		   String name = (String) iterator.next();
		   if (!table_registrados.get(name).equals(this)){ //nao listo o proprio orbmanager
			   prompt(i+". "+name+" -> "+table_registrados.get(name));
			   i++;
		   }
		}
	}
	
	/**
	 * Lista os objetos migrados
	 */
	public void migrated() {
		// TODO Auto-generated method stub

	}
	
	public void prompt(String msg) {
		System.out.println("> "+msg);
	}

	private void registerObjectImpl(ObjectImpl objectimpl) {
		
	}
	
	private void echo(String msg){
		System.out.println("[OrbManagerImpl] "+msg);
	}
}
