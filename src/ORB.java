//package fixmico;

/**
 * <p>Title: ORB.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */
import java.util.*;
import java.io.*;

public class ORB
{
  private static ORB _orb= new ORB();
  private static String LOG_FILE = "orb_"+_orb.toString()+".log"; 
  private static Dictionary  _objKeyImplMap = new Hashtable();
  private Address _addr;
  private static BufferedWriter out = null;
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
    _transp = _addr.createTransport ();
    
	try {
		out = new BufferedWriter(new FileWriter(LOG_FILE, true));
		echo("LOG FILE: "+LOG_FILE);
	} catch (IOException e1) {
		e1.printStackTrace();
	}
	
  }

  public Address address()
  {
    return _addr;
  }

  public void registerObjectImpl(String ior, ObjectImpl impl)
  {
    _objKeyImplMap.put(ior, impl);
    //echo("objeto registrado: "+ior+" <-> "+impl);
  }
  
  public static ObjectImpl getObjectImpl(String ior) {
	  return (ObjectImpl) _objKeyImplMap.get(ior);
  }

  public static String objectToString(Object obj)
  {
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
	                ORB.log("referencia do objecto para invoke: "+req.getReference());
	                ObjectImpl impl = ORB.getObjectImpl(req.getReference());
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
	}finally{
		try {
			out.close();
		} catch (IOException e) {}
	}

  }
  
  public static void echo(String msg) {
	  System.out.println(new Date()+" [ORB] "+msg);
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
                ObjectImpl impl = ORB.getObjectImpl(req.getReference());
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










