//package fixmico;

/**
 * <p>Title: ServerRequest.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */
import java.util.*;
import java.io.*;

public class ServerRequest
{
  private PDUXml _requestPdu;
  private PDUXml _replyPdu;

  public ServerRequest(PDUXml requestPdu)
  {
    _requestPdu = requestPdu;
    _replyPdu = new PDUXml(requestPdu);
  }

  public String getReference()
  {
    return _requestPdu.getReference();
  }

  public String opname()
  {
    return _requestPdu.opname();
  }

  public void exceptionRaised()
  {
    _replyPdu.exceptionRaised();
  }

  /**
   * metodo para envio do reply
   */
  public void sendReply() {
	  
	  XmlReply  xml_reply = (XmlReply) _replyPdu.xmlObject();
	  xml_reply.endResult();
	  xml_reply.endXml();
	  
	  _replyPdu.send(_replyPdu.codec().toString());

  }

	public int getInteger() {
		return _requestPdu.codec().getInteger();
	}

	public String getString() {
		return _requestPdu.codec().getString();
	}
	
	public void putInteger(int amount) {
		_requestPdu.codec().putInteger(amount);		
	}
  
	public void beginSequenceReply(){
		_replyPdu.codec().addField(2, "sequence", "BEGIN");
	}
	
	public void endSequenceReply(){
		_replyPdu.codec().addField(2, "sequence", "END");
	}
	
	///////////////
	// REPLY //////
	///////////////
	
	public void putIntegerReply(int amount) {
		_replyPdu.codec().putInteger(amount);		
	}
	
	public void putStringReply(String value) {
		_replyPdu.codec().putString(value);		
	}

	public void putBooleanReply(boolean result) {
		_replyPdu.codec().putBoolean(result);
	}
	
	public void putObjectReferenceReply(String xml_reference) {
		_replyPdu.codec().putObjectReference(xml_reference);
	}
	
	public void putNullObjectReferenceReply() {
		StringBuffer buffer_xml = new StringBuffer();
		buffer_xml.append("<reference>\n");
		buffer_xml.append("   <object>null</object>\n");
		buffer_xml.append("   <host>null</host>\n");
		buffer_xml.append("   <port>0</port>\n");
		buffer_xml.append("</reference>\n");
		_replyPdu.codec().putObjectReference(buffer_xml.toString());
	}
	
	public void putSequenceReply(ArrayList sequence) {
		// TODO implementacao do metodo que escreve uma sequence no no xml de reply
		
	}
	
	public void putSequenceReferenceReply(Map hashmap) {
		beginSequenceReply();
		Iterator iterator = hashmap.keySet().iterator();
		while (iterator.hasNext()) {
		   String name = (String) iterator.next();
		   //echo("Room name: "+name);
		   Object obj = (Object) hashmap.get(name);
		   //putStringReply(name);
		   putObjectReferenceReply(obj.objectReference().getXmlReference());
		}
		endSequenceReply();
	}
	
	private void echo(String string) {
		System.out.println("[ServerRequest] "+string);
		
	}

	/**
	 * TODO implementar metodos abaixo
	 * @return
	 */
	public ArrayList getSequence() {
		// TODO criar implementacao do getSequence
		return null;
	}

	public List getSequenceString() {
		return _requestPdu.codec().getSequenceString();
	}

	public String getObjectReference() {
		return _requestPdu.codec().getObjectReference();
	}

	public String getObjectHost() {
		return _requestPdu.codec().getObjectHost();
	}

	public String getObjectPort() {
		return _requestPdu.codec().getObjectPort();
	}

	public String getObjectReferenceFromSequence() {
		return _requestPdu.codec().getObjectReferenceFromSequence();
	}

	public List getSequenceReference() {
		// TODO Auto-generated method stub
		return _requestPdu.codec().getSequenceReference();
	}

	/**
	 * Retorna lista de struct
	 * @return
	 */
	public List getStruct() {
		// TODO Auto-generated method stub
		return _requestPdu.codec().getSruct();
	}
	
	/**
	 * Retorna lista de struct sem a tag de inicio/fim do struc
	 * @return
	 */
	public List getStructStriped() {
		// TODO Auto-generated method stub
		return _requestPdu.codec().getSructStriped();
	}

	public XmlMapper getXmlMapper(String xml) {
		return _requestPdu.codec().getXmlMapper(xml);
	}

}

