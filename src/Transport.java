//package fixmico;

/**
 * <p>Title: Transport.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

abstract class Transport
{
  protected boolean _isClosed;

  public Transport()
  {
    _isClosed = true;
  }

  boolean isClosed()
  {
    return _isClosed;
  }

  abstract public boolean accept();
  abstract public boolean open();
  abstract public int send(String message);
  abstract public int recv(StringBuffer messageBuffer, int size);
  abstract public StringBuffer recv();
  abstract public void close();
  abstract public void closeSocket();


}
