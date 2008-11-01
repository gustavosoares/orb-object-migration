//package fixmico;

/**
 * <p>Title: TCPAdress.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

import java.io.*;
import java.lang.*;
import java.util.*;
import java.net.*;

public class TCPAddress extends Address
{
  private String _host;
  private int _port;

  private static Dictionary  _transportMap = new Hashtable();

  public TCPAddress(String host, int port)
  {
    super();
    _host = host;
    _port = port;
  }

  public String host()
  {
    return _host;
  }

  public int port()
  {
    return _port;
  }

  public Transport createTransport()
  {
    if (_transport != null)
      return _transport;

    String str = "";
    str = str + _host + ":" + _port;

    if ( _transportMap.get(str) == null )
    {
      TCPTransport transport = new TCPTransport(this);
      _transportMap.put(str, transport);
      return transport;
    }

    return (Transport)_transportMap.get(str);
  }
}

