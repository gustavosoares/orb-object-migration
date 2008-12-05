import java.lang.reflect.*;
import java.util.Map;


public class TestReflection {

	public static void main(String[] args) {
		
	    String server_host = "localhost";
	    int server_port = 5555;
	    if (args.length == 1){
	    	try {
	    		server_port = Integer.valueOf(args[0]);
	    	}catch(Exception e){
	    		System.out.println("erro na conversao da porta. Sera usada a porta 5555");
	    		server_port = 5555;
	    	}
	    }

	    TCPAddress addr = new TCPAddress (server_host, server_port);
	    ORB.instance().address(addr);
	    
	    String ior = "";
	    
	    ObjectImpl obj_impl = null;
	    //obj_impl = new RoomRegistryImpl();
	    String classname = "RoomRegistryImpl";
	    
		Class c = null;
		try {
			c = Class.forName(classname);
			Class partypes[] = new Class[0];
			Constructor ct = c.getConstructor(partypes);
			Object arglist[] = new Object[0];
			obj_impl = (ObjectImpl) ct.newInstance(arglist);
			Map temp = obj_impl.filhos();
			
			System.out.println(temp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("------");

		classname = classname.replaceAll("Impl", "Stub");		
		System.out.println(classname);
		
		Class clazz = null;
		try {
			clazz = Class.forName(classname);
			Class[] parameter = new Class[1];
			parameter[0] = ObjectReference.class;
			ObjectReference ref = new ObjectReference("aaa",server_host,String.valueOf(server_port));
			Constructor ct = clazz.getConstructor(parameter);
			ObjectReference arglist[] = new ObjectReference[1];
			arglist[0] = ref;
			Object new_proxy = (Object) ct.newInstance(arglist);
			Method[] methods = clazz.getDeclaredMethods();
			Method selectedMethod = null;
			for (int i = 0; i < methods.length; i++){
				System.out.println(methods[i]);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}
