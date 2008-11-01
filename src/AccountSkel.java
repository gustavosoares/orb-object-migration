//package fixmico;

/**
 * <p>Title: AccountSkel.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

import java.util.*;
import java.io.*;

 abstract class AccountSkel extends Account implements ObjectImpl
{
  public AccountSkel()
  {
	  
    Address addr = ORB.instance().address();
    ObjectReference ior = new ObjectReference ("IDL:Account:1.0", addr);
    objectReference (ior);

    ORB.instance().registerObjectImpl(ior.stringify(),this);
  }

  public void invoke(ServerRequest req)
  {
    boolean a = dispatch (req);
    assert (a):"dispatch Error";
  }

  protected boolean dispatch(ServerRequest req) {

    if (req.opname().equals("deposit")) {
        int amount = req.getInteger();

        req.putStringReply("deposit "+String.valueOf(amount));
        
        System.out.println("[AccountSkel] deposit "+amount);
        deposit (amount);
        return true;
    }
    else if (req.opname().equals("withdraw") ) {
        int amount = req.getInteger();
        
        req.putStringReply("withdraw "+String.valueOf(amount));
        
        System.out.println("[AccountSkel] withdraw "+amount);
        withdraw (amount);
        return true;
    }

    else if (req.opname().equals("balance") ) {
        int amount = this.balance();
        System.out.println("[AccountSkel] balance amount: "+amount);
        req.putIntegerReply(amount);
        return true;
    }

    return false;
  }

}
