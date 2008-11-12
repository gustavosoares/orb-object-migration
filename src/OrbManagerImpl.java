import java.util.HashMap;
import java.util.Map;


public class OrbManagerImpl extends OrbManagerSkel {
	
	private ORB _orb = null;
	
	public OrbManagerImpl(ORB orb) {
		super();
		_orb = orb;
	}
	
	@Override
	public void migrate(Map obj_impl) {
		// TODO Auto-generated method stub
		echo("Registrando os objetos recebidos");
		System.out.println(obj_impl);
		
	}
	
	
	private void echo(String msg){
		System.out.println("[OrbManagerImpl] "+msg);
	}

	public void show(String msg) {
		System.out.println("> "+msg);
	}

}
