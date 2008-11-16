import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Vector;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class PDUXml {

	private long        _pduType; //0 request - 1 reply
	private Transport   _transp = null;
	private CodecXml    _codec;
	private String		_ref;
	private XmlObject	_xmlobject;
	
	// Request information
	private static long _nextRequestId = 1;
	private long        _requestId;
	private String       _opname;
	
	// Reply information
	private long 		_replyStatus;
	private static long _nextReplyId = 1;
	private long        _replyId;
	private String 		_replyMessage;
	
	  /**
	   * PDU de request
	   * @param ref
	   * @param opname
	   */
	  public  PDUXml (ObjectReference ref, String opname)
	  {
	    _pduType = 0;

	    _transp = ref.transport();
	    if (_transp.isClosed()){
	    	echo("request construido com transporte fechado");
	    }else{
	    	echo("request construido com transporte aberto");
	    	_transp.closeSocket();
	    }
	    
	    // Request ID
	    _requestId = _nextRequestId++;
	    
	    //Crio o xml de request
	    _xmlobject = new XmlRequest(String.valueOf(_requestId), ref.stringify(), opname);
	    
	    //Criar o codec
	    _codec = (CodecXml) _xmlobject.codec();

	  }
	  
	  /**
	   * 
	   * @param transp
	   */
	  public PDUXml (Transport transp) {
		
	    _pduType = -1;
	    _transp = transp;
	    _codec = null;
	  }

	  /**
	   * Construtor do PDU de reply
	   * @param replyPdu
	   */
	  public  PDUXml(PDUXml replyPdu) {
	    _pduType = 1;

	    _transp = replyPdu.transport();

	    // Request ID
	    _requestId = replyPdu.requestId ();
	    
	    //Constroi o inicio do reply xml
	    
	    // Reply  ID
	    _replyId = _nextReplyId++;
	    
	    //Crio o xml de request
	    _xmlobject = new XmlReply(_replyId, "return");
	    
	    //Criar o codec
	    _codec = (CodecXml) _xmlobject.codec();

	  }
	  
	  public XmlObject xmlObject(){
		  return _xmlobject;
	  }
	  
	  public CodecXml codec () {
	    return _codec;
	  }
	
	  public Transport transport ()
	  {
	    return _transp;
	  }
	
	  // Interface for Request PDU
	  public long requestId ()
	  {
	    return _requestId;
	  }
	  
	  public long recvNextPdu () {
		  
	    //Leio o tamanho da mensagem retornado
	    StringBuffer messageBuffer = _transp.recv();
	    if (messageBuffer == null) {
	    	echo("messageBuffer esta nulo!");
	    }
	    
	    _codec = new CodecXml(messageBuffer);
	    
	    if (messageBuffer.toString().startsWith("<reques")){
	    	//validateXml("request", messageBuffer);
	    	_pduType = 0;
	    	
	    	//ler a referencia do objeto
	    	_ref = _codec.getObjectReference();
	    	
	    	//ler o nome da operacao
	    	_opname = _codec.getOperation();
	    	
	    }else if (messageBuffer.toString().startsWith("<reply")){
	    	_pduType = 1;
	    	//echo("[PDUXml] reply message received");
	    	setReplyMessage(messageBuffer.toString());
	    }

	    return _pduType;
	  }
	  
	  /**
	   * Verifica se a conexao ta fechada
	   * @return
	   */
	  public boolean isClosed(){
		 return _transp.isClosed(); 
	  }
	  
	  /**
	   * Abrir conexao
	   */
	  public void open() {
		 _transp.open(); 
	  }
	  
	  public void send () {
		  
		  StringBuffer replyMessage = new StringBuffer();
		  
		  int message_size = replyMessage.toString().length();
		  String aux = String.valueOf(message_size)+ "\n" + replyMessage.toString();
		  
		  if (_transp.isClosed ())
			  _transp.open ();

		  _transp.send (aux);

	  }

	  /**
	   * Metodo de send chamado no reply de um PDU
	   * @param message
	   */
	  public void send (String message) {
		  
		  int message_size = message.length();
		  String aux = String.valueOf(message_size)+ "\n" + message;
		  
		  /*
		  if (_transp.isConnected()) {
			  echo("transporte esta conectado");
		  }else{
			  echo("transporte nao esta conectado");
		  }
		  */
		  
		  if (_transp.isClosed ()){
			  
			  _transp.open ();
			  echo("[PDUXML] Transporte aberto");
		  }
			  

		  _transp.send(aux);
		  
		  //_transp.close();

	  }
	  
	public void exceptionRaised() {
		// TODO Auto-generated method stub
		echo("Exception no PDUXml!!!!!");
		echo("Implementar tratamento");
		
	}

	public String opname() {
		return _opname;
	}
	
	public String getReference(){
		return _ref;
	}
	
	public long replyStatus () {
		assert _pduType == 1: "Should be PDU_REPLY";
	    return _replyStatus;
	}
	
	private void setReplyMessage(String message){
		_replyMessage = message;
	}
	
	public String getReplyMessage(){
		return _replyMessage;
	}
	
	private void echo(String msg) {
		ORB.log(msg);
		//System.out.println(new Date()+" [PDUXml] "+msg);
	}
}
