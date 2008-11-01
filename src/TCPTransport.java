//package fixmico;

import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;

/**
 * <p>Title: TCPTransport.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

public class TCPTransport extends Transport
{
  private TCPAddress _addr;
  private Socket _socket;
  private ServerSocket _serverSocket;

  public TCPTransport (TCPAddress addr)
  {
    _addr = addr;
    _socket = null;
    _serverSocket = null;
  }

  public boolean accept()
  {
    try {
      _serverSocket = new ServerSocket(_addr.port());

    } catch (BindException e)
    {
      System.out.println("Port "+ _addr.port()+ " seems to be in use: Can't bind");
      System.exit(1);
    } catch(SocketException e)
    {
      System.out.println("Socket Exception Error ");
      System.exit(1);
    } catch (IOException e)
    {
      System.out.println("I/O Exception at port "+ _addr.port());
      System.exit(1);
    }

    try {
      _socket = _serverSocket.accept();
    } catch (IOException e)
    {
      System.out.println("I/O Exception at port "+ _addr.port());
      System.exit(1);
    }

    _isClosed = false;
    return true;

  }

  public boolean open()
  { 
	  	boolean state = false;
	    try {
	      _socket = new Socket(_addr.host(), _addr.port());
	      state = true;
	    } catch (UnknownHostException e)
	    {
	      System.err.println("Don't know about host "+ _addr.host());
	    } catch(ConnectException e)
	    {
	      System.err.println("Failure in obtaining connection with server ");
	    } catch (IOException e)
	    {
	      System.err.println("Couldn't get I/O for host "+ _addr.host());
	    }

    _isClosed = _socket.isClosed();
    return state;

  }

  public int send(String message)
  {
    OutputStream os;
    BufferedWriter wr;
    int fSize = message.length();
    echo("bytes to send: "+fSize);

    if (_isClosed) {
    	echo("Transporte fechado!!");
    }else{
    	echo("Transporte aberto!!");
    }

    try {
    	/*
    	if (_socket.isConnected()) {
    		echo("socket UP");
    	}else{
    		echo("socket DOWN");
    	}
    	
    	if (_socket.isBound()) {
    		echo("socket isBound");
    	}else{
    		echo("socket notBound");
    	}
    	*/
        os = _socket.getOutputStream();
        wr = new BufferedWriter(new OutputStreamWriter(os));
        wr.write(message);
        wr.flush();
        echo("message sent:");
        echo("----------------------");
        echo(message+"\n----------------------------");
      } catch (IOException e) {
        echo("IOException on socket listen: " + e);
        e.printStackTrace();
      }

    return 0;
  }

  public int recv(StringBuffer messageBuffer, int size) {
    InputStream is=null;
    int num=0;
    int avaiable = 0;
    int left = size;
    byte[] buff= new byte[size];

    assert (_isClosed == false): "Transport is closed" ;

    try {

      is = _socket.getInputStream();

      while ( left >0 ) {
    	avaiable = is.available();
 
        num = is.read(buff);
        if ( num == -1)
        {
          is.close();
          return -1;
        }
        if (num == 0)
           break;

        for (int i = 0; i < num; i++) {
        	messageBuffer.append(buff[i]);
        }
        
        echo("received:\n"+messageBuffer.toString());
        
        left -= num;
      }
    } catch (IOException e) {
    	e.printStackTrace();
    	echo("erro no recv ");
    	return -1;
    }

    return left;
  }

  public void close()
  {
    try {
      if (_isClosed == false)
        _isClosed = true;

      _socket.close();
      _serverSocket.close();
      echo("conexao "+_addr.host()+":"+_addr.port()+" encerrada");
      
    } catch (IOException e)
      {
        System.err.println("I/O Exception ");
      }
  }
  
  public void closeSocket() {
	  try {
		  if (_isClosed == false) _isClosed = true;
		  
		  _socket.close();
		  echo("conexao "+_addr.host()+":"+_addr.port()+" encerrada");
	  }catch(Exception e){}
  }

	public StringBuffer recv() {
		
		int message_size = 0;
	    InputStream is = null;
	    BufferedReader rd = null;
	    int avaiable = 0;
	    StringBuffer buffer = new StringBuffer();
	    
	    if (_isClosed) {
	    	echo("Transporte fechado!!");
	    }else{
	    	echo("Transporte aberto!!");
	    }
	    

	    try {

	      is = _socket.getInputStream();
	      avaiable = is.available();

	      rd = new BufferedReader(new InputStreamReader(is));
	      int letra;
	      char c;
	      while ((letra = rd.read()) != 10) { //10 e o \n
	    	  if (letra == -1) break;
	    	  c = (char)letra;
	    	  buffer.append(c);
	    	  //System.out.println("[TCPTransport] lido: "+(char)letra +" -> "+letra);
	      }
	      try{
		      message_size = Integer.valueOf(buffer.toString());
	      }catch (Exception e) {
	    	  message_size = 0;
	      }
	      echo("message size: "+message_size);
	      buffer.delete(0, buffer.length());
	      
	      //leio o tamanho da mensagem obtido
	      int size_count = 0;
	      while (size_count < message_size) {
	    	  size_count = size_count + 1;
	    	  letra = rd.read();
	    	  c = (char)letra;
	    	  buffer.append(c);
	      }
	      
	      echo("message read:");
	      echo("----------------------");
	      echo("\n"+buffer.toString());
	      echo("----------------------");
	      
	    } catch (IOException e) {
	    	e.printStackTrace();
	    	echo("Erro no recv: "+e.getMessage());
	    	return null;
	    }

		return buffer;
	}
	
	public boolean isConnected(){
		return _socket.isConnected();
	}
	
	public boolean isBound(){
		return _socket.isBound();
	}
	
	private void echo(String msg) {
		ORB.log(msg);
		//System.out.println(new Date()+" [TCPTransport] "+msg);
	}
}



