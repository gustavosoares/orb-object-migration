//package fixmico;

/**
 * <p>Title: Request.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

import java.util.*;
import java.io.*;

import xml.CodecXml;
import xml.XmlObject;
import xml.XmlRequest;

public class Request {
  private PDUXml 	_pdu;
  private XmlObject _xmlobject;
  private CodecXml 	_codec;

  public Request(ObjectReference ref, String opname) {
	_pdu = new PDUXml(ref, opname);

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
  
  public void invoke() {
	  
	  //System.out.println("[Request] invoke: \n"+_codec.getBuffer().toString());
	  
	    _pdu.send(_codec.getBuffer().toString());
	
	    long pduType = -1;
	    pduType = _pdu.recvNextPdu ();
	    if (pduType == 1) {
	    	//trato o reply
	    	StringBuffer buff = new StringBuffer();
	    	buff.append(_pdu.getReplyMessage());
	    	_codec.setBuffer(buff);
	    	//_pdu.transport().close();
	    }else{
	    	/**
	    	 * Implementar lancamento de exception
	    	 */
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
  
}

