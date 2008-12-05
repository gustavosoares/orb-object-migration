//package fixmico;

/**
 * <p>Title: ORB.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */
import java.util.*;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class ORB
{
  private static ORB _orb= new ORB();
  private static String LOG_FILE = "orb_"+_orb.toString()+".log"; 
  private static Map  _objKeyImplMap = new HashMap();
  private static Map  _objKeyMigrated = new HashMap();
  private Address _addr;
  private static BufferedWriter out = null;
  private static ObjectImpl _mainObjImpl = null;
  Transport _transp;

  public ORB()
  {
    _addr = null;
  }

  public static ORB instance()
  {
    return _orb;
  }

  public void address(Address addr)
  {
    _addr = addr;
    _transp = _addr.createTransport();
    
	try {
		out = new BufferedWriter(new FileWriter(LOG_FILE, true));
		echo("LOG FILE: "+LOG_FILE);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	
  }

  public Address address() {
	  return _addr;
  }

  /**
   * Registra esqueletos
   * @param ior
   * @param impl
   */
  public void registerObjectImpl(String ior, ObjectImpl impl) {
	  if (_objKeyMigrated.containsKey(ior)) {
		  removeMigrated(ior);
	  }
	  if (_objKeyImplMap.containsKey(ior)) {
		  echo("A chave "+ior+" esta registrada");
	  }
	  _objKeyImplMap.put(ior, impl);
	  echo("objeto registrado: "+ior+" <-> "+impl);
  }
  
  /**
   * Registra objetos do tipo proxy
   * @param ior
   * @param proxy
   */
  public static void registerObject(String ior, Object proxy) {
	  if (_objKeyImplMap.containsKey(ior)) {
		  echo("A chave "+ior+" esta registrada... sobreescrevendo");
	  }
	  _objKeyImplMap.put(ior, proxy);
	  echo("objeto proxy registrado: "+ior+" <-> "+proxy);
  }
  
  public void updateKey(String oldkey, String newkey){
	  if (_objKeyImplMap.containsKey(oldkey)) {
		  echo("A chave "+oldkey+" sera atualizada");
		  ObjectImpl object_impl = (ObjectImpl) getObject(oldkey);
		  _objKeyImplMap.put(newkey, object_impl);
		  echo("Chave atualizada para "+newkey);
		  _objKeyImplMap.remove(oldkey);
	  }else{
		  echo("A chave "+oldkey+" nao existe no ORB");
	  }
	  
  }
  
  /**
   * Obtem um ObjImpl no hashtable a partir de uma chave
   * @param ior
   * @return
   */
  public static Object getObject(String ior) {
	  Object obj = null; 
	  obj = (Object) _objKeyImplMap.get(ior);
	  return obj;
	  //return (ObjectImpl) _objKeyImplMap.get(ior);
  }

  /**
   * Obtem lista de objetos registrados no hashtable
   * @return
   */
  public static Map getListaObjRegistrados(){
	  	return _objKeyImplMap;
	  	/*
	  	List array_registrados = new ArrayList(); 
		Iterator iterator = _objKeyImplMap.keySet().iterator();
		while (iterator.hasNext()) {
		   String name = (String) iterator.next();
		   array_registrados.add(name);
		}
		return array_registrados;
		*/
  }
  
  /**
   * Adiciona chave do objeto (ior) na lista de migrados
   * @param ior
   */
  public static void addMigrated(String key, ObjectReference object_reference){
	  echo("key -> "+key+" migrada");
	  _objKeyMigrated.put(key, object_reference);
  }
  
	/**
	 * Registra objetos migrados no ORB
	 */
	public static void registraMigrados(Map hashmap, OrbManagerStub orb_manager_stub){
		if (hashmap != null) {
			Iterator iterator = hashmap.keySet().iterator();
			while (iterator.hasNext()) {
			   String key = (String) iterator.next();
			   ObjectImpl object_impl = null;
			   Object object = null;
			   Map hashmap_filho = null;
			   String key_aux = "";
			   object = (Object) hashmap.get(key);
			   if (object instanceof ObjectImpl) {
				   key_aux = object.objectReference().stringify();
				   object_impl = (ObjectImpl) object;
				   hashmap_filho = object_impl.filhos();
				   ORB.instance().addMigrated(key_aux, orb_manager_stub.objectReference());
				   
				   //Criar proxy?
				   if (! object_impl.equals(ORB.getMainSkel())) {
					   String newhost = orb_manager_stub.objectReference().getHost();
					   String newport = String.valueOf(orb_manager_stub.objectReference().getPort());
					   echo("Criando proxy para "+object_impl.getKey()+" em "+newhost+":"+newport);
					   String classname = object_impl.getClass().getName();
					   classname = classname.replaceAll("Impl", "Stub");
					   Class clazz = null;
					   try {
						   clazz = Class.forName(classname);
						   Class[] parameter = new Class[1];
						   parameter[0] = ObjectReference.class;
						   ObjectReference ref = new ObjectReference(object_impl.getKey(),newhost,newport);
						   Constructor ct = clazz.getConstructor(parameter);
						   ObjectReference arglist[] = new ObjectReference[1];
						   arglist[0] = ref;
						   Object new_proxy = (Object) ct.newInstance(arglist);
						   registerObject(object_impl.getKey(), new_proxy);
					   } catch (Exception e) {
						   e.printStackTrace();
					   }
				   }
			   }
			   registraMigrados(hashmap_filho, orb_manager_stub);
			}
		}
	}
	
  /**
   * Remove um objeto da lista de migrados
   * @param key
   */
  public static void removeMigrated(String key) {
	  _objKeyMigrated.remove(key);
  }
  /**
   * Verifica se um objeto impl foi migrado
   * @param reference
   * @return
   */
  private boolean isMigrated(String reference) {
	  return _objKeyMigrated.containsKey(reference);
  }
  /**
   * Obtem HashMap dos objetos migrados
   * @return
   */
  public static Map getListaObjMigrados() {
	  return _objKeyMigrated;
  }
  
  public static String objectToString(Object obj) {
    return obj.objectReference().stringify();
  }

  public void run() {
	
    // Enter endless loop to service client requests
	try {
	    while (true) {
	    	
	        // Wait for a client to connect
	    	log("esperando conexao");
	        _transp.accept ();	        
	        //new trataConexao(_transp).start();
	        
	        ORB.log("conexao recebida");
	        try{
	            while (true) {
	            	PDUXml pdu = new PDUXml(_transp);
	                long pdu_type = pdu.recvNextPdu();
	                
	                if (pdu_type == -1) {
	                    _transp.close ();
	                    break;
	                }

	                // Check PDU type
	                assert pdu_type == 0;

	                ServerRequest req = new ServerRequest(pdu); 
	                ////////////////////////////////
	                // VERIFICANDO SE FOI MIGRADO //
	                ////////////////////////////////
	                String ref_aux = req.getReference();
	                Object obj = (Object) ORB.getObject(ref_aux);

	                if (isMigrated(ref_aux)) {
	                	ObjectReference reference = (ObjectReference) _orb.getListaObjMigrados().get(ref_aux);
	                	String host = reference.getHost();
	                	String port = String.valueOf(reference.getPort());
	                	req.setReplyType("error");
	                	req.putStringReply(ref_aux+":"+host+":"+port);
			            req.sendReply();
			            _transp.close();
			            break;
	                } else {
		                ObjectImpl impl = (ObjectImpl) ORB.getObject(ref_aux);
		                if (impl == null){
		                	ORB.echo("Objeto para invoke "+ ref_aux+" nao foi encontrado no ORB");
				            req.sendReply();
				            _transp.close();
				            break;
		                }else{
		                	impl.invoke(req); //invoke servant                	
				            //Envio mensagem de reply              	
				            req.sendReply();
				            _transp.close();
				            break;
		                }
	                }
             
	                //if (pdu_type == 1) ORB.echo("PDU de reply");
	            }
	        }catch (Exception e){
	        	e.printStackTrace();
	        	ORB.log("A conexao sera encerrada");
	            _transp.close ();       	
	        }
	    }
	}finally{
		try {
			out.close();
		} catch (IOException e) {}
	}

  }
  
  public static void echo(String msg) {
	  //System.out.println(new Date()+" [ORB] "+msg);
	  System.out.println("[ORB] "+msg);
  }
  
  public static void log(String string) {
	  
	  //echo(string);
	  
	try {
		out.write(new Date()+" "+string+"\n");
		out.flush();
	} catch (IOException e) {
		e.printStackTrace();
	}
	
  }

	public static void setMainSkel(ObjectImpl MainObjImpl) {
		_mainObjImpl = MainObjImpl;
	}
	
	public static ObjectImpl getMainSkel() {
		return _mainObjImpl;
	}

}


class trataConexao extends Thread {
	
	Transport _transp;
	
	public trataConexao(Transport transp) {
		_transp = transp;
	}
	
	public void run(){
		
        ORB.log("conexao recebida");
        try{
            while (true) {
            	PDUXml pdu = new PDUXml(_transp);
                long pdu_type = pdu.recvNextPdu();
                
                if (pdu_type == -1) {
                    _transp.close ();
                    break;
                }
                ORB.echo("PDU TYPE: "+pdu_type);
                // Check PDU type
                assert pdu_type == 0;

                ServerRequest req = new ServerRequest(pdu); 
                ORB.log("referencia do objecto para invoke: "+req.getReference());
                ObjectImpl impl = (ObjectImpl) ORB.getObject(req.getReference());
                if (impl == null){
                    // Object key doesn't exist
                	ORB.echo("Objeto "+ req.getReference()+" nao foi encontrado no ORB");
                }else{

                	impl.invoke(req); //chamado Skel                	
		            //Envio mensagem de reply              	
		            req.sendReply();
		            _transp.close();
		            break;
                }
                
                if (pdu_type == 1) ORB.echo("PDU de reply");
            }
        }catch (Exception e){
        	e.printStackTrace();
        	ORB.log("A conexao sera encerrada");
            _transp.close ();       	
        }
        
	}
}










