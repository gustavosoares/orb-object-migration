//package fixmico;

/**
 * <p>Title: Address.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

abstract class Address
{
  protected Transport _transport;

  public Address()
  {
    _transport = null;
  }

  abstract public Transport createTransport();
}
