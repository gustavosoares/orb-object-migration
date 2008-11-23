//package fixmico;

/**
 * <p>Title: Object.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */
import java.util.*;
import java.io.*;

public class Object {
	
  private ObjectReference _ref;

  public Object() {
    _ref = null;
  }

  public ObjectReference objectReference() {
    return _ref;
  }

  protected void objectReference (ObjectReference ref) {
    _ref = ref;
  }

  protected Request createRequest(String opname) {
    return new Request (_ref, opname);
  }
 
	protected List getParsed(String msg, String delimitador) {
		List parsed = new ArrayList();
		StringTokenizer st = new StringTokenizer(msg,delimitador);
		while (st.hasMoreTokens()) {
			parsed.add(st.nextToken());
		}
		return parsed;
	}
	
	protected void echo(String msg) {
		System.out.println("["+this.getClass().getName()+"] "+msg);
	}
}
