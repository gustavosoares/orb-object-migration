//package fixmico;

/**
 * <p>Title: ObjectReference.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */
import java.util.*;
import java.io.*;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class ObjectReference
{
  private Address   			_addr;
  private Transport 			_transp;
  private  String   			_typeId;
  private  String   			_host;
  private  int      			_port;
  private String    			_ref;
  private StringBuffer 			_buffer_xml;
  private ObjectXmlReference 	_obj_xml_reference;

  public ObjectReference(String typeId, Address addr)
  {
    _typeId = typeId;
    _addr = addr;
    _ref = this.toString();
    _transp = _addr.createTransport();

    // We assume that we have a TCPAddress. Extract host and port.
    TCPAddress tcp_addr = (TCPAddress) (addr);

   assert tcp_addr != null;
    _host = tcp_addr.host ();
    _port = tcp_addr.port ();
    
    echo("host: "+getHost());
    echo("port: "+getPort());
  }

  public ObjectReference(String ior)
  {
	  _ref = ior;
	  _addr = new TCPAddress (_host, _port);
	  _transp = _addr.createTransport ();
  }
  
  public ObjectReference(String ref, String host, String port){
	  _ref = ref;
	  _host = host;
	  _port = Integer.valueOf(port);
	  _addr = new TCPAddress (host, Integer.valueOf(port));
	  _transp = _addr.createTransport ();
  }


  public String stringify()
  {
	  return _ref;
  }

  /**
   * Atualiza a string que representa o ior
   * @param ref
   */
  public void updateIor(String ref) {
	  _ref = ref;
  }

  /**
   * Atualiza a referencia do object reference
   * @param ref
   * @param host
   * @param port
   */
  public void updateReference(String ref, String host, String port) {
	  String tmp_ref = stringify();
	  String tmp_host = getHost();
	  String tmp_port = String.valueOf(getPort());
	  updateIor(ref);
	  setHost(host);
	  setPort(Integer.valueOf(port));
	  _addr = new TCPAddress (getHost(), getPort());
	  _transp = _addr.createTransport ();
  }
  
  public Transport transport()
  {
    return _transp;
  }

  public String typeId()
  {
    return _typeId;
  }

	public String getHost() {
		return _host;
	}
	
	public void setHost(String _host) {
		this._host = _host;
	}
	
	public int getPort() {
		return _port;
	}
	
	public void setPort(int _port) {
		this._port = _port;
	}
	
	public String getXmlReference() {
		
		_buffer_xml = null;
		_buffer_xml = new StringBuffer();
		_buffer_xml.append("<reference>\n");
		_buffer_xml.append("   <object>"+stringify()+"</object>\n");
		_buffer_xml.append("   <host>"+getHost()+"</host>\n");
		_buffer_xml.append("   <port>"+getPort()+"</port>\n");
		_buffer_xml.append("</reference>\n");
		
		return _buffer_xml.toString();
		
	}
	
	/**
	 * Retorna objeto que mapeia o xml de referencia
	 * @param xml
	 * @return
	 */
	public ObjectXmlReference getObjectXml(String xml) {
		
		XStream _xstream = new XStream(new DomDriver());
	    
	    _xstream.alias("reference", ObjectXmlReference.class);
	    _obj_xml_reference = (ObjectXmlReference) _xstream.fromXML(xml);
	    
	    _xstream = null;
		return _obj_xml_reference;
	}
	
	public ObjectXmlReference getObjectXml() {
		return getObjectXml(getXmlReference());
	}
	
	private void echo(String msg) {
		System.out.println("[ObjectReference] "+msg);
	}
  
  

}

