//package fixmico;

/**
 * <p>Title: Server.java </p>
 * <p>Description: </p>
 *
 * @author: Jessica Rogers
 */

import java.io.*;
import java.lang.*;
import java.net.*;
import java.util.*;

import xml.ObjectXmlReference;
import xml.old.PDUXmlRequest;

import com.thoughtworks.xstream.XStream;

public class Server
{
  public static void main(String[] args) throws IOException
  {
    BufferedWriter outfile = null;
    String server_host = "localhost";
    int server_port = 1234;

    TCPAddress addr = new TCPAddress (server_host, server_port);
    String ior = "";
    Account the_account = null;


    ORB.instance().address(addr);

    the_account = new AccountImpl();
    //the_account.objectReference().setHost(server_host);
    //the_account.objectReference().setPort(server_port);
    
    ior = ORB.objectToString(the_account);

    //Xml com a referencia do objeto
    ObjectXmlReference acc_xml = new ObjectXmlReference(ior, server_host, String.valueOf(server_port));
	XStream xstream = new XStream();
	xstream.alias("reference", ObjectXmlReference.class);
	
	String ref_xml = xstream.toXML(acc_xml);
	System.out.println("xml com a referencia: ");
	System.out.println(ref_xml);
	
    outfile = new BufferedWriter(new FileWriter("account.xml"));
    outfile.write(ref_xml);
    outfile.close();
	
    System.out.println("\nRunning on "+server_host+":"+server_port);

    ORB.instance().run ();

  }
}
