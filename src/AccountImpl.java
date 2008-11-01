
public class AccountImpl extends AccountSkel{

	  private int _balance;

	  public AccountImpl ()
	  {
	    super();
	    _balance = 0;
	  }

	  public void deposit (int amount)
	  {
	    System.out.println("Server: deposit " + amount);
	    _balance += amount;
	 }

	  public void withdraw (int amount) {
	    System.out.println("Server: withdraw  " + amount);
	    if (_balance >= amount) {
	    	_balance -= amount;
	    	//return true;
	    } else {
	    	System.out.println("Server: withdraw failed.");
	    	//return false;
	    }
	    
	 }

	  public int balance ()
	  {
	    System.out.println("Server: balance " + _balance);
	    return _balance;
	  }
}
