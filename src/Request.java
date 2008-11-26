//package fixmico;

/**
 * <p>Title: Request.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

import java.util.*;
import java.io.*;


public class Request {
  private PDUXml 	_pdu;
  private XmlObject _xmlobject;
  private CodecXml 	_codec;
  private ObjectReference _ref;
  private String _opname;

  public Request(ObjectReference ref, String opname) {
	_opname = opname;
	_ref = ref;  
	_pdu = new PDUXml(_ref, _opname);

    _xmlobject = _pdu.xmlObject();
    _codec = _pdu.codec();
  }

  public void addString (String arg)
  {
	  _codec.putString(arg);
  }

  public void addInteger(int arg)
  {
	  _codec.putInteger(arg);
  }

  public void addBoolean(boolean arg)
  {
	  _codec.putBoolean(arg);
  }

  public void addDecimal(Double arg)
  {
	  _codec.putDecimal(arg);
  }

	public void addObjectId(String arg) {
		_codec.putObjectId(arg);
	}

	public void addClassName(String arg) {
		_codec.putClassName(arg);	
	}
	
	public void addObjectImplementation(Map obj_impl) {
		_codec.putObjectImplementation(obj_impl);
	}
	
	public void addXmlMapper(XmlMapper xmlmapper) {
		_codec.putXmlMapper(xmlmapper);
	}
	
	public void addSequenceReference(Map hashmap) {
		beginSequence();
		Iterator iterator = hashmap.keySet().iterator();
		while (iterator.hasNext()) {
		   String name = (String) iterator.next();
		   //echo("Room name: "+name);
		   Object obj = (Object) hashmap.get(name);
		   //putStringReply(name);
		   addObjectReference(obj.objectReference().getXmlReference());
		}
		endSequence();
	}
	
  public boolean raisedException() {
	
	  if (_pdu.replyStatus()==1)
		  return true ;
	  else
		  return false;

  }

  /**
   * Obtem o tipo de reply
   * @return
   */
	public String getReplyType() {
		return _codec.getReplyType();
	}
	
  public String getString() {
    return _codec.getString();
  }

  public int getInteger() {
    return _codec.getInteger();
  }

  public boolean getBoolean() {
    return _codec.getBoolean();
  }

  public boolean getDouble() {
    return _codec.getDouble();
  }
  
  /**
   * Envia a mensagem para o skel
   */
  public void invoke() {
	  	if (_codec.getTmpBuffer().toString().length() == 0) {
	  		_pdu.send(_codec.getBuffer().toString());
	  		_codec.clearTmpBuffer();
	  	}else{
	  		_pdu.send(_codec.getTmpBuffer().toString());
	  		_codec.clearTmpBuffer();
	  	}

	    _codec.setTmpBuffer(_codec.getBuffer());
	    long pduType = -1;
	    pduType = _pdu.recvNextPdu ();
	    if (pduType == 1) {
	    	//trato o reply
	    	StringBuffer buff = new StringBuffer();
	    	buff.append(_pdu.getReplyMessage());
	    	_codec.setBuffer(buff);
	    	
	    	//Reply de erro
			String reply_type = getReplyType();
			if (reply_type.equals("error")) {
				String msg = getString();
				List parsed = getParsed(msg, ":");
				String ref_aux = (String) parsed.get(0);
				String host = (String) parsed.get(1);
				String port = (String) parsed.get(2);
				echo("atualizando a referencia");
				_ref.updateReference(ref_aux, host, port);
				_pdu.updateReference(_ref, _opname);
				echo("encaminhando o request para o endereco novo...");
				//Re-invoke
				invoke();
			}
			
	    }else{
	    	System.out.println("[Request] reply not sent");
	    }
    
  }

  public CodecXml codec() {
    return _codec;
  }
  
  public XmlObject XmlObject(){
	  return _xmlobject;
  }
  
	public void endXml(){
		_codec.append("</request>");
	}
	
	public void beginParameter(){
		_codec.addField(1, "parameters", "BEGIN");
	}
	
	public void endParameter(){
		_codec.addField(1, "parameters", "END");
	}

	public void beginSequence(){
		_codec.addField(2, "sequence", "BEGIN");
	}
	
	public void endSequence(){
		_codec.addField(2, "sequence", "END");
	}
	
	public void beginStruct() {
		_codec.addField(3, "struct", "BEGIN");
	}

	public void endStruct() {
		_codec.addField(3, "struct", "END");
	}
	
	public void beginString() {
		_codec.addField(3, "string", "BEGIN");
	}
	
	public void endString() {
		_codec.addField(3, "string", "END");
	}
	
	public void addSequence(String type, String value){
		_codec.addField(3, type, value);
	}

	public void addObjectReference(String xml) {
		// TODO Auto-generated method stub
		_codec.addField(xml);
	}

	public String getObjectReference() {
		// TODO Auto-generated method stub
		return _codec.getObjectReference();
	}

	public String getObjectHost() {
		return _codec.getObjectHost();
	}
	
	public String getObjectPort() {
		return _codec.getObjectPort();
	}
	/////////////
	//SEQUENCE //
	/////////////
	public List getSequenceReference() {
		return _codec.getSequenceReference();
	}
	
	public List getSequenceString() {
		return _codec.getSequenceString();
	}
	
	private void echo(String msg) {
		System.out.println("[Request] "+msg);
	}
	
	protected List getParsed(String msg, String delimitador) {
		List parsed = new ArrayList();
		StringTokenizer st = new StringTokenizer(msg,delimitador);
		while (st.hasMoreTokens()) {
			parsed.add(st.nextToken());
		}
		return parsed;
	}
  
}

