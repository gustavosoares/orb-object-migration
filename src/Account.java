//package fixmico;

/**
 * <p>Title: Account.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

abstract class Account extends Object
{
  abstract public void deposit(int amount);
  abstract public void withdraw(int amount);
  abstract public int balance();
}
