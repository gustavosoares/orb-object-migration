//package fixmico;

/**
 * <p>Title: AccountStub.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

import java.util.*;
import java.io.*;

public class AccountStub extends Account
{
	
	  public AccountStub(ObjectReference ref) {
		  objectReference(ref);
	  }

  public void deposit(Account other, int amount)
  {
    Request req = createRequest ("deposit");
    req.beginParameter();
    if (other instanceof AccountStub) {
    	//req.addObjectReference(other.objectReference().stringify(), other.objectReference().getHost(), String.valueOf(other.objectReference().getPort()));
    } else {
    	
    }
    req.addInteger(amount);
    req.endParameter();
    req.endXml();
    req.invoke ();
    req = null;
  }

  public void deposit(int amount)
  {
    Request req = createRequest ("deposit");
    
    String xml_reference = objectReference().getXmlReference();
    //echo("XML REFERENCE:");
    //echo("\n"+xml_reference);
    //echo("");
    req.beginParameter();
    req.addInteger(amount);
    req.addObjectReference(xml_reference);
    req.endParameter();
    req.endXml();
    req.invoke ();
    req = null;
  }
  public void withdraw(int amount)
  {
    Request req = createRequest ("withdraw");
    req.beginParameter();
    req.addInteger(amount);
    req.endParameter();
    req.endXml();
    req.invoke ();
    req = null;
  }

  public int balance()
  {
    Request req = createRequest ("balance");
    req.beginParameter();
    req.endParameter();
    req.endXml();
    req.invoke();
    
    //System.out.println("[AccountStub] req: \n"+req.XmlObject().codec().toString());
    int result = req.getInteger();
    req = null;
    return result;
  }
  
  private void echo(String msg) {
	  System.out.println("[AccountStub] "+msg);
  }
  
  
}

