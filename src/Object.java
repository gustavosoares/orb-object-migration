//package fixmico;

/**
 * <p>Title: Object.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */
import java.util.*;
import java.io.*;

public class Object
{
  private ObjectReference _ref;

  public Object()
  {
    _ref = null;
  }

  public ObjectReference objectReference()
  {
    return _ref;
  }

  protected void objectReference (ObjectReference ref)
  {
    _ref = ref;
  }

  protected Request createRequest(String opname)
  {
    return new Request (_ref, opname);
  }
}
